package edu.sc.csce740;

import edu.sc.csce740.exception.InvalidUserException;
import edu.sc.csce740.exception.NoLoggedInUserException;
import edu.sc.csce740.model.Bill;
import edu.sc.csce740.model.StudentRecord;
import edu.sc.csce740.model.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;

/**
 * This test is to test the BILLIntf.
 * Instead of testing different implementations, testing the interface is more convenient and accurate
 * as customers will be using our interface, not caring about the implementations.
 * We can add more parameters if there are more implementations of BILLIntf.
 */
@RunWith(Parameterized.class)
public class BILLIntfTest {

    private final BILLIntf billIntf;
    private static final String USER_FILE = "file/users.txt";
    private static final String RECORD_FILE = "file/students.txt";
    private static final String INVALID_FILE_NAME = "file/noSuchFile.txt";
    private static final String USER_ID = "rbob";
    private static final String INVALID_USER_ID = "noSuchUserId";

    public BILLIntfTest(BILLIntf billIntf) {
        this.billIntf = billIntf;
    }

    @Before
    public void setup() throws Exception {
        billIntf.loadUsers(USER_FILE);
        billIntf.loadRecords(RECORD_FILE);
    }

    @Test
    public void testLoadUsers() throws Exception {
        billIntf.loadUsers(USER_FILE);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadUsersWithException() throws Exception {
        billIntf.loadUsers(INVALID_FILE_NAME);
    }

    @Test
    public void testLoadRecords() throws Exception {
        billIntf.loadRecords(RECORD_FILE);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadRecordsWithException() throws Exception {
        billIntf.loadRecords(INVALID_FILE_NAME);
    }

    @Test
    public void testLogIn() throws Exception {
        billIntf.logIn(USER_ID);
    }

    @Test(expected = InvalidUserException.class)
    public void testLogInWithException() throws Exception {
        billIntf.logIn(INVALID_USER_ID);
    }

    @Test
    public void testLogOut() throws Exception {
        billIntf.logIn(USER_ID);
        billIntf.logOut();
    }

    @Test(expected = NoLoggedInUserException.class)
    public void testLogOutWithException() throws Exception {
        billIntf.logOut();
    }



    @Parameterized.Parameters
    public static Collection<Object[]> createImplementations() throws FileNotFoundException {
        return Arrays.asList(new Object[][] {
                { new BILL() }
        });
    }
}
