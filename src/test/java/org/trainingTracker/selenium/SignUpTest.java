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
    private static final String USERNAME = "ruben";
    private static final String EMAIL= "prueba@prueba.com";
    private static final String PASS = "pass";

    @BeforeClass
    public static void setUp(){
        driver = new FirefoxDriver();
    }

    /*
     * Tests the registration process with correct inputs
     * in the form.
     */
    @Test
    public void okTest(){
        driver.get("http://localhost:8080");
        WebElement element;
        try{
            // It calls a function to go to the signUp page.
            goToSignUpPage();
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, PASS);
            element = driver.findElement(By.name("registrarse"));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        try{
            // Checks if there's been an error in the registration process. If so, a failure is forced.
            driver.findElement(By.name("error"));
            fail();
        }
        catch (NoSuchElementException e){
            // Checks if the redirection to the homepage has been made, knowing that the registration
            // process has been successful.
            assertTrue((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));
		}
    }

    /*
     * Tests the registration process introducing a bad 'repass' input,
     * so both passwords doesn't match.
     */
    @Test
    public void wrongRepass(){
        driver.get("http://localhost:8080");
        WebElement element;
        try{
            // It calls a function to go to the signUp page.
            goToSignUpPage();
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, EMAIL, PASS, "pas");
            element = driver.findElement(By.name("registrarse"));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
            // Checks if there's been an error in the registration process. If so, the test has been
            // successful, since the two passwords were different.
            driver.findElement(By.name("error"));
        }
        catch (NoSuchElementException e){
            // Checks if the registration process has been successful and a redirection to the homepage
            // has been made, which should not happen.
            assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));
        }
    }

    /*
    * Tests the registration process introducing a bad 'email' input.
    */
    @Test
    public void wrongEmail(){
        driver.get("http://localhost:8080");
        WebElement element;
        try{
            // It calls a function to go to the signUp page.
            goToSignUpPage();
            // It calls a function to fill the registration form and clicks the registration button.
            fillForm(USERNAME, "blablabla", PASS, PASS);
            element = driver.findElement(By.name("registrarse"));
            element.click();
            // In this case, the sleep time is larger cause it has to wait for the request
            // to reach the backend and the database, return and redirect to the homepage
            // if the registration has been successful.
            Thread.sleep(SLEEP_FOR_LOAD);
            // Checks whether the form is sent with the 'email' field having a wrong input.
            assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the registration process with all the possible
     * combinations in the form.
     */
    @Ignore
    @Test
    public void blankFields(){
        String [] [] signUpArray = new String[15][4];
        fillArray(signUpArray);
        driver.get("http://localhost:8080");
        WebElement element;
        WebElement registration;
        try{
            // It calls a function to go to the signUp page.
            goToSignUpPage();
            registration = driver.findElement(By.name("registrarse"));
            // Iterates the matrix
            for(int i=0;i<signUpArray.length;i++){
                quickFillForm(signUpArray[i][0], signUpArray[i][1], signUpArray[i][2], signUpArray[i][3]);
                registration.click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                assertFalse((driver.getCurrentUrl().equals("http://localhost:8080/#/home")));
                clearForm();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     *  Redirects to 'signup' page begining from the 'starter' page.
     */
    private void goToSignUpPage() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.linkText("Registrarse"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
        // Checks if the redirection have been made correctly.
        assertTrue((driver.getCurrentUrl().equals("http://localhost:8080/#/signUp")));
    }

    /*
     * Fills the registration form.
     */
    private void fillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name("username"));
        element.sendKeys(user);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name("email"));
        element.sendKeys(email);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name("password"));
        element.sendKeys(pass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
        element = driver.findElement(By.name("rePassword"));
        element.sendKeys(repass);
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    /*
     * Fills the registration form quicker than 'fillForm' method.
     */
    private void quickFillForm(String user, String email, String pass, String repass) throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name("username"));
        element.sendKeys(user);
        element = driver.findElement(By.name("email"));
        element.sendKeys(email);
        element = driver.findElement(By.name("password"));
        element.sendKeys(pass);
        element = driver.findElement(By.name("rePassword"));
        element.sendKeys(repass);
    }


    private void clearForm(){
        WebElement element;
        element = driver.findElement(By.name("username"));
        element.clear();
        element = driver.findElement(By.name("email"));
        element.clear();
        element = driver.findElement(By.name("password"));
        element.clear();
        element = driver.findElement(By.name("rePassword"));
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
