package edu.sc.csce740;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import edu.sc.csce740.enums.*;
import edu.sc.csce740.exception.*;
import edu.sc.csce740.model.*;


public class BILL implements BILLIntf {
    /**
     * Currently active user id.
     */
    private String currentUser;

    /**
     * A hash map used to store {@link UserInfo}
     * Key: String of user id
     */
    private Map<String, UserInfo> userInfos;

    /**
     * A hash map used to store {@link StudentRecord}
     * Key: String of user id
     */
    private Map<String, StudentRecord> studentRecords;

    /**
     *  A hash map used to store {@link Bill}
     *  Key: String of user id
     */
    private Map<String, Bill> bills;

//    private Set<String>

    /**
     * A class loader to read files.
     */
    private ClassLoader classLoader = getClass().getClassLoader();

    /**
     * Constructor
     * @throws FileNotFoundException
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
     * @throws InvalidUserException when loading users to the system.
     */
    public void loadUsers(String usersFile) throws FileNotFoundException {
        try {
            final List<UserInfo> userInfosList =
                    new Gson().fromJson(new FileReader(new File(classLoader.getResource(usersFile).getFile())),
                            new TypeToken<List<UserInfo>>() {
                            }.getType());
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
     * @throws InvalidRecordException if errors happen when loading user records to the system.
     */
    public void loadRecords(String recordsFile) throws FileNotFoundException {
        try {
            final List<StudentRecord> studentRecordsList =
                    new Gson().fromJson(new FileReader(new File(classLoader.getResource(recordsFile).getFile())),
                            new TypeToken<List<StudentRecord>>() {
                            }.getType());

            for (StudentRecord studentRecord : studentRecordsList) {
                if (studentRecords.containsKey(studentRecord.getStudent().getId())) {
                    throw new DuplicateRecordException();
                }
                studentRecords.put(studentRecord.getStudent().getId(), studentRecord);
            }
        } catch (NullPointerException ex) {
            throw new FileNotFoundException("Encounters errors when loading student records to the systen: " + ex.getMessage());
        }
    }

    /**
     * Sets the user id of the user currently using the system.
     * @param userId  the id of the user to log in.
     * @throws InvalidUserException Encounters if the user id is invalid.
     */
    public void logIn(String userId) throws InvalidUserException {
        validateUser(userId, userInfos.keySet());
        this.currentUser = userId;
    }

    /**
     * Closes the current session, logs the user out, and clears any session data.
     * @throws InvalidUserException if the user id is invalid.
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
     * @throws InvalidUserException if the user id is invalid.
     * @throws PermissionDeniedException if the current user is not an admin.
     */
    public List<String> getStudentIDs() throws NoLoggedInUserException, PermissionDeniedException {
        validateLoggedInUser();

        if (userInfos.get(currentUser).getRole() == Role.STUDENT) {
            throw new PermissionDeniedException("You don't have permission to get student IDs.");
        } else {
            // Admin
            List<String> studentIdList = new ArrayList<String>();
            final College currentAdminCollege = userInfos.get(currentUser).getCollege();

            /*
               If this is a ADMIN and from Graduate School, he can view all graduate students' records.
               If this is an ADMIN but not from Graduate School, he can only view students' records from
               his college.
            */
            for (String studentUserId: studentRecords.keySet()) {
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
    }

    /**
     * Gets the raw student record data for a given userId.
     * @param userId  the identifier of the student.
     * @return the student record data.
     * @throws InvalidUserException if the user id is invalid.
     * @throws PermissionDeniedException if the current user does not have permission
     *              to view the record of given user id.
     */
    public StudentRecord getRecord(String userId)
            throws NoLoggedInUserException, PermissionDeniedException, InvalidUserException  {
        validateLoggedInUser();
        validatePermission(userId);
        validateUser(userId, studentRecords.keySet());
        return studentRecords.get(userId);
    }

    /**
     * Saves a new set of student data to the records data.
     * @param userId the student ID to overwrite.
     * @param record  the new student record
     * @param permanent  a status flag indicating whether (if false) to make a
     * temporary edit to the in-memory structure or (if true) a permanent edit.
     * @throws InvalidUserException if the user id is invalid.
     * @throws PermissionDeniedException if the current user does not have permission
     *              to view the record of given user id.
     * @throws InvalidRecordException if the transcript data could not be saved.
     */
    public void editRecord(String userId, StudentRecord record, Boolean permanent)
            throws NoLoggedInUserException, PermissionDeniedException,
            InvalidUserException, InvalidRecordException, IOException {
        if (record == null) {
            return;
        }

        validateLoggedInUser();
        validatePermission(userId);
        validateUser(userId, studentRecords.keySet());

        final Role role = userInfos.get(currentUser).getRole();
        if (role == Role.STUDENT) {
            studentRecords.get(userId).setStudent(record.getStudent());
        } else {
            StudentRecord oldRecord = getRecord(userId);
            if (oldRecord != null && record.getStudent().getId().equals(userId)) {
                studentRecords.put(userId, record);

                // TODO Re-generate bill
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
            } else {
                throw new PermissionDeniedException("You don't have permission to change the user id.");
            }
        }

        if (permanent) {
            saveStudentRecords();
        }
    }

    /**
     * Generates current bill.
     * @param userId the student to generate the bill for.
     * @returns the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill generateBill(String userId)
            throws NoLoggedInUserException, PermissionDeniedException, InvalidUserException {
        validateLoggedInUser();
        validatePermission(userId);

        if (bills.containsKey(userId)) {
            return bills.get(userId);
        } else {
            final Bill currentBill = new Bill(studentRecords.get(userId), null, null, null);
            bills.put(userId, currentBill);
            return currentBill;
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
     * @returns the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill viewCharges(String userId, int startMonth, int startDay, int startYear,
                            int endMonth, int endDay, int endYear)
            throws Exception {
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

    }

    /**
     * Makes a payment for the student.
     * @param userId  the student to make a payment for.
     * @param amount  amount to apply to the balance.
     * @param note  a string indicating the reason for the payment
     * @throws Exception if the payment fails a validity check
     * or fails to save to file.
     * SEE NOTE IN CLASS HEADER.
     */
    public void applyPayment(String userId, BigDecimal amount, String note)
            throws Exception {
        validateLoggedInUser();
        validatePermission(userId);

        if (!bills.containsKey(userId)) {
            generateBill(userId);
        }

        bills.get(userId).makePayment(amount, note);
    }

    /**
     * Saves the student records permanently to a file.
     * @throws InvalidRecordException if the transcript data could not be saved.
     */
    private void saveStudentRecords() throws InvalidRecordException, IOException {
        String representation = new GsonBuilder().setPrettyPrinting().create().toJson(studentRecords);

//        FileWriter fileWriter = new FileWriter(new File(classLoader.getResource("file/students.txt").getFile()));
//        fileWriter.write(representation);
//        fileWriter.flush();
//        fileWriter.close();
    }

    /**
     * Checks the validity of a given user id.
     * @param userId A string that represents the user id to be checked.
     * @throws InvalidUserException if given userId is null.
     */
    private void validateUser(String userId, Set<String> idSet) throws InvalidUserException {
        if (userId == null || !idSet.contains(userId)) {
            throw new InvalidUserException("Invalid user id");
        }
    }

    private void validateLoggedInUser() throws NoLoggedInUserException {
        try {
            validateUser(currentUser, userInfos.keySet());
        } catch (InvalidUserException ex) {
            throw new NoLoggedInUserException();
        }
    }

    /**
     * Checks the permission
     * @param userId
     */
    private void validatePermission(String userId)
            throws InvalidUserException, PermissionDeniedException, NoLoggedInUserException {
        validateUser(userId, userInfos.keySet());

        if (userInfos.get(currentUser).getRole() == Role.STUDENT) {
            if (!currentUser.equals(userId)) {
                throw new PermissionDeniedException();
            }
        } else {
            List<String> userGroup = getStudentIDs();
            if(!userGroup.contains(userId)) {
                throw new PermissionDeniedException();
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
        billTester.applyPayment("mhunt", new BigDecimal(12000), "Make a 1000 payment");
        System.out.println(billTester.generateBill("mhunt"));

        StudentRecord newRecord = billTester.getRecord("mhunt");
        newRecord.getStudent().setFirstName("Zheng");
        newRecord.setResident(true);
        newRecord.setActiveDuty(true);
        newRecord.setStudyAboard(StudyAbroad.COHORT);

        billTester.editRecord("mhunt", newRecord, true);

//        billTester.applyPayment("mhunt", new BigDecimal(321.32451), "Make a 1000 payment");

        System.out.println(billTester.generateBill("mhunt"));

        //        billTester.applyPayment(id, new BigDecimal(-1), "Make a 1000 payment");
        Bill bill = billTester.viewCharges("mhunt", 1, 1, 2017, 12,31,2017);
        System.out.println(bill);
        System.out.print("Done");

    }
}
