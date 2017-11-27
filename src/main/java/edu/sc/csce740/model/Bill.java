package edu.sc.csce740.model;

import edu.sc.csce740.config.FeeConstant;
import edu.sc.csce740.enums.*;
import edu.sc.csce740.exception.InvalidPaymentException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * This is used to output the bill information.
 */
@Getter
@Setter
@ToString
public class Bill {
    private Student student;
    private College college;
    private ClassStatus classStatus;
    private double balance;
    private List<Transaction> transactions;

    public Bill(StudentRecord studentRecord, Bill currentBill, Date startDate, Date endDate) {
        this.student = studentRecord.getStudent();
        this.college = studentRecord.getCollege();
        this.classStatus = studentRecord.getClassStatus();
        this.transactions = new ArrayList<Transaction>();

        if (startDate == null) {
            // Generate a new bill
            calculateCharge(studentRecord);
        } else {
            // View transaction history
            this.balance = currentBill.getBalance();

            // Check transactions from student record
            if (studentRecord.getTransactions() != null) {
                for (Transaction transaction : studentRecord.getTransactions()) {
                    if (transaction.getTransactionDate().isBetween(startDate, endDate)) {
                        transactions.add(transaction);
                    }
                }
            }

            // Check transactions from current bill
            if (currentBill.getTransactions() != null) {
                for (Transaction transaction : currentBill.getTransactions()) {
                    if (transaction.getTransactionDate().isBetween(startDate, endDate)) {
                        transactions.add(transaction);
                    }
                }
            }
        }
    }

    /**
     * Calculate charge based on the student information.
     * @param studentRecord student record
     */
    private void calculateCharge(StudentRecord studentRecord) {
        // Calculate total credits, lab fee and online course fee
        int totalCredit = checkCourse(studentRecord);

        // Tuition
        calculateTuition(studentRecord, totalCredit);

        // Check technology fee
        calculateOtherFee(studentRecord, totalCredit);
    }

    /**
     * Check the course the student takes. Look for possible lab fees. Calculate total credits of the courses.
     * @param studentRecord student record
     * @return Number of total credits
     */
    private int checkCourse(StudentRecord studentRecord) {
        // Calculate total credits, lab fee and online course fee
        int totalCredit = 0;
        for (Course course : studentRecord.getCourses()) {
            // Calculate total credits
            totalCredit += course.getNumCredits();

            String courseId = course.getId();

            // Get the department of the course
            String courseIdDepartment = "";
            int pos = courseId.lastIndexOf(" ");
            if (pos != -1) {
                courseIdDepartment = courseId.substring(0, pos);
            }

            // Check lab fee
            if (FeeConstant.labFee.containsKey(courseId)) {
                addCharge(FeeConstant.labFee.get(courseId), "LAB FEE - " + courseId);
            } else if (FeeConstant.labFee.containsKey(courseIdDepartment)) {
                addCharge(FeeConstant.labFee.get(courseIdDepartment), "LAB FEE - " + courseId);
            }

            // Check APOGEE fee
            if (courseIdDepartment.equalsIgnoreCase("CSCE") && course.getOnline()) {
                addCharge(FeeConstant.APOGEE_PER_CREDIT_HOUR * course.getNumCredits(), "APOGEE FEE - " + courseId);
            }
        }
        return totalCredit;
    }

