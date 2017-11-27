package edu.sc.csce740;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.sc.csce740.enums.*;
import edu.sc.csce740.exception.*;
import edu.sc.csce740.model.*;

/**
 * An implementation of BILLIntf.
 */
public class BILL implements BILLIntf {
    /**
     * Currently active user id.
     */
    String currentUser;

    /**
     * A hash map used to store {@link UserInfo}
     * Key: String of user id
     */
    Map<String, UserInfo> userInfos;

    /**
     * A hash map used to store {@link StudentRecord}
     * Key: String of user id
     */
    Map<String, StudentRecord> studentRecords;

    /**
     *  A hash map used to store {@link Bill}
     *  Key: String of user id
     */
    Map<String, Bill> bills;

    /**
     * A class loader to read files.
     */
    private final ClassLoader classLoader = getClass().getClassLoader();

    /**
     * Constructor
     * @throws FileNotFoundException when file not found
     */
    public BILL() throws FileNotFoundException {
        currentUser = null;
        userInfos = new HashMap<String, UserInfo>();
        studentRecords = new HashMap<String, StudentRecord>();
        bills = new HashMap<String, Bill>();
        loadUsers("file/users.txt");
        loadRecords("file/students.txt");
    }

    /**
     * Loads the list of system usernames and userInfos.
     * @param usersFile the filename of the users file.
     * @throws FileNotFoundException when loading users to the system.
     */
    public void loadUsers(String usersFile) throws FileNotFoundException {
        try {
            final List<UserInfo> userInfosList =
                    new Gson().fromJson(new FileReader(new File(classLoader.getResource(usersFile).getFile())),
                            new TypeToken<List<UserInfo>>(){}.getType());
            for (UserInfo userInfo : userInfosList) {
                if (userInfos.containsKey(userInfo.getId())) {
                    throw new DuplicateRecordException();
                }
                userInfos.put(userInfo.getId(), userInfo);
            }
        } catch (NullPointerException ex) {
            throw new FileNotFoundException("Encounters errors when loading users to the system: " + ex.getMessage());
        }
    }

    /**
     * Loads the list of system transcripts.
     * @param recordsFile the filename of the transcripts file.
     * @throws FileNotFoundException if errors happen when loading user records to the system.
     */
    public void loadRecords(String recordsFile) throws FileNotFoundException {
        try {
            final List<StudentRecord> studentRecordsList =
                    new Gson().fromJson(new FileReader(new File(classLoader.getResource(recordsFile).getFile())),
                            new TypeToken<List<StudentRecord>>(){}.getType());

            for (StudentRecord studentRecord : studentRecordsList) {
                if (studentRecords.containsKey(studentRecord.getStudent().getId())) {
                    throw new DuplicateRecordException();
                }
                studentRecords.put(studentRecord.getStudent().getId(), studentRecord);
            }
        } catch (NullPointerException ex) {
            throw new FileNotFoundException(
                    "Encounters errors when loading student records to the system: " + ex.getMessage());
        }
    }

    /**
     * Sets the user id of the user currently using the system.
     * @param userId  the id of the user to log in.
     * @throws InvalidUserException if the user id is invalid.
     */
    public void logIn(String userId) throws InvalidUserException {
        validateUser(userId, userInfos.keySet());
        this.currentUser = userId;
    }

    /**
     * Closes the current session, logs the user out, and clears any session data.
     * @throws NoLoggedInUserException if the user id is invalid.
     */
    public void logOut() throws NoLoggedInUserException {
        validateLoggedInUser();
        this.currentUser = null;
    }

    /**
     * Gets the user id of the user currently using the system.
     * @return  the user id of the user currently using the system.
     */
    public String getUser() {
        return this.currentUser;
    }

    /**
     * Gets a list of the userIds of the students that an admin can view.
     * @return a list containing the userId of for each student in the
     *      college belonging to the current user
     * @throws NonExistentStudentIdException if errors happen when getting student id list.
     */
    public List<String> getStudentIDs() throws NonExistentStudentIdException {
        try {
            validateLoggedInUser();

            if (userInfos.get(currentUser).getRole() == Role.STUDENT) {
                // Current user is student
                throw new PermissionDeniedException("You don't have permission to get student IDs.");
            } else {
                // Current user is admin
                List<String> studentIdList = new ArrayList<String>();
                final College currentAdminCollege = userInfos.get(currentUser).getCollege();

            /*
               If this is a ADMIN and from Graduate School, he can view all graduate students' records.
               If this is an ADMIN but not from Graduate School, he can only view students' records from
               his college.
            */
                for (String studentUserId : studentRecords.keySet()) {
                    StudentRecord studentRecord = studentRecords.get(studentUserId);
                    ClassStatus studentClassStatus = studentRecord.getClassStatus();

                    if ((currentAdminCollege == College.GRADUATE_SCHOOL
                            && (studentClassStatus == ClassStatus.MASTERS || studentClassStatus == ClassStatus.PHD))
                            || (currentAdminCollege == studentRecord.getCollege())) {
                        studentIdList.add(studentUserId);
                    }
                }

                return studentIdList;
            }
        } catch (Exception ex) {
            throw new NonExistentStudentIdException("Either no student id is found or encounters errors when " +
                    "getting student ids: " + ex);
        }
    }

