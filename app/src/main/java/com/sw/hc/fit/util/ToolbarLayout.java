package com.sw.hc.fit.util;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sw.hc.fit.App;
import com.sw.hc.fit.R;
import com.sw.hc.fit.activities.MainActivity;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by fcampos on 11/12/2017.
 */
public class ToolbarLayout {

    @BindColor(R.color.light_blue)
    int blue;

    @BindColor(R.color.purple_dark)
    int purpleDark;

    @BindColor(R.color.purple_light)
    int purpleLight;

    private MainActivity mainActivity;
    private int TAG;

    public ToolbarLayout(MainActivity mainActivity, int TAG) {

        this.mainActivity = mainActivity;
        this.TAG = TAG;
    }

    public void invoke() {

        ButterKnife.bind(this, mainActivity);

        ImageView iv = mainActivity.toolbar.findViewById(R.id.iv_placeholder);
        TextView tv = mainActivity.toolbar.findViewById(R.id.logo_tv);

        // iv.setVisibility(View.GONE); // Logo visible :)
        tv.setVisibility(View.VISIBLE);
        Typeface typeface = App.getCustomFont(mainActivity);
        tv.setTextSize(16);
        tv.setTypeface(typeface);

        // Toolbar (ActionBar) setup
        mainActivity.toolbarActionBarSetUP();

        switch (TAG) {


            case MainActivity.HOME_MAIN:

                // iv.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("Home");

                iv.setVisibility(View.INVISIBLE);

                // Background
                mainActivity.toolbar.setBackgroundColor(purpleDark);

                mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                break;

            default:

                // iv.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);

                // Background
                mainActivity.toolbar.setBackgroundColor(blue);

                mainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_actionbar_hamburger); // set Hamburger default back to the Actionbar ;)

                break;

        }
    }
}
