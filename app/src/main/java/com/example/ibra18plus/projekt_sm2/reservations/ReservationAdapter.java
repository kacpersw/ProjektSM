package com.example.ibra18plus.projekt_sm2.reservations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ibra18plus.projekt_sm2.main.MainActivity;
import com.example.ibra18plus.projekt_sm2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class ReservationAdapter extends ArrayAdapter<Reservation> {
    private Context context;
    private List<Reservation> values;
    Retrofit.Builder builder;
    Retrofit retrofit;
    ReservationService service;
    Call<List<Reservation>> repos;

    public ReservationAdapter(Context context, List<Reservation> values) {
        super(context, R.layout.delete_list_layout, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.reservation_row, parent, false);
        }

        TextView textView = (TextView) row.findViewById(R.id.tvRow2);
        ImageButton imageButton = (ImageButton) row.findViewById(R.id.ibDeleteRoom2);

        Reservation item = values.get(position);
        textView.setText(item.toString());

        imageButton.setOnClickListener(e -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete this Reservation?");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    builder = new Retrofit.Builder()
                            .baseUrl(ReservationUtils.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create());
                    retrofit = builder.build();
                    ReservationService Reservation = retrofit.create(ReservationService.class);
                    Call<Reservation> call = Reservation.deleteReservation(item.getId());
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                            Fragment fragment = null;
                            Class fragmentClass = null;
                            fragmentClass = ListWithReservationRoomsFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }

                            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                            fragmentManager.beginTransaction().
                                    replace(R.id.cotent_frame, fragment).
                                    commit();
                        }

                        @Override
                        public void onFailure(Call<Reservation> call, Throwable t) {
                            Fragment fragment = null;
                            Class fragmentClass = null;
                            fragmentClass = ListWithReservationRoomsFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }

                            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                            fragmentManager.beginTransaction().
                                    replace(R.id.cotent_frame, fragment).
                                    commit();
                        }
                    });
                }
            });
            adb.show();
        });

        return row;
    }
}
