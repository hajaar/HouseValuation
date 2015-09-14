package com.houseevaluation.kartikn.housevaluation;

import java.util.ArrayList;

/**
 * Created by kartikn on 13-09-2015.
 */
public class House {

    private double principal = 0;
    private double monthly_interest = 0;
    private double months = 0;
    private double emi = 0;
    private ArrayList<Double> opening_balance = new ArrayList<Double>();
    private ArrayList<Double> interest_component = new ArrayList<Double>();
    private ArrayList<Double> principal_component = new ArrayList<Double>();
    private ArrayList<Double> yearly_rent = new ArrayList<Double>();
    private ArrayList<Double> yearly_interest_component = new ArrayList<Double>();
    private ArrayList<Double> yearly_principal_component = new ArrayList<Double>();
    private ArrayList<Double> yearly_tax_savings = new ArrayList<Double>();
    private ArrayList<Double> total_outflow = new ArrayList<Double>();
    private String schedule = "Op Bal , Interest , Principal \n";
    private String yearly_schedule = "Interest , Principal , Rent , Tax Saving, Total Outflow \n";
    private int start_month = 5;
    private double first_rent = 15000;
    private double rent_increase = 0.05;
    private int occupation_status=0;
    static final double self_occupied_max_interest = 200000;

    public House(double principal, double monthly_interest, double months, int occupation_status) {
        this.principal = principal;
        this.monthly_interest = monthly_interest;
        this.months = months;
        this.occupation_status = occupation_status;
        setEmi();
        createSchedule();
        calculateTaxAndOutflow();
    }

    public double getEmi() {
        return emi;
    }

    public void setEmi(double emi) {
        this.emi = emi;
    }

    public void setEmi() {
        emi = Math.round(principal * monthly_interest * Math.pow(1 + monthly_interest, months) / (Math.pow(1 + monthly_interest, months) - 1)*100)/100;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getYearly_schedule() {
        return yearly_schedule;
    }

    public void createSchedule() {
        double op_bal = principal;
        double temp_int = 0;
        double temp_principal = 0;
        double temp_yrly_int = 0;
        double temp_yrly_principal = 0;
        int j = start_month;
        for (int i=0; i<months; i++) {
            opening_balance.add(op_bal);
            temp_int = Math.round(op_bal*monthly_interest*100)/100;
            temp_principal =  emi - temp_int;
            interest_component.add(temp_int);
            principal_component.add(temp_principal);
            temp_yrly_int += temp_int;
            temp_yrly_principal += temp_principal;
            schedule += op_bal + " , " + temp_int + " , " + temp_principal + "\n";
            op_bal = op_bal -temp_principal;
            if (j%12 ==0 ) {
                if (occupation_status == 0) {
                    if (temp_yrly_int > self_occupied_max_interest) {
                        temp_yrly_int = self_occupied_max_interest;
                    }

                }
                yearly_interest_component.add(temp_yrly_int);
                yearly_principal_component.add(temp_yrly_principal);
                j =0;
                temp_yrly_principal = 0;
                temp_yrly_int = 0;
            }
            j += 1;
        }
    }

    public void setRent() {
        double temp_yrly_rent = 0;
        int j = start_month;
        int k = 0;
        if (occupation_status == 0) {
            first_rent = 0.0;
        }
        for (int i=0;i<months;i++) {
            temp_yrly_rent += first_rent;
            if (j%12 ==0 ) {
                yearly_rent.add((double)Math.round(temp_yrly_rent*Math.pow(1+rent_increase,k)*100)/100);
                j =0;
                temp_yrly_rent = 0;
                k += 1;
            }
            j += 1;
        }
    }

    public void calculateTaxAndOutflow() {
        int j = start_month;
        int k = 0;
        setRent();
        for (int i=0;i<months;i++) {
            if (j%12 ==0 ) {
                yearly_tax_savings.add((double) Math.round((-yearly_rent.get(k) * .7 + yearly_interest_component.get(k)) * .309 * 100) / 100);

                total_outflow.add(yearly_rent.get(k)+yearly_tax_savings.get(k)-yearly_interest_component.get(k)-yearly_principal_component.get(k));
                yearly_schedule += yearly_interest_component.get(k) + " , " + yearly_principal_component.get(k) + " , " + yearly_rent.get(k) + " , " + yearly_tax_savings.get(k) + " , " + total_outflow.get(k) + "\n";
                j =0;
                k += 1;
            }
            j += 1;
        }
    }
}