    /**
     * Gets the raw student record data for a given userId.
     * @param userId  the identifier of the student.
     * @return the student record data.
     * @throws NonExistentRecordException if either failed in validating user or failed in validating record.
     */
    public StudentRecord getRecord(String userId) throws NonExistentRecordException {
        try {
            validateLoggedInUser();
            validatePermission(userId);
            validateUser(userId, studentRecords.keySet());
            return studentRecords.get(userId);
        } catch (Exception ex) {
            throw new NonExistentRecordException("Errors happen when getting student records: " + ex);
        }
    }

    /**
     * Saves a new set of student data to the records data.
     * @param userId the student ID to overwrite.
     * @param record  the new student record
     * @param permanent  a status flag indicating whether (if false) to make a
     * temporary edit to the in-memory structure or (if true) a permanent edit.
     * @throws IllegalRecordEditException if errors happen in editing a record.
     */
    public void editRecord(String userId, StudentRecord record, Boolean permanent)
            throws IllegalRecordEditException {
        try {
            if (record == null) {
                throw new IllegalRecordEditException("Cannot process empty record.");
            }

            validateLoggedInUser();
            validatePermission(userId);
            validateUser(userId, studentRecords.keySet());

            final Role role = userInfos.get(currentUser).getRole();
            if (role == Role.STUDENT) {
                // Current user is student. Can only modify the Student profiles.
                studentRecords.get(userId).setStudent(record.getStudent());
            } else {
                // Current user is admin. Can modify anything except the user id
                final StudentRecord oldRecord = getRecord(userId);
                if (oldRecord != null && record.getStudent().getId().equals(userId)) {
                    studentRecords.put(userId, record);
                    regenerateBill(userId);
                }
            }

            if (permanent) {
                saveStudentRecords();
            }
        } catch (Exception ex) {
            throw new IllegalRecordEditException("Errors happen when editing student record: " + ex);
        }
    }

    /**
     * Generates current bill.
     * @param userId the student to generate the bill for.
     * @return the student's bill in a data class matching the I/O file.
     * @throws BillGenerationException if the bill could not be generated.
     */
    public Bill generateBill(String userId) throws BillGenerationException {
        try {
            // Validation
            validateLoggedInUser();
            validatePermission(userId);

            if (bills.containsKey(userId)) {
                return bills.get(userId);
            } else {
                final Bill currentBill = new Bill(studentRecords.get(userId), null, null, null);
                bills.put(userId, currentBill);
                return currentBill;
            }
        } catch (Exception ex) {
            throw new BillGenerationException("Errors happen when generating a bill: " + ex);
        }
    }

    /**
     * Generates a list of transactions for a chosen period.
     * @param userId the student to generate the list for.
     * @param startMonth the month of the start date.
     * @param startDay the day of the start date.
     * @param startYear the year of the start date.
     * @param endMonth the month of the end date.
     * @param endDay the day of the end date.
     * @param endYear the year of the end date.
     * @return the student's bill in a data class matching the I/O file.
     * @throws BillGenerationException if the bill could not be generated.
     */
    public Bill viewCharges(String userId, int startMonth, int startDay, int startYear,
                            int endMonth, int endDay, int endYear) throws BillGenerationException {
        try {
            validateLoggedInUser();
            validatePermission(userId);

            final Date startDate = new Date(startMonth, startDay, startYear);
            final Date endDate = new Date(endMonth, endDay, endYear);

            if (!Date.isBefore(startDate, endDate)) {
                throw new WrongDateException();
            } else {
                if (!bills.containsKey(userId)) {
                    generateBill(userId);
                }
                return new Bill(studentRecords.get(userId), bills.get(userId), startDate, endDate);
            }
        } catch (Exception ex) {
            throw new BillGenerationException("Errors happen when viewing charges: " + ex);
        }
    }

