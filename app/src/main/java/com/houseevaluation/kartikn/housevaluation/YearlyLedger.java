package com.houseevaluation.kartikn.housevaluation;

/**
 * Created by kartikn on 15-09-2015.
 */
public class YearlyLedger {

    private int year;


    private int year_id;
    private double yearly_interest;
    private double yearly_principal;
    private double yearly_rent;
    private char tax_status;
    private double tax_saving;
    private double total_outflow;


    public YearlyLedger(int year_id, int year, double yearly_interest, double yearly_principal) {
        this.year = year;
        this.year_id = year_id;
        this.yearly_interest = yearly_interest;
        this.yearly_principal = yearly_principal;
        this.yearly_rent = 0;
        this.tax_status = 'C';
        this.tax_saving = 0;
        this.total_outflow = 0;
    }

    public char getTax_status() {
        return tax_status;
    }

    public void setTax_status(char tax_status) {
        this.tax_status = tax_status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getYearly_interest() {
        return yearly_interest;
    }

    public void setYearly_interest(double yearly_interest) {
        this.yearly_interest = yearly_interest;
    }

    public double getYearly_principal() {
        return yearly_principal;
    }

    public void setYearly_principal(double yearly_principal) {
        this.yearly_principal = yearly_principal;
    }

    public double getYearly_rent() {
        return yearly_rent;
    }

    public void setYearly_rent(double yearly_rent) {
        this.yearly_rent = yearly_rent;
    }

    public double getTax_saving() {
        return tax_saving;
    }

    public void setTax_saving(double tax_saving) {
        this.tax_saving = tax_saving;
    }

    public double getTotal_outflow() {
        return total_outflow;
    }

    public void setTotal_outflow() {

        total_outflow = tax_saving + yearly_rent - yearly_principal - yearly_interest;
    }

    public int getYear_id() {
        return year_id;
    }

    public void setYear_id(int year_id) {
        this.year_id = year_id;
    }

}



