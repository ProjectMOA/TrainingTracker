package org.trainingTracker.selenium;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;
import static org.trainingTracker.selenium.TestUtils.goToStarter;
import static org.trainingTracker.selenium.TestUtils.login;

/**
 * Test class to check if the process to save a new cardiovascular record works correctly.
 */
public class SaveCardioRecordTest {

    private static WebDriver driver;
    private static final int CARDIO_EXERCISE_ID = 1;
    private static final String ADD_CARDIO_RECORD_ICON = "cardioAdd";
    private static final String SAVE_BUTTON = "GuardiarCardioRec";
    private static final String CANCEL_BUTTON = "CancelarCardioRec";
    private static final String TIME_FIELD = "timeCardio";
    private static final String DISTANCE_FIELD = "distanceCardio";
    private static final String INTENSITY_SELECT = "intensityCardio";
    private static final String TIME = "30";
    private static final String DISTANCE = "10";
    private static final int INTENSITY = 1;

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN CARDIO_REC: " + res);
        CardioExercisesDAO.addDefaultExercise(1, USERNAME);
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            login(driver);
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
    public void saveNewCardioRecordTest(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name(ADD_CARDIO_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm(TIME, INTENSITY, "");
            element = driver.findElement(By.name(SAVE_BUTTON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful. If not, the test will fail.
            assertFalse((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Tests the process to save a new record with
     * the correct inputs, including the optional "distance" field.
     */
    @Test
    public void saveNewCardioRecordTestWithDistance(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name(ADD_CARDIO_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm(TIME, INTENSITY, DISTANCE);
            element = driver.findElement(By.name(SAVE_BUTTON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful. If not, the test will fail.
            assertFalse((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Tests the process to save a new cardiovascular record with all the possible
     * combinations in the form.
     */
    @Test
    public void inputFieldsAreBlankTest(){
        WebElement saveButton = driver.findElement(By.name(SAVE_BUTTON));
        try{
            WebElement element = driver.findElement(By.name(ADD_CARDIO_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm("", INTENSITY, "");
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful, which should not happen.
            assertTrue((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty() &&
                (driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            clearForm();
            fillForm(TIME, 0, "");
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful, which should not happen.
            assertTrue((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty() &&
                (driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            clearForm();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                driver.findElement(By.name(CANCEL_BUTTON)).click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }


    /*
     * Checks the process to save a cardiovascular record using non valid
     * inputs on each field.
     */
    @Test
    public void wrongInputsTest(){
        WebElement addButton = driver.findElement(By.name(ADD_CARDIO_RECORD_ICON));
        WebElement saveButton = driver.findElement(By.name(SAVE_BUTTON));
        try{
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a character string insted of a number in the "time" field.
            fillForm("aaa", INTENSITY, "");
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a character string insted of a number in the "Distance" field.
            fillForm(TIME, INTENSITY, "aaa");
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Fills the form to add a new  cardiovascular record.
     */
    private static void fillForm(String t, int i, String d) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.id(TIME_FIELD));
        element.sendKeys(t);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        Select intensity = new Select(driver.findElement(By.id(INTENSITY_SELECT)));
        intensity.selectByIndex(INTENSITY);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id(DISTANCE_FIELD));
        element.sendKeys(d);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Clears the form to save a new cardiovascular record.
     */
    private static void clearForm(){
        WebElement element;
        element = driver.findElement(By.id(TIME_FIELD));
        element.clear();
        element = driver.findElement(By.id(DISTANCE_FIELD));
        element.clear();
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        CardioExercisesDAO.deleteOwnExercise(USERNAME, CARDIO_EXERCISE_ID);
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN CARDIO_REC: " + res);
    }
}
