package com.sw.hc.fit.util;

import android.support.v4.app.Fragment;

import com.sw.hc.fit.activities.MainActivity;
import com.sw.hc.fit.fragments.UnderDevelopmentFragment;

import com.sw.hc.fit.fragments.home.HomeFragment;


/**
 * Created by fcampos on 01/11/2017.
 */
public class SwitchToolbar {
    private MainActivity mainActivity;
    private int position;
    private Fragment f;
    private int TAG;

    public SwitchToolbar(MainActivity mainActivity, int position) {
        this.mainActivity = mainActivity;
        this.position = position;
    }

    public Fragment getF() {
        return f;
    }

    public int getTAG() {
        return TAG;
    }

    public SwitchToolbar invoke() {

        TAG = position;

        switch (position) {

            case MainActivity.HOME_MAIN:

                f = HomeFragment.newInstance();

                break;

            default:

                f = UnderDevelopmentFragment.newInstance();
                TAG = MainActivity.UNDER_DEVELOPMENT_FRAGMENT;

                break;

        }
        return this;
    }
}
