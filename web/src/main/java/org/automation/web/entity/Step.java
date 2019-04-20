package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.automation.web.entity.enums.Command;

/**
 * 测试步骤
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Step {

    /**
     * 步骤名称
     */
    private String name;

    /**
     * 命令
     * input    输入
     * click    点击
     */
    private Command cmd;

    /**
     * 操作的元素-ID定位
     */
    private String targetId;
    /**
     * 操作的元素-css定位
     */
    private String css;

    /**
     * 给目标赋值
     */
    private String value;

    /**
     * 延时一段时间后再执行,单位：毫秒
     */
    private long delay;

    /**
     * 附加随机数的个数
     */
    private boolean appendRandom;

}
