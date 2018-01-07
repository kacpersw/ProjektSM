package com.example.ibra18plus.projekt_sm2.rooms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.reservations.AddReservation;

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

public class DeleteRoomFragment extends Fragment {

    ListView listView;
    RoomAdapter roomAdapter;
    ImageButton imageButton;

    Retrofit.Builder builder;
    Retrofit retrofit;
    RoomService service;
    Call<List<Room>> repos;
    List<Room> q = new ArrayList<>();
    private static final String ID_ROOM = "id_room";
    private static final String ID_USER = "id_user";
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_list_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Integer idd = roomAdapter.getItem(position).getId();

            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = AddReservation.class;
            // Handle navigation view item clicks here.
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(ID_ROOM, idd);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.cotent_frame, fragment).commit();
        });
        return view;

    }

    public void recreateList() {
        progressDialog.show();
        builder = new Retrofit.Builder()
                .baseUrl(RoomUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
        RoomService room = retrofit.create(RoomService.class);
        Call<List<Room>> call = room.getAllRooms();
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.d("Dobrze: ", response.body().toString());
                progressDialog.dismiss();
                List<Room> rooms = response.body();
                roomAdapter = new RoomAdapter(getContext(), rooms);
                listView.setAdapter(roomAdapter);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getContext(), "error :(", Toast.LENGTH_SHORT);
                Log.d("AAAAAAAAAAAAAAAAAAAAAAAAA", "error");
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        recreateList();
    }
}
