import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by inigo on 16/10/16.
 */
public class SeleniumFirstTest {

    @Test
    public void test(){
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080");
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
            System.out.print("InterruptedException");
        }
        WebElement register = driver.findElement(By.linkText("Registrarse"));
        register.click();
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
            System.out.print("InterruptedException");
        }
        driver.close();
        driver.quit();
    }
}
