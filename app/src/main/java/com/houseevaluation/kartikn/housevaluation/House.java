package com.houseevaluation.kartikn.housevaluation;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kartikn on 13-09-2015.
 */
public class House {

    private static final double self_occupied_max_interest = 200000;
    private static final double rent_claimable_percentage = 0.7;
    private static final double max_tax_rate = 0.309;
    private double principal = 0;
    private double monthly_interest = 0;
    private double months = 0;
    private double emi = 0;
    private String schedule = "ID, Month, Year, Op Bal , Principal Paid, Interest Paid, Closing Balance, Rent, Tax Status \n";
    private String yearly_schedule = "ID, Year,  Principal , Interest ,Tax Status, Rent , Tax Saving, Total Outflow \n";
    private int loan_start_month;
    private int loan_start_year;
    private int handover_month;
    private int handover_year;
    private int rent_start_month;
    private int rent_start_year;
    private double first_rent;
    private double rent_increase;
    private boolean self_occupied;
    private ArrayList<MonthlyLedger> monthlyLedgers = new ArrayList<>();
    private ArrayList<YearlyLedger> yearlyLedgers = new ArrayList<>();

    public House(double principal, double monthly_interest, double months) {
        this.principal = principal;
        this.monthly_interest = monthly_interest;
        this.months = months;
        this.self_occupied = true;
        setEmi();
    }

    public void setFirst_rent(double first_rent) {
        this.first_rent = first_rent;
    }

    public void setRent_increase(double rent_increase) {
        this.rent_increase = rent_increase;
    }

    public boolean isSelf_occupied() {
        return self_occupied;
    }

    public void setSelf_occupied(boolean self_occupied) {
        this.self_occupied = self_occupied;
    }

    public int getLoan_start_year() {
        return loan_start_year;
    }

    public void setLoan_start_year(int loan_start_year) {
        this.loan_start_year = loan_start_year;
    }

    public int getHandover_month() {
        return handover_month;
    }

    public void setHandover_month(int handover_month) {
        this.handover_month = handover_month;
    }

    public int getHandover_year() {
        return handover_year;
    }

    public void setHandover_year(int handover_year) {
        this.handover_year = handover_year;
    }

    public int getRent_start_month() {
        return rent_start_month;
    }

    public void setRent_start_month(int rent_start_month) {
        this.rent_start_month = rent_start_month;
    }

    public int getRent_start_year() {
        return rent_start_year;
    }

    public void setRent_start_year(int rent_start_year) {
        this.rent_start_year = rent_start_year;
    }

    public int getLoan_start_month() {
        return loan_start_month;
    }

    public void setLoan_start_month(int loan_start_month) {
        this.loan_start_month = loan_start_month;
    }

    public double getEmi() {
        return emi;
    }

