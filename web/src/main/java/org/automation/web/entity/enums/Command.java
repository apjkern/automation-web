package org.automation.web.entity.enums;

/**
 * 元素
 *
 * @author xuzhijie
 */
public enum Command {

    /**
     * 输入
     */
    INPUT,

    /**
     * 打开
     */
    OPEN,

    /**
     * 鼠标悬停在元素上
     */
    HOVER,

    /**
     * 刷新页面
     */
    REFRESH,

    /**
     * 点击
     */
    CLICK;

    public static Command of(String value) {
        if (value == null) {
            return null;
        }
        switch (value.toLowerCase()) {
            case "input":
                return INPUT;
            case "click":
                return CLICK;
            case "open":
                return OPEN;
            case "hover":
                return HOVER;
            case "refresh":
                return REFRESH;
            default:
                return null;
        }
    }
}
