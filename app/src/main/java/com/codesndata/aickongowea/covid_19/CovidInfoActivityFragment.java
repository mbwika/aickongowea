package com.codesndata.aickongowea.covid_19;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codesndata.aickongowea.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CovidInfoActivityFragment extends Fragment {

    public CovidInfoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_covid_info, container, false);
    }
}
