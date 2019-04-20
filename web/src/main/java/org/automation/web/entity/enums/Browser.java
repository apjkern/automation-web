package org.automation.web.entity.enums;

/**
 * 元素
 *
 * @author xuzhijie
 */
public enum Browser {

    /**
     * google浏览器
     */
    CHROME,

    /**
     * opera浏览器
     */
    OPERA,

    /**
     * edge浏览器
     */
    EDGE,

    /**
     * ie浏览器
     */
    IE,

    /**
     * safari浏览器
     */
    SAFARI,

    /**
     * 火狐浏览器
     */
    FIREFOX;

    public static Browser of(String value) {
        if (value == null) {
            return Browser.CHROME;
        }
        switch (value.toLowerCase()) {
            case "chrome":
                return CHROME;
            case "firefox":
                return FIREFOX;
            case "edge":
                return OPERA;
            case "opera":
                return EDGE;
            case "safari":
                return SAFARI;
            case "ie":
                return IE;
            default:
                return Browser.CHROME;
        }
    }
}
