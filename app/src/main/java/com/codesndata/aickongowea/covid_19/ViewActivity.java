package com.codesndata.aickongowea.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.codesndata.aickongowea.MainActivity;
import com.codesndata.aickongowea.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    String FinalJSonObject, ParseResult, fname, lname, service, phoneno, response, handed_service;
    ListView lv;
    PdfDocument.Page page;
    Canvas canvas;
    RadioButton checkRButton;
    TextView response_tv;
    ProgressBar progressBar;
    HashMap<String, String> ResultHash = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        handed_service = getIntent().getStringExtra("SERVICE");
        if(handed_service != null){
            switch (handed_service) {
                case "1st Service: 8:30 - 9:30 AM":
                    checkRButton = findViewById(R.id.service_1);
                    checkRButton.setChecked(true);
                    break;
                case "2nd Service: 10:30 - 11:30 AM":
                    checkRButton = findViewById(R.id.service_2);
                    checkRButton.setChecked(true);
                    break;
                case "3rd Service: 12:30 - 1:30 PM":
                    checkRButton = findViewById(R.id.service_3);
                    checkRButton.setChecked(true);
                    break;
                case "4th Service: 2:30 - 3:30 PM":
                    checkRButton = findViewById(R.id.service_4);
                    checkRButton.setChecked(true);
                    break;
            }
        }
        lv = findViewById(R.id.data_lv);
        progressBar = findViewById(R.id.progressBar);
        response_tv = findViewById(R.id.response_tv);
        radioGroup = findViewById(R.id.radiogrup);

        if(handed_service != null) {
            checkYourself();
        }
        checkWriteExternalStoragePermission();
    }

    //Method to show current record Current Selected Record
    public void HttpWebCall(final String service) {

        @SuppressLint("StaticFieldLeak")
        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog2 = ProgressDialog.show(ShowSingleUnitActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressBar.setVisibility(View.GONE);
                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;
                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ViewActivity.this).execute();
            }

            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("service", params[0]);
                ParseResult = httpParse.postRequest(ResultHash, URLs.FETCH_DATA_URL);
                return ParseResult;
            }
        }
        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();
        httpWebCallFunction.execute(service);
    }

    // Parsing Complete JSON Object.
    @SuppressLint("StaticFieldLeak")
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        Context context;
        List<Attendee> attendees;

        GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if (FinalJSonObject != null) {

                    JSONArray jsonArray;
                    try {
                        Attendee attendee;
                        attendees = new ArrayList<>();
                            response = FinalJSonObject;

                        jsonArray = new JSONArray(FinalJSonObject);
                        JSONObject jsonObject;
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        int capacity = 20;
                            attendee = new Attendee();
                            jsonObject = jsonArray.getJSONObject(i);
                            //pulling data.
                            if(jsonArray.length() > capacity && i == capacity) {
                                attendee.pos = "*";
                                attendee.FName = "CHURCH";
                                attendee.LName = "FULL";
                                attendee.Service = "Please, Choose Another Service";
                                attendee.TBooked = "XXXXXXXXXX";
                                attendees.add(attendee);
                            } else {
                                if(i < capacity) {
                                    attendee.pos = i+1 +".";
                                    attendee.FName = jsonObject.getString("fname");
                                    attendee.LName = jsonObject.getString("lname");
                                    attendee.Service = jsonObject.getString("service");
                                    attendee.TBooked = jsonObject.getString("phoneno");
                                    attendees.add(attendee);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            response_tv.setText("");
                if (attendees != null) {
                    ViewAdapter adapter = new ViewAdapter(attendees, context);
                    lv.setAdapter(adapter);
            } else {
                    progressBar.setVisibility(View.GONE);
                    response_tv.setText(R.string.no_result);
            }
        }
    }
    public void fetchData(View view) {
        response_tv.setText("");
        progressBar.setVisibility(View.VISIBLE);

        int buttonId = radioGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {
            RadioButton checkedRButton = findViewById(buttonId);
            service = checkedRButton.getText().toString();

            new GetHttpResponse(ViewActivity.this).execute();
            HttpWebCall(service);

        }
        else {
            response_tv.setText(R.string.service_choice);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void checkYourself() {
            new GetHttpResponse(ViewActivity.this).execute();
            HttpWebCall(handed_service);
    }

    public void printData(View view) {
        response_tv.setText("");
        progressBar.setVisibility(View.VISIBLE);

        int buttonId = radioGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {
            RadioButton checkedRButton = findViewById(buttonId);
            service = checkedRButton.getText().toString();

            new HttpResponse(ViewActivity.this).execute();
            WebCall(service);
        } else {
            response_tv.setText(R.string.service_choice);
        }
    }

    // Parsing Complete JSON Object.
    private class HttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        HttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            response_tv.setText(R.string.gen_pdf);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //////////////////////////////////////////
            File root = new File(Environment.getExternalStorageDirectory(), "AIC Kongowea");

            if (!root.exists()) {
                root.mkdir();
            }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                Date current = new Date();
                //long record_time = new Time(System.nanoTime()).getTime();
                String record_time = simpleDateFormat.format(current);
                String textFileName = "List_of_Attendees-";
                String fileName = textFileName + record_time;
            //create a new document
            PdfDocument document = new PdfDocument();

            //Create a page description
            //PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(598, 842, 1).create();
            //A4 page size 598px by 842px

            ////////////////////////////
            try {
                if (FinalJSonObject != null) {
                    JSONArray jsonArray;
                    try {

                        jsonArray = new JSONArray(FinalJSonObject);
                        JSONObject jsonObject;

                        for (int i = 0; i <= 40; i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            int y;

                            // Units from db copied to variable holders
                            String fnameHolder = jsonObject.getString("fname");
                            String lnameHolder = jsonObject.getString("lname");
                            String phoneHolder = jsonObject.getString("phoneno");
                            //serialHolder = jsonObject.getString("receipt_serial");

                            if(i < 10) {
                                y = i;
                                    Rect r = new Rect();
                                    if (i == 0) {
                                        //Start a page
                                        page = document.startPage(pageInfo);
                                    }
                                    canvas = page.getCanvas();
                                    Paint paint = new Paint();
                                    //Load a bitmap to draw on page
                                    Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.aic_header);
                                    Bitmap logo = Bitmap.createScaledBitmap(src, 530, 120, false);
                                    String aic = "AIC KONGOWEA";
                                    String list = "List of Church Attendees";
                                    String time = "Date: .../.../2020  " + service;
                                    String copyright = "© 2020 codesndata.com";
                                    paint.setTextAlign(Paint.Align.LEFT);
                                    paint.setColor(Color.BLACK);
                                    canvas.getClipBounds(r);
                                    //int cHeight = r.height();
                                    int cWidth = r.width();
                                    paint.getTextBounds(aic, 0, aic.length(), r);
                                    float x = cWidth / 2f - r.width() / 2f - r.left;
                                    paint.getTextBounds(list, 0, list.length(), r);
                                    float x1 = cWidth / 2f - r.width() / 2f - r.left;
                                    paint.getTextBounds(time, 0, time.length(), r);
                                    float xx = cWidth / 2f - r.width() / 2f - r.left;
                                    String sx = "OFFICIAL MANIFEST";
                                    paint.getTextBounds(sx, 0, sx.length(), r);
                                    //Add the image to the page
                                    Paint paintHeader = new Paint();
                                    paintHeader.setTextSize(30);
                                    //paintHeader.setColor(getResources().getColor());
                                    Paint paintSubHeader = new Paint();
                                paintSubHeader.setTextSize(25);
                                    canvas.drawBitmap(logo, 20, 30, paint);
                                    logo.recycle();
                                    //Typeface header = Typeface.
                                    canvas.drawText(aic, 25, 170, paintHeader);
                                    canvas.drawText(list, 25, 205, paintSubHeader);
                                    canvas.drawText(time, 25, 240, paintSubHeader);

                                    canvas.drawLine(20, 260, 575, 260, paint);

                                    int k = 20;
                                    int l = 280;

                                    canvas.drawText((i + 1) + ".", 60, l + (k * y), paint);
                                    canvas.drawText(fnameHolder, 90, l + (k * y), paint);
                                    canvas.drawText(lnameHolder, 190, l + (k * y), paint);
                                    canvas.drawText("0" + phoneHolder, 290, l + (k * y), paint);

                                    canvas.drawText("Ukurasa wa 1", 200, l + (k * 25), paint);
                                    canvas.drawText(copyright, 200, l + (k * 25 + 30), paint);

                                    canvas.drawLine(20, 260, 20, l + (k * 25 + 10), paint);
                                    canvas.drawLine(575, 260, 575, l + (k * 25 + 10), paint);
                                    canvas.drawLine(20, l + (k * 25 + 10), 575, l + (k * 25 + 10), paint);
                                    if (i == 9) {
                                        //finish page
                                        document.finishPage(page);
                                    }
                                }

                            if(i > 9 && i < 20) {
                                y = (i - 10);
                                    Rect r = new Rect();
                                    if (i == 10) {
                                        //Start a page
                                        page = document.startPage(pageInfo);
                                    }
                                    canvas = page.getCanvas();
                                    Paint paint = new Paint();
                                    String aic = "AIC KONGOWEA";
                                    String list = "List of Church Attendees";
                                    String time = "Date: .../.../2020  " + service;
                                    String copyright = "© 2020 codesndata.com";
                                    Paint paintHeader = new Paint();
                                    paintHeader.setTextSize(30);
                                    Paint paintSubHeader = new Paint();
                                    paintSubHeader.setTextSize(25);
                                    //Typeface header = Typeface.
                                    canvas.drawText(aic, 25, 60, paintHeader);
                                    canvas.drawText(list, 25, 95, paintSubHeader);
                                    canvas.drawText(time, 25, 130, paintSubHeader);

                                    canvas.drawLine(20, 150, 575, 150, paint);

                                    int k = 20;
                                    int l = 170;

                                    canvas.drawText((i + 1) + ".", 60, l + (k * y), paint);
                                    canvas.drawText(fnameHolder, 90, l + (k * y), paint);
                                    canvas.drawText(lnameHolder, 190, l + (k * y), paint);
                                    canvas.drawText("0" + phoneHolder, 290, l + (k * y), paint);

                                    canvas.drawText("Ukurasa wa 2", 200, l + (k * 25), paint);
                                    canvas.drawText(copyright, 200, l + (k * 25 + 30), paint);

                                    canvas.drawLine(20, 150, 20, l + (k * 25 + 10), paint);
                                    canvas.drawLine(575, 150, 575, l + (k * 25 + 10), paint);
                                    canvas.drawLine(20, l + (k * 25 + 10), 575, l + (k * 25 + 10), paint);
                                    if (i == 19) {
                                        //finish page
                                        document.finishPage(page);
                                    }
                                }

                            if(i > 19 && i < 30) {
                                y = (i - 20);
                                    Rect r = new Rect();
                                    if (i == 20) {
                                        //Start a page
                                        page = document.startPage(pageInfo);
                                    }
                                    canvas = page.getCanvas();
                                    Paint paint = new Paint();
                                    String aic = "AIC KONGOWEA";
                                    String list = "List of Church Attendees";
                                    String time = "Date: .../.../2020  " + service;
                                    String copyright = "© 2020 codesndata.com";
                                    Paint paintHeader = new Paint();
                                    paintHeader.setTextSize(30);
                                    //paintHeader.setColor(getResources().getColor());
                                    Paint paintSubHeader = new Paint();
                                    paintSubHeader.setTextSize(25);
                                    //Typeface header = Typeface.
                                canvas.drawText(aic, 25, 60, paintHeader);
                                canvas.drawText(list, 25, 95, paintSubHeader);
                                canvas.drawText(time, 25, 130, paintSubHeader);

                                canvas.drawLine(20, 150, 575, 150, paint);

                                int k = 20;
                                int l = 170;

                                canvas.drawText((i + 1) + ".", 60, l + (k * y), paint);
                                canvas.drawText(fnameHolder, 90, l + (k * y), paint);
                                canvas.drawText(lnameHolder, 190, l + (k * y), paint);
                                canvas.drawText("0" + phoneHolder, 290, l + (k * y), paint);

                                canvas.drawText("Ukurasa wa 3", 200, l + (k * 25), paint);
                                canvas.drawText(copyright, 200, l + (k * 25 + 30), paint);

                                canvas.drawLine(20, 150, 20, l + (k * 25 + 10), paint);
                                canvas.drawLine(575, 150, 575, l + (k * 25 + 10), paint);
                                canvas.drawLine(20, l + (k * 25 + 10), 575, l + (k * 25 + 10), paint);
                                    if (i == 29) {
                                        //finish page
                                        document.finishPage(page);
                                    }
                                }

                        if(i > 29 && i < 40) {
                            y = (i - 30);
                                Rect r = new Rect();

                                if (i == 30) {
                                    //Start a page
                                    page = document.startPage(pageInfo);
                                }
                                canvas = page.getCanvas();
                                Paint paint = new Paint();
                                String aic = "AIC KONGOWEA";
                                String list = "List of Church Attendees";
                                String time = "Date: .../.../2020  " + service;
                                String copyright = "© 2020 codesndata.com";
                                Paint paintHeader = new Paint();
                                paintHeader.setTextSize(30);
                                //paintHeader.setColor(getResources().getColor());
                                Paint paintSubHeader = new Paint();
                            paintSubHeader.setTextSize(25);
                                //Typeface header = Typeface.
                            canvas.drawText(aic, 25, 60, paintHeader);
                            canvas.drawText(list, 25, 95, paintSubHeader);
                            canvas.drawText(time, 25, 130, paintSubHeader);

                            canvas.drawLine(20, 150, 575, 150, paint);

                            int k = 20;
                            int l = 170;

                            canvas.drawText((i + 1) + ".", 60, l + (k * y), paint);
                            canvas.drawText(fnameHolder, 90, l + (k * y), paint);
                            canvas.drawText(lnameHolder, 190, l + (k * y), paint);
                            canvas.drawText("0" + phoneHolder, 290, l + (k * y), paint);

                            canvas.drawText("Ukurasa wa 4", 200, l + (k * 25), paint);
                            canvas.drawText(copyright, 200, l + (k * 25 + 30), paint);

                            canvas.drawLine(20, 150, 20, l + (k * 25 + 10), paint);
                            canvas.drawLine(575, 150, 575, l + (k * 25 + 10), paint);
                            canvas.drawLine(20, l + (k * 25 + 10), 575, l + (k * 25 + 10), paint);
                                if (i == 39) {
                                    //finish page
                                    document.finishPage(page);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //draws the text on the graphics object of the page
            //finish page
            document.finishPage(page);
            //write the document content
            File filepath = new File(root, fileName + ".pdf");
            try {
                document.writeTo(new FileOutputStream(filepath));
            } catch (IOException e) {
                e.fillInStackTrace();
            }

            //close the document
            document.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            response_tv.setText(R.string.generated_pdf);
        }
    }

    //Method to show current record Current Selected Record
    public void WebCall(final String service) {

        @SuppressLint("StaticFieldLeak")
        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog2 = ProgressDialog.show(ShowSingleUnitActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressBar.setVisibility(View.GONE);
                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;
                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new HttpResponse(ViewActivity.this).execute();
            }

            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("service", params[0]);
                ParseResult = httpParse.postRequest(ResultHash, URLs.PRINT_LIST_URL);
                return ParseResult;
            }
        }
        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();
        httpWebCallFunction.execute(service);
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 55;
    public void checkWriteExternalStoragePermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
               // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted. Do the
            // writing-related task you need to do.
            ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //TODO: code to execute with permission
        } else {

            // Permission denied, Disable the functionality that depends on this permission.
            Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission denied", Toast.LENGTH_LONG).show();
        }
            }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}