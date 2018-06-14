package com.sw.hc.fit.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.sw.hc.fit.App;
import com.sw.hc.fit.activities.MainActivity;
import com.sw.hc.fit.fragments.home.HomeFragment;
import java.util.concurrent.TimeUnit;

import static com.sw.hc.fit.activities.MainActivity.REQUEST_PERMISSIONS_REQUEST_CODE;
import static com.sw.hc.fit.activities.MainActivity.client;

/**
 * Created by fcampos on 17/05/2018.
 */

public class GoogleFitUtils {

    private final static String LOG_TAG = GoogleFitUtils.class.getName();
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 200;
    private static final int SENSORS_REQUEST_CODE = 300;

    private static boolean checkPermissionsAccessFineLocation() {

        int permissionState = ActivityCompat.checkSelfPermission(App.getInstance(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    public static void checkSensorsPermission(Activity activity) {

        if (!checkPermissionsBodySensor()) {

            if (activity != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BODY_SENSORS}, SENSORS_REQUEST_CODE);
            }
        }
    }


    public static void checkAccessFineLocationPermission(Activity activity) {

        if (!checkPermissionsAccessFineLocation()) {

            if (activity != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
        }
    }

    // Sensor
    private static boolean checkPermissionsBodySensor() {


        int permissionState = ActivityCompat.checkSelfPermission(App.getInstance(),
                Manifest.permission.BODY_SENSORS);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private static void updateStepsGoogle(int stepsCount, FragmentManager fm) {

        Log.d(LOG_TAG, "updateStepsGoogle ===== " + stepsCount);

        HomeFragment homeFrag = (HomeFragment) fm.findFragmentByTag(String.valueOf(MainActivity.HOME_MAIN));

        if (homeFrag != null && homeFrag.isVisible()) {

            homeFrag.stepsFromGoogleFit(stepsCount);
        }
    }


    private static void updateDistanceGoogle(float distanceCount, FragmentManager fm) {

        Log.d(LOG_TAG, "updateDistanceGoogle ===== " + distanceCount);

        HomeFragment homeFrag = (HomeFragment) fm.findFragmentByTag(String.valueOf(MainActivity.HOME_MAIN));

        if (homeFrag != null && homeFrag.isVisible()) {

            homeFrag.distanceFromGoogleFit(distanceCount);
        }
    }


    /*
   Build a GoogleApiClient that will authenticate the user and allow the application to connect to Fitness APIs.
    */
    public static GoogleApiClient buildFitnessClient(GoogleApiClient client, Activity activity) {

        Log.d(LOG_TAG, "buildFitnessClient called");

        if (client == null && GoogleFitUtils.checkPermissionsAccessFineLocation() && GoogleFitUtils.checkPermissionsBodySensor()) {

            client = new GoogleApiClient.Builder(activity)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(@Nullable Bundle bundle) {
                                    Log.d(LOG_TAG, "buildFitnessClient connected");
                                    // Now you can make calls to the Fitness APIs

                                    // the HomeFragment will call the "subscribeDailySteps()"

                                }

                                @Override
                                public void onConnectionSuspended(int i) {

                                    Log.i(LOG_TAG, "buildFitnessClient connection suspended");
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.w(LOG_TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.w(LOG_TAG, "Connection lost.  Reason: Service Disconnected");
                                    }
                                }


                            }
                    )
                    .enableAutoManage((MainActivity) activity, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.e(LOG_TAG, "Google Play services failed. Cause: " + connectionResult.toString());

                        }
                    })
                    .build();

        }

        return client;
    }


    public static void requestGoogleFitPermissionResult(int requestCode, @NonNull int[] grantResults,
                                                        GoogleApiClient client, Activity activity) {
        Log.i(LOG_TAG, "onRequestPermissionsResult");

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LOG_TAG, "User interaction was cancelled");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                buildFitnessClient(client, activity);
            } else {
                // Permission denied
                Toast.makeText(App.getInstance(), "Permission denied for Google FIT", Toast.LENGTH_LONG).show();
            }
        }
    }


    public static class VerifyDataTaskSteps extends AsyncTask<GoogleApiClient, Void, Void> {

        int total = 0;

        protected Void doInBackground(GoogleApiClient... clients) {

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(clients[0], DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            } else {
                Log.e(LOG_TAG, "There was a problem getting the step count");
            }

            Log.i(LOG_TAG, "Total steps: " + total);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // UI
            updateStepsGoogle(total, MainActivity.mainActivity.getSupportFragmentManager());

        }

    }


    public static class VerifyDataTaskDistance extends AsyncTask<GoogleApiClient, Void, Void> {

        float total = 0;

        protected Void doInBackground(GoogleApiClient... clients) {

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(clients[0], DataType.TYPE_DISTANCE_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
                }
            } else {
                Log.e(LOG_TAG, "There was a problem getting the distance count");
            }

            Log.i(LOG_TAG, "Total distance: " + total);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // UI
            updateDistanceGoogle(total, MainActivity.mainActivity.getSupportFragmentManager());

        }

    }


    private static void readStepsToday() {
        new VerifyDataTaskSteps().execute(client);
    }

    // ----------- Google Fit Daily STEPS -----------
    public static void subscribeDailySteps() {

        Log.d(LOG_TAG, "subscribeDailySteps was called");

        if (client != null) {

            // To create a subscription, invoke the Recording API.
            // As soon as the subscription is active, fitness data will start recording
            Fitness.RecordingApi.subscribe(client, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

                                if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                    Log.d(LOG_TAG, "Existing subscription for activity detected.");

                                } else {
                                    Log.d(LOG_TAG, "Successfully subscribed");

                                }

                                // :)
                                readStepsToday();

                            } else {
                                Log.e(LOG_TAG, "There was a problem subscribing");
                            }

                        }
                    });

        }
    }


    private static void readDistanceToday() {
        new VerifyDataTaskDistance().execute(client);
    }


    // ----------- Google Fit Daily DISTANCE -----------
    public static void subscribeDailyDistance() {


        Log.d(LOG_TAG, "subscribeDailyDistance was called");

        if (client != null) {

            // To create a subscription, invoke the Recording API.
            // As soon as the subscription is active, fitness data will start recording
            Fitness.RecordingApi.subscribe(client, DataType.TYPE_DISTANCE_DELTA)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

                                if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                    Log.d(LOG_TAG, "Existing subscription for activity detected.");

                                } else {
                                    Log.d(LOG_TAG, "Successfully subscribed");

                                }

                                // :)
                                readDistanceToday();

                            } else {
                                Log.e(LOG_TAG, "There was a problem subscribing");
                            }

                        }
                    });

        }

    }


}