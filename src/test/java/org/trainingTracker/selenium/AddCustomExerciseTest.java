package org.trainingTracker.selenium;


import org.junit.AfterClass;
import org.junit.BeforeClass;
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
    private static final String ADD_EXERCISE_BUTTON = "addCusButton";
    private static final String MG_SELECT = "selectMGCustom";
    private static final String EXERCISE_NAME_FIELD = "customNameExercise";
    private static final String SUCCESS_MESSAGE = "successAddingExercise";
    private static final String ERROR_MESSAGE = "errorAddingExercise";
    private static final String NAME_FIELD_MAX_LENGTH = "nameMaxLength";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN CUSTOM: " + res);
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
    public void addCustomExerciseTest(){
        WebElement exerciseName = null;
        try{
            selectCustom();
            exerciseName = driver.findElement(By.id(EXERCISE_NAME_FIELD));
            WebElement addButton = driver.findElement(By.name(ADD_EXERCISE_BUTTON));
            // Finds the select with the muscle group options
            Select select = new Select(driver.findElement(By.id(MG_SELECT)));
            Iterator<WebElement> iter1 = select.getOptions().iterator();
            // Skips the first option in the select (which is blank)
            iter1.next();
            // Iterates all the muscle group options
            while (iter1.hasNext()){
                // Inserts a custom name for the exercise, tries to add the new exercise and
                // cheks if the process have been sucecssful
                iter1.next().click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                exerciseName.sendKeys(EXERCISE);
                Thread.sleep(SLEEP_FOR_DISPLAY);
                addButton.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse(driver.findElements(By.name(SUCCESS_MESSAGE)).isEmpty());
                exerciseName.clear();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            exerciseName.clear();
        }
    }

    /*
     * Tests the process to add a new custom exercise leaving the
     * exercise name field blank, a field that is requested.
     */
    @Test
    public void nameFieldIsBlankTest(){
        try{
            selectCustom();
            Select select;
            WebElement addButton = driver.findElement(By.name(ADD_EXERCISE_BUTTON));
            // Finds the select with the muscle group options and select the first one.
            select = new Select(driver.findElement(By.id(MG_SELECT)));
            select.selectByIndex(1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            addButton.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if the process to add a new exercise has been successful, which should not.
            assertTrue((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty() && (driver.findElements(By.name(ERROR_MESSAGE))).isEmpty());
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
    public void nameFieldOverflowTest(){
        WebElement exerciseName = null;
        try{
            selectCustom();
            exerciseName = driver.findElement(By.id(EXERCISE_NAME_FIELD));
            Select select;
            // Finds the select with the muscle group options and selects the first one.
            select = new Select(driver.findElement(By.id(MG_SELECT)));
            select.selectByIndex(1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            exerciseName.sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Thread.sleep(SLEEP_FOR_DISPLAY);
            // Checks if an error message has appeared because of the length of the exercise name.
            assertFalse(driver.findElements(By.name(NAME_FIELD_MAX_LENGTH)).isEmpty());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            exerciseName.clear();
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
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN CUSTOM: " + res);
    }

}
