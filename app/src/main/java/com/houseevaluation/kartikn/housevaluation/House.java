package com.houseevaluation.kartikn.housevaluation;

import java.util.ArrayList;

/**
 * Created by kartikn on 13-09-2015.
 */
public class House {

    static final double self_occupied_max_interest = 200000;
    private double principal = 0;
    private double monthly_interest = 0;
    private double months = 0;
    private double emi = 0;
    private String schedule = "ID, Month, Year, Op Bal , Principal Paid, Interest Paid, Closing Balance, Rent \n";
    private String yearly_schedule = "Interest , Principal , Rent , Tax Saving, Total Outflow \n";
    private int loan_start_month = 9;
    private int loan_start_year = 2015;
    private int rent_start_month = 0;
    private int rent_start_year = 2016;
    private int handover_month = 11;
    private int handover_year = 2015;
    private double first_rent = 15000;
    private double rent_increase = 0.05;
    private int occupation_status = 0;
    private ArrayList<MonthlyLedger> monthlyLedgers = new ArrayList<>();
    private ArrayList<YearlyLedger> yearlyLedgers = new ArrayList<>();

    public House(double principal, double monthly_interest, double months, int occupation_status) {
        this.principal = principal;
        this.monthly_interest = monthly_interest;
        this.months = months;
        this.occupation_status = occupation_status;
        setEmi();
        createSchedule();
        setRent();

    }

    public double getEmi() {
        return emi;
    }

    public void setEmi(double emi) {
        this.emi = emi;
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

        for (int i = current_month; i < months; i++) {
            temp_int = Math.round(op_bal * monthly_interest * 100) / 100;
            temp_yrly_int += temp_int;
            temp_principal = emi - temp_int;
            temp_yrly_principal += temp_principal;
            MonthlyLedger monthlyLedger = new MonthlyLedger(i - current_month, i, current_year, op_bal, temp_principal, temp_int);
            monthlyLedgers.add(monthlyLedger);
            op_bal = op_bal - temp_principal;
            if (i % 12 == 0) {
                YearlyLedger yearlyLedger = new YearlyLedger(current_year, current_year - loan_start_year, temp_yrly_int, temp_yrly_principal);
                yearlyLedgers.add(yearlyLedger);
                current_year += 1;
            }
        }
        exportSchedule();
    }

    public void setRent() {
        int temp_yrly_rent = 0;
        int temp_mthly_rent = 0;
        int delta_between_loan_and_handover = getID(loan_start_month, loan_start_year, handover_month, handover_year);
        int delta_between_handover_and_rent = getID(handover_month, handover_year, rent_start_month, rent_start_year);
        for (int i = 0; i < delta_between_loan_and_handover + delta_between_handover_and_rent; i++) {
            monthlyLedgers.get(i).setRent_collected(0);
            if (i <= delta_between_loan_and_handover) {
                monthlyLedgers.get(i).setTax_status('C');
            } else {
                monthlyLedgers.get(i).setTax_status('H');
            }


            if (i % 12 == 0) {
                yearlyLedgers.get(getYearID(i)).setYearly_rent((double) Math.round(temp_yrly_rent * Math.pow(1 + rent_increase, getYearID(i)) * 100) / 100);
            }
        }

        exportSchedule();
        }


    private int getID(int start_month, int start_year, int end_month, int end_year) {
        return (end_year - start_year) * 12 + (end_month - start_month) + 1;
    }

    private int getYearID(int ID) {
        return ID / 12;
    }

    private void exportSchedule() {
        for (MonthlyLedger i : monthlyLedgers) {
            schedule += i.getMonth_id() + "," + i.getMonth() + "," + i.getYear() + "," + i.getOpening_balance() + "," + i.getPrincipal_paid() + "," + i.getInterest_paid() + "," + i.getClosing_balance() + "," + i.getRent_collected() + "\n";
        }
    }

}
