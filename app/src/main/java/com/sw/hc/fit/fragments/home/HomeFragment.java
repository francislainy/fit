package com.sw.hc.fit.fragments.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.sw.hc.fit.R;
import com.sw.hc.fit.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sw.hc.fit.util.GoogleFitUtils.checkAccessFineLocationPermission;
import static com.sw.hc.fit.util.GoogleFitUtils.checkSensorsPermission;
import static com.sw.hc.fit.util.GoogleFitUtils.subscribeDailyDistance;
import static com.sw.hc.fit.util.GoogleFitUtils.subscribeDailySteps;


public class HomeFragment extends Fragment {

    private final String LOG_TAG = HomeFragment.class.getName();

    @BindView(R.id.tv_steps_count)
    TextView tvStepsCount;

    @BindView(R.id.tv_distance)
    TextView tvDistance;

    @BindView(R.id.donut_progress_steps)
    DonutProgress donutProgressSteps;

    @BindView(R.id.donut_progress_distance)
    DonutProgress donutProgressDistance;

    @BindView(R.id.iv_stat_steps)
    ImageView ivStatSteps;

    @BindView(R.id.iv_stat_distance)
    ImageView ivStatDistance;

    @BindColor(android.R.color.transparent)
    int transp;

    @BindColor(R.color.light_blue)
    int blue;

    private List<ObjectAnimator> listObjectAnimator = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);

        ButterKnife.bind(this, view);

        // Get the Google Fit API
        googleFitMethods();


        return view;
    }


    public void googleFitMethods() {

        ((MainActivity) getActivity()).buildFitnessClient();
        subscribeDailySteps();
        subscribeDailyDistance();

        checkSensorsPermission(getActivity());
        checkAccessFineLocationPermission(getActivity());
    }


    private void updateDonutProgress(final DonutProgress donutProgress, final ImageView imageView, float progressParam) {

        final float progress;
        if (progressParam >= 100) {
            progress = 100; // this is just in case if the user is over the target so the Donut doenst go over 100%
        } else {
            progress = progressParam;
        }


        // Clear up first (in case a Style was set previous)
        imageView.setColorFilter(null);


        // Donut Progress Animation :)
        getActivity().runOnUiThread(new Runnable() {

            public void run() {


                ObjectAnimator anim = ObjectAnimator.ofFloat(donutProgress, "progress", 0, progress);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.setDuration(1000); // Todo: Check if should get back to 2000
                anim.start();

                listObjectAnimator.add(anim);

                donutProgress.setInnerBackgroundColor(transp);

                imageView.setColorFilter(null); //tint White


                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.d(LOG_TAG, "onAnimationStart");

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.d(LOG_TAG, "onAnimationCancel");

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Log.d(LOG_TAG, "onAnimationRepeat");

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        Log.d(LOG_TAG, "onAnimationEnd");


                        if (progress >= 100) {
                            imageView.setColorFilter(Color.argb(255, 255, 255, 255)); //tint White

                            // fill
                            donutProgress.setInnerBackgroundColor(blue);

                            imageView.setColorFilter(Color.argb(255, 255, 255, 255)); //tint White

                        } else {

                            donutProgress.setInnerBackgroundColor(transp);

                            imageView.setColorFilter(null);
                        }

                    }

                });

            }
        });

    }


    /*
    * From Main, its called after the Google Fit API onPost execution ;)
    */
    public void stepsFromGoogleFit(int stepsFromGoogleFit) {

        updateHomeStepsUI(stepsFromGoogleFit);
    }


    public void distanceFromGoogleFit(float distanceFromGoogleFit) {

        updateHomeDistanceUI(distanceFromGoogleFit%1000);
    }


    private void updateHomeStepsUI(int steps) {

        // STEPS
        float targetSteps = 10000;

        float percentSteps = percentOfNumber(steps, targetSteps);

        tvStepsCount.setText("" + steps);


        updateDonutProgress(donutProgressSteps, ivStatSteps, percentSteps);
    }


    private void updateHomeDistanceUI(float distance) {

        // DISTANCE
        float targetDistance = 10;

        float percentDistance = percentOfNumber(distance, targetDistance);

        tvDistance.setText(String.format("%.2f", distance / 1000)); // Transform meters into Kms


        updateDonutProgress(donutProgressDistance, ivStatDistance, percentDistance);
    }


    private float percentOfNumber(float number, float total) {

        return (number * 100) / total;
    }

}