package edu.sc.csce740.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DateTest {

    private Date setUp(int month, int day, int year) {
        return new Date(month, day, year);
    }

    @Test
    public void testIsBetween() {
        Date dateBefore = setUp(1, 1, 2017);
        Date dateAfter = setUp(2, 1, 2017);
        Date dateBetween = setUp(1, 20, 2017);
        assertTrue(dateBetween.isBetween(dateBefore, dateAfter));
    }

}
