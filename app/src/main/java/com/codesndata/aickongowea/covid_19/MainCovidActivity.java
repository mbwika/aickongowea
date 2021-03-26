package com.codesndata.aickongowea.covid_19;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codesndata.aickongowea.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainCovidActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    EditText fname, lname, phoneno;
    String service;
    int buttonId;
    RadioButton checkedRButton;
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincovid);

        radioGroup = findViewById(R.id.radiogrup);
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        phoneno = findViewById(R.id.phoneno);
        response = findViewById(R.id.response_tv);

        FloatingActionButton fab = findViewById(R.id.fab_0);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainCovidActivity.this, ViewActivity.class));
                finish();
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

    public void submitDetails(View view){

        // Reset errors.
        fname.setError(null);
        lname.setError(null);
        phoneno.setError(null);

        buttonId = radioGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {

            checkedRButton = findViewById(buttonId);
            service = checkedRButton.getText().toString();
            String f_name = fname.getText().toString();
            String l_name = lname.getText().toString();
            String phone_no = phoneno.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid phone no, if the user entered one.
            if (TextUtils.isEmpty(phone_no)){
                phoneno.setError(getString(R.string.fill_all_details));
                focusView = phoneno;
                cancel = true;
            } else if (!phoneNoValid(phone_no)) {
                phoneno.setError(getString(R.string.valid_phone_no));
                focusView = phoneno;
                cancel = true;
            }
            if (TextUtils.isEmpty(l_name)) {
                lname.setError(getString(R.string.fill_all_details));
                focusView = lname;
                cancel = true;
            }
            if (TextUtils.isEmpty(f_name)) {
                fname.setError(getString(R.string.fill_all_details));
                focusView = fname;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt to register. Focus on the field in error
                focusView.requestFocus();
            } else {
                //sending data to the handover class to the serve
                        URLs sync = new URLs();
                        sync.execute(f_name, l_name, phone_no, service);

                //Call intent to open ViewActivity
                Intent viewActivity = new Intent(this, ViewActivity.class);
                // Sending checked radioButton value using intent.
                viewActivity.putExtra("SERVICE", service);
                viewActivity.putExtra("ID", buttonId);
                //start the activity
                startActivity(viewActivity);

                        lname.getText().clear();
                        fname.getText().clear();
                        phoneno.getText().clear();
                        radioGroup.clearCheck();

                    }
        }

            else {
                response.setText(R.string.service_choice);
            }

    }

    private boolean phoneNoValid(String phoneNo) {
        return phoneNo.length() == 10 || phoneNo.length() == 12;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

