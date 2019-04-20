import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Demo {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "E:\\idea-workspace\\automation-web\\drivers\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();

        // 打开需要测试的网页
        driver.get("http://192.168.1.107:6060/login");
        driver.findElement(By.id("user")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("bitnei");
        driver.findElement(By.id("submit")).click();

        waitFor(1000);

        WebElement errorAlert = find(driver, By.className("el-alert--error"));
        if (errorAlert != null) {
            System.err.println(errorAlert.findElement(By.className("el-alert__title")).getText());
            driver.quit();
            return;
        }

        // 定位左侧菜单 条件
        driver.findElement(By.className("top-menu")).findElements(By.tagName("li")).get(4).click();

        waitFor(1000);


        // 打开故障码报警设置页面 表达式 expression expression
        driver.get("http://192.168.1.107:6060/codeType");
        WebElement element = find(driver, By.className("xy-button-left"));
        if (element == null) {
            return;
        }

        driver.findElement(By.className("xy-button-item")).findElement(By.tagName("button")).click();

        waitFor(1000);

        // 填写内容
        driver.findElement(By.tagName("input")).sendKeys("selenium-输入");

        waitFor(5000);

        driver.quit();
    }

    private static void waitFor(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    private static WebElement find(ChromeDriver driver, By selector) {
        try {
            return driver.findElement(By.className("el-alert--error"));
        } catch (Exception e) {
            System.err.println("找不到Element");
        }
        return null;
    }

}
