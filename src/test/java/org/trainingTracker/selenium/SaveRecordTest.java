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
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

public class SaveRecordTest {

    private static WebDriver driver;
    private static final String S_FIELD = "successSavingRecord";
    private static final String ER_FIELD = "errorSavingRecord";
    private static final String EXERCISE = "My Exercise";
    private static final String MG = "Espalda";
    private static final String WEIGHT = "10.2";
    private static final String SERIES = "4";
    private static final String REPETITIONS = "12";
    private static final String COMMENT = "Test comment";
    private static int ExerciseID;

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExerciseID = ExercisesDAO.addCustomExercise(EXERCISE, MG, USERNAME);
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
            fillForm(WEIGHT, SERIES, REPETITIONS, "");
            element = driver.findElement(By.name("Guardar"));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful. If not, the test will fail.
            assertFalse((driver.findElements(By.name(S_FIELD))).isEmpty());
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
    public void okTestWithCommentary(){
        WebElement element;
        try{
            // It clicks the "+" button to add a new record, it fills the fields and saves them.
            element = driver.findElement(By.name("add"));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            fillForm(WEIGHT, SERIES, REPETITIONS, COMMENT);
            element = driver.findElement(By.name("Guardar"));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process has been successful. If not, the test will fail.
            assertFalse((driver.findElements(By.name(S_FIELD))).isEmpty());
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
    public void blankFields(){
        String [] [] saveRecordArray = new String[8][3];
        String fields []= {WEIGHT, SERIES, REPETITIONS};
        fillArray(saveRecordArray, fields);
        WebElement saveRecord;
        WebElement element;
        try{
            saveRecord = driver.findElement(By.name("Guardar"));
            element = driver.findElement(By.name("add"));
            element.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            for (String [] s : saveRecordArray){
                quickFillForm(s[0], s[1], s[2]);
                saveRecord.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                // Checks if the process has been successful, which should not happen.
                assertTrue((driver.findElements(By.name(S_FIELD))).isEmpty());
                clearForm();
            }
            element = driver.findElement(By.name("Cancelar"));
            element.click();
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
     * Checks the process to save a record using non valid
     * inputs on each field.
     */
    @Test
    public void wrongInputs(){
        WebElement element;
        WebElement addButton;
        WebElement saveButton;
        addButton = driver.findElement(By.name("add"));
        saveButton = driver.findElement(By.name("Guardar"));
        try{
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a real number with a "," insted of a "." in the "Weight" field.
            fillForm("10,5", SERIES, REPETITIONS, COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ER_FIELD))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a real number instead of an integer in the "Series" field.
            fillForm(WEIGHT, "10.5", REPETITIONS, COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ER_FIELD))).isEmpty());
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Tries to input a real number instead of an integer in the "Repetitions" field.
            fillForm(WEIGHT, SERIES, "10.5", COMMENT);
            saveButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if there's been an error, which should happen.
            assertFalse((driver.findElements(By.name(ER_FIELD))).isEmpty());
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
        assertTrue((driver.getCurrentUrl().equals(HOME_URL)));
    }

    /*
     * Fills the form to add a new record.
     */
    private static void fillForm(String w, String s, String r, String c) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.id("weightExercise"));
        element.sendKeys(w);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id("seriesExercise"));
        element.sendKeys(s);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id("repetitionsExercise"));
        element.sendKeys(r);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.id("commentaryExercise"));
        element.sendKeys(c);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Fills the form to add a new record quicker than 'fillForm' method
     * and without a comment.
     */
    private static void quickFillForm(String w, String s, String r){
        WebElement element;
        element = driver.findElement(By.id("weightExercise"));
        element.sendKeys(w);
        element = driver.findElement(By.id("seriesExercise"));
        element.sendKeys(s);
        element = driver.findElement(By.id("repetitionsExercise"));
        element.sendKeys(r);
    }

    /*
     * Clears the form to save a new record.
     */
    private static void clearForm(){
        WebElement element;
        element = driver.findElement(By.id("weightExercise"));
        element.clear();
        element = driver.findElement(By.id("seriesExercise"));
        element.clear();
        element = driver.findElement(By.id("repetitionsExercise"));
        element.clear();
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        ExercisesDAO.deleteCustomExercise(ExerciseID);
        UsersDAO.deleteUser(USERNAME);
    }
}
