package com.example.ibra18plus.projekt_sm2.rooms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.main.MainActivity;
import com.example.ibra18plus.projekt_sm2.users.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class RoomAdapter extends ArrayAdapter<Room> {
    private Context context;
    private List<Room> values;
    Retrofit.Builder builder;
    Retrofit retrofit;
    RoomService service;
    Call<List<Room>> repos;
    private Boolean IS_ADMIN;

    public RoomAdapter(Context context, List<Room> values) {
        super(context, R.layout.delete_list_layout, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, parent, false);
        }

        TextView textView = (TextView) row.findViewById(R.id.tvRow);
        ImageButton imageButton = (ImageButton) row.findViewById(R.id.ibDeleteRoom);

        Room item = values.get(position);
        if (SharedPrefManager.getInstance(context).getAdmin() != null) {
            IS_ADMIN = SharedPrefManager.getInstance(context).getAdmin();
            if (!IS_ADMIN)
                imageButton.setVisibility(View.INVISIBLE);

        } else {
            IS_ADMIN = false;
        }
        Log.d("QWWQWQ", " " + IS_ADMIN);


        textView.setText(item.toString());

        imageButton.setOnClickListener(e -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete this room?");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    builder = new Retrofit.Builder()
                            .baseUrl(RoomUtils.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create());
                    retrofit = builder.build();
                    RoomService room = retrofit.create(RoomService.class);
                    Call<Room> call = room.deleteRoom(item.getId());
                    Log.d("ID::::::::::::", " " + item.getId());
                    call.enqueue(new Callback<Room>() {
                        @Override
                        public void onResponse(Call<Room> call, Response<Room> response) {
                            Fragment fragment = null;
                            Class fragmentClass = null;
                            fragmentClass = DeleteRoomFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("ID1::::::::::::", " " + item.getId());
                            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                            fragmentManager.beginTransaction().
                                    replace(R.id.cotent_frame, fragment).
                                    commit();
                        }

                        @Override
                        public void onFailure(Call<Room> call, Throwable t) {
                            Fragment fragment = null;
                            Class fragmentClass = null;
                            fragmentClass = DeleteRoomFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("ID2::::::::::::", " " + item.getId());
                            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
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
