package com.example.trackcovid;

public class TrackCovidData {
    String mState;
    String mConfirmed;
    String mActive;
    String mRecovered;
    String mDeaths;
    TrackCovidData(String state, String confirmed, String active, String recovered, String deaths){
        mState = state;
        mConfirmed = confirmed;
        mActive = active;
        mRecovered = recovered;
        mDeaths = deaths;
    }
}
