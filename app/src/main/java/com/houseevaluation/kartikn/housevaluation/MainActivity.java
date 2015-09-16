package com.houseevaluation.kartikn.housevaluation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_ID1 = 0;
    static final int DIALOG_ID2 = 1;
    static final int DIALOG_ID3 = 2;
    private House house;
    private int year_x, month_x, day_x, loan_month, loan_year, handover_month, handover_year, rent_month, rent_year;
    private DatePickerDialog.OnDateSetListener dpickerListener1
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            loan_year = year;
            month_x = monthOfYear;
            loan_month = monthOfYear;
            day_x = dayOfMonth;
            Button btn1 = (Button) findViewById(R.id.loan_button);
            btn1.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListener2
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            handover_year = year;
            month_x = monthOfYear;
            handover_month = monthOfYear;
            day_x = dayOfMonth;
            Button btn2 = (Button) findViewById(R.id.handover_button);
            btn2.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListener3
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            rent_year = year;
            month_x = monthOfYear;
            rent_month = monthOfYear;
            day_x = dayOfMonth;
            Button btn3 = (Button) findViewById(R.id.rent_button);
            btn3.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
    }

    public void showDialogOnButtonClick() {
        Button btn1 = (Button) findViewById(R.id.loan_button);
        Button btn2 = (Button) findViewById(R.id.handover_button);
        Button btn3 = (Button) findViewById(R.id.rent_button);
        btn1.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        btn2.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        btn3.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        loan_year = year_x;
        handover_year = year_x;
        rent_year = year_x;
        loan_month = month_x;
        handover_month = month_x;
        rent_month = month_x;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID3);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID1:
                return new DatePickerDialog(this, dpickerListener1, year_x, month_x, day_x);
            case DIALOG_ID2:
                return new DatePickerDialog(this, dpickerListener2, year_x, month_x, day_x);
            case DIALOG_ID3:
                return new DatePickerDialog(this, dpickerListener3, year_x, month_x, day_x);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showEMI(View v) {
        house = new House(
                Double.valueOf(((TextView) findViewById(R.id.principal)).getText().toString()),
                Double.valueOf(((TextView) findViewById(R.id.interest)).getText().toString()) / 1200,
                Double.valueOf(((TextView) findViewById(R.id.years)).getText().toString()) * 12
        );
        ((TextView) findViewById(R.id.emi)).setText("Your EMI is " + house.getEmi());
    }

    public void exportSchedule(View v) {
        String propertyName = ((TextView) findViewById(R.id.property_name)).getText().toString();
        boolean self_occupied = ((ToggleButton) findViewById(R.id.self_occupied)).isChecked();
        house.setSelf_occupied(self_occupied);
        house.setLoan_start_month(loan_month);
        house.setLoan_start_year(loan_year);
        Log.d("exportSchedule", "Loan Year " + loan_year);
        house.setHandover_month(handover_month);
        house.setHandover_year(handover_year);
        if (house.isSelf_occupied()) {
            house.setRent_start_month(handover_month);
            house.setRent_start_year(handover_year);
        } else {
            house.setRent_start_month(rent_month);
            house.setRent_start_year(rent_year);
            house.setFirst_rent(Double.valueOf(((EditText) findViewById(R.id.first_rent)).getText().toString()));
            house.setRent_increase(Double.valueOf(((EditText) findViewById(R.id.rent_increase)).getText().toString()) / 100);
        }
        house.createSchedule();
        house.setRent();
        exportFile("monthly_schedule_" + propertyName + ".txt", house.getSchedule());
        exportFile("yearly_schedule_" + propertyName + ".csv", house.getYearly_schedule());
        Toast.makeText(getApplicationContext(), "The schedules have been exported to your downloads folder", Toast.LENGTH_LONG).show();
    }


    public void exportFile(String exportFile, String exportString) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "HouseValuation");

            String filename = file.toString() + "/" + exportFile;

            FileWriter fw = new FileWriter(filename);
            fw.write(exportString);
            fw.flush();
            fw.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        String Months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return Months[month];
    }
}


