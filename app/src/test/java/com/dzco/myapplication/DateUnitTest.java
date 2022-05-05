package com.dzco.myapplication;

import org.junit.Assert;
import org.junit.Test;
import com.dzco.myapplication.Models.Date;
import com.dzco.myapplication.Models.User;

public class DateUnitTest {

    /*
    isNull() testing
     */

    @Test
    public void isNullPositiveTest() {
        Date date = new Date();
        Assert.assertEquals(true, date.isNull());
    }

    @Test
    public void isNullNegativeTest() {
        Date date = new Date(User.MIN_DATE);
        Assert.assertEquals(false, date.isNull());
    }

    /*
    isMinDate() testing
     */

    @Test
    public void isMinDatePositiveTest() {
        Date date = new Date(User.MIN_DATE);
        Assert.assertEquals(true, date.isMinDate());
    }

    @Test
    public void isMinDateNegativeTest() {
        Date date = new Date();
        Assert.assertEquals(false, date.isMinDate());
    }

    /*
    isSmaller(Date date) testing
     */

    @Test
    public void isSmallerYearPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 5, 2022);
        Assert.assertEquals(true, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerMonthPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 6, 2021);
        Assert.assertEquals(true, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerDayPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(6, 5, 2022);
        Assert.assertEquals(true, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerYearNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 5, 2020);
        Assert.assertEquals(false, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerMonthNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 4, 2021);
        Assert.assertEquals(false, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerDayNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(4, 5, 2021);
        Assert.assertEquals(false, date1.isSmallerThan(date2));
    }

    @Test
    public void isSmallerEqualTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 5, 2021);
        Assert.assertEquals(false, date1.isSmallerThan(date2));
    }

    /*
    isBigger(Date date) testing
     */

    @Test
    public void isBiggerYearPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 5, 2020);
        Assert.assertEquals(true, date1.isBiggerThan(date2));
    }

    @Test
    public void isBiggerMonthPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 4, 2021);
        Assert.assertEquals(true, date1.isBiggerThan(date2));
    }

    @Test
    public void isBiggerDayPositiveTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(4, 5, 2022);
        Assert.assertEquals(true, date1.isBiggerThan(date2));
    }

    @Test
    public void isBiggerYearNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 5, 2022);
        Assert.assertEquals(false, date1.isBiggerThan(date2));
    }

    @Test
    public void isBiggerMonthNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(5, 6, 2021);
        Assert.assertEquals(false, date1.isBiggerThan(date2));
    }

    @Test
    public void isBiggerDayNegativeTest() {
        Date date1 = new Date(5, 5, 2021);
        Date date2 = new Date(6, 5, 2022);
        Assert.assertEquals(false, date1.isBiggerThan(date2));
    }
}
