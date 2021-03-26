package com.codesndata.aickongowea.covid_19;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by Collins on 7/24/2020 @ 17:28 PM.
 * Package Name: com.codesndata.aickongowea
 * Project Name : Service Booking
 */


public class URLs extends AsyncTask<String, Void, String> {

    static final String FETCH_DATA_URL = "http://192.168.43.58/aickongowea/fetch_data.php";
    static final String PRINT_LIST_URL = "http://192.168.43.58/aickongowea/print_attendee_list.php";
    //https://reverse-articles.000webhostapp.com/studentportal/app/filter_years.php?programme=";

    String result;

    URLs() {

    }

    @Override
    protected void onPreExecute() {
//        alertDialog.setTitle("Server Response");

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public String doInBackground(String... params) {
        String post_url = "http://192.168.43.58/aickongowea/post_details.php";

        String f_name = params[0];
        String l_name = params[1];
        String phone_no = params[2];
        String service = params[3];

        try {
            URL url = new URL(post_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            try {
                String data = URLEncoder.encode("f_name", "UTF-8") + "=" + URLEncoder.encode(f_name, "UTF-8") + "&" +
                        URLEncoder.encode("l_name", "UTF-8") + "=" + URLEncoder.encode(l_name, "UTF-8") + "&" +
                        URLEncoder.encode("phone_no", "UTF-8") + "=" + URLEncoder.encode(phone_no, "UTF-8") + "&" +
                        URLEncoder.encode("service", "UTF-8") + "=" + URLEncoder.encode(service, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (NetworkOnMainThreadException | IOException e) {
            e.getStackTrace();
        }


        return result;
    }
}

