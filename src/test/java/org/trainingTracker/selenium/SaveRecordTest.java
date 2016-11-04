package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import static org.junit.Assert.assertFalse;
import static org.trainingTracker.selenium.TestUtils.*;

@Ignore
public class SaveRecordTest {

    private static WebDriver driver;
    private static final int SLEEP_FOR_DISPLAY = 1000;
    private static final int SLEEP_FOR_LOAD = 4000;
    private static final String STARTER_URL = "http://localhost:8080/#/starter";
    private static final String SIGNUP_URL = "http://localhost:8080/#/signUp";
    private static final String U_FIELD = "username";
    private static final String P_FIELD = "password";
    private static final String L_FIELD = "login";
    private static final String S_FIELD = "successSavingRecord";
    private static final String USERNAME = "test";
    private static final String EMAIL= "test@prueba.com";
    private static final String PASS = "pass";
    private static final String EXERCISE = "My Exercise";
    private static final String MG = "Espalda";
    private static final String WEIGHT = "10.2";
    private static final String SERIES = "4";
    private static final String REPETITIONS = "12";

    @BeforeClass
    public static void setUp(){
        //UsersDAO.addUser(USERNAME, PASS, EMAIL);
        //ExercisesDAO.addExercise(EXERCISE, MG, USERNAME);
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            login();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the process to save a new record with
     * the correct inputs.
     */
    @Test
    public void okTest(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name("add"));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.id("weightExercise"));
            element.sendKeys(WEIGHT);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.id("seriesExercise"));
            element.sendKeys(SERIES);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.id("repetitionsExercise"));
            element.sendKeys(REPETITIONS);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            element = driver.findElement(By.name("Guardar"));
            element.click();
            // Checks if the process has been successful. If not, the test will fail.
            assertFalse((driver.findElements(By.name(S_FIELD))).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Logs in the app with the created user.
     */
    private static void login() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.sendKeys(USERNAME);
        element = driver.findElement(By.name(P_FIELD));
        element.sendKeys(PASS);
        element = driver.findElement(By.name(L_FIELD));
        element.click();
        Thread.sleep(SLEEP_FOR_LOAD);
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        // TODO: Remove created user and exercise on setUp. Remove exercise first.
    }
}