    public void setEmi() {
        emi = Math.round(principal * monthly_interest * Math.pow(1 + monthly_interest, months) / (Math.pow(1 + monthly_interest, months) - 1) * 100) / 100;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getYearly_schedule() {
        return yearly_schedule;
    }

    public void createSchedule() {
        double op_bal = principal;
        double temp_int;
        double temp_principal;
        double temp_yrly_int = 0;
        double temp_yrly_principal = 0;
        int current_month = loan_start_month;
        int current_year = loan_start_year;

        for (int i = current_month; i < current_month + months; i++) {
            temp_int = Math.round(op_bal * monthly_interest * 100) / 100;
            temp_yrly_int += temp_int;
            temp_principal = emi - temp_int;
            temp_yrly_principal += temp_principal;
            MonthlyLedger monthlyLedger = new MonthlyLedger(i - current_month, i % 12, current_year, op_bal, temp_principal, temp_int);
            monthlyLedgers.add(monthlyLedger);
            op_bal = op_bal - temp_principal;
            if ((i + 1) % 12 == 0) {
                YearlyLedger yearlyLedger = new YearlyLedger(current_year - loan_start_year, current_year, temp_yrly_int, temp_yrly_principal);
                yearlyLedgers.add(yearlyLedger);
                current_year += 1;
                temp_yrly_int = 0;
                temp_yrly_principal = 0;
            }
        }
    }

    public void setRent() {
        double temp_yrly_rent = 0;
        double temp_mthly_rent = 0;
        int delta_between_loan_and_handover = getID(loan_start_month, loan_start_year, handover_month, handover_year);
        int delta_between_handover_and_rent = getID(handover_month, handover_year, rent_start_month, rent_start_year);
        Log.d("setRent", "delta_between_handover_and_rent " + delta_between_handover_and_rent);
        Log.d("setRent", "delta_between_loan_and_handover " + delta_between_loan_and_handover);
        char temp_yrly_status = 'C';
        for (int i = 0; i < months; i++) {
            if (i < delta_between_loan_and_handover) {
                monthlyLedgers.get(i).setTax_status('C');
                temp_yrly_status = 'C';
            } else {
                if (i >= delta_between_handover_and_rent && i < (delta_between_loan_and_handover + delta_between_handover_and_rent)) {
                    monthlyLedgers.get(i).setTax_status('H');
                    temp_yrly_status = 'H';
                } else {
                    if (self_occupied) {
                        monthlyLedgers.get(i).setTax_status('S');
                        temp_yrly_status = 'S';
                    } else {
                        monthlyLedgers.get(i).setTax_status('R');
                        temp_yrly_status = 'R';
                        temp_mthly_rent = first_rent;
                    }
                }
            }
            monthlyLedgers.get(i).setRent_collected(temp_mthly_rent);
            temp_yrly_rent += temp_mthly_rent;
            if ((i + 1) % 12 == 0) {
                yearlyLedgers.get(getYearID(i)).setYearly_rent((double) Math.round(temp_yrly_rent * Math.pow(1 + rent_increase, getYearID(i)) * 100) / 100);
                yearlyLedgers.get(getYearID(i)).setTax_status(temp_yrly_status);
                temp_yrly_rent = 0;
            }
            temp_mthly_rent = 0;
        }
        calculateTaxAndOutflow();
        exportSchedule();
        exportYearlySchedule();
    }


    private void calculateTaxAndOutflow() {
        double tmp_tax_saving = 0;
        double construction_interest = 0;
        char tmp_tax_status;
        int counter = 0;

        for (YearlyLedger i : yearlyLedgers) {
            tmp_tax_status = i.getTax_status();
            if (tmp_tax_status == 'C') {
                tmp_tax_saving = 0;
                construction_interest += i.getYearly_interest();
            }
            if ((tmp_tax_status == 'H') || (tmp_tax_status == 'S')) {
                tmp_tax_saving = i.getYearly_interest();
                if (counter < 5) {
                    tmp_tax_saving += construction_interest / 5;
                    counter++;
                }
                tmp_tax_saving = Math.min(tmp_tax_saving, self_occupied_max_interest);
            }
            if (i.getTax_status() == 'R') {
                tmp_tax_saving = (i.getYearly_interest() - i.getYearly_rent() * rent_claimable_percentage) * max_tax_rate;
                if (counter < 5) {
                    tmp_tax_saving += construction_interest / 5;
                    counter++;
                }
            }
            i.setTax_saving(Math.round(tmp_tax_saving * 100) / 100);
            i.setTotal_outflow();
        }
    }


    private int getID(int start_month, int start_year, int end_month, int end_year) {
        return (end_year - start_year) * 12 + (end_month - start_month);
    }

    private int getYearID(int ID) {
        return ID / 12;
    }

    private void exportSchedule() {
        for (MonthlyLedger i : monthlyLedgers) {
            schedule += (i.getMonth_id() + 1) + "," +
                    i.getMonth() + "," +
                    i.getYear() + "," +
                    i.getOpening_balance() + "," +
                    i.getPrincipal_paid() + "," +
                    i.getInterest_paid() + "," +
                    i.getClosing_balance() + "," +
                    i.getRent_collected() + "," +
                    i.getTax_status() + "\n";
        }
    }

    private void exportYearlySchedule() {
        for (YearlyLedger i : yearlyLedgers) {
            yearly_schedule += (i.getYear_id() + 1) + "," +
                    i.getYear() + "," +
                    i.getYearly_principal() + "," +
                    i.getYearly_interest() + "," +
                    i.getTax_status() + "," +
                    i.getYearly_rent() + "," +
                    i.getTax_saving() + "," +
                    i.getTotal_outflow() + "\n";
        }
    }

}
