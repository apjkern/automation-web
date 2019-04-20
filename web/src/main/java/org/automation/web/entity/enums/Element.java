package org.automation.web.entity.enums;

/**
 * 元素
 *
 * @author xuzhijie
 */
public enum Element {

    /**
     * 元素是否显示
     */
    DISPLAY,

    /**
     * 元素是否存在
     */
    EXIST;

    public static Element of(String value) {
        if (value == null) {
            return null;
        }
        switch (value.toLowerCase()) {
            case "display":
                return DISPLAY;
            case "exist":
                return EXIST;
            default:
                return null;
        }
    }
}
