package edu.sc.csce740;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sc.csce740.exception.BillGenerationException;
import edu.sc.csce740.exception.IllegalRecordEditException;
import edu.sc.csce740.exception.InvalidUserException;
import edu.sc.csce740.exception.NonExistentRecordException;
import edu.sc.csce740.exception.NoLoggedInUserException;
import edu.sc.csce740.exception.NonExistentStudentIdException;
import edu.sc.csce740.exception.DuplicateRecordException;
import edu.sc.csce740.model.Bill;
import edu.sc.csce740.model.StudentRecord;
import edu.sc.csce740.model.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BILLTest {
    BILL billImpl;
    private final ClassLoader classLoader = getClass().getClassLoader();

    @Before
    public void setup() throws FileNotFoundException {
        billImpl = new BILL();
        billImpl.loadUsers(TestConstant.USER_FILE);
        billImpl.loadRecords(TestConstant.RECORD_FILE);
    }

    @After
    public void cleanup() {
        billImpl.currentUser = null;
        billImpl.userInfos = new HashMap<String, UserInfo>();
        billImpl.studentRecords = new HashMap<String, StudentRecord>();
        billImpl.bills = new HashMap<String, Bill>();
    }

    @Test(expected = DuplicateRecordException.class)
    public void testLoadUsers() throws Exception {
        billImpl.loadUsers(TestConstant.USER_FILE);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadUsersWithException() throws Exception {
        billImpl.loadUsers(TestConstant.INVALID_FILE_NAME);
    }

    @Test(expected = DuplicateRecordException.class)
    public void testLoadRecords() throws Exception {
        billImpl.loadRecords(TestConstant.RECORD_FILE);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadRecordsWithException() throws Exception {
        billImpl.loadRecords(TestConstant.INVALID_FILE_NAME);
    }

    @Test
    public void testLogIn() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
    }

    @Test(expected = InvalidUserException.class)
    public void testLogInWithException() throws Exception {
        billImpl.logIn(TestConstant.INVALID_USER_ID);
    }

    @Test
    public void testLogOut() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
        billImpl.logOut();
    }

    @Test(expected = NoLoggedInUserException.class)
    public void testLogOutWithException() throws Exception {
        billImpl.logOut();
    }

    @Test
    public void testGetUser() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
        String userId = billImpl.getUser();
        assertEquals(TestConstant.ADMIN_ID, userId);
    }

    @Test
    public void testGetStudentIDs() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
        List<String> studentIDList = billImpl.getStudentIDs();
        List<String> expectedStudentIDList = new ArrayList<String>();
        expectedStudentIDList.add("ggay");
        assertEquals(expectedStudentIDList, studentIDList);
    }

    @Test
    public void testGetStudentIDsGraduateSchool() throws Exception {
        billImpl.logIn(TestConstant.GRADUATE_SCHOOL_USER_ID);
        List<String> studentIDList = billImpl.getStudentIDs();
        List<String> expectedStudentIDList = new ArrayList<String>();
        expectedStudentIDList.add("mhunt");
        assertEquals(expectedStudentIDList, studentIDList);
    }

    @Test(expected = NonExistentStudentIdException.class)
    public void testGetStudentIDsWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.getStudentIDs();
    }

    @Test
    public void testGetRecord() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        StudentRecord studentRecord = billImpl.getRecord(TestConstant.STUDENT_ID);
        assertEquals(billImpl.studentRecords.get(TestConstant.STUDENT_ID), studentRecord);
    }

    @Test(expected = NonExistentRecordException.class)
    public void testGetRecordWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        StudentRecord studentRecord = billImpl.getRecord(TestConstant.INVALID_USER_ID);
    }

    @Test
    public void testEditRecordByStudent() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);

        final List<StudentRecord> studentRecordsList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("file/testRecords.txt").getFile())),
                        new TypeToken<List<StudentRecord>>() {
                        }.getType());

        billImpl.editRecord(TestConstant.STUDENT_ID, studentRecordsList.get(0),false);
        //TODO - how to assert equal to two objects?
//        assertEquals(billImpl.studentRecords.get(TestConstant.STUDENT_ID), studentRecordsList.get(0));
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordEmptyRecord() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByStudentWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(TestConstant.ADMIN_ID, null, true);
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByAdminWithException() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    @Test
    public void testGenerateBill() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        Bill bill = billImpl.generateBill(TestConstant.STUDENT_ID);
        assertEquals(billImpl.bills.get(TestConstant.STUDENT_ID), bill);
    }

    @Test
    public void testGenerateBillNotExistBill() throws Exception {
        billImpl.logIn(TestConstant.GRADUATE_SCHOOL_USER_ID);
        StudentRecord record = parseStudentRecordFromFile("file/students.txt");
        Bill expectedBill = new Bill(record, null, null, null);
        Bill actualBill = billImpl.generateBill(TestConstant.STUDENT_ID);

        //TODO - why assert equal does not return true.
//        assertEquals(expectedBill, actualBill);
    }

    @Test(expected = BillGenerationException.class)
    public void testGenerateBillWithException() throws Exception {
        billImpl.logIn(TestConstant.ADMIN_ID);
        billImpl.generateBill(TestConstant.STUDENT_ID);
    }

    @Test(expected = BillGenerationException.class)
    public void testViewChargesStartDateLaterThanEndDate() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.viewCharges(TestConstant.STUDENT_ID, 2, 1, 2017,
                1, 1, 2017);
    }

    @Test
    public void testViewChargesNotExistBill() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        Bill actualBill = billImpl.viewCharges(TestConstant.STUDENT_ID, 1, 1, 2017
        , 3, 1, 2017);
//        Bill expectedBill = new Bill();
    }

    private StudentRecord parseStudentRecordFromFile(String recordsFile) throws FileNotFoundException {
        final List<StudentRecord> studentRecordsList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource(recordsFile).getFile())),
                        new TypeToken<List<StudentRecord>>() {
                        }.getType());
        return studentRecordsList.get(0);
    }
}
