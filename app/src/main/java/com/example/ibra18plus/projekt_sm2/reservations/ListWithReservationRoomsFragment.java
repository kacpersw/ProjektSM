package com.example.ibra18plus.projekt_sm2.reservations;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.users.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class ListWithReservationRoomsFragment extends Fragment {
    ListView listView;
    ReservationAdapter roomAdapter;
    Integer id_user = SharedPrefManager.getInstance(getContext()).getId();
    Retrofit.Builder builder;
    Retrofit retrofit;
    private ProgressDialog progressDialog;
    private final Boolean isAdmin = SharedPrefManager.getInstance(getContext()).getAdmin();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_list_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        return view;

    }

    public void recreateList() {
        progressDialog.show();


        if (isAdmin) {
            builder = new Retrofit.Builder().baseUrl(ReservationUtils.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
            ReservationService room = retrofit.create(ReservationService.class);
            Call<List<Reservation>> call = room.getAllReservations();

            call.enqueue(new Callback<List<Reservation>>() {
                @Override
                public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                    List<Reservation> rooms = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++)
                        rooms.add(response.body().get(i));
                    roomAdapter = new ReservationAdapter(getContext(), rooms);
                    listView.setAdapter(roomAdapter);
                    roomAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Reservation>> call, Throwable t) {
                    Toast.makeText(getContext(), "error :(", Toast.LENGTH_SHORT).show();
                    Log.d("AAAAAAAAAAAAAAAAAAAAAAAAA", "error");
                    progressDialog.dismiss();
                }
            });
        } else {
            builder = new Retrofit.Builder().baseUrl(ReservationUtils.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
            ReservationService room = retrofit.create(ReservationService.class);
            Call<List<Reservation>> call = room.getAllReservationsByUserId(id_user);

            call.enqueue(new Callback<List<Reservation>>() {
                @Override
                public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                    List<Reservation> rooms = new ArrayList<>();
                    progressDialog.dismiss();
                    try {
                        for (int i = 0; i < response.body().size(); i++)
                            rooms.add(response.body().get(i));
                        roomAdapter = new ReservationAdapter(getContext(), rooms);
                        listView.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();

                    } catch (NullPointerException ne){
                        ne.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<List<Reservation>> call, Throwable t) {
                    Toast.makeText(getContext(), "error :(", Toast.LENGTH_SHORT).show();
                    Log.d("AAAAAAAAAAAAAAAAAAAAAAAAA", "error");
                    progressDialog.dismiss();
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        recreateList();
    }
}
