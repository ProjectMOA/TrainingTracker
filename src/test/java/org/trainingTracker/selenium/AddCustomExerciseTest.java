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
import static org.trainingTracker.selenium.TestUtils.goToStarter;
import static org.trainingTracker.selenium.TestUtils.login;

/**
 * Test class to check if the process to add a new custom exercise
 * to the home page works correctly.
 */
@Ignore
public class AddCustomExerciseTest {

    private static WebDriver driver;
    private static final String A_FIELD = "addCusButton";
    private static final String SEL_FIELD = "selectMGCustom";
    private static final String EX_N_FIELD = "customNameExercise";
    private static final String SC_FIELD = "successAddingExercise";
    private static final String EXERCISE_NAME = "My Exercise";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            login(driver);
            goToAddCustomExercise();
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
    public static void okPredeterminedTest(){
        Select select;
        WebElement addButton = driver.findElement(By.name(A_FIELD));
        WebElement exerciseName = driver.findElement(By.id(EX_N_FIELD));
        try{
            // Finds the select with the muscle group options
            select = new Select(driver.findElement(By.id(SEL_FIELD)));
            Iterator<WebElement> iter1 = select.getOptions().iterator();
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
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Redirects to 'addExercise' page begining from the 'home' page and
     * selects the "Custom" option.
     */
    private static void goToAddCustomExercise() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.id("addExercise"));
        element.click();
        Thread.sleep(SLEEP_FOR_LOAD);
        assertTrue(driver.getCurrentUrl().equals(ADD_NEW_URL));
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
