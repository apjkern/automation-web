package org.automation.web.xml;

import lombok.extern.slf4j.Slf4j;
import org.automation.web.entity.*;
import org.automation.web.entity.enums.Browser;
import org.automation.web.entity.enums.Command;
import org.automation.web.entity.enums.Expression;
import org.automation.web.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * xml解析
 * 执行器
 *
 * @author xuzhijie
 */
@Slf4j
public class Xml {

    private static final String SUCCESS_ELEMENT = "success";

    public static Unit parse(File xmlFile) {
        Document document;
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(xmlFile);
            return buildUnitEntity(document.getRootElement());
        } catch (DocumentException e) {
            log.error("解析失败", e);
        }
        return null;
    }

    private static Unit buildUnitEntity(Element rootElement) {
        try {
            Browser browser = Browser.of(rootElement.attributeValue("browser"));
            String driver = rootElement.attributeValue("driver");
            String domQueryTimeout = rootElement.attributeValue("domQueryTimeout");
            String close = rootElement.attributeValue("close");

            List<Group> groups = new ArrayList<>();
            Iterator<Element> groupElements = rootElement.elementIterator();
            while (groupElements.hasNext()) {
                Group groupEntity = buildGroupEntity(groupElements.next());
                if (groupEntity == null) {
                    continue;
                }
                groups.add(groupEntity);
            }
            if (groups.isEmpty()) {
                log.warn("没有配置测试用例");
                return null;
            }
            return Unit
                .builder()
                .driver(driver)
                // 默认5秒内查找不到dom，则失败
                .domQueryTimeout(StringUtils.isNotBlank(domQueryTimeout) ? Integer.valueOf(domQueryTimeout) : 5)
                .browser(browser)
                .close("true".equalsIgnoreCase(close) ? true : false)
                .groups(groups)
                .build();
        } catch (Exception e) {
            log.error("解析失败", e);
            return null;
        }
    }

    private static Group buildGroupEntity(Element groupElement) {
        String groupName = groupElement.attributeValue("name");
        String interrupt = groupElement.attributeValue("interrupt");
        String delay = groupElement.attributeValue("delay");
        List<Case> cases = new ArrayList<>();

        Iterator<Element> caseElements = groupElement.elementIterator();
        while (caseElements.hasNext()) {
            Case caseEntity = buildCaseEntity(groupName, caseElements.next());
            if (caseEntity == null) {
                continue;
            }
            cases.add(caseEntity);
        }
        if (cases.isEmpty()) {
            log.warn("分组[{}]-没有测试用例", groupName);
            return null;
        }
        return Group.builder()
            .name(groupName)
            .interrupt("false".equalsIgnoreCase(interrupt) ? false : true)
            .delay(StringUtils.isBlank(delay) ? 0 : Long.valueOf(delay))
            .cases(cases)
            .build();
    }

    private static Case buildCaseEntity(String groupName, Element caseElement) {
        String caseName = caseElement.attributeValue("name");
        long sleep = asLong(caseElement.attributeValue("sleep"));
        Iterator<Element> stepElements = caseElement.elementIterator();
        List<Step> steps = new ArrayList<>();

        Success success = null;
        while (stepElements.hasNext()) {
            Element step = stepElements.next();
            if (SUCCESS_ELEMENT.equalsIgnoreCase(step.getName())) {
                if (success == null) {
                    success = buildSuccessEntity(groupName, caseName, step);
                }
                continue;
            }
            Step stepEntity = buildStepEntity(groupName, caseName, step);
            if (stepEntity == null) {
                return null;
            }
            steps.add(stepEntity);
        }

        if (steps.isEmpty()) {
            log.warn("分组[{}]-测试用例[{}]-没有测试步骤", groupName, caseName);
            return null;
        }

        return Case.builder()
            .name(caseName)
            .steps(steps)
            .sleep(sleep)
            .success(success)
            .build();
    }

    private static Step buildStepEntity(String groupName, String caseName, Element stepElement) {
        String stepName = stepElement.attributeValue("name");
        Command cmd = Command.of(stepElement.attributeValue("cmd"));
        String targetId = stepElement.attributeValue("targetId");
        String css = stepElement.attributeValue("css");
        String value = stepElement.attributeValue("value");
        String delay = stepElement.attributeValue("delay");
        String appendRandom = stepElement.attributeValue("appendRandom");

        if (cmd == null) {
            log.warn("分组[{}]-测试用例[{}]-测试步骤[{}]-cmd命令错误[{}]", groupName, caseName, stepName, stepElement.attributeValue("cmd"));
            return null;
        }

        boolean ignore = StringUtils.isBlank(targetId) && StringUtils.isBlank(css);
        if (ignore) {
            if (!cmd.equals(Command.OPEN) && !cmd.equals(Command.REFRESH)) {
                log.warn("分组[{}]-测试用例[{}]-测试步骤[{}]-未指定targetId或css", groupName, caseName, stepName);
                return null;
            }
        }

        return Step.builder()
            .name(stepName)
            .cmd(cmd)
            .targetId(targetId)
            .delay(StringUtils.isBlank(delay) ? 0 : Long.valueOf(delay))
            .appendRandom("true".equalsIgnoreCase(appendRandom) ? true : false)
            .css(css)
            .value(value)
            .build();
    }

    private static Success buildSuccessEntity(String groupName, String caseName, Element successElement) {
        Expression expression = Expression.of(successElement.attributeValue("expression"));
        List<Condition> conds = new ArrayList<>();
        Iterator<Element> conditionElements = successElement.elementIterator();
        while (conditionElements.hasNext()) {
            conds.add(buildConditionEntity(groupName, caseName, conditionElements.next()));
        }

        return Success.builder()
            .expression(expression)
            .conditions(conds)
            .build();
    }

    private static Condition buildConditionEntity(String groupName, String caseName, Element conditionElement) {
        org.automation.web.entity.enums.Element element = org.automation.web.entity.enums.Element.of(conditionElement.attributeValue("element"));
        String targetId = conditionElement.attributeValue("targetId");
        String css = conditionElement.attributeValue("css");
        String desc = conditionElement.attributeValue("desc");

        boolean ignore = StringUtils.isBlank(targetId) && StringUtils.isBlank(css);
        if (ignore) {
            log.warn("分组[{}]-测试用例[{}]-用例成功条件[{}]-未指定targetId或css", groupName, caseName, desc);
            return null;
        }

        return Condition.builder()
            .element(element)
            .targetId(targetId)
            .css(css)
            .desc(desc)
            .build();
    }

    private static long asLong(String sleepValue) {
        long sleep = 0;
        if (StringUtils.isNotBlank(sleepValue)) {
            try {
                sleep = Long.valueOf(sleepValue);
            } catch (Exception e) {
                log.warn("不是一个有效的数字[{}]", sleepValue);
            }
        }
        return sleep;
    }

}
