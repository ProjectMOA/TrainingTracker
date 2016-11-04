package org.trainingTracker.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestUtils {

    public static final String STARTER_URL = "http://localhost:8080/#/starter";
    public static final int SLEEP_FOR_DISPLAY = 1000;
    public static final int SLEEP_FOR_LOAD = 3000;
    public static final String HOME_URL = "http://localhost:8080/#/home";
    public static final String SIGNUP_URL = "http://localhost:8080/#/signUp";
    public static final String U_FIELD = "username";
    public static final String P_FIELD = "password";
    public static final String L_FIELD = "login";
    public static final String USERNAME = "test";
    public static final String EMAIL= "test@prueba.com";
    public static final String PASS = "pass";

    /*
     * Checks the current URL and redirects to the
     * starter page if not already there.
     */
   public static void goToStarter(WebDriver driver) throws InterruptedException{
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
     * Fills an array with all the possible combinations for the fields in the registration form.
     */
    public static void fillArray(String [][] array, String[] fields){
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
}
