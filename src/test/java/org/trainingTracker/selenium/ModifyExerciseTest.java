package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;


/**
 * Test class to check if the process to modify an existing custom exercise
 * in the user's home page works correctly..
 */
public class ModifyExerciseTest {

    private static WebDriver driver;
    private static int ExerciseID;
    private static final String NAME_FIELD = "nameExercise";
    private static final String SEL_MG_FIELD = "muscleGroupExercise";
    private static final String G_FIELD = "GuardarMod";
    private static final String CAN_FIELD = "CancelarMod";
    private static final String MAX_NAME_FIELD = "nameMaxLength";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN DEL: " + res);
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
     * Tests the process of modifying an existing custom
     * exercise in the user's home page. The method test all
     * the existing muscle groups with custom names.
     */
    @Test
    public void modifyTest(){
        WebElement name = driver.findElement(By.id(NAME_FIELD));
        WebElement save = driver.findElement(By.name(G_FIELD));
        Select select = new Select(driver.findElement(By.id(SEL_MG_FIELD)));
        int numOptions = select.getOptions().size();
        try{
            //Iterates through all the muscle group options.
            for(int n=0; n<numOptions-1;n++){
                // Inserts a custom name for the exercise, tries to add the new exercise and
                // cheks if the process have been sucecssful
                driver.findElement(By.name("modify")).click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                name.clear();
                name.sendKeys(EXERCISE+n);
                Thread.sleep(SLEEP_FOR_DISPLAY);
                select.selectByIndex(n);
                Thread.sleep(SLEEP_FOR_DISPLAY);
                save.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse(driver.findElements(By.name(SC_FIELD)).isEmpty());
            }
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
    * Tests the process to modify an existing custom exercise leaving the
    * exercise name field blank, a field that is requested.
    */
    @Test
    public void blankNameTest(){
        WebElement pencil = driver.findElement(By.name("modify"));
        WebElement name = driver.findElement(By.id(NAME_FIELD));
        WebElement save = driver.findElement(By.name(G_FIELD));
        try{
            pencil.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            name.clear();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            save.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process to add a new exercise has been successful, which should not.
            assertTrue((driver.findElements(By.name(SC_FIELD))).isEmpty() && (driver.findElements(By.name(ER_FIELD))).isEmpty());
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
     * Tests the process to modify an existing custom exercise introducing an
     * exercise name larger than allowed.
     */
    @Test
    public void nameFieldOverflowTest(){
        WebElement pencil = driver.findElement(By.name("modify"));
        WebElement name = driver.findElement(By.id(NAME_FIELD));
        WebElement cancel = driver.findElement(By.name(CAN_FIELD));
        try{
            pencil.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            name.clear();
            name.sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if an error message has appeared because of the length of the exercise name.
            assertFalse((driver.findElements(By.name(MAX_NAME_FIELD))).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                cancel.click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        ExercisesDAO.deleteCustomExercise(ExerciseID);
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN DEL: " + res);
    }
}
