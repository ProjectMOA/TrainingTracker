package org.trainingTracker.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;

class TestUtils {

    static final String STARTER_URL = "http://localhost:8080/#/starter";
    static final String HOME_URL = "http://localhost:8080/#/home";
    static final String SIGNUP_URL = "http://localhost:8080/#/signUp";
    static final String ADD_NEW_URL ="http://localhost:8080/#/addExercise";
    static final int SLEEP_FOR_DISPLAY = 1000;
    static final int SLEEP_FOR_LOAD = 3000;
    static final String USERNAME_FIELD = "username";
    static final String PASSWORD_FIELD = "password";
    static final String LOGIN_BUTTON = "login";
    static final String SUCCESS_MESSAGE = "successWithExerciseAction";
    static final String ERROR_MESSAGE = "errorWithExerciseAction";
    static final String USERNAME = "test";
    static final String EMAIL= "test@prueba.com";
    static final String PASS = "pass";
    static final String EXERCISE = "My Exercise";
    static final String MG = "Espalda";

    /*
     * Checks the current URL and redirects to the
     * starter page if not already there.
     */
   static void goToStarter(WebDriver driver) throws InterruptedException{
        WebElement element;
        if(driver.getCurrentUrl().equals(SIGNUP_URL)){
            element = driver.findElement(By.id("hombeButton"));
            element.click();
        }
        else if (!driver.getCurrentUrl().equals(STARTER_URL)){
            element = driver.findElement(By.linkText("Salir"));
            element.click();
        }
        Thread.sleep(SLEEP_FOR_LOAD);
    }

    /*
     * Fills an array with all the possible combinations for the fields in the registration and
     * new record forms.
     */
    static void fillArray(String [][] array, String[] fields){
        int n = array[0].length;
        int rows = (int) Math.pow(2,n);
        int aux [][] = new int[array.length][array[0].length];

        for (int i=0; i<rows-1; i++) {
            for (int j = n - 1; j >= 0; j--) {
                aux[i][j] = (i/(int) Math.pow(2, j))%2;
            }
        }

        for (int i=0; i<aux.length; i++){
            for (int j=0; j<aux[i].length; j++){
                if(aux[i][j]==1){
                    array[i][j] = fields[j];
                }
                else{
                    array[i][j] = "";
                }
            }
        }
    }

    /*
    * Logs in the app with the created user.
    */
    static void login(WebDriver driver) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(USERNAME_FIELD));
        element.sendKeys(USERNAME);
        element = driver.findElement(By.name(PASSWORD_FIELD));
        element.sendKeys(PASS);
        element = driver.findElement(By.name(LOGIN_BUTTON));
        element.click();
        Thread.sleep(SLEEP_FOR_LOAD);
        assertTrue((driver.getCurrentUrl().equals(HOME_URL)));
    }

    static void goToAddExercise(WebDriver driver) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.id("addExercise"));
        element.click();
        Thread.sleep(SLEEP_FOR_LOAD);
        assertTrue(driver.getCurrentUrl().equals(ADD_NEW_URL));
    }
}
