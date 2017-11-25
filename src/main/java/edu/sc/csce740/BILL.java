package edu.sc.csce740;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import edu.sc.csce740.enums.College;
import edu.sc.csce740.enums.Role;
import edu.sc.csce740.exception.IllegalPermissionException;
import edu.sc.csce740.exception.InvalidUserException;
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

    /**
     * A class loader to read files.
     */
    private ClassLoader classLoader = getClass().getClassLoader();

    public BILL() {
        currentUser = null;
        userInfos = new HashMap<String, UserInfo>();
        studentRecords = new HashMap<String, StudentRecord>();
        bills = new HashMap<String, Bill>();
    }

    /**
     * Loads the list of system usernames and userInfos.
     * @param usersFile the filename of the users file.
     * @throws FileNotFoundException Encounters when file is not found in given location.
     * @throws NullPointerException Encounters when parsing the file.
     */
    public void loadUsers(String usersFile) throws FileNotFoundException, NullPointerException {
        List<UserInfo> userInfosList =
                new Gson().fromJson( new FileReader(new File(classLoader.getResource(usersFile).getFile())),
                        new TypeToken<List<UserInfo>>(){}.getType());
        for (UserInfo permission : userInfosList) {
            userInfos.put(permission.getId(), permission);
        }
    }

    /**
     * Loads the list of system transcripts.
     * @param recordsFile the filename of the transcripts file.
     * @throws FileNotFoundException Encounters when file is not found in given location.
     * @throws NullPointerException Encounters when parsing the file.
     */
    public void loadRecords(String recordsFile) throws FileNotFoundException, NullPointerException {
        List<StudentRecord> studentRecordsList =
                new Gson().fromJson( new FileReader(new File(classLoader.getResource(recordsFile).getFile())),
                        new TypeToken<List<StudentRecord>>(){}.getType());

        for (StudentRecord studentRecord : studentRecordsList) {
            studentRecords.put(studentRecord.getStudent().getId(), studentRecord);
        }
    }

    /**
     * Sets the user id of the user currently using the system.
     * @param userId  the id of the user to log in.
     * @throws InvalidUserException Encounters if the user id is invalid.
     */
    public void logIn(String userId) throws InvalidUserException {
        if (!userInfos.containsKey(userId)) {
            throw new InvalidUserException("Not a valid user.");
        } else {
            this.currentUser = userId;
        }
    }

    /**
     * Closes the current session, logs the user out, and clears any session data.
     * @throws InvalidUserException Encounters if the user id is invalid.
     */
    public void logOut() throws InvalidUserException {
        if (!validate(currentUser)) {
            throw new InvalidUserException("Not a valid user.");
        }
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
     * @throws InvalidUserException Encounters if the user id is invalid.
     * @throws IllegalPermissionException is the current user is not an admin.
     */
    public List<String> getStudentIDs() throws InvalidUserException, IllegalPermissionException {
        if (!validate(currentUser)) {
            throw new InvalidUserException("Current user is null");
        } else if (userInfos.get(currentUser).getRole() == Role.STUDENT) {
            throw new IllegalPermissionException("You don't have permission to view student IDs.");
        } else {
            List<String> studentIdList = new ArrayList<String>();
            College currentUserCollege = userInfos.get(currentUser).getCollege();

            for (String userId : userInfos.keySet()) {
                UserInfo userInfo = userInfos.get(userId);
                if (!userId.equals(currentUser) && userInfo.getCollege() == currentUserCollege) {
                    studentIdList.add(userId);
                }
            }
            return studentIdList;
        }
    }

    /**
     * Gets the raw student record data for a given userId.
     * @param userId  the identifier of the student.
     * @return the student record data.
     * @throws Exception if the form data could not be retrieved. SEE NOTE IN
     *      CLASS HEADER.
     */
    public StudentRecord getRecord(String userId) throws Exception {
        if (!validate(currentUser) || !studentRecords.containsKey(userId)) {
            throw new Exception("Current user is null or no record matching given user ID.");
        } else {
            Role role = userInfos.get(currentUser).getRole();
            if (role == Role.STUDENT) {
                if (!currentUser.equals(userId)) {
                    throw new Exception("You don't have permission to view other student's record.");
                } else {
                    return studentRecords.get(userId);
                }
            } else {
                List<String> studentIdList = getStudentIDs();
                for (String studentId: studentIdList) {
                    if (userId.equals(studentId)) {
                        return studentRecords.get(userId);
                    }
                }
                throw new Exception("You don't have permission to view other student's record.");
            }
        }
    }

    /**
     * Saves a new set of student data to the records data.
     * @param userId the student ID to overwrite.
     * @param record  the new student record
     * @param permanent  a status flag indicating whether (if false) to make a
     * temporary edit to the in-memory structure or (if true) a permanent edit.
     * @throws Exception if the transcript data could not be saved or failed
     * a validity check.  SEE NOTE IN CLASS HEADER.
     */
    public void editRecord(String userId, StudentRecord record, Boolean permanent)
            throws Exception {
        if (!validate(currentUser)) {
            throw new Exception("Current user is null.");
        } else {
            Role role = userInfos.get(userId).getRole();
            if (role == Role.STUDENT) {
                if (!record.getStudent().getId().equals(userId)) {
                    throw new Exception("You dont have permission to edit other's record.");
                } else {
                    studentRecords.get(userId).setStudent(record.getStudent());
                }
            } else {
                StudentRecord oldRecord = getRecord(userId);
                if (oldRecord != null && record.getStudent().getId().equals(userId)) {
                    studentRecords.put(userId, record);
                } else {
                    throw new Exception("You dont have permission to edit other's record.");
                }
            }

            if (permanent) {
                saveStudentRecords();
            }
        }
    }

    /**
     * Generates current bill.
     * @param userId the student to generate the bill for.
     * @returns the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill generateBill(String userId) throws Exception {
        if (bills.containsKey(userId)) {
            return bills.get(userId);
        } else {
            Bill currentBill = new Bill(null, studentRecords.get(userId), null, null);
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
        Date startDate = new Date(startMonth, startDay, startYear);
        Date endDate = new Date(endMonth, endDay, endYear);
        if (!Date.isBefore(startDate, endDate)) {
            throw new Exception();
        } else {
            if (!bills.containsKey(userId)) {
                generateBill(userId);
            }
            return new Bill(bills.get(userId), studentRecords.get(userId), startDate, endDate);
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
        if (!bills.containsKey(userId)) {
            generateBill(userId);
        }

        bills.get(userId).makePayment(amount, note);
    }

    /**
     * Saves the student records permanently to a file.
     */
    private void saveStudentRecords() {
        String representation = new GsonBuilder().setPrettyPrinting().create().toJson(studentRecords);
        //TODO - output the string to a file
    }

    /**
     * Checks the validity of a given user id.
     * @param userId A string that represents the user id to be checked.
     * @return True if given user id is valid.
     */
    private boolean validate(String userId) {
        return userId != null;
    }

    /**
     * Test the BILL back end system
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        BILLIntf billIntf = new BILL();

        try {
            billIntf.loadUsers("file/users.txt");
            billIntf.loadRecords("file/students.txt");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        String id = "mhunt";
        billIntf.logIn(id);
        System.out.println(billIntf.generateBill(id));

        billIntf.applyPayment(id, new BigDecimal(321.32451), "Make a 1000 payment");
        Bill bill = billIntf.viewCharges(id, 1, 1, 2017, 12,31,2017);
        System.out.println(bill);
        System.out.print("Done");

    }
}
