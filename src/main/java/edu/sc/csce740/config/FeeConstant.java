package edu.sc.csce740.config;

import java.util.Map;
import java.util.HashMap;

/**
 * A class to store the fees and tuition constants.
 * Change the constants when fess change.
 */
public class FeeConstant {
    // Undergraduate tuition
    public static final double UNDERGRADUATE_RESIDENT_TUITION_FULL_TIME = 5727;
    public static final double UNDERGRADUATE_RESIDENT_TUITION_PART_TIME = 477.25;
    public static final double UNDERGRADUATE_NONRESIDENT_TUITION_FULL_TIME = 5727;
    public static final double UNDERGRADUATE_NONRESIDENT_TUITION_PART_TIME = 1286.75;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_FULL_TIME = 8502;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_PART_TIME = 708.5;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_FULL_TIME = 5727;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_PART_TIME = 477.25;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_FULL_TIME = 8502;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_PART_TIME = 708.5;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_FULL_TIME = 10965;
    public static final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_PART_TIME = 913.75;
    public static final double UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_FULL_TIME = 3351;
    public static final double UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_PART_TIME = 279.25;
    public static final double UNDERGRADUATE_RESIDENT_SCHOLARSHIP_ACTIVE_DUTY_MILITARY_17_HOURS_AND_ABOVE = 80;
    public static final double UNDERGRADUATE_NONRESIDENT_17_HOURS_AND_ABOVE = 208;

    // Graduate tuition
    public static final double GRADUATE_RESIDENT_TUITION_FULL_TIME = 6399;
    public static final double GRADUATE_RESIDENT_TUITION_PART_TIME = 533.25;
    public static final double GRADUATE_NONRESIDENT_TUITION_FULL_TIME = 13704;
    public static final double GRADUATE_NONRESIDENT_TUITION_PART_TIME = 1142;
    public static final double GRADUATE_RESIDENT_17_HOURS_AND_ABOVE = 80;
    public static final double GRADUATE_NONRESIDENT_17_HOURS_AND_ABOVE = 170;

    // Technology fee
    public static final double TECHNOLOGY_FEE_FULL_TIME = 200;
    public static final double TECHNOLOGY_FEE_PART_TIME = 17;

    // Other fee
    public static final double INTERNATIONAL_STUDENT_ENROLLMENT_FEE_ONE_TIME_CHARGE = 750;
    public static final double SHORT_TERM_INTERNATIONAL_STUDENT_FEE = 187.5;
    public static final double SPONSORED_INTERNATIONAL_STUDENT_FEE = 250;
    public static final double STUDY_ABROAD = 150;
    public static final double COHORT_STUDY_ABROAD = 300;
    public static final double STUDY_ABROAD_EXCHANGE_PROGRAM_DEPOSIT_NONREFUNDABLE = 500;
    public static final double MANDATORY_STUDY_ABROAD_INSURANCE = 360;
    public static final double MATRICULATION_FEE = 80;
    public static final double CAPSTONE_SCHOLAR_FEE_PER_SEMESTER = 100;
    public static final double NATIONAL_STUDENT_EXCHANGE_PLACEMENT_ADMINISTRATIVE_FEE = 250;

    // Health Insurance
    public static final double HEALTH_INSURANCE = 2547;
    public static final double GRADUATE_ASSISTANTS_LESS_THAN_12_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER = 178;
    public static final double GRADUATE_STUDENTS_9_TO_11_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER = 178;
    public static final double GRADUATE_STUDENTS_6_TO_8_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER = 119;
    public static final double UNDERGRADUATE_STUDENTS_6_TO_11_HOURS_REQUIRED_STUDENT_HEALTH_CENTER_FEE_PER_SEMESTER = 119;

    // Engineering fee
    public static final double APOGEE_PER_CREDIT_HOUR = 220;
    public static final double ENGINEERING_AND_COMPUTING_PROGRAM_FEE_FULL_TIME = 918;
    public static final double ENGINEERING_AND_COMPUTING_PROGRAM_FEE_PART_TIME = 76.5;