    /**
     * Calculate tuition.
     * @param studentRecord student record
     * @param totalCredit total credits
     */
    private void calculateTuition(StudentRecord studentRecord, int totalCredit) {
        if (studentRecord.getFreeTuition()) {
            return;
        }

        // Calculate tuition
        boolean isFullTime = (totalCredit >= 12);
        ClassStatus classStatus = studentRecord.getClassStatus();

        if (classStatus == ClassStatus.MASTERS || classStatus == ClassStatus.PHD) {
            // Graduate student
            if (isFullTime) {
                // Full-time
                if (studentRecord.getResident()) {
                    // Resident
                    addCharge(FeeConstant.GRADUATE_RESIDENT_TUITION_FULL_TIME
                            , "GRADUATE - RESIDENT - TUITION");
                    addCharge(FeeConstant.GRADUATE_RESIDENT_17_HOURS_AND_ABOVE * (totalCredit - 16)
                            , "GRADUATE - RESIDENT - 17 HOURS AND ABOVE");
                } else {
                    // Non-resident
                    addCharge(FeeConstant.GRADUATE_NONRESIDENT_TUITION_FULL_TIME
                            , "GRADUATE - NONRESIDENT - TUITION");
                    addCharge(FeeConstant.GRADUATE_NONRESIDENT_17_HOURS_AND_ABOVE * (totalCredit - 16)
                            , "GRADUATE - NONRESIDENT - 17 HOURS AND ABOVE");
                }

            } else {
                // Part-time
                if (studentRecord.getResident()) {
                    // Resident
                    addCharge(FeeConstant.GRADUATE_RESIDENT_TUITION_PART_TIME * totalCredit
                            , "GRADUATE - RESIDENT - TUITION");

                } else {
                    // Non-resident
                    addCharge(FeeConstant.GRADUATE_NONRESIDENT_TUITION_PART_TIME * totalCredit
                            , "GRADUATE - NONRESIDENT - TUITION");
                }
            }
        }  else if (classStatus == ClassStatus.FRESHMAN || classStatus == ClassStatus.SOPHOMORE ||
                classStatus == ClassStatus.JUNIOR || classStatus == ClassStatus.SENIOR) {
            // Undergraduate student
            if (isFullTime) {
                // Full-time
                if (studentRecord.getActiveDuty() || studentRecord.getVeteran()) {
                    // Active duty or veteran
                    addCharge(FeeConstant.UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_FULL_TIME,
                            "ACTIVE DUTY MILITARY UNDERGRADUATE - TUITION");
                } else {
                    // Not active duty or veteran
                    if (studentRecord.getResident()) {
                        // Resident
                        addCharge(FeeConstant.UNDERGRADUATE_RESIDENT_TUITION_FULL_TIME,
                                "UNDERGRADUATE - RESIDENT - TUITION");
                    } else {
                        // Non-resident
                        if (studentRecord.getScholarship() == Scholarship.WOODROW ||
                                studentRecord.getScholarship() == Scholarship.DEPARTMENTAL) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_FULL_TIME,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - WOODROW & DEPARTMENTAL");
                        } else if (studentRecord.getScholarship() == Scholarship.GENERAL) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_FULL_TIME,
                                    "UNDERGRADUATE - NONRESIDENT SCHOLARSHIP - GENERAL UNIVERSITY");
                        } else if (studentRecord.getScholarship() == Scholarship.ATHLETIC) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_FULL_TIME,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - ATHLETICS");
                        } else if (studentRecord.getScholarship() == Scholarship.SIMS) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_FULL_TIME,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP -SIMS");
                        } else {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_TUITION_FULL_TIME,
                                    "UNDERGRADUATE - NONRESIDENT - TUITION");
                        }
                    }
                }

                // 17 HOURS AND ABOVE Tuition
                if (!studentRecord.getResident() && studentRecord.getScholarship() == Scholarship.NONE &&
                        !studentRecord.getActiveDuty() && !studentRecord.getVeteran()) {
                    addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_17_HOURS_AND_ABOVE * (totalCredit - 16),
                            "UNDERGRADUATE - NONRESIDENT - 17 HOURS AND ABOVE");

                } else {
                    addCharge(FeeConstant.UNDERGRADUATE_RESIDENT_SCHOLARSHIP_ACTIVE_DUTY_MILITARY_17_HOURS_AND_ABOVE
                            * (totalCredit - 16),
                            "UNDERGRADUATE - RESIDENT, NONRESIDENT SCHOLARSHIP, ACTIVE DUTY MILITARY - 17 HOURS AND ABOVE");

                }
            } else {
                // Part-time
                if (studentRecord.getActiveDuty() || studentRecord.getVeteran()) {
                    // Active duty or veteran
                    addCharge(FeeConstant.UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_PART_TIME * totalCredit,
                            "ACTIVE DUTY MILITARY UNDERGRADUATE - TUITION");
                } else {
                    // Not active duty or veteran
                    if (studentRecord.getResident()) {
                        // Resident
                        addCharge(FeeConstant.UNDERGRADUATE_RESIDENT_TUITION_PART_TIME * totalCredit,
                                "UNDERGRADUATE - NONRESIDENT - TUITION");
                    } else {
                        // Non resident
                        if (studentRecord.getScholarship() == Scholarship.WOODROW ||
                                studentRecord.getScholarship() == Scholarship.DEPARTMENTAL) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_PART_TIME * totalCredit,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - WOODROW & DEPARTMENTAL");
                        } else if (studentRecord.getScholarship() == Scholarship.GENERAL) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_PART_TIME * totalCredit,
                                    "UNDERGRADUATE - NONRESIDENT SCHOLARSHIP - GENERAL UNIVERSITY");
                        } else if (studentRecord.getScholarship() == Scholarship.ATHLETIC) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_PART_TIME * totalCredit,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - ATHLETICS");
                        } else if (studentRecord.getScholarship() == Scholarship.SIMS) {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_PART_TIME * totalCredit,
                                    "UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP -SIMS");
                        } else {
                            addCharge(FeeConstant.UNDERGRADUATE_NONRESIDENT_TUITION_PART_TIME * totalCredit,
                                    "UNDERGRADUATE - NONRESIDENT - TUITION");
                        }
                    }
                }
            }

        }
    }

    /**
     * Calculate other fees.
     * @param studentRecord student record
     * @param totalCredit total credits
     */
    private void calculateOtherFee(StudentRecord studentRecord, int totalCredit) {
        boolean isFullTime = (totalCredit >= 12);

        // Tech fee
        if (isFullTime) {
            addCharge(FeeConstant.TECHNOLOGY_FEE_FULL_TIME
                    , "TECHNOLOGY FEE");
        } else {
            addCharge(FeeConstant.TECHNOLOGY_FEE_PART_TIME
                    , "TECHNOLOGY FEE");
        }

        // Insurance
        if (!studentRecord.getOutsideInsurance()) {
            addCharge(FeeConstant.HEALTH_INSURANCE,
                    "HEALTH INSURANCE - (STUDENTS WITHOUT COVERAGE) - CONTRACT W/THIRD PARTY");
        }

        // Capstone
        if (studentRecord.getCapstoneEnrolled() != null) {
            addCharge(FeeConstant.CAPSTONE_SCHOLAR_FEE_PER_SEMESTER,
                    "CAPSTONE SCHOLAR FEE - PER SEMESTER");
        }

        // Health center
        if (studentRecord.getClassStatus() == ClassStatus.MASTERS ||
                studentRecord.getClassStatus() == ClassStatus.PHD) {
            if (studentRecord.getGradAssistant()) {
                if (totalCredit < 12) {
                    addCharge(FeeConstant.GRADUATE_ASSISTANTS_LESS_THAN_12_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER,
                            "GRADUATE ASSISTANTS - LESS THAN 12 HOURS - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER");
                }
            } else {
                if (totalCredit >= 9 && totalCredit <= 11) {
                    addCharge(FeeConstant.GRADUATE_STUDENTS_9_TO_11_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER,
                            "GRADUATE STUDENTS - (9 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER");
                } else if (totalCredit >= 6 && totalCredit <= 8) {
                    addCharge(FeeConstant.GRADUATE_STUDENTS_6_TO_8_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER,
                            "GRADUATE STUDENTS - (6 TO 8 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER");
                }
            }
        } else if (studentRecord.getClassStatus() == ClassStatus.FRESHMAN ||
                studentRecord.getClassStatus() == ClassStatus.SOPHOMORE ||
                studentRecord.getClassStatus() == ClassStatus.JUNIOR ||
                studentRecord.getClassStatus() == ClassStatus.SENIOR){
            if (totalCredit >= 6 && totalCredit <= 11) {
                addCharge(FeeConstant.UNDERGRADUATE_STUDENTS_6_TO_11_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER,
                        "UNDERGRADUATE STUDENTS - (6 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER");
            }
        }

        // International
        if (studentRecord.getInternational()) {
            if (isFirstSemester(studentRecord.getTermBegan().getSemester(), studentRecord.getTermBegan().getYear())) {
                addCharge(FeeConstant.INTERNATIONAL_STUDENT_ENROLLMENT_FEE_ONE_TIME_CHARGE,
                        "INTERNATIONAL STUDENT ENROLLMENT FEE - ONE TIME CHARGE");
            }
            if (studentRecord.getInternationalStatus() == InternationalStatus.SHORT_TERM) {
                addCharge(FeeConstant.SHORT_TERM_INTERNATIONAL_STUDENT_FEE,
                        "SHORT TERM INTERNATIONAL STUDENT FEE");
            } else if (studentRecord.getInternationalStatus() == InternationalStatus.SPONSORED) {
                addCharge(FeeConstant.SPONSORED_INTERNATIONAL_STUDENT_FEE,
                        "SPONSORED INTERNATIONAL STUDENT FEE");
            }
        }

        // Study abroad
        if (studentRecord.getStudyAboard() == StudyAbroad.REGULAR ||
                studentRecord.getStudyAboard() == StudyAbroad.COHORT) {
            addCharge(FeeConstant.STUDY_ABROAD_EXCHANGE_PROGRAM_DEPOSIT_NONREFUNDABLE,
                    "STUDY ABROAD EXCHANGE PROGRAM DEPOSIT - NONREFUNDABLE");
            addCharge(FeeConstant.MANDATORY_STUDY_ABROAD_INSURANCE,
                    "MANDATORY STUDY ABROAD INSURANCE");
            if (studentRecord.getStudyAboard() == StudyAbroad.REGULAR) {
                addCharge(FeeConstant.STUDY_ABROAD, "STUDY ABROAD");
            } else if (studentRecord.getStudyAboard() == StudyAbroad.COHORT) {
                addCharge(FeeConstant.COHORT_STUDY_ABROAD, "COHORT STUDY ABROAD");
            }
        }

        // National student exchange
        if (studentRecord.getNationalStudentExchange()) {
            addCharge(FeeConstant.NATIONAL_STUDENT_EXCHANGE_PLACEMENT_ADMINISTRATIVE_FEE,
                    "NATIONAL STUDENT EXCHANGE PLACEMENT & ADMINISTRATIVE FEE");
        }

        // Matriculation fee
        if (isFirstSemester(studentRecord.getTermBegan().getSemester(), studentRecord.getTermBegan().getYear())) {
            addCharge(FeeConstant.MATRICULATION_FEE,
                    "MATRICULATION FEE");
        }

        // Engineering and computing program fee
        if (studentRecord.getCollege() == College.ENGINEERING_AND_COMPUTING) {
            if (isFullTime) {
                addCharge(FeeConstant.ENGINEERING_AND_COMPUTING_PROGRAM_FEE_FULL_TIME,
                        "ENGINEERING AND COMPUTING PROGRAM FEE - PER SEMESTER");
            } else {
                addCharge(FeeConstant.ENGINEERING_AND_COMPUTING_PROGRAM_FEE_PART_TIME * totalCredit,
                        "ENGINEERING AND COMPUTING PROGRAM FEE - PER SEMESTER");
            }
        }
    }

    /**
     * Add charge to the transaction history
     * @param amount the amount of the bill
     * @param note charge note
     */
    private void addCharge(double amount, String note) {
        if (amount <= 0) {
            return;
        }
        this.balance += amount;
        this.transactions.add(new Transaction(Type.CHARGE, amount, note));
    }

    /**
     * Make a payment
     * @param amount the mount of the payment
     * @param note payment note
     * @throws InvalidPaymentException when payment fails
     */
    public void makePayment(BigDecimal amount, String note) throws InvalidPaymentException {
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new InvalidPaymentException("Payment is less than 0.");
        }

        BigDecimal balanceBD = new BigDecimal(this.balance);
        if (amount.compareTo(balanceBD) == 1) {
            throw new InvalidPaymentException("Payment is greater than balance.");
        } else {
            balanceBD = balanceBD.subtract(amount);
            this.balance = balanceBD.doubleValue();
            transactions.add(new Transaction(Type.PAYMENT, amount.doubleValue(), note));
        }

    }

    /**
     * Check if the input start semester is the current semester
     * @param startSemester start semester
     * @param startYear start year
     * @return if it is the first semester
     */
    private boolean isFirstSemester(Semester startSemester, int startYear) {
        Calendar now = Calendar.getInstance();
        Semester currentSemester;
        Date currentDate = new Date(now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.YEAR));
        Date date1 = new Date(5, 15, now.get(Calendar.YEAR));
        Date date2 = new Date(8, 15, now.get(Calendar.YEAR));
        if (currentDate.isBefore(date1)) {
            currentSemester = Semester.SPRING;
        } else if (date2.isBefore(currentDate)) {
            currentSemester = Semester.FALL;
        } else {
            currentSemester = Semester.SUMMER;
        }

        return startYear == now.get(Calendar.YEAR) && startSemester == currentSemester;
    }

    /**
     * Print out the bill
     * @return The current bill
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("ID: ").append(this.getStudent().getId()).append("\n");
        output.append("Balance: ").append(this.getBalance()).append("\n\n");
        for (Transaction transaction : transactions) {
            output.append(transaction.toString()).append("\n");
        }
        return output.toString();
    }
}
