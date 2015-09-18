package com.houseevaluation.kartikn.housevaluation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_ID1 = 0;
    static final int DIALOG_ID2 = 1;
    static final int DIALOG_ID3 = 2;
    private House house;
    private String monthlyschedule = "";
    private String yearlyschedule = "";
    private ShareActionProvider mshareActionProvider;
    private ArrayList<Uri> files = new ArrayList<Uri>();

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
            //  if (validateDates) {
            btn1.setText(day_x + "-" + getMonthName(month_x) + "-" + year_x);
        }
        // else
        //}
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
        house = new House(0, 0, 0);
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
        EditText textPrincipal = (EditText) findViewById(R.id.principal);
        textPrincipal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateEMI();
            }
        });
        EditText textYears = (EditText) findViewById(R.id.years);
        textYears.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateEMI();
            }
        });
        EditText textInterest = (EditText) findViewById(R.id.interest);
        textInterest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateEMI();
            }
        });
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.self_occupied);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean i;
                if (isChecked) {
                    i = false;
                } else {
                    i = true;
                }
                ((TextView) findViewById(R.id.occupation_status_label)).setEnabled(i);
                ((Button) findViewById(R.id.rent_button)).setEnabled(i);
                ((TextView) findViewById(R.id.rent_label)).setEnabled(i);
                ((TextView) findViewById(R.id.percentage_label)).setEnabled(i);
                ((TextView) findViewById(R.id.rate_label)).setEnabled(i);
                ((EditText) findViewById(R.id.first_rent)).setEnabled(i);
                ((EditText) findViewById(R.id.rent_increase)).setEnabled(i);
            }
        });
    }

    public void calculateEMI() {
        double emi = 0;
        EditText textPrincipal = (EditText) findViewById(R.id.principal);
        EditText textYears = (EditText) findViewById(R.id.years);
        EditText textInterest = (EditText) findViewById(R.id.interest);
        if (!isEmpty(textPrincipal) && !isEmpty(textInterest) && !isEmpty(textYears)) {
            house.setPrincipal(Double.valueOf(((TextView) findViewById(R.id.principal)).getText().toString()));
            house.setMonthly_interest(Double.valueOf(((TextView) findViewById(R.id.interest)).getText().toString()) / 1200);
            house.setMonths(Double.valueOf(((TextView) findViewById(R.id.years)).getText().toString()) * 12);
            house.setEmi();
            emi = house.getEmi();
        }
        ((TextView) findViewById(R.id.emi)).setText("Your EMI is " + emi);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
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
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mshareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        return true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "House Valuation Files");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "PFA yearly and monthly schedules");
        shareIntent.putExtra(Intent.EXTRA_STREAM, files);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


    public void exportSchedule(View v) {
        String propertyName = ((TextView) findViewById(R.id.property_name)).getText().toString();
        boolean self_occupied = ((ToggleButton) findViewById(R.id.self_occupied)).isChecked();
        if (isDateSmaller(loan_month, loan_year, handover_month, handover_year)) {
            if (self_occupied || isDateSmaller(handover_month, handover_year, rent_month, rent_year)) {
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
                Calendar cal = Calendar.getInstance();
                DateFormat date = new SimpleDateFormat("MMddHHmmss");
                monthlyschedule = house.getSchedule();
                yearlyschedule = house.getYearly_schedule();
                exportFile(propertyName + "_monthly_schedule_" + date.format(cal.getTime()) + ".csv", monthlyschedule);
                exportFile(propertyName + "_yearly_schedule_" + date.format(cal.getTime()) + ".csv", yearlyschedule);
                if (mshareActionProvider != null) {
                    mshareActionProvider.setShareIntent(createShareForecastIntent());
                }
                ((TextView) findViewById(R.id.value_80c)).setText(house.getAnalysis_80c());
                ((TextView) findViewById(R.id.value_24b)).setText(house.getAnalysis_24b());
                ((TextView) findViewById(R.id.value_principal)).setText(house.getAnalysis_principal());
            } else {
                Toast.makeText(getApplicationContext(), "Handover Date should be earlier than Rent Date", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Loan Date should be earlier than Handover Date", Toast.LENGTH_LONG).show();
        }
    }


    public void exportFile(String exportFile, String exportString) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "HouseValuation");
            file.mkdirs();
            String filename = file.toString() + "/" + exportFile;

            FileWriter fw = new FileWriter(filename);
            fw.write(exportString);
            fw.flush();
            fw.close();
            MediaScannerConnection.scanFile(this, new String[]{filename}, null, null);
            files.add(Uri.parse(filename));


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        String Months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return Months[month];
    }

    private boolean isDateSmaller(int start_date, int start_year, int end_date, int end_year) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
            String str1 = "" + start_date + "/" + start_year;
            Date date1 = formatter.parse(str1);
            String str2 = "" + end_date + "/" + end_year;
            Date date2 = formatter.parse(str2);
            if (date1.compareTo(date2) <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}


