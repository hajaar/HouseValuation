package com.houseevaluation.kartikn.housevaluation;

/**
 * Created by kartikn on 15-09-2015.
 */
public class MonthlyLedger {

    private int financial_year;
    private int calendar_year;
    private int month;
    private int month_id;
    private double opening_balance;
    private double principal_paid;
    private double closing_balance;
    private double interest_paid;
    private char tax_status;
    private double rent_collected;

    public MonthlyLedger(int month_id, int month, int financial_year, double opening_balance, double principal_paid, double interest_paid) {
        this.financial_year = financial_year;
        this.month = month;
        if (month < 3) {
            calendar_year = financial_year + 1;
        } else {
            calendar_year = financial_year;
        }
        this.month_id = month_id;
        this.opening_balance = opening_balance;
        this.principal_paid = principal_paid;
        this.interest_paid = interest_paid;
        this.closing_balance = opening_balance - principal_paid;
        this.rent_collected = 0;
        this.tax_status = 'C';

    }

    public int getCalendar_year() {
        return calendar_year;
    }

    public char getTax_status() {
        return tax_status;
    }

    public void setTax_status(char tax_status) {
        this.tax_status = tax_status;
    }

    public int getFinancial_year() {
        return financial_year;
    }

    public void setFinancial_year(int financial_year) {
        this.financial_year = financial_year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth_id() {
        return month_id;
    }

    public void setMonth_id(int moth_id) {
        this.month_id = moth_id;
    }

    public double getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(double opening_balance) {
        this.opening_balance = opening_balance;
    }

    public double getPrincipal_paid() {
        return principal_paid;
    }

    public void setPrincipal_paid(double principal_paid) {
        this.principal_paid = principal_paid;
    }

    public double getClosing_balance() {
        return closing_balance;
    }

    public void setClosing_balance(double closing_balance) {
        this.closing_balance = closing_balance;
    }

    public double getInterest_paid() {
        return interest_paid;
    }

    public void setInterest_paid(double interest_paid) {
        this.interest_paid = interest_paid;
    }

    public double getRent_collected() {
        return rent_collected;
    }

    public void setRent_collected(double rent_collected) {
        this.rent_collected = rent_collected;
    }
}
