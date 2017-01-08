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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to filter existing exercises works correctly.
 */
public class FilterTest {

    private static WebDriver driver;
    private static ArrayList<Integer> predeterminedExercises;
    //Press banca-Pecho
    private static final int PREDETERMINED_EXERCISE1_ID = 1;
    //Dominada con peso libre-Espalda
    private static final int PREDETERMINED_EXERCISE2_ID = 3;
    //Remo-Espalda
    private static final int PREDETERMINED_EXERCISE3_ID = 4;
    private static final String EXERCISE_CARD = "exercise-card";
    private static final String MG_SELECT = "selectMGFilter";
    private static final String NAME_FILTER_FIELD = "nameExerciseFilter";
    private static final String CLEAR_FILTER_BUTTON = "clear";
    private static final String MG_TO_FILTER = "Espalda";
    private static final String NAME_TO_FILTER = "Remo";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN FIL: " + res);
        predeterminedExercises = new ArrayList<>();
        predeterminedExercises.add(ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE1_ID, USERNAME));
        predeterminedExercises.add(ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE2_ID, USERNAME));
        predeterminedExercises.add(ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE3_ID, USERNAME));
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
    * Tests the process to filter existing exercises by
    * muscular group and the process to clear the filters.
    */
    @Test
    public void filterByMGTest(){
        List<WebElement> exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
        int exNum = exercisesList.size();
        WebElement clear = driver.findElement(By.name(CLEAR_FILTER_BUTTON));
        try{
            Select MGSelect = new Select(driver.findElement(By.id(MG_SELECT)));
            MGSelect.selectByValue(MG_TO_FILTER);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
            int newExNum = exercisesList.size();
            assertTrue(exNum!=newExNum && newExNum==2);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            clear.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertTrue((MGSelect.getFirstSelectedOption().getText()).equals("Filtra por grupo muscular"));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                clear.click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
    * Tests the process to filter existing exercises by
    * name and the process to clear the filters.
    */
    @Test
    public void filterByNameTest(){
        List<WebElement> exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
        int exNum = exercisesList.size();
        WebElement clear = driver.findElement(By.name(CLEAR_FILTER_BUTTON));
        try{
            WebElement name = driver.findElement(By.id(NAME_FILTER_FIELD));
            name.sendKeys(NAME_TO_FILTER);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
            int newExNum = exercisesList.size();
            assertTrue(exNum!=newExNum && newExNum==1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            clear.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertTrue((name.getText()).equals(""));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                clear.click();
                Thread.sleep(SLEEP_FOR_LOAD);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
    * Tests the process to filter existing exercises by
    * muscular group and name and the process to clear the filters.
    */
    @Test
    public void filterByMGAndNameTest(){
        List<WebElement> exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
        int exNum = exercisesList.size();
        WebElement clear = driver.findElement(By.name(CLEAR_FILTER_BUTTON));
        try{
            Select MGSelect = new Select(driver.findElement(By.id(MG_SELECT)));
            MGSelect.selectByValue(MG_TO_FILTER);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            WebElement name = driver.findElement(By.id(NAME_FILTER_FIELD));
            name.sendKeys(NAME_TO_FILTER);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            exercisesList = driver.findElements(By.tagName(EXERCISE_CARD));
            int newExNum = exercisesList.size();
            assertTrue(exNum!=newExNum && newExNum==1);
            Thread.sleep(SLEEP_FOR_DISPLAY);
            clear.click();
            Thread.sleep(SLEEP_FOR_DISPLAY);
            assertTrue((name.getText()).equals("") &&
                (MGSelect.getFirstSelectedOption().getText()).equals("Filtra por grupo muscular"));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            try {
                clear.click();
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
        Iterator <Integer> iter = predeterminedExercises.iterator();
        while (iter.hasNext()){
            ExercisesDAO.deleteOwnExercise(USERNAME, iter.next());
        }
        boolean res = UsersDAO.deleteUser(USERNAME);
        System.out.println("***** EJECUTA DELETE EN FIL: " + res);
    }
}
