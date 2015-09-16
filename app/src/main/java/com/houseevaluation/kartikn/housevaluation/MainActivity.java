package com.houseevaluation.kartikn.housevaluation;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private House house;
    private int sid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spinner = (Spinner) findViewById(R.id.occupation_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.occupationstatus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sid = spinner.getSelectedItemPosition();

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

    public void selectDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");
    }

    public void showEMI(View v) {
        house = new House(
                Double.valueOf(((TextView) findViewById(R.id.principal)).getText().toString()),
                Double.valueOf(((TextView) findViewById(R.id.interest)).getText().toString()) / 1200,
                Double.valueOf(((TextView) findViewById(R.id.years)).getText().toString()) * 12,
                sid
        );
        ((TextView) findViewById(R.id.emi)).setText("Your EMI is " + house.getEmi());
        findViewById(R.id.export_schedule).setVisibility(View.VISIBLE);
    }

    public void exportSchedule(View v) {
        String propertyName = ((TextView) findViewById(R.id.property_name)).getText().toString();
        exportFile(propertyName + "schedule.csv",house.getSchedule());
        exportFile(propertyName + "tax_savings.csv", house.getYearly_schedule());
        ((TextView) findViewById(R.id.export_message)).setText("The schedules have been exported to your downloads folder");
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