    /**
     * Makes a payment for the student.
     * @param userId  the student to make a payment for.
     * @param amount  amount to apply to the balance.
     * @param note  a string indicating the reason for the payment
     * @throws PaymentSubmissionException if the payment fails a validity check or fails to save to file.
     */
    public void applyPayment(String userId, BigDecimal amount, String note) throws PaymentSubmissionException{
        try {
            validateLoggedInUser();
            validatePermission(userId);

            if (!bills.containsKey(userId)) {
                generateBill(userId);
            }

            bills.get(userId).makePayment(amount, note);
        } catch (Exception ex) {
            throw new PaymentSubmissionException(
                    "Payment fails a validity check or fails to save to file" + ex);
        }
    }

    /**
     * Saves the student records permanently to a file.
     * @throws InvalidRecordException if the transcript data could not be saved.
     */
    private void saveStudentRecords() throws InvalidRecordException, IOException {
        String representation = new GsonBuilder().setPrettyPrinting().create().toJson(studentRecords);

//        FileWriter out = new FileWriter(classLoader.getResource("students.txt").getFile());
//        out.write(representation);
//        out.close();
    }

    /**
     * Checks the validity of a given user id.
     * @param userId A string that represents the user id to be checked.
     * @throws InvalidUserException if given userId is null.
     */
    private void validateUser(String userId, Set<String> idSet) throws InvalidUserException {
        if (userId == null || idSet == null || !idSet.contains(userId)) {
            throw new InvalidUserException("Invalid user id");
        }
    }

    /**
     * Checks whether there's a user logged in.
     * @throws NoLoggedInUserException if the current user id is not in user info set.
     */
    private void validateLoggedInUser() throws NoLoggedInUserException {
        if (getUser() == null) {
            throw new NoLoggedInUserException();
        }
//        try {
//            validateUser(currentUser, userInfos.keySet());
//        } catch (InvalidUserException ex) {
//            throw new NoLoggedInUserException();
//        }
    }

    /**
     * Checks the permission of the given user id.
     * @param userId A string that represents the user id to be checked.
     * @throws InvalidUserException if given userId is null.
     * @throws PermissionDeniedException if current logged in user does not have permission
     * to view given user's record.
     * @throws NoLoggedInUserException if the current user id is not in user info set.
     */
    private void validatePermission(String userId)
            throws InvalidUserException, PermissionDeniedException
            , NoLoggedInUserException, NonExistentStudentIdException {
        validateUser(userId, userInfos.keySet());

        if (userInfos.get(currentUser).getRole() == Role.STUDENT) {
            if (!currentUser.equals(userId)) {
                throw new PermissionDeniedException();
            }
        } else {
            List<String> userGroup = getStudentIDs();
            if(userGroup == null || !userGroup.contains(userId)) {
                throw new PermissionDeniedException();
            }
        }
    }

    /**
     * Regenerates bill.
     * @param userId A string that represents the user of the bill to be regenerated.
     * @throws BillGenerationException if the bill could not be generated.
     */
    private void regenerateBill(String userId) throws BillGenerationException {
        if (bills.containsKey(userId)) {
            double totalPayment = 0;
            List<Transaction> paymentHistory = new ArrayList<Transaction>();

            for (Transaction transactionHistory : bills.get(userId).getTransactions()) {
                if (transactionHistory.getType() == Type.PAYMENT) {
                    paymentHistory.add(transactionHistory);
                    totalPayment += transactionHistory.getAmount();
                }
            }

            bills.remove(userId);
            generateBill(userId);
            bills.get(userId).setBalance(bills.get(userId).getBalance() - totalPayment);

            for (Transaction payment : paymentHistory) {
                bills.get(userId).getTransactions().add(payment);
            }
        }
    }

    /**
     * Test the BILL back end system
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String id = "jsmith";

        BILLIntf billTester = new BILL();
        billTester.logIn(id);
        billTester.logOut();

        billTester.logIn(id);
//        billTester.applyPayment("mhunt", new BigDecimal(12000), null);
        System.out.println(billTester.generateBill("mhunt"));
//        System.out.println(billTester.generateBill("ggay"));

        StudentRecord newRecord = billTester.getRecord("mhunt");
        newRecord.getStudent().setFirstName("Zheng");
        newRecord.setResident(true);
        newRecord.setActiveDuty(true);
        newRecord.setStudyAboard(StudyAbroad.COHORT);
        billTester.editRecord("mhunt", newRecord, true);

        System.out.println(billTester.generateBill("mhunt"));

        //        billTester.applyPayment(id, new BigDecimal(-1), "Make a 1000 payment");
        Bill bill = billTester.viewCharges("mhunt", 1, 1, 2018, 12,31,2018);
        System.out.println(bill);
        System.out.print("Done");

    }
}
