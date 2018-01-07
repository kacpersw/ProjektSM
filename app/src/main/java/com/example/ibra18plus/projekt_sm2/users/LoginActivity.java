package com.example.ibra18plus.projekt_sm2.users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.main.MainActivity;
import com.example.ibra18plus.projekt_sm2.rooms.RoomUtils;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    Button login, register, polishBTN, englishBTN;
    EditText username, password;
    private static final String ID_USER = "id_user";
    ProgressDialog progressDialog;

    Retrofit.Builder builder;
    Retrofit retrofit;
    UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.btLogin);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        register = (Button) findViewById(R.id.btRegisterInLogin);
        polishBTN = findViewById(R.id.changePolish);
        englishBTN = findViewById(R.id.changeEnglish);

        polishBTN.setOnClickListener(e -> {
            changeLanguage("pl");
        });

        englishBTN.setOnClickListener(e -> {
            changeLanguage("en");
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        register.setOnClickListener(e -> {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = RegistrationFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (
                    Exception er) {
                er.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.cotent_frame2, fragment).
                    commit();
        });


        login.setOnClickListener(e -> {
            if (username.getText().toString().length() == 0) {
                username.setError("Username is required!");
                return;
            }

            if (password.getText().toString().length() == 0) {
                password.setError("password is required!");
                return;
            }


            builder = new Retrofit.Builder()
                    .baseUrl(RoomUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
            UserService room = retrofit.create(UserService.class);
            Call<User> call = room.getUser(username.getText().toString());
            progressDialog.show();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressDialog.dismiss();
                    try {
                        SharedPrefManager.getInstance(getBaseContext()).userLogin(response.body().getId(), response.body().getUsername(), response.body().getEmail(), response.body().getAdminRole());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } catch (NullPointerException npe) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                        adb.setTitle("Warning?");
                        adb.setMessage("There is no user with name: " + username.getText().toString() + " or the password is incorrect");
                        adb.setNegativeButton("Cancel", null);
                        adb.show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        });

    }



    public void changeLanguage(String language) {
        String languageToLoad = language;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getBaseContext().getResources().getDisplayMetrics());
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }




}
