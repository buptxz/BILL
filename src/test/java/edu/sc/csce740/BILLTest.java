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
import edu.sc.csce740.exception.PaymentSubmissionException;
import edu.sc.csce740.model.Bill;
import edu.sc.csce740.model.Course;
import edu.sc.csce740.model.StudentRecord;
import edu.sc.csce740.model.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static edu.sc.csce740.TestConstant.ADMIN_ID;
import static edu.sc.csce740.TestConstant.ENG_STUDENT_ID;
import static org.junit.Assert.assertThat;

public class BILLTest {

    private BILL billImpl;
    private final ClassLoader classLoader = getClass().getClassLoader();

    @Before
    public void setup() throws FileNotFoundException {
        billImpl = new BILL();
    }

    @After
    public void cleanup() {
        billImpl.setCurrentUser(null);
        billImpl.setUserInfos(new HashMap<String, UserInfo>());
        billImpl.setStudentRecords(new HashMap<String, StudentRecord>());
        billImpl.setBills(new HashMap<String, Bill>());
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
        billImpl.logIn(ADMIN_ID);
    }

    @Test(expected = InvalidUserException.class)
    public void testLogInWithException() throws Exception {
        billImpl.logIn(TestConstant.INVALID_USER_ID);
    }

    @Test
    public void testLogOut() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.logOut();
    }

    @Test(expected = NoLoggedInUserException.class)
    public void testLogOutWithException() throws Exception {
        billImpl.logOut();
    }

    @Test
    public void testGetUser() throws Exception {
        billImpl.logIn(ADMIN_ID);
        String userId = billImpl.getUser();
        assertEquals(ADMIN_ID, userId);
    }

    @Test
    public void testGetStudentIDs() throws Exception {
        billImpl.logIn(ADMIN_ID);
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
        assertEquals(billImpl.getStudentRecords().get(TestConstant.STUDENT_ID), studentRecord);
    }

    @Test(expected = NonExistentRecordException.class)
    public void testGetRecordWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        StudentRecord studentRecord = billImpl.getRecord(TestConstant.INVALID_USER_ID);
    }

    @Test
    public void testEditRecordByStudent() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);

        final List<StudentRecord> studentRecordsList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("file/testRecords.txt").getFile())),
                        new TypeToken<List<StudentRecord>>() {
                        }.getType());

        billImpl.editRecord(ENG_STUDENT_ID, studentRecordsList.get(0),false);
        assertEquals(studentRecordsList.get(0).getStudent(), billImpl.getStudentRecords().get(ENG_STUDENT_ID).getStudent());
    }

    @Test
    public void testEditRecordByAdmin() throws Exception {
        billImpl.logIn(ADMIN_ID);

        final List<StudentRecord> studentRecordsList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("file/testRecords.txt").getFile())),
                        new TypeToken<List<StudentRecord>>() {
                        }.getType());

        billImpl.editRecord(ENG_STUDENT_ID, studentRecordsList.get(0), false);
        assertEquals(studentRecordsList.get(0), billImpl.getStudentRecords().get(ENG_STUDENT_ID));
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordEmptyRecord() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByStudentWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(ADMIN_ID, null, true);
    }

    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByAdminWithException() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    @Test
    // TODO: fix the assertion.
    public void testGenerateBill() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);

        final List<Bill> billList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("bill.txt").getFile())),
                        new TypeToken<List<Bill>>() {
                        }.getType());

        final List<Bill> billList1 =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("bill.txt").getFile())),
                        new TypeToken<List<Bill>>() {
                        }.getType());

        Bill actualBill = billImpl.generateBill(ENG_STUDENT_ID);
        assertEquals(billList.get(1), billList1.get(1));
    }

    @Test(expected = BillGenerationException.class)
    public void testGenerateBillWithException() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.generateBill(TestConstant.STUDENT_ID);
    }

    @Test(expected = BillGenerationException.class)
    public void testViewChargesStartDateLaterThanEndDate() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.viewCharges(TestConstant.STUDENT_ID, 2, 1, 2017,
                1, 1, 2017);
    }

    @Test
    public void testApplyPament() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        double expectedBill = billImpl.generateBill(ENG_STUDENT_ID).getBalance();
        billImpl.applyPayment(ENG_STUDENT_ID, new BigDecimal(100), "Test payment.");
        assertEquals(expectedBill - 100
                , billImpl.getBills().get(ENG_STUDENT_ID).getBalance(), 0.001);
    }

    @Test(expected = PaymentSubmissionException.class)
    public void testApplyPaymentWithException() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        billImpl.applyPayment(ENG_STUDENT_ID, new BigDecimal(10000), "Test payment.");
    }

    @Test(expected = BillGenerationException.class)
    public void testInvalidDateExceptionViewCharges() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        billImpl.viewCharges(ENG_STUDENT_ID, 13, 0, 0, 13, 0, 0);
    }

}
