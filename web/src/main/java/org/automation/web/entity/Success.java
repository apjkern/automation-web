package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.automation.web.entity.enums.Expression;

import java.util.List;

/**
 * 测试用例成功的条件
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Success {

    /**
     * 默认只要一个条件符合该测试用例就算通知
     */
    private Expression expression = Expression.OR;

    /**
     * 测试用例成功的条件
     */
    private List<Condition> conditions;

}
