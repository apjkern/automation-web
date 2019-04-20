package org.automation.web.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程工具类
 *
 * @author xuzhijie
 */
@Slf4j
public class ThreadUtil {

    public static void waitFor(final long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            // 等待失败
            log.error("线程异常", e);
        }
    }


}
