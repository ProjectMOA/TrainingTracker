package org.trainingTracker.database.dataAccesObject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.trainingTracker.database.valueObject.UserVO;

/**
 * Created by sergio on 25/10/16.
 */
public class UsersDAOTest {

    @Test
    @Ignore
    public void addUser() throws Exception {
        UsersDAO.addUser("JohnDoe1","JohnPass1","John1@a.com");
    }

    @Test
    @Ignore
    public void findUser() throws Exception {
            
        UserVO doe1 = UsersDAO.findUser("JohnDoe1");
        UserVO fault = UsersDAO.findUser("Nobody");

        Assert.assertEquals("JohnDoe1", doe1.getNick());
        Assert.assertEquals("JohnPass1", doe1.getPass());
        Assert.assertEquals("John1@a.com", doe1.getMail());

        Assert.assertEquals("Nickname: JohnDoe1 Mail: John1@a.com Pass: JohnPass1", doe1.toString().substring(0,52));

        Assert.assertEquals(fault, null);

    }

}