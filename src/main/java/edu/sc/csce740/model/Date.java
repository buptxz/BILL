package edu.sc.csce740.model;

import edu.sc.csce740.exception.InvalidDateException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Date {
    private int month;
    private int day;
    private int year;

    public Date(int month, int day, int year)  {
        if (month <= 0 || month >= 12 || day <= 0 || day >= 31) {
            throw new InvalidDateException();
        } else {
            this.month = month;
            this.day = day;
            this.year = year;
        }
    }

    /**
     *
     * @param otherDate
     * @return
     */
    public boolean isBefore(Date otherDate) {
        if (this.year < otherDate.getYear()) {
            return true;
        } else if (this.year > otherDate.getYear()) {
            return false;
        } else {
            if (this.month < otherDate.getMonth()) {
                return true;
            } else if (this.month > otherDate.getMonth()) {
                return false;
            } else {
                if (this.day <= otherDate.getDay()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public boolean isBetween(Date date1, Date date2) {
        Date currentDate = new Date(this.month, this.day, this.year);
        return isBefore(date1, currentDate) && isBefore(currentDate, date2);
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBefore(Date date1, Date date2) {
        if (date1.getYear() < date2.getYear()) {
            return true;
        } else if (date1.getYear() > date2.getYear()) {
            return false;
        } else {
            if (date1.getMonth() < date2.getMonth()) {
                return true;
            } else if (date1.getMonth() > date2.getMonth()) {
                return false;
            } else {
                if (date1.getDay() <= date2.getDay()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     *
     * @param date1 input date
     * @param date2 start date
     * @param date3 end date
     * @return
     */
    public static boolean isBeween(Date date1, Date date2, Date date3) {
        return isBefore(date2, date3) && isBefore(date2, date1) && isBefore(date1, date3);
    }

    /**
     *
     * @return
     */
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}
