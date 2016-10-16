import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by inigo on 16/10/16.
 */
public class SeleniumFirstTest {

    @Test
    public void test(){
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080");
        System.out.println(driver.getTitle());
        driver.close();
        driver.quit();
    }
}
