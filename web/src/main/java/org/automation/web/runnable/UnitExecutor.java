package org.automation.web.runnable;

import lombok.extern.slf4j.Slf4j;
import org.automation.web.entity.Group;
import org.automation.web.entity.Unit;
import org.automation.web.entity.enums.Browser;
import org.automation.web.util.ThreadUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.concurrent.TimeUnit;

/**
 * 单元测试执行器
 *
 * @author xuzhijie
 */
@Slf4j
public class UnitExecutor {

    public static void execute(Unit unit) {
        if (unit == null) {
            return;
        }
        System.setProperty("webdriver.chrome.driver", unit.getDriver());
        WebDriver driver = initDriver(unit.getBrowser());
        driver.manage().window().maximize();
        // 设置隐式等待
        driver.manage().timeouts().implicitlyWait(unit.getDomQueryTimeout(), TimeUnit.SECONDS);
        for (int groupIdx = 0; groupIdx < unit.getGroups().size(); groupIdx++) {
            Group group = unit.getGroups().get(groupIdx);
            if (group.getDelay() > 0) {
                ThreadUtil.waitFor(group.getDelay());
            }
            for (int caseIdx = 0; caseIdx < group.getCases().size(); caseIdx++) {
                boolean executeState = new CaseRunnable(group.getName(), group.getCases().get(caseIdx), driver).getAsBoolean();
                if (group.isInterrupt() && !executeState) {
                    log.warn("测试流程出现异常，已被中断");
                    if (unit.isClose()) {
                        driver.quit();
                    }
                    return;
                }
            }
        }
        if (unit.isClose()) {
            driver.quit();
        }
    }

    private static WebDriver initDriver(Browser browser) {
        WebDriver driver;
        switch (browser) {
            case IE:
                driver = new InternetExplorerDriver();
                break;
            case EDGE:
                driver = new EdgeDriver();
                break;
            case OPERA:
                driver = new OperaDriver();
                break;
            case CHROME:
                driver = new ChromeDriver();
                break;
            case SAFARI:
                driver = new SafariDriver();
                break;
            case FIREFOX:
                driver = new FirefoxDriver();
                break;
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;
        }
        return driver;
    }

}
