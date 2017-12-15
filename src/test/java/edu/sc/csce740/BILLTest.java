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
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

import static edu.sc.csce740.TestConstant.ADMIN_ID;
import static edu.sc.csce740.TestConstant.ENG_STUDENT_ID;

public class BILLTest {

    private BILL billImpl;
    private final ClassLoader classLoader = getClass().getClassLoader();

    /**
     * Initialization
     * @throws FileNotFoundException
     */
    @Before
    public void setup() throws FileNotFoundException {
        billImpl = new BILL();
    }

    /**
     * Tear down
     */
    @After
    public void cleanup() {
        billImpl.setCurrentUser(null);
        billImpl.setUserInfos(new HashMap<String, UserInfo>());
        billImpl.setStudentRecords(new HashMap<String, StudentRecord>());
        billImpl.setBills(new HashMap<String, Bill>());
    }

    /**
     * Test load users
     * @throws Exception
     */
    @Test(expected = DuplicateRecordException.class)
    public void testLoadUsers() throws Exception {
        billImpl.loadUsers(TestConstant.USER_FILE);
    }

    /**
     * Test load users exception
     * @throws Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testLoadUsersWithException() throws Exception {
        billImpl.loadUsers(TestConstant.INVALID_FILE_NAME);
    }

    /**
     * Test load records
     * @throws Exception
     */
    @Test(expected = DuplicateRecordException.class)
    public void testLoadRecords() throws Exception {
        billImpl.loadRecords(TestConstant.RECORD_FILE);
    }

    /**
     * Test load records exception
     * @throws Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testLoadRecordsWithException() throws Exception {
        billImpl.loadRecords(TestConstant.INVALID_FILE_NAME);
    }

    /**
     * Test login
     * @throws Exception
     */
    @Test
    public void testLogIn() throws Exception {
        billImpl.logIn(ADMIN_ID);
        assertEquals(ADMIN_ID, billImpl.getUser());
    }

    /**
     * Test invalid login
     * @throws Exception
     */
    @Test(expected = InvalidUserException.class)
    public void testLogInWithException() throws Exception {
        billImpl.logIn(TestConstant.INVALID_USER_ID);
    }

