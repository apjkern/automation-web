package org.automation.web.runnable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.automation.web.entity.Case;
import org.automation.web.entity.Condition;
import org.automation.web.entity.Step;
import org.automation.web.entity.Success;
import org.automation.web.util.StringUtils;
import org.automation.web.util.ThreadUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

/**
 * @author xuzhijie
 */
@Slf4j
@AllArgsConstructor
public class CaseRunnable implements BooleanSupplier {

    private String groupName;
    private Case caseEntity;
    private WebDriver driver;
    private static final Random RANDOM = new Random();
    private static final int RANDOM_MAX = 6;

    /**
     * 执行测试用例
     */
    private boolean execute() {
        // 执行测试步骤
        List<Step> steps = caseEntity.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            if( step.getDelay() > 0 ){
                ThreadUtil.waitFor(step.getDelay());
            }
            boolean state = execStep(step);
            if (!state) {
                log.error("执行中断，遇到异常");
                return false;
            }
        }

        // 验证执行结果
        boolean ignore = caseEntity.getSuccess() == null || caseEntity.getSuccess().getConditions() == null || caseEntity.getSuccess().getConditions().isEmpty();
        if (ignore) {
            // 如果验证条件为空，返回测试成功
            return true;
        }

        Success success = caseEntity.getSuccess();
        for (int condIdx = 0; condIdx < success.getConditions().size(); condIdx++) {
            switch (success.getExpression()) {
                case OR: {
                    boolean flag = processExpression(success.getConditions().get(condIdx));
                    if (flag) {
                        // 有一个条件为true,都算成功
                        return true;
                    }
                    break;
                }
                case AND: {
                    boolean flag = processExpression(success.getConditions().get(condIdx));
                    if (!flag) {
                        // 有一个条件为false，都算失败
                        return false;
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return false;
    }

    private boolean processExpression(Condition condition) {
        if (condition.getElement() == null) {
            return true;
        }
        switch (condition.getElement()) {
            case EXIST: {
                try {
                    driver.findElement(initDocumentQuery(condition));
                    return true;
                } catch (Exception e) {
                    // 查找不到元素
                    return false;
                }
            }
            case DISPLAY: {
                try {
                    WebElement element = driver.findElement(initDocumentQuery(condition));
                    return element.isDisplayed();
                } catch (Exception e) {
                    // 查找不到元素
                    return false;
                }
            }
            default:
                break;
        }
        return true;
    }

    private boolean execStep(Step step) {
        log.info("{} -> {} -> {}", groupName, caseEntity.getName(), step.getName());
        boolean execState;
        try {
            switch (step.getCmd()) {
                case OPEN:
                    driver.get(step.getValue());
                    break;
                case INPUT:
                    WebElement inputElement = driver.findElement(initDocumentQuery(step));
                    inputElement.sendKeys("");
                    if( step.isAppendRandom() ){
                        inputElement.sendKeys(step.getValue() + generateRandom());
                    }else{
                        inputElement.sendKeys(step.getValue());
                    }
                    break;
                case CLICK:
                    driver.findElement(initDocumentQuery(step)).click();
                    break;
                case HOVER:
                    WebElement hover = driver.findElement(initDocumentQuery(step));
                    new Actions(driver).moveToElement(hover).perform();
                    break;
                case REFRESH:
                    driver.navigate().refresh();
                    break;
                default:
                    break;
            }
            execState = true;
        } catch (Exception e) {
            execState = false;
            log.error("执行步骤失败", e);
        }
        return execState;
    }

    private By initDocumentQuery(Step step) {
        if (StringUtils.isNotBlank(step.getTargetId())) {
            return By.id(step.getTargetId());
        }
        return By.cssSelector(step.getCss());
    }

    private By initDocumentQuery(Condition condition) {
        if (StringUtils.isNotBlank(condition.getTargetId())) {
            return By.id(condition.getTargetId());
        }
        return By.cssSelector(condition.getCss());
    }

    @Override
    public boolean getAsBoolean() {
        long startTime = System.currentTimeMillis();
        boolean flag = execute();
        long endTime = System.currentTimeMillis();
        log.info("{} -> {} -> 测试" + (flag ? "通过" : "失败") + " {} ms", groupName, caseEntity.getName(), (endTime - startTime));
        return flag;
    }

    private String generateRandom(){
        String randomString = Math.abs(RANDOM.nextInt()) + "";
        if( randomString.length() > RANDOM_MAX ){
            return randomString.substring(0, RANDOM_MAX);
        }
        return randomString;
    }

}
