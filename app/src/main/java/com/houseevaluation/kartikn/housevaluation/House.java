package com.houseevaluation.kartikn.housevaluation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

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
    private String schedule = "ID, Month, Cal.Year, Fin. Year, Op Bal , Principal Paid, Interest Paid, Closing Balance, Rent, Tax Status \n";
    private String yearly_schedule = "ID, Fin. Year,  Principal , Interest ,Tax Status, Rent , Tax Saving, Total Outflow, Notation \n";
    private int loan_start_month;
    private int loan_start_year;
    private int handover_month;
    private int handover_year;
    private int rent_start_month;
    private int rent_start_year;
    private int financial_month;
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

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public void setMonthly_interest(double monthly_interest) {
        this.monthly_interest = monthly_interest;
    }

    public void setMonths(double months) {
        this.months = months;
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
        if (current_month <= 2) {
            financial_month = 10 + current_month;
        } else {
            financial_month = current_month - 2;
        }

        for (int i = 0; i < months; i++) {
            temp_int = Math.round(op_bal * monthly_interest * 100) / 100;
            temp_yrly_int += temp_int;
            temp_principal = emi - temp_int;
            temp_yrly_principal += temp_principal;
            MonthlyLedger monthlyLedger = new MonthlyLedger(i, (i + current_month) % 12, current_year, op_bal, temp_principal, temp_int);
            monthlyLedgers.add(monthlyLedger);
            op_bal = op_bal - temp_principal;
            if ((i + financial_month) % 12 == 0) {
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
        double current_rent = first_rent;
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
                    if (self_occupied) {
                        monthlyLedgers.get(i).setTax_status('S');
                        temp_yrly_status = 'S';
                    } else {
                        monthlyLedgers.get(i).setTax_status('R');
                        temp_yrly_status = 'R';
                        if (i < (delta_between_handover_and_rent + delta_between_loan_and_handover)) {
                            temp_mthly_rent = 0;
                        } else {
                            temp_mthly_rent = current_rent;
                        }

                    }
                }
            monthlyLedgers.get(i).setRent_collected(Math.round(temp_mthly_rent));
            temp_yrly_rent += temp_mthly_rent;
            if ((i + financial_month) % 12 == 0) {
                yearlyLedgers.get(getYearID(i)).setYearly_rent(temp_yrly_rent);
                yearlyLedgers.get(getYearID(i)).setTax_status(temp_yrly_status);
                temp_yrly_rent = 0;

                if (temp_yrly_status == 'R') {
                    current_rent = current_rent * (1 + rent_increase);
                }
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
        String notation = "";

        for (YearlyLedger i : yearlyLedgers) {
            tmp_tax_status = i.getTax_status();
            if (tmp_tax_status == 'C') {
                tmp_tax_saving = 0;
                construction_interest += i.getYearly_interest();
                Log.d("calculateTax ", "construction interest " + construction_interest);
                notation = "Tax exemption on interest payments is not allowed until handover. One-fifth of the total interest can be claimed annually for the first five years after handover";
            }
            if (tmp_tax_status == 'S') {
                tmp_tax_saving = i.getYearly_interest();
                notation = "Tax exemption on interest payments is capped to Rs. " + self_occupied_max_interest;
                if (counter < 5) {
                    tmp_tax_saving += construction_interest / 5 * max_tax_rate;
                    counter++;
                    notation = "One-fifth of pre-handover interest can be claimed in this year. Tax exemption on interest payments is capped to Rs. " + self_occupied_max_interest;
                }
                tmp_tax_saving = Math.min(tmp_tax_saving, self_occupied_max_interest);
            }
            if (i.getTax_status() == 'R') {
                tmp_tax_saving = (i.getYearly_interest() - i.getYearly_rent() * rent_claimable_percentage) * max_tax_rate;
                notation = "Tax exemption is applicable for the entire interest component this year.";
                Log.d("calculateTax", "tax " + tmp_tax_saving);
                if (counter < 5) {
                    tmp_tax_saving += construction_interest / 5 * max_tax_rate;
                    notation = "One-fifth of pre-handover interest can be claimed in this year. Tax exemption is applicable for the entire interest component this year.";
                    Log.d("calculateTax", " tax with int " + tmp_tax_saving + "counter " + counter);
                    counter++;
                }
            }
            i.setTax_saving(Math.round(tmp_tax_saving));
            i.setTotal_outflow();
            i.setNotation(notation);
        }
    }


    private int getID(int start_month, int start_year, int end_month, int end_year) {
        return (end_year - start_year) * 12 + (end_month - start_month);
    }

    private int getYearID(int ID) {
        return ID / 12;
    }

    private String getMonthName(int month) {
        String Months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return Months[month];
    }

    private void exportSchedule() {
        Calendar cal = Calendar.getInstance();
        for (MonthlyLedger i : monthlyLedgers) {
            schedule += (i.getMonth_id() + 1) + "," +
                    getMonthName(i.getMonth()) + "," +
                    i.getCalendar_year() + "," +
                    i.getFinancial_year() + "," +
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
                    i.getFinancial_year() + "," +
                    i.getYearly_principal() + "," +
                    i.getYearly_interest() + "," +
                    i.getTax_status() + "," +
                    i.getYearly_rent() + "," +
                    i.getTax_saving() + "," +
                    i.getTotal_outflow() + "," +
                    i.getNotation() + "\n";
        }
    }

}
