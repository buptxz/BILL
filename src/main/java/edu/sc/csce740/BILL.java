package edu.sc.csce740;

import java.math.BigDecimal;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import edu.sc.csce740.model.Permission;
import edu.sc.csce740.model.Bill;
import edu.sc.csce740.model.StudentRecord;


import edu.sc.csce740.model.Student;


public class BILL implements BILLIntf {

    /**
     *
     */
    private String activeUserId;

    /**
     *
     */
    private Map<String, Permission> permissions;

    /**
     *
     */
    private Map<String, StudentRecord> studentRecords;

    /**
     *
     */
    private Map<String, Bill> bills;

    ClassLoader classLoader = getClass().getClassLoader();

    public BILL() {
        activeUserId = null;
        permissions = new HashMap<String, Permission>();
        studentRecords = new HashMap<String, StudentRecord>();
        bills = new HashMap<String, Bill>();
    }

    /**
     * Loads the list of system usernames and permissions.
     * @param usersFile the filename of the users file.
     * @throws Exception for I/O errors.  SEE NOTE IN CLASS HEADER.
     */
    public void loadUsers(String usersFile) throws Exception {
        List<Permission> permissionsList =
                new Gson().fromJson( new FileReader(new File(classLoader.getResource(usersFile).getFile())),
                        new TypeToken<List<Permission>>(){}.getType());
        for (Permission permission : permissionsList) {
            permissions.put(permission.getId(), permission);
        }
    }

    /**
     * Loads the list of system transcripts.
     * @param recordsFile the filename of the transcripts file.
     * @throws Exception for I/O errors.  SEE NOTE IN CLASS HEADER.
     */
    public void loadRecords(String recordsFile) throws Exception {
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
     * @throws Exception  if the user id is invalid.  SEE NOTE IN CLASS HEADER.
     */
    public void logIn(String userId) throws Exception {
        if (!permissions.containsKey(userId)) {
            throw new Exception("Not a valid user.");
        } else {
            this.activeUserId = userId;
        }
    }

    /**
     * Closes the current session, logs the user out, and clears any session data.
     * @throws Exception  if the user id is invalid.  SEE NOTE IN CLASS HEADER.
     */
    public void logOut() throws Exception {
        this.activeUserId = null;
    }

    /**
     * Gets the user id of the user currently using the system.
     * @return  the user id of the user currently using the system.
     */
    public String getUser() {
        return this.activeUserId;
    }

    /**
     * Gets a list of the userIds of the students that an admin can view.
     * @return a list containing the userId of for each student in the
     *      college belonging to the current user
     * @throws Exception is the current user is not an admin.
     */
    public List<String> getStudentIDs() throws Exception {
        if (activeUserId == null) {
            throw new Exception("Current user is null, please log in to view student IDs");
        } else if (permissions.get(activeUserId).getRole() == "student") {
            throw new Exception("You don't have permission to view student IDs.");
        } else {
            List<String> studentIdList = new ArrayList<String>();
            String currentUserCollege = permissions.get(activeUserId).getCollege();

            for (String userId : permissions.keySet()) {
                Permission userPermission = permissions.get(userId);
                if (userId != activeUserId && userPermission.getCollege() == currentUserCollege) {
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
        return new StudentRecord();
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

    }

    /**
     * Generates current bill.
     * @param userId the student to generate the bill for.
     * @returns the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill generateBill(String userId) throws Exception {
        return new Bill(new Student(), "1", "2");
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
        return new Bill(new Student(), "1", "2");
    }

    /**
     * Makes a payment for the student
     * @param userId  the student to make a payment for.
     * @param amount  amount to apply to the balance.
     * @param note  a string indicating the reason for the payment
     * @throws Exception if the payment fails a validity check
     * or fails to save to file.
     * SEE NOTE IN CLASS HEADER.
     */
    public void applyPayment(String userId, BigDecimal amount, String note)
            throws Exception {

    }
}
