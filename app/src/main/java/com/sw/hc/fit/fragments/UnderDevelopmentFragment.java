package com.sw.hc.fit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sw.hc.fit.App;
import com.sw.hc.fit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Caio Bulgarelli
 */
public class UnderDevelopmentFragment extends Fragment {


    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;

    public UnderDevelopmentFragment() {
        // Required empty public constructor
    }

    public static UnderDevelopmentFragment newInstance() {
        return new UnderDevelopmentFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_under_development, container, false);

        ButterKnife.bind(this, view);


        String version = App.getAppVersion();


        mTvAppVersion.setText("Version: " + version);


        return view;
    }

}
