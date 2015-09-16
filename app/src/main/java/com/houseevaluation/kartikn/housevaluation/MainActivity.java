package com.houseevaluation.kartikn.housevaluation;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private House house;

    private int loan_month, handover_month, rent_month = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner loan_spinner = (Spinner) findViewById(R.id.loan_month);
        final Spinner handover_spinner = (Spinner) findViewById(R.id.handover_month);
        final Spinner rent_spinner = (Spinner) findViewById(R.id.rent_month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loan_spinner.setAdapter(adapter);
        handover_spinner.setAdapter(adapter);
        rent_spinner.setAdapter(adapter);
        loan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loan_month = loan_spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        handover_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handover_month = handover_spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        rent_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rent_month = rent_spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        house.setLoan_start_year(Integer.valueOf(((EditText) findViewById(R.id.loan_year)).getText().toString()));
        house.setHandover_month(handover_month);
        house.setHandover_year(Integer.valueOf(((EditText) findViewById(R.id.handover_year)).getText().toString()));
        if (house.isSelf_occupied()) {
            house.setRent_start_month(handover_month);
            house.setRent_start_year(Integer.valueOf(((EditText) findViewById(R.id.handover_year)).getText().toString()));
        } else {
            house.setRent_start_month(rent_month);
            house.setRent_start_year(Integer.valueOf(((EditText) findViewById(R.id.rent_year)).getText().toString()));
            house.setFirst_rent(Double.valueOf(((EditText) findViewById(R.id.first_rent)).getText().toString()));
            house.setRent_increase(Double.valueOf(((EditText) findViewById(R.id.rent_increase)).getText().toString()) / 100);
        }
        house.createSchedule();
        house.setRent();
        exportFile(propertyName + "schedule.csv", house.getSchedule());
        exportFile(propertyName + "tax_savings.csv", house.getYearly_schedule());
        Toast.makeText(getApplicationContext(), "The schedules have been exported to your downloads folder", Toast.LENGTH_LONG);
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
}


