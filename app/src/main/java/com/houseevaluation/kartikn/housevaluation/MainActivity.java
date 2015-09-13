package com.houseevaluation.kartikn.housevaluation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private House house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void generateSchedule(View v) {
        ((TextView) findViewById(R.id.emi)).setText(house.getYearly_schedule());
    }

}
