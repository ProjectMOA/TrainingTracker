package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class to check if the login process works correctly.
 */
@Ignore
public class SignInTest {

    private static WebDriver driver;
    private static final int SLEEP_FOR_DISPLAY = 1000;
    private static final int SLEEP_FOR_LOAD = 4000;
    private static final String STARTER_URL = "http://localhost:8080/#/starter";
    private static final String HOME_URL = "http://localhost:8080/#/home";
    private static final String SIGNUP_URL = "http://localhost:8080/#/signUp";
    private static final String U_FIELD = "username";
    private static final String P_FIELD = "password";
    private static final String L_FIELD = "login";
    private static final String ER_FIELD = "errorSignIn";
    private static final String USERNAME = "ruben";
    private static final String PASS = "pass1";

    @BeforeClass
    public static void setUp(){
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
    }

    /*
     * Tests the login process with correct inputs
     * in the form.
     */
    @Test
    public void okTest(){
        WebElement element;
        try{
            goToStarter();
            element = driver.findElement(By.name(U_FIELD));
            element.sendKeys(USERNAME);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name(P_FIELD));
            element.sendKeys(PASS);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name(L_FIELD));
            element.click();
            Thread.sleep(SLEEP_FOR_LOAD);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
             // Checks if there's been an error in the login process. If so, a failure is forced.
            driver.findElement(By.name(ER_FIELD));
            fail();
        }
        catch (NoSuchElementException e){
             // Checks if the redirection to the homepage has been made, knowing that the login
             // process has been successful.
            assertTrue((driver.getCurrentUrl().equals(HOME_URL)));
        }

    }

    /*
     * Tests the login process with a wrong password.
     */
    @Test
    public void wrongPass(){
        WebElement element;
        try{
            goToStarter();
            element = driver.findElement(By.name(U_FIELD));
            element.sendKeys(USERNAME);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name(P_FIELD));
            element.sendKeys("pas");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name(L_FIELD));
            element.click();
            Thread.sleep(SLEEP_FOR_LOAD);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
            // Checks if there's been an error in the login process, which should happen.
            driver.findElement(By.name(ER_FIELD));
        }
        catch (NoSuchElementException e){
            // Checks if the redirection to the homepage has been made, knowing that the login
            // process has failed.
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
        }
        driver.navigate().refresh();
    }

    /*
     * Tests the login process with some or all fields in blank.
     */
    @Test
    public void blankFields(){
        WebElement element;
        WebElement login;
        try{
            goToStarter();
            // Checks whether the user logs in with all fields blank.
            login = driver.findElement(By.name(L_FIELD));
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));

            // Checks whether the user logs in with password field blank
            element = driver.findElement(By.name(U_FIELD));
            element.sendKeys(USERNAME);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));

            // Checks whether the user logs in with username field blank.
            element.clear();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name(P_FIELD));
            element.sendKeys(PASS);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        driver.navigate().refresh();
    }

    /*
     * Checks the current URL and redirects to the
     * starter page if not already there.
     */
    private static void goToStarter() throws InterruptedException{
        WebElement element;
        if(driver.getCurrentUrl().equals(SIGNUP_URL)){
            element = driver.findElement(By.id("hombeButton"));
            element.click();
        }
        else if (!driver.getCurrentUrl().equals(STARTER_URL)){
            element = driver.findElement(By.linkText("Salir"));
            element.click();
        }
        Thread.sleep(SLEEP_FOR_LOAD);
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
    }
}
