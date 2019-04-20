package org.automation.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.automation.web.entity.enums.Element;

/**
 * case成功的条件
 *
 * @author xuzhijie
 */
@Getter
@Setter
@Builder
public class Condition {
    /**
     * 操作的元素-ID定位
     */
    private String targetId;
    /**
     * 操作的元素-css定位
     */
    private String css;

    /**
     * display|exist
     */
    private Element element;

    /**
     * 描述
     */
    private String desc;
}