    // Lab fee
    public static final Map<String, Double> labFee;
    static {
        labFee = new HashMap<String, Double>();
        labFee.put("ART EDUCATION 101", 40.0);
        labFee.put("ART EDUCATION 260", 40.0);
        labFee.put("ART EDUCATION 520", 40.0);
        labFee.put("ART EDUCATION 530", 40.0);
        labFee.put("ART EDUCATION 535", 40.0);
        labFee.put("ART EDUCATION 541", 40.0);
        labFee.put("ART EDUCATION 555", 40.0);
        labFee.put("ART EDUCATION 560", 40.0);
        labFee.put("ART EDUCATION 595", 40.0);
        labFee.put("ART HISTORY 105", 40.0);
        labFee.put("ART HISTORY 106", 40.0);
        labFee.put("ART HISTORY 313", 40.0);
        labFee.put("ART HISTORY 315", 40.0);
        labFee.put("ART HISTORY 320", 40.0);
        labFee.put("ART HISTORY 321", 40.0);
        labFee.put("ART HISTORY 325", 40.0);
        labFee.put("ART HISTORY 326", 40.0);
        labFee.put("ART HISTORY 327", 40.0);
        labFee.put("ART HISTORY 330", 40.0);
        labFee.put("ART HISTORY 335", 40.0);
        labFee.put("ART HISTORY 337", 40.0);
        labFee.put("ART HISTORY 340", 40.0);
        labFee.put("ART HISTORY 341", 40.0);
        labFee.put("ART HISTORY 342", 40.0);
        labFee.put("ART HISTORY 345", 40.0);
        labFee.put("ART HISTORY 346", 40.0);
        labFee.put("ART HISTORY 350", 40.0);
        labFee.put("ART HISTORY 365", 40.0);
        labFee.put("ART HISTORY 366", 40.0);
        labFee.put("ART HISTORY 370", 40.0);
        labFee.put("ART HISTORY 390", 40.0);
        labFee.put("ART HISTORY 399", 40.0);
        labFee.put("ART HISTORY 498", 40.0);
        labFee.put("ART HISTORY 499", 40.0);
        labFee.put("ART HISTORY 501", 40.0);
        labFee.put("ART HISTORY 511", 40.0);
        labFee.put("ART HISTORY 514", 40.0);
        labFee.put("ART HISTORY 519", 40.0);
        labFee.put("ART HISTORY 520", 40.0);
        labFee.put("ART HISTORY 521", 40.0);
        labFee.put("ART HISTORY 522", 40.0);
        labFee.put("ART HISTORY 523", 40.0);
        labFee.put("ART HISTORY 524", 40.0);
        labFee.put("ART HISTORY 525", 40.0);
        labFee.put("ART HISTORY 526", 40.0);
        labFee.put("ART HISTORY 527", 40.0);
        labFee.put("ART HISTORY 529", 40.0);
        labFee.put("ART HISTORY 534", 40.0);
        labFee.put("ART HISTORY 535", 40.0);
        labFee.put("ART HISTORY 536", 40.0);
        labFee.put("ART HISTORY 537", 40.0);
        labFee.put("ART HISTORY 539", 40.0);
        labFee.put("ART HISTORY 540", 40.0);
        labFee.put("ART HISTORY 542", 40.0);
        labFee.put("ART HISTORY 543", 40.0);
        labFee.put("ART HISTORY 550", 40.0);
        labFee.put("ART HISTORY 557", 40.0);
        labFee.put("ART HISTORY 560", 40.0);
        labFee.put("ART HISTORY 561", 40.0);
        labFee.put("ART HISTORY 562", 40.0);
        labFee.put("ART HISTORY 566", 40.0);
        labFee.put("ART HISTORY 569", 40.0);
        labFee.put("ART HISTORY 590", 40.0);
        labFee.put("ART HISTORY 720", 40.0);
        labFee.put("ART HISTORY 725", 40.0);
        labFee.put("ART HISTORY 730", 40.0);
        labFee.put("ART HISTORY 735", 40.0);
        labFee.put("ART HISTORY 737", 40.0);
        labFee.put("ART HISTORY 769", 40.0);
        labFee.put("ART HISTORY 790", 40.0);
        labFee.put("DANCE 102", 75.0);
        labFee.put("DANCE 112", 75.0);
        labFee.put("DANCE 160", 75.0);
        labFee.put("DANCE 170", 75.0);
        labFee.put("DANCE 171", 75.0);
        labFee.put("DANCE 177", 75.0);
        labFee.put("DANCE 178", 75.0);
        labFee.put("DANCE 202", 75.0);
        labFee.put("DANCE 203", 75.0);
        labFee.put("DANCE 204", 75.0);
        labFee.put("DANCE 212", 75.0);
        labFee.put("DANCE 278", 75.0);
        labFee.put("DANCE 302", 75.0);
        labFee.put("DANCE 303", 75.0);
        labFee.put("DANCE 307", 75.0);
        labFee.put("DANCE 312", 75.0);
        labFee.put("DANCE 360", 75.0);
        labFee.put("DANCE 378", 75.0);
        labFee.put("DANCE 385", 75.0);
        labFee.put("DANCE 402", 75.0);
        labFee.put("DANCE 403", 75.0);
        labFee.put("DANCE 407", 75.0);
        labFee.put("DANCE 412", 75.0);
        labFee.put("DANCE 440", 75.0);
        labFee.put("DANCE 460", 75.0);
        labFee.put("DANCE 577", 75.0);
        labFee.put("MEDIA ARTS", 100.0);
        labFee.put("STUDIO ARTS", 100.0);
        labFee.put("MSCI 460", 300.0);
        labFee.put("MATH 141", 105.0);
        labFee.put("MATH 142", 105.0);
        labFee.put("MATH 526", 105.0);
        labFee.put("STAT 201", 105.0);
        labFee.put("PHYSICS", 105.0);
        labFee.put("ASTRONOMY", 105.0);
        labFee.put("BIOLOGY", 105.0);
        labFee.put("CHEMISTRY", 105.0);
        labFee.put("ENVIRONMENT", 105.0);
        labFee.put("GEOLOGY AND MARINE SCIENCE", 105.0);
        labFee.put("PSYC 227", 105.0);
        labFee.put("PSYC 228", 105.0);
        labFee.put("PSYC 489", 105.0);
        labFee.put("PSYC 498", 105.0);
        labFee.put("PSYC 570", 105.0);
        labFee.put("PSYC 571", 105.0);
        labFee.put("PSYC 572", 105.0);
        labFee.put("PSYC 574", 105.0);
        labFee.put("PSYC 575", 105.0);
        labFee.put("PSYC 598", 105.0);
        labFee.put("PSYC 599", 105.0);
        labFee.put("PSYC 709", 105.0);
        labFee.put("PSYC 710", 105.0);
        labFee.put("PSYC 762", 105.0);
        labFee.put("ANTH 161", 105.0);
        labFee.put("ANTH 391", 105.0);
        labFee.put("ANTH 561", 105.0);
        labFee.put("GEOG 201", 105.0);
        labFee.put("GEOG 202", 105.0);
        labFee.put("GEOL 735", 105.0);
        labFee.put("GEOL 750", 105.0);
        labFee.put("CSCE 101", 148.0);
        labFee.put("CSCE 102", 148.0);
    }
}