    /**
     * Test logout
     * @throws Exception
     */
    @Test
    public void testLogOut() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.logOut();
        assertEquals(null, billImpl.getUser());
    }

    /**
     * Test invalid logout
     * @throws Exception
     */
    @Test(expected = NoLoggedInUserException.class)
    public void testLogOutWithException() throws Exception {
        billImpl.logOut();
    }

    /**
     * Test get users
     * @throws Exception
     */
    @Test
    public void testGetUser() throws Exception {
        billImpl.logIn(ADMIN_ID);
        String userId = billImpl.getUser();
        assertEquals(ADMIN_ID, userId);
    }

    /**
     * Test get student id
     * @throws Exception
     */
    @Test
    public void testGetStudentIDs() throws Exception {
        billImpl.logIn(ADMIN_ID);
        List<String> studentIDList = billImpl.getStudentIDs();
        Set studentIDSet = new HashSet(studentIDList);
        Set<String> expectedStudentIDSet = new HashSet<String>();
        expectedStudentIDSet.add("ggay");
        expectedStudentIDSet.add("buptxz");
        expectedStudentIDSet.add("jj");
        expectedStudentIDSet.add("lp");
        expectedStudentIDSet.add("jh");
        expectedStudentIDSet.add("lbj");
        assertEquals(expectedStudentIDSet, studentIDSet);
    }

    /**
     * Test get studetn id, admin is from graduate school
     * @throws Exception
     */
    @Test
    public void testGetStudentIDsGraduateSchool() throws Exception {
        billImpl.logIn(TestConstant.GRADUATE_SCHOOL_USER_ID);
        List<String> studentIDList = billImpl.getStudentIDs();
        Set studentIDSet = new HashSet(studentIDList);
        Set<String> expectedStudentIDSet = new HashSet<String>();
        expectedStudentIDSet.add("mhunt");
        expectedStudentIDSet.add("buptxz");
        expectedStudentIDSet.add("lbj");
        assertEquals(expectedStudentIDSet, studentIDSet);
    }

    /**
     * Test get student id request from a student
     * @throws Exception
     */
    @Test(expected = NonExistentStudentIdException.class)
    public void testGetStudentIDsWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.getStudentIDs();
    }

    /**
     * Test get record
     * @throws Exception
     */
    @Test
    public void testGetRecord() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        StudentRecord studentRecord = billImpl.getRecord(TestConstant.STUDENT_ID);
        assertEquals(billImpl.getStudentRecords().get(TestConstant.STUDENT_ID), studentRecord);
    }

    /**
     * Test get invalid get record
     * @throws Exception
     */
    @Test(expected = NonExistentRecordException.class)
    public void testGetRecordWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        StudentRecord studentRecord = billImpl.getRecord(TestConstant.INVALID_USER_ID);
    }

    /**
     * Test edit record by student
     * @throws Exception
     */
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

    /**
     * Test edit record by admin
     * @throws Exception
     */
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

    /**
     * Test illegal edit record
     * @throws Exception
     */
    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordEmptyRecord() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    /**
     * Test illegal edit record
     * @throws Exception
     */
    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByStudentWithException() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.editRecord(ADMIN_ID, null, true);
    }

    /**
     * Test illegal edit record
     * @throws Exception
     */
    @Test(expected = IllegalRecordEditException.class)
    public void testEditRecordByAdminWithException() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.editRecord(TestConstant.STUDENT_ID, null, true);
    }

    /**
     * Test generate bill
     * @throws Exception
     */
    @Test
    public void testGenerateBillGraduate() throws Exception {
        billImpl.logIn("mmatthews");

        final List<Bill> billList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("bill.txt").getFile())),
                        new TypeToken<List<Bill>>() {
                        }.getType());

        Bill actualBill1 = billImpl.generateBill("mhunt");
        Bill actualBill2 = billImpl.generateBill("buptxz");
        assertEquals(billList.get(0), actualBill1);
        assertEquals(billList.get(2), actualBill2);
    }

    /**
     * Test generate bill
     * @throws Exception
     */
    @Test
    public void testGenerateBillEngineering() throws Exception {
        billImpl.logIn("rbob");

        final List<Bill> billList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("bill.txt").getFile())),
                        new TypeToken<List<Bill>>() {
                        }.getType());

        Bill actualBill1 = billImpl.generateBill("jj");
        Bill actualBill2 = billImpl.generateBill("lp");
        Bill actualBill3 = billImpl.generateBill("jh");
        Bill actualBill4 = billImpl.generateBill("lbj");
        Bill actualBill5 = billImpl.generateBill("ggay");
        assertEquals(billList.get(3), actualBill1);
        assertEquals(billList.get(4), actualBill2);
        assertEquals(billList.get(5), actualBill3);
        assertEquals(billList.get(6), actualBill4);
        assertEquals(billList.get(1), actualBill5);
    }

    /**
     * Test generate bill exception
     * @throws Exception
     */
    @Test(expected = BillGenerationException.class)
    public void testGenerateBillWithException() throws Exception {
        billImpl.logIn(ADMIN_ID);
        billImpl.generateBill(TestConstant.STUDENT_ID);
    }

    /**
     * Test view charge with invalid date input
     * @throws Exception
     */
    @Test(expected = BillGenerationException.class)
    public void testViewChargesStartDateLaterThanEndDate() throws Exception {
        billImpl.logIn(TestConstant.STUDENT_ID);
        billImpl.viewCharges(TestConstant.STUDENT_ID, 2, 1, 2017,
                1, 1, 2017);
    }

    /**
     * Test view charge with invalid date
     * @throws Exception
     */
    @Test(expected = BillGenerationException.class)
    public void testInvalidDateExceptionViewCharges() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        billImpl.viewCharges(ENG_STUDENT_ID, 13, 0, 0, 13, 0, 0);
    }

    /**
     * Test view charge
     * @throws Exception
     */
    @Test
    public void testViewCharge() throws Exception {
        billImpl.logIn("mhunt");
        Bill actualBill = billImpl.viewCharges("mhunt", 1,1,1990,
                12,31,2017);
        final List<Bill> billList =
                new Gson().fromJson(new FileReader(new File(classLoader.getResource("charge.txt").getFile())),
                        new TypeToken<List<Bill>>() {
                        }.getType());
        assertEquals(billList.get(0), actualBill);
    }

    /**
     * Test apply payment
     * @throws Exception
     */
    @Test
    public void testApplyPayment() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        double expectedBill = billImpl.generateBill(ENG_STUDENT_ID).getBalance();
        billImpl.applyPayment(ENG_STUDENT_ID, new BigDecimal(100), "Test payment.");
        assertEquals(expectedBill - 100
                , billImpl.getBills().get(ENG_STUDENT_ID).getBalance(), 0.001);
    }

    /**
     * Test apply payment with invalid input amount
     * @throws Exception
     */
    @Test(expected = PaymentSubmissionException.class)
    public void testApplyPaymentWithOverflow() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        billImpl.applyPayment(ENG_STUDENT_ID, new BigDecimal(100000), "Test payment.");
    }

    /**
     * Test apply payment with invalid payment amount
     * @throws Exception
     */
    @Test(expected = PaymentSubmissionException.class)
    public void testApplyPaymentWithInvalidNumber() throws Exception {
        billImpl.logIn(ENG_STUDENT_ID);
        billImpl.applyPayment(ENG_STUDENT_ID, new BigDecimal(-100), "Test payment.");
    }
}
