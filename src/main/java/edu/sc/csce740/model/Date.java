package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Date {
    private int month;
    private int day;
    private int year;

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

    public boolean isBetween(Date date1, Date date2) {
        Date currentDate = new Date(this.month, this.day, this.year);
        return isBefore(date1, currentDate) && isBefore(currentDate, date2);
    }

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

    public String toString() {
        return month + "/" + day + "/" + year;
    }
}
