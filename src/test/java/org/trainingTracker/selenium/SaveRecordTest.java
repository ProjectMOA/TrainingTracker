package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to save a new record works correctly.
 */
@Ignore
public class SaveRecordTest {

    private static WebDriver driver;
    private static final String ADD_RECORD_ICON = "add";
    private static final String SAVE_BUTTON = "GuardarRec";
    private static final String CANCEL_BUTTON = "CancelarRec";
    private static final String WEIGHT_FIELD = "weightExercise";
    private static final String SERIES_FIELD = "seriesExercise";
    private static final String REPETITIONS_FIELD = "repetitionsExercise";
    private static final String COMMENTARY_FIELD = "commentaryExercise";
    private static final String WEIGHT = "10.2";
    private static final String SERIES = "4";
    private static final String REPETITIONS = "12";
    private static final String COMMENT = "Test comment";
    private static final String WEIGHT_FIELD_MAX_LENGTH = "weightMaxLength";
    private static final String SERIES_FIELD_MAX_LENGTH = "seriesMaxLength";
    private static final String REPETITIONS_FIELD_MAX_LENGTH = "repetitionsMaxLength";
    private static final String COMMENTARY_FIELD_MAX_LENGTH = "commentaryMaxLength";
    private static int ExerciseID;

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN REC: " + res);
        ExerciseID = ExercisesDAO.addCustomExercise(EXERCISE, MG, USERNAME);
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
    public void saveNewRecordTest(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name(ADD_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm(WEIGHT, SERIES, REPETITIONS, "");
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
    * the correct inputs, including the optional comment.
    */
    @Test
    public void saveNewRecordWithCommentaryTest(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name(ADD_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm(WEIGHT, SERIES, REPETITIONS, COMMENT);
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
     * Tests the process to save a new record with all the possible
     * combinations in the form.
     */
    @Test
    public void inputFieldsAreBlankTest(){
        String [] [] saveRecordArray = new String[8][3];
        String fields []= {WEIGHT, SERIES, REPETITIONS};
        fillArray(saveRecordArray, fields);
        WebElement saveRecord = driver.findElement(By.name(SAVE_BUTTON));
        WebElement element;
        try{
            element = driver.findElement(By.name(ADD_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            for (String [] s : saveRecordArray){
                quickFillForm(s[0], s[1], s[2], "");
                saveRecord.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                // Checks if the process has been successful, which should not happen.
                assertTrue((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty() &&
                    (driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
                clearForm();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                // Closes the pop-up if any assert fails.
                driver.findElement(By.name(CANCEL_BUTTON)).click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Checks the process to save a record using non valid
     * inputs on each field.
     */
    @Test
    public void wrongInputsTest(){
        WebElement addButton = driver.findElement(By.name(ADD_RECORD_ICON));
        WebElement saveButton = driver.findElement(By.name(SAVE_BUTTON));
        try{
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a real number instead of an integer in the "Series" field.
            fillForm(WEIGHT, "10.5", REPETITIONS, COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a real number instead of an integer in the "Repetitions" field.
            fillForm(WEIGHT, SERIES, "10.5", COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a character string instead of a number in the "Weight" field.
            fillForm("aaa", SERIES, REPETITIONS, COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a character string instead of a number in the "Series" field.
            fillForm(WEIGHT, "aaa", REPETITIONS, COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a character string instead of a number in the "Repetitions" field.
            fillForm(WEIGHT, SERIES, "aaa", COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
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

    @Test
    public void inputFieldsOverflowTest(){
        WebElement element;
        try{
            element = driver.findElement(By.name(ADD_RECORD_ICON));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            quickFillForm("100.102", SERIES, REPETITIONS, COMMENT);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse(driver.findElements(By.name(WEIGHT_FIELD_MAX_LENGTH)).isEmpty());
            clearForm();
            quickFillForm(WEIGHT, "1000", REPETITIONS, COMMENT);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse(driver.findElements(By.name(SERIES_FIELD_MAX_LENGTH)).isEmpty());
            clearForm();
            quickFillForm(WEIGHT, SERIES, "1000", COMMENT);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse(driver.findElements(By.name(REPETITIONS_FIELD_MAX_LENGTH)).isEmpty());
            clearForm();
            quickFillForm(WEIGHT, SERIES, REPETITIONS, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertFalse(driver.findElements(By.name(COMMENTARY_FIELD_MAX_LENGTH)).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                // Closes the pop-up if any assert fails.
                driver.findElement(By.name(CANCEL_BUTTON)).click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Fills the form to add a new record.
     */
    private static void fillForm(String w, String s, String r, String c) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.id(WEIGHT_FIELD));
        element.sendKeys(w);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id(SERIES_FIELD));
        element.sendKeys(s);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id(REPETITIONS_FIELD));
        element.sendKeys(r);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id(COMMENTARY_FIELD));
        element.sendKeys(c);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Fills the form to add a new record quicker than 'fillForm' method
     * and without a comment.
     */
    private static void quickFillForm(String w, String s, String r, String c){
        WebElement element;
        element = driver.findElement(By.id(WEIGHT_FIELD));
        element.sendKeys(w);
        element = driver.findElement(By.id(SERIES_FIELD));
        element.sendKeys(s);
        element = driver.findElement(By.id(REPETITIONS_FIELD));
        element.sendKeys(r);
        element = driver.findElement(By.id(COMMENTARY_FIELD));
        element.sendKeys(c);
    }

    /*
     * Clears the form to save a new record.
     */
    private static void clearForm(){
        WebElement element;
        element = driver.findElement(By.id(WEIGHT_FIELD));
        element.clear();
        element = driver.findElement(By.id(SERIES_FIELD));
        element.clear();
        element = driver.findElement(By.id(REPETITIONS_FIELD));
        element.clear();
        element = driver.findElement(By.id(COMMENTARY_FIELD));
        element.clear();
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        ExercisesDAO.deleteCustomExercise(ExerciseID);
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN REC: " + res);
    }
}
