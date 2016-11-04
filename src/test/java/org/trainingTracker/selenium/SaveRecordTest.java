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

@Ignore
public class SaveRecordTest {

    private static WebDriver driver;
    private static final String S_FIELD = "successSavingRecord";
    private static final String EXERCISE = "My Exercise";
    private static final String MG = "Espalda";
    private static final String WEIGHT = "10.2";
    private static final String SERIES = "4";
    private static final String REPETITIONS = "12";
    private static final String COMMENT = "Test comment";

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
            fillForm(WEIGHT, SERIES, REPETITIONS, "");
            element = driver.findElement(By.name("Guardar"));
            element.click();
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
            for (int i=0; i<saveRecordArray.length; i++){
                quickFillForm(saveRecordArray[i][0], saveRecordArray[i][1], saveRecordArray[i][2]);
                saveRecord.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
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
        // TODO: Remove created user and exercise on setUp. Remove exercise first.
    }
}
