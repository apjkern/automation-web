package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.automation.web.entity.enums.Browser;

import java.util.List;

/**
 * 自动化测试
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Unit {

    /**
     * 浏览器类型
     */
    private Browser browser;

    /**
     * 驱动路径
     */
    private String driver;

    /**
     * 单位：秒
     * 当dom需要一定时间才动态加载出来时，这个属性很有用
     * 比如点击按钮，显示编辑页面的dialog，这时候可能需要异步加载数据，等待数据返回，才显示dialog，设置该值，如果一定时间没有在dom查找到该节点，就返回失败
     */
    private int domQueryTimeout = 5;

    /**
     * 测试完后是否关闭浏览器
     */
    private boolean close;

    /**
     * 测试分组
     */
    List<Group> groups;
}
