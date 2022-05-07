package com.dzco.vaccination.Models;

import java.time.ZonedDateTime;

public class Date {
    public  static final Date NULL_DATE = new Date();
    private int day, year, month;

    public Date() {
        this.day = 0;
        this.month = 0;
        this.year = 0;
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(Date date) {
        this.day = date.day;
        this.month = date.month;
        this.year = date.year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isSmallerThan(Date date) {
        if (this.year < date.year) {
            return true;
        }
        if (this.year == date.year && this.month < date.month) {
            return true;
        }
        return this.year == date.year && this.month == date.month && this.day < date.day;
    }

    public boolean isBiggerThan(Date date) {
        if (this.year > date.year) {
            return true;
        }
        if (this.year == date.year && this.month > date.month) {
            return true;
        }
        return this.year == date.year && this.month == date.month && this.day > date.day;
    }

    public boolean isNull() {
        return this.day == NULL_DATE.day && this.month == NULL_DATE.month && this.year == NULL_DATE.year;
    }

    public boolean isMinDate() {
        return this.day == User.MIN_DATE.day && this.month == User.MIN_DATE.month && this.year == User.MIN_DATE.year;
    }

    public void setCurrent() {
        this.year = ZonedDateTime.now().getYear();
        this.month = ZonedDateTime.now().getMonthValue();
        this.day = ZonedDateTime.now().getDayOfMonth();
    }

    @Override
    public String toString() {
        return day + "-" + month + "-" + year;
    }
}
