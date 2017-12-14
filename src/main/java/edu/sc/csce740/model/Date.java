package edu.sc.csce740.model;

import edu.sc.csce740.exception.InvalidDateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class Date {
    private int month;
    private int day;
    private int year;

    public Date(int month, int day, int year)  {
        if (month <= 0 || month > 12 || day <= 0 || day > 31 || year < 1000 || year > 9999) {
            throw new InvalidDateException();
        } else {
            this.month = month;
            this.day = day;
            this.year = year;
        }
    }

    /**
     * Check if the current date is before the provided date
     * @param otherDate the other date
     * @return if the current date is before the provided date
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
                return this.day <= otherDate.getDay();
            }
        }
    }

    /**
     * Check if the date1 is before the date2
     * @param date1 the first date
     * @param date2 the second date
     * @return if the date1 is before the date2
     */
    public boolean isBetween(Date date1, Date date2) {
        Date currentDate = new Date(this.month, this.day, this.year);
        return isBefore(date1, currentDate) && isBefore(currentDate, date2);
    }

    /**
     * Check if the current date is between the start and the end date
     * @param startDate start date
     * @param endDate end date
     * @return if the current date is between the start and the end date
     */
    public static boolean isBefore(Date startDate, Date endDate) {
        if (startDate.getYear() < endDate.getYear()) {
            return true;
        } else if (startDate.getYear() > endDate.getYear()) {
            return false;
        } else {
            if (startDate.getMonth() < endDate.getMonth()) {
                return true;
            } else if (startDate.getMonth() > endDate.getMonth()) {
                return false;
            } else {
                return startDate.getDay() <= endDate.getDay();
            }
        }
    }
}
