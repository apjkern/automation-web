package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 测试用例分组
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Group {

    /**
     * 分组名称
     */
    private String name;

    /**
     * 设置为true时，只要该分组下出现测试失败，则中断整个测试流程，包括后面的
     */
    private boolean interrupt;

    /**
     * 延时一段时间后再执行,单位：毫秒
     */
    private long delay;

    /**
     * 测试用例
     */
    List<Case> cases;

}
