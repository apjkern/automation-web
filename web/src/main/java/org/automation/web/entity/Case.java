package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 测试用例
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Case {

    /**
     * 测试用例描述
     */
    private String name;

    /**
     * 执行完后等待x秒后再执行下一步-全局配置
     */
    private long sleep;

    /**
     * 测试用例步骤
     */
    private List<Step> steps;

    /**
     * 测试使用成功的条件
     */
    private Success success;

}
