package org.automation.web.entity.enums;

/**
 * 表达式
 *
 * @author xuzhijie
 */
public enum Expression {

    /**
     * 所有条件通过该case才算成功
     */
    AND,

    /**
     * 有一个条件通过该case都算成功
     */
    OR;

    public static Expression of(String value) {
        if (value == null) {
            return Expression.OR;
        }
        switch (value.toLowerCase()) {
            case "and":
                return AND;
            default:
                return OR;
        }
    }
}
