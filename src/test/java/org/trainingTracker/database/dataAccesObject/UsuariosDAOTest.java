package org.trainingTracker.database.dataAccesObject;

import org.junit.Assert;
import org.junit.Test;
import org.trainingTracker.database.valueObject.UsuarioVO;

/**
 * Created by sergio on 25/10/16.
 */
public class UsuariosDAOTest {

    @Test
    public void addUser() throws Exception {
        UsuariosDAO.addUser("JohnDoe1","JohnPass1","John1@a.com");
    }

    @Test
    public void findUser() throws Exception {

        UsuarioVO doe1 = UsuariosDAO.findUser("JohnDoe1");
        UsuarioVO fault = UsuariosDAO.findUser("Nobody");

        Assert.assertEquals("JohnDoe1", doe1.getNick());
        Assert.assertEquals("JohnPass1", doe1.getPass());
        Assert.assertEquals("John1@a.com", doe1.getMail());

        Assert.assertEquals(fault, null);

    }

}
