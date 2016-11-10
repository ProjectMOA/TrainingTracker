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
import org.trainingTracker.database.valueObject.ExerciseVO;

import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to add a new custom exercise
 * to the home page works correctly.
 */
public class AddCustomExerciseTest {

    private static WebDriver driver;
    private static final String A_FIELD = "addCusButton";
    private static final String SEL_FIELD = "selectMGCustom";
    private static final String EX_N_FIELD = "customNameExercise";
    private static final String SC_FIELD = "successAddingExercise";
    private static final String MAX_NAME_FIELD = "nameMaxLength";
    private static final String EXERCISE_NAME = "My Exercise";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            login(driver);
            goToAddExercise(driver);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the process of adding a new custom
     * exercise to the home page. The method test all
     * the existing muscle groups with custom names.
     */
    @Test
    public void okCustomTest(){
        try{
            selectCustom();
            Select select;
            WebElement addButton = driver.findElement(By.name(A_FIELD));
            WebElement exerciseName = driver.findElement(By.id(EX_N_FIELD));
            // Finds the select with the muscle group options
            select = new Select(driver.findElement(By.id(SEL_FIELD)));
            Iterator<WebElement> iter1 = select.getOptions().iterator();
            // Skips the first option in the select (which is blank)
            iter1.next();
            // Iterates all the muscle group options
            while (iter1.hasNext()){
                // Inserts a custom name for the exercise, tries to add the new exercise and
                // cheks if the process have been sucecssful
                iter1.next().click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                exerciseName.sendKeys(EXERCISE_NAME);
                Thread.sleep(SLEEP_FOR_DISPLAY);
                addButton.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse(driver.findElements(By.name(SC_FIELD)).isEmpty());
                exerciseName.clear();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            driver.navigate().refresh();
        }
    }

    /*
     * Tests the process to add a new custom exercise leaving the
     * exercise name field blank, a field that is requested.
     */
    @Test
    public void blankExName(){
        try{
            selectCustom();
            Select select;
            WebElement addButton = driver.findElement(By.name(A_FIELD));
            // Finds the select with the muscle group options and select the first one.
            select = new Select(driver.findElement(By.id(SEL_FIELD)));
            select.selectByIndex(1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process to add a new exercise has been successful, which should not.
            assertTrue(driver.findElements(By.name(SC_FIELD)).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the process to add a new custom exercise introducing an
     * exercise name larger than allowed.
     */
    @Test
    public void ExNameOverflow(){
        try{
            selectCustom();
            Select select;
            WebElement exerciseName = driver.findElement(By.id(EX_N_FIELD));
            // Finds the select with the muscle group options and selects the first one.
            select = new Select(driver.findElement(By.id(SEL_FIELD)));
            select.selectByIndex(1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            exerciseName.sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if an error message because of the length of the exercise name has appeared.
            assertFalse(driver.findElements(By.name(MAX_NAME_FIELD)).isEmpty());
            exerciseName.clear();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Redirects to 'addExercise' page begining from the 'home' page and
     * selects the "Custom" option.
     */
    private static void selectCustom() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name("customButton"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        Iterator<ExerciseVO> iter = (ExercisesDAO.listUserExercises(USERNAME)).iterator();
        while(iter.hasNext()){
            ExercisesDAO.deleteCustomExercise(iter.next().getId());
        }
        UsersDAO.deleteUser(USERNAME);
    }

}
