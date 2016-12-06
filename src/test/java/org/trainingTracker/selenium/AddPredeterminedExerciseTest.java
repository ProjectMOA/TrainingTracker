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
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to add a new predetermined exercise
 * to the home page works correctly.
 */
public class AddPredeterminedExerciseTest {

    private static WebDriver driver;
    private static final String A_FIELD = "addPredButton";
    private static final String SEL_MG_FIELD = "selectMGPredetermined";
    private static final String SEL_EX_FIELD = "selectExercise";
    private static final String SC_FIELD = "successAddingExercise";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN PRED: " + res);
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
     * Tests the process of adding a new predetermined
     * exercise to the home page. The method test all
     * the existing muscle groups and the exercises
     * with that muscle groups.
     */
    @Test
    public void okPredeterminedTest(){
        try{
            selectPredefined();
            WebElement addButton = driver.findElement(By.name(A_FIELD));
            // Finds the select with the muscle group options
            Select select = new Select(driver.findElement(By.id(SEL_MG_FIELD)));
            Iterator<WebElement> iter1 = select.getOptions().iterator();
            // Skips the first option in the select (which is blank)
            iter1.next();
            // Iterates all the muscle group options
            while (iter1.hasNext()){
                iter1.next().click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                // Findes the select with the exercise options for the selected muscle group
                select = new Select(driver.findElement(By.id(SEL_EX_FIELD)));
                Iterator<WebElement> iter2 = select.getOptions().iterator();
                // Skips the first option in the select (which is blank)
                iter2.next();
                // Iterates all the exercise options for that muscle group
                while (iter2.hasNext()){
                    // Tries to add a new exercise with the selected options and check if the process has been successful.
                    iter2.next().click();
                    Thread.sleep(SLEEP_FOR_DISPLAY);
                    addButton.click();
                    Thread.sleep(SLEEP_FOR_DISPLAY);
                    assertFalse(driver.findElements(By.name(SC_FIELD)).isEmpty());
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Redirects to 'addExercise' page begining from the 'home' page and
     * selects the "Predetermined" option.
     */
    private static void selectPredefined() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name("predButton"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        System.out.println("EJECUTA DELETE EN PRED: " + UsersDAO.deleteUser(USERNAME));
    }
}

