package com.example.ibra18plus.projekt_sm2.users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibra18plus.projekt_sm2.R;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class RegistrationFragment extends Fragment {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    EditText firstName;
    EditText surName;
    EditText email;
    EditText username;
    EditText password;
    Button register;
    Button finishBTN;
    private UserService mAPIService;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, container, false);

        firstName = (EditText) view.findViewById(R.id.etFirstName);
        surName = (EditText) view.findViewById(R.id.etSurname);
        email = (EditText) view.findViewById(R.id.etEmail);
        username = (EditText) view.findViewById(R.id.etUsernameRegister);
        password = (EditText) view.findViewById(R.id.etPasswordRegister);
        register = (Button) view.findViewById(R.id.btRegister);
        finishBTN = view.findViewById(R.id.finishRegisterBTN);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        finishBTN.setOnClickListener(e -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });


        register.setOnClickListener(e -> {
            if (firstName.getText().toString().length() == 0) {
                firstName.setError("First name is required!");
                return;
            }

            if (surName.getText().toString().length() == 0) {
                surName.setError("Surname is required!");
                return;
            }
            if (email.getText().toString().length() == 0) {
                email.setError("Email is required!");
                return;
            } else if (!isValidEmail(email.getText().toString())) {
                email.setError("Wrong pattern, use e-mail pattern!");
                return;
            }

            if (username.getText().toString().length() == 0) {
                username.setError("Username is required!");
                return;
            }
            if (password.getText().toString().length() == 0) {
                password.setError("Password is required!");
                return;
            }
            progressDialog.show();
            sendPost(username.getText().toString(), password.getText().toString(), firstName.getText().toString(), surName.getText().toString(), email.getText().toString());

            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
            adb.setTitle("Done");
            adb.setMessage("Registered!");
            progressDialog.dismiss();
            adb.setPositiveButton("Ok", (dialog, which) -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
            adb.show();


        });

        return view;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public void sendPost(String username, String password, String firstName, String surname, String email) {
        mAPIService = UserUtils.getAPIService();
        mAPIService.createUser(firstName, surname, email, username, password, false).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

}
