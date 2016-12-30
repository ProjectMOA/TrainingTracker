package org.trainingTracker.database.dataAccesObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.trainingTracker.database.valueObject.UserVO;

import static org.junit.Assert.*;

/**
 * Created by sergio on 13/12/16.
 */
public class UsersDAOTest {

    public static final String USR = "usr_Users";

    @Before
    public void setUp() throws Exception {
        UsersDAO.addUser(USR,"pass","mail");
    }

    @After
    public void tearDown() throws Exception {
        UsersDAO.deleteUser(USR);
    }

    @Test
    public void addUser() throws Exception {
        Assert.assertTrue( UsersDAO.addUser("usr1","pass","mail1") ); //Check it inserts OK

        Assert.assertFalse( UsersDAO.addUser("","pass","mail2") ); //nick can't be empty
        Assert.assertFalse( UsersDAO.addUser("usr2","","mail3") ); //pass can't be empty
        Assert.assertFalse( UsersDAO.addUser("usr2","pass","") ); //mail cant be empty

        Assert.assertFalse( UsersDAO.addUser(null,"pass","mail4") ); //nick can't be null
        Assert.assertFalse( UsersDAO.addUser("usr3",null,"mail5") ); //pass can't be null
        Assert.assertFalse( UsersDAO.addUser("usr3","pass",null) ); //mail can't be null

        UsersDAO.deleteUser("usr1");
    }

    @Test
    public void deleteUser() throws Exception {
        UsersDAO.addUser("usr1","pass","mail1"); //Add user

        Assert.assertEquals("usr1", UsersDAO.findUser("usr1").getNick()); //Check it exist
        Assert.assertTrue( UsersDAO.deleteUser("usr1")); //Delete User
        Assert.assertEquals(null, UsersDAO.findUser("usr1")); //Check it doesnt exists

        Assert.assertTrue( UsersDAO.deleteUser("NO_NEXISTING_USER")); //Check it doesn't crash
        Assert.assertFalse( UsersDAO.deleteUser(null)); //Check it doesn't crash
    }

    @Test
    public void findUser() throws Exception {
        UserVO user = UsersDAO.findUser(USR);
        Assert.assertEquals(USR, user.getNick());
        Assert.assertEquals("pass", user.getPass());
        Assert.assertEquals("mail", user.getMail());
        Assert.assertNotNull(user.getDate());
    }

}
