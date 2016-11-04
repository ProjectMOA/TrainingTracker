package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the registration process works correctly.
 */
@Ignore
public class SignUpTest {

    private static WebDriver driver;
    private static final String E_FIELD = "email";
    private static final String RP_FIELD = "rePassword";
    private static final String R_FIELD = "registrarse";
    private static final String ER_FIELD = "errorSignUp";

    @BeforeClass
    public static void setUp(){
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            goToSignUpPage();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the registration process with correct inputs
     * in the form.
     */
    @Test
    public void okTest(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, PASS);
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Tries to find an error message. If there's an error, test will fail.
            assertTrue((driver.findElements(By.name(ER_FIELD))).isEmpty());
            // If there's no error, the process has been successful and checks wheter the redirection has been made.
            assertTrue((driver.getCurrentUrl().equals(HOME_URL)));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try{
                goToStarter(driver);
                goToSignUpPage();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Tests the registration process introducing a bad 'repass' input,
     * so both passwords doesn't match.
     */
    @Test
    public void wrongRepass(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, "pas");
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Tries to find an error message. If there's no error, test will fail.
            assertFalse((driver.findElements(By.name(ER_FIELD))).isEmpty());
            // If there's an error, the process has failed and checks wheter the redirection has been made, which should not.
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            driver.navigate().refresh();
        }
    }

    /*
    * Tests the registration process introducing a bad 'email' input.
    */
    @Test
    public void wrongEmail(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, "blablabla", PASS, PASS);
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Checks whether the form is sent with the 'email' field having a wrong input.
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            driver.navigate().refresh();
        }
    }

    /*
     * Tests the registration process with all the possible
     * combinations in the form.
     */
    @Test
    public void blankFields(){
        String [] [] signUpArray = new String[15][4];
        String fields []= {USERNAME, EMAIL, PASS, PASS};
        fillArray(signUpArray, fields);
        WebElement registration;
        try{
            registration = driver.findElement(By.name(R_FIELD));
            // Iterates the matrix
            for(int i=0;i<signUpArray.length;i++){
                quickFillForm(signUpArray[i][0], signUpArray[i][1], signUpArray[i][2], signUpArray[i][3]);
                registration.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
                clearForm();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            driver.navigate().refresh();
        }
    }

    /**
     *  Redirects to 'signup' page begining from the 'starter' page.
     */
    private static void goToSignUpPage() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.linkText("Registrarse"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
        // Checks if the redirection have been made correctly.
        assertTrue((driver.getCurrentUrl().equals(SIGNUP_URL)));
    }

    /*
     * Fills the registration form.
     */
    private void fillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.sendKeys(user);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(E_FIELD));
        element.sendKeys(email);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(P_FIELD));
        element.sendKeys(pass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(RP_FIELD));
        element.sendKeys(repass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Fills the registration form quicker than 'fillForm' method.
     */
    private void quickFillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.sendKeys(user);
        element = driver.findElement(By.name(E_FIELD));
        element.sendKeys(email);
        element = driver.findElement(By.name(P_FIELD));
        element.sendKeys(pass);
        element = driver.findElement(By.name(RP_FIELD));
        element.sendKeys(repass);
    }

    /*
     * Clears the registration form.
     */
    private void clearForm(){
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.clear();
        element = driver.findElement(By.name(E_FIELD));
        element.clear();
        element = driver.findElement(By.name(P_FIELD));
        element.clear();
        element = driver.findElement(By.name(RP_FIELD));
        element.clear();
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        // TODO: Remove created user on okTest.
    }
}
