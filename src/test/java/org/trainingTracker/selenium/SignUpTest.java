package org.trainingTracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class to check if the registration process works correctly.
 */
@Ignore
public class SignUpTest {

    private static WebDriver driver;
    private static final int SLEEP_FOR_DISPLAY = 1000;
    private static final int SLEEP_FOR_LOAD = 4000;
    private static final String STARTER_URL = "http://localhost:8080/#/starter";
    private static final String HOME_URL = "http://localhost:8080/#/home";
    private static final String SIGNUP_URL = "http://localhost:8080/#/signUp";
    private static final String U_FIELD = "username";
    private static final String E_FIELD = "email";
    private static final String P_FIELD = "password";
    private static final String RP_FIELD = "rePassword";
    private static final String R_FIELD = "registrarse";
    private static final String ER_FIELD = "errorSignUp";
    private static final String USERNAME = "inigo";
    private static final String EMAIL= "inigo@prueba.com";
    private static final String PASS = "pass2";

    @BeforeClass
    public static void setUp(){
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter();
            goToSignUpPage();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the registration process with correct inputs
     * in the form.
     */
    @Ignore
    @Test
    public void okTest(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, PASS);
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Tries to find an error message.
            // If there's no error, the process has been successful and checks wheter the redirection has been made.
            if((driver.findElements(By.name(ER_FIELD))).isEmpty()){
                assertTrue((driver.getCurrentUrl().equals(HOME_URL)));
                goToStarter();
                goToSignUpPage();
            }
            //There's been an error, so a failure is forced.
            else{
                fail();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the registration process introducing a bad 'repass' input,
     * so both passwords doesn't match.
     */
    @Test
    public void wrongRepass(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, "pas");
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Tries to find an error message.
            // If there's an error, the process has failed and checks wheter the redirection has been made, which should not.
            if(!(driver.findElements(By.name(ER_FIELD))).isEmpty()){
                assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
            }
            //There hasn't been an error, so a failure is forced.
            else{
                fail();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        driver.navigate().refresh();
    }

    /*
    * Tests the registration process introducing a bad 'email' input.
    */
    @Test
    public void wrongEmail(){
        WebElement element;
        try{
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, "blablabla", PASS, PASS);
            element = driver.findElement(By.name(R_FIELD));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Checks whether the form is sent with the 'email' field having a wrong input.
            assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        driver.navigate().refresh();
    }

    /*
     * Tests the registration process with all the possible
     * combinations in the form.
     */
    @Test
    public void blankFields(){
        String [] [] signUpArray = new String[15][4];
        fillArray(signUpArray);
        WebElement element;
        WebElement registration;
        try{
            registration = driver.findElement(By.name(R_FIELD));
            // Iterates the matrix
            for(int i=0;i<signUpArray.length;i++){
                quickFillForm(signUpArray[i][0], signUpArray[i][1], signUpArray[i][2], signUpArray[i][3]);
                registration.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse((driver.getCurrentUrl().equals(HOME_URL)));
                clearForm();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        driver.navigate().refresh();
    }

    /**
     *  Redirects to 'signup' page begining from the 'starter' page.
     */
    private static void goToSignUpPage() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.linkText("Registrarse"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
        // Checks if the redirection have been made correctly.
        assertTrue((driver.getCurrentUrl().equals(SIGNUP_URL)));
    }

    /*
     * Checks the current URL and redirects to the
     * starter page if not already there.
     */
    private static void goToStarter() throws InterruptedException{
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
     * Fills the registration form.
     */
    private void fillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.sendKeys(user);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(E_FIELD));
        element.sendKeys(email);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(P_FIELD));
        element.sendKeys(pass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name(RP_FIELD));
        element.sendKeys(repass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Fills the registration form quicker than 'fillForm' method.
     */
    private void quickFillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.sendKeys(user);
        element = driver.findElement(By.name(E_FIELD));
        element.sendKeys(email);
        element = driver.findElement(By.name(P_FIELD));
        element.sendKeys(pass);
        element = driver.findElement(By.name(RP_FIELD));
        element.sendKeys(repass);
    }


    private void clearForm(){
        WebElement element;
        element = driver.findElement(By.name(U_FIELD));
        element.clear();
        element = driver.findElement(By.name(E_FIELD));
        element.clear();
        element = driver.findElement(By.name(P_FIELD));
        element.clear();
        element = driver.findElement(By.name(RP_FIELD));
        element.clear();
    }

    private void fillArray(String [][] array){
        array [0][0] = USERNAME;
        array [0][1] = EMAIL;
        array [0][2] = PASS;
        array [0][3] = "";
        array [1][0] = USERNAME;
        array [1][1] = EMAIL;
        array [1][2] = "";
        array [1][3] = PASS;
        array [2][0] = USERNAME;
        array [2][1] = EMAIL;
        array [2][2] = "";
        array [2][3] = "";
        array [3][0] = USERNAME;
        array [3][1] = "";
        array [3][2] = PASS;
        array [3][3] = PASS;
        array [4][0] = USERNAME;
        array [4][1] = "";
        array [4][2] = PASS;
        array [4][3] = "";
        array [5][0] = USERNAME;
        array [5][1] = "";
        array [5][2] = "";
        array [5][3] = PASS;
        array [6][0] = USERNAME;
        array [6][1] = "";
        array [6][2] = "";
        array [6][3] = "";
        array [7][0] = "";
        array [7][1] = EMAIL;
        array [7][2] = PASS;
        array [7][3] = PASS;
        array [8][0] = "";
        array [8][1] = EMAIL;
        array [8][2] = PASS;
        array [8][3] = "";
        array [9][0] = "";
        array [9][1] = EMAIL;
        array [9][2] = "";
        array [9][3] = PASS;
        array [10][0] = "";
        array [10][1] = EMAIL;
        array [10][2] = "";
        array [10][3] = "";
        array [11][0] = "";
        array [11][1] = "";
        array [11][2] = PASS;
        array [11][3] = PASS;
        array [12][0] = "";
        array [12][1] = "";
        array [12][2] = PASS;
        array [12][3] = "";
        array [13][0] = "";
        array [13][1] = "";
        array [13][2] = "";
        array [13][3] = PASS;
        array [14][0] = "";
        array [14][1] = "";
        array [14][2] = "";
        array [14][3] = "";

    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
    }
}
