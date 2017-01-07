package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to delete an existing exercise
 * in the user's home page works correctly..
 */
public class DeleteExerciseTest {

    private static WebDriver driver;
    private static ArrayList<Integer> customExercises;
    private static final int PREDETERMINED_EXERCISE_ID = 1;
    private static final String DELETE_BUTTON = "delete";
    private static final String CANCEL_BUTTON = "CancelarDel";
    private static final String TRASH_ICON = "Borrar";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN DEL: " + res);
        customExercises = new ArrayList<>();
        customExercises.add(ExercisesDAO.addCustomExercise(EXERCISE, MG, USERNAME));
        customExercises.add(ExercisesDAO.addCustomExercise(EXERCISE+"2", MG, USERNAME));
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
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
     * Tests the process to delete both a custom and a predetermined exercise.
     */
    @Test
    public void deleteExerciseTest(){
        WebElement delete = driver.findElement(By.name(TRASH_ICON));
        try{
            for(int n=0;n<2;n++){
                // It uses the same index number 'cause after the first iteration
                // the next exercise will be on the same possition as the deleted one.
                driver.findElements(By.name(DELETE_BUTTON)).get(0).click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                delete.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse((driver.findElements(By.name(SUCCESS_MESSAGE))).isEmpty());
            }
            customExercises.remove(0);
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
     * Tests the process to cancel an ongoing exercise deletion.
     */
    @Test
    public void cancelDeletionTest(){
        WebElement trash = driver.findElement(By.name(DELETE_BUTTON));
        WebElement cancel = driver.findElement(By.name(CANCEL_BUTTON));
        try{
            trash.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            cancel.click();
            // Checks if there's been a success or error message, which should not.
            assertTrue(driver.findElements(By.name(SUCCESS_MESSAGE)).isEmpty()
                && driver.findElements(By.name(ERROR_MESSAGE)).isEmpty());
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

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        Iterator <Integer> iter = customExercises.iterator();
        while (iter.hasNext()){
            ExercisesDAO.deleteCustomExercise(iter.next());
        }
        ExercisesDAO.deleteOwnExercise(USERNAME, PREDETERMINED_EXERCISE_ID);
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN DEL: " + res);
    }
}
