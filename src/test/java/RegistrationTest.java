import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by inigo on 16/10/16.
 */
public class RegistrationTest {

    @Test
    public void RegistrarionSimpleTest(){
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080");
        WebElement register = driver.findElement(By.linkText("Registrarse"));
        register.click();
        driver.close();
        driver.quit();
    }
}
