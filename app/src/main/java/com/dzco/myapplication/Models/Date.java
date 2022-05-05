package com.dzco.myapplication.Models;

public class Date {
    public  static final Date NULL_DATE = new Date();
    private int day, year, month;

    public Date() {}

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
        if (this.year == date.year && this.month == date.month && this.day < date.day) {
            return true;
        }
        return false;
    }

    public boolean isBiggerThan(Date date) {
        if (this.year > date.year) {
            return true;
        }
        if (this.month > date.month) {
            return true;
        }
        if (this.day > date.day) {
            return true;
        }
        return false;
    }

    public boolean isNull() {
        if (this.day == NULL_DATE.day && this.month == NULL_DATE.month && this.year == NULL_DATE.year) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMinDate() {
        if (this.day == User.MIN_DATE.day && this.month == User.MIN_DATE.month && this.year == User.MIN_DATE.year) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return day + "-" + month + "-" + year;
    }
}
