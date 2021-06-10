package com.example.trackcovid;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList mCovidData = new ArrayList<TrackCovidData>();
    TrackCovidAdapter mAdapter = new TrackCovidAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        fetchResults();
        rvItems.setAdapter(mAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setTotal(JSONObject jsonObject) throws JSONException, ParseException {
        TextView tvConfirmed = findViewById(R.id.tvConfirmed);
        TextView tvActive = findViewById(R.id.tvActive);
        TextView tvRecovered = findViewById(R.id.tvRecovered);
        TextView tvDeaths = findViewById(R.id.tvDeaths);

        tvConfirmed.setText(jsonObject.getString("confirmed"));
        tvActive.setText(jsonObject.getString("active"));
        tvRecovered.setText(jsonObject.getString("recovered"));
        tvDeaths.setText(jsonObject.getString("deaths"));

        updatedTime(jsonObject.getString("lastupdatedtime"));
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    void updatedTime(String dateTime) throws ParseException {
        Date dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTime);
        String timeFormat = new SimpleDateFormat("H:mm").format(dateTimeFormat);
        String dayFormat = new SimpleDateFormat("dd").format(dateTimeFormat);

        String[] time = timeFormat.split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);

        int currDay = LocalDateTime.now().getDayOfMonth();
        int currHour = LocalDateTime.now().getHour();
        int currMin = LocalDateTime.now().getMinute();

        int day = Integer.parseInt(dayFormat);

        TextView tvLastUpdated = findViewById(R.id.tvLastUpdated);
        if (currDay == day) {
            if ((currHour - hour) == 0) {
                int updateMin = (currMin - min);
                if (updateMin > 1) {
                    tvLastUpdated.setText(updateMin + " MINUTES AGO");
                } else if (updateMin <= 1) {
                    tvLastUpdated.setText(updateMin + " MINUTE AGO");
                }
            } else {
                int updateHour = currHour - hour;
                if (updateHour == 1) {
                    tvLastUpdated.setText(updateHour + " HOUR AGO");
                }
                if (updateHour > 1) {
                    tvLastUpdated.setText(updateHour + " HOURS AGO");
                }
            }
        }
        else if(currDay > day) {
            int updateDate = currDay - day;
            if(updateDate>1)
                tvLastUpdated.setText(updateDate + " DAYS AGO");
            else if(updateDate==1)
                tvLastUpdated.setText(updateDate + " DAY AGO");
        }
    }
        void fetchResults () {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.covid19india.org/data.json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                try {
                                    String data = response.body().string();
                                    JSONObject jsonObject = new JSONObject(data);
                                    JSONArray jsonArray = jsonObject.getJSONArray("statewise");
                                    setTotal(jsonArray.getJSONObject(0));
                                    for (int i = 1; i < jsonArray.length(); i++) {
                                        JSONObject stateJsonData = jsonArray.getJSONObject(i);
                                        String state = stateJsonData.getString("state");
                                        String confirmed = stateJsonData.getString("confirmed");
                                        String active = stateJsonData.getString("active");
                                        String recovered = stateJsonData.getString("recovered");
                                        String deaths = stateJsonData.getString("deaths");
                                        mCovidData.add(new TrackCovidData(state, confirmed, active, recovered, deaths));
                                    }
                                    mAdapter.UpdateData(mCovidData);
                                } catch (IOException | JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }