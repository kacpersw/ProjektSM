package com.example.ibra18plus.projekt_sm2.reservations;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.rooms.DeleteRoomFragment;
import com.example.ibra18plus.projekt_sm2.users.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class AddReservation extends Fragment implements SensorEventListener {


    EditText startDate, endDate, startHour, endHour;
    Button addReservationBt;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private static final String ID_ROOM = "id_room";
    Integer id_room, id_user = SharedPrefManager.getInstance(getContext()).getId();
    private static final String ID_USER = "id_user";
    private ReservationService mAPIService;
    Calendar startDateDate, startHourDate, endDateDate, endHourDate;
    int hour, minute, mYear, mMonth, mDay;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private ProgressDialog progressDialog;
    String dateFormatted;
    private final String TAG = "AddReservation";
    private ListView listView;
    private ReservationAdapter roomAdapter;
    private final Boolean isAdmin = SharedPrefManager.getInstance(getContext()).getAdmin();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_reservation_layout, container, false);

        startDate = (EditText) view.findViewById(R.id.etStartDate);
        endDate = (EditText) view.findViewById(R.id.etEndDate);
        startHour = (EditText) view.findViewById(R.id.etStartHour);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_room = bundle.getInt(ID_ROOM);
            Log.d("id_room room", "" + id_room);
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        recreateList(view);

        startDate.setShowSoftInputOnFocus(false);
        startHour.setShowSoftInputOnFocus(false);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        startDate.setOnClickListener(e -> {
            setDateTimeField();
        });


        startHour.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
                startHour.setText(selectedHour + ":" + selectedMinute);
                hour = selectedHour;
                minute = selectedMinute;
            }, hour, minute, true);//Yes 24 hour time
            makeDebugLog(hour + " " + minute + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            timePickerDialog.show();
        });

        addReservationBt = (Button) view.findViewById(R.id.btAddReservation);
        addReservationBt.setOnClickListener(e -> doEverything());
        return view;
    }


    public void recreateList(View view) {
        progressDialog.show();

        listView = (ListView) view.findViewById(R.id.add_reservationLV);


        Retrofit.Builder builder;
        Retrofit retrofit;
        builder = new Retrofit.Builder().baseUrl(ReservationUtils.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
        ReservationService room = retrofit.create(ReservationService.class);
        Call<List<Reservation>> call = room.getAllReservationsByRoomId(id_room);

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                List<Reservation> rooms = new ArrayList<>();
                progressDialog.dismiss();
//                Log.d(TAG, response.body().toString());
                if (response.body() == null) {
                    return;
                }
                for (int i = 0; i < response.body().size(); i++)
                    rooms.add(response.body().get(i));
                roomAdapter = new ReservationAdapter(getContext(), rooms);
                listView.setAdapter(roomAdapter);
                roomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Toast.makeText(getContext(), "error :(", Toast.LENGTH_SHORT).show();
                Log.d("AAAAAAAAAAAAAAAAAAAAAAAAA", "error");
                progressDialog.dismiss();
            }
        });


    }


    private void setDateTimeField() {
        DatePickerDialog fromDatePickerDialog;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            startDate.setText(dateFormatter.format(newDate.getTime()));
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void makeDebugLog(String msg) {
        Log.d(TAG, msg);
    }

    public void doEverything() {
        if (startDate.getText().toString().length() == 0) {
            startDate.setError("Start date is required!");
            return;
        }

        if (startHour.getText().toString().length() == 0) {
            startHour.setError("Start hour is required!");
            return;
        }
        if (endDate.getText().toString().length() == 0) {
            endDate.setError("Number of hours is required!");
            return;
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        makeDebugLog("in doEverything()");
        progressDialog.show();
        mAPIService = ReservationUtils.getAPIService();

        startDateDate = new GregorianCalendar(mYear, mMonth, mDay, hour, minute);
        fmt.setCalendar(startDateDate);
        dateFormatted = fmt.format(startDateDate.getTime());
        makeDebugLog(dateFormatted + "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");

        endDateDate = new GregorianCalendar(mYear, mMonth, mDay, hour + Integer.parseInt(endDate.getText().toString()), minute);


        fmt.setCalendar(endDateDate);
        String dateFormatted2 = fmt.format(endDateDate.getTime());


        sendPost(dateFormatted, dateFormatted2, id_room, id_user);

        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle("Added");
        adb.setMessage("Added a reservation!");
        adb.setPositiveButton("Ok", (dialog, which) -> {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = DeleteRoomFragment.class;
            // Handle navigation view item clicks here.
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.cotent_frame, fragment).commit();
        });
        adb.show();
    }


    public void sendPost(String startDate, String endDate, Integer id_room, Integer id_user) {
        mAPIService.createReservation(startDate, endDate, id_room, id_user).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                progressDialog.dismiss();
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                makeDebugLog("onResponse");
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                progressDialog.dismiss();
                makeDebugLog("onFailure");

            }
        });
    }


    public void showResponse(String response) {
        Log.d("zxc", response.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                doEverything();
            } else {
                //far
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
