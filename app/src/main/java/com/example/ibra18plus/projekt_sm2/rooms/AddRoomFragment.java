package com.example.ibra18plus.projekt_sm2.rooms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibra18plus.projekt_sm2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class AddRoomFragment extends Fragment {

    EditText roomName, roomNunber, capacity;
    Button addRoomBt;
    private RoomService mAPIService;
    private static final String ID_USER = "id_user";
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_room_layout, container, false);

        roomName = (EditText) view.findViewById(R.id.etRoomName);
        roomNunber = (EditText) view.findViewById(R.id.etRoomNumber);
        capacity = (EditText) view.findViewById(R.id.etCapacity);
        addRoomBt = (Button) view.findViewById(R.id.btAddRoom);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");

        addRoomBt.setOnClickListener(e -> {
            if (roomName.getText().toString().length() == 0) {
                roomName.setError("Room name is required!");
                return;
            }

            if (roomNunber.getText().toString().length() == 0) {
                roomNunber.setError("Room number is required!");
                return;
            }
            if (capacity.getText().toString().length() == 0) {
                capacity.setError("Capacity is required!");
                return;
            }
            mAPIService = RoomUtils.getAPIService();
            progressDialog.show();
            sendPost(roomName.getText().toString(), Integer.parseInt(roomNunber.getText().toString()), Integer.parseInt(capacity.getText().toString()));
        });


        return view;
    }

    public void sendPost(String roomName, Integer nr, Integer capacity) {
        mAPIService.createRoom(roomName, nr, capacity).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.d("qwe", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                progressDialog.dismiss();
                createDialog();
                Log.d("asd", "Unable to submit post to API.");
            }
        });

    }

    public void createDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle("Added");
        adb.setMessage("Added a room!");
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    public void showResponse(String response) {
        Log.d("zxc", response.toString());
    }

}
