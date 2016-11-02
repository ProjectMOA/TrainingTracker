package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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

    @BeforeClass
    public static void setUp(){
        driver = new FirefoxDriver();
    }

    /*
     * Tests the login process with correct inputs
     * in the form.
     */
    @Test
    public void okTest(){
        driver.get("http://localhost:8080");
        WebElement element;
        try{
            element = driver.findElement(By.name("username"));
            element.sendKeys("ruben");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name("password"));
            element.sendKeys("pass");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name("login"));
            element.click();
            Thread.sleep(SLEEP_FOR_LOAD);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
             // Checks if there's been an error in the login process. If so, a failure is forced.
            driver.findElement(By.name("error"));
            fail();
        }
        catch (NoSuchElementException e){
             // Checks if the redirection to the homepage has been made, knowing that the login
             // process has been successful.
            assertTrue((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));
        }

    }

    /*
     * Tests the login process with some or all fields in blank.
     */
    @Test
    public void blankFields(){
        driver.get("http://localhost:8080");
        WebElement element;
        WebElement login;
        try{
            // Checks whether the user logs in with all fields blank.
            login = driver.findElement(By.name("login"));
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));

            // Checks whether the user logs in with password field blank
            element = driver.findElement(By.name("username"));
            element.sendKeys("ruben");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));

            // Checks whether the user logs in with username field blank.
            element.clear();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name("password"));
            element.sendKeys("pass");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            login.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
    }
}
