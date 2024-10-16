package fr.android.mybiblio;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {

    private EditText signup_email, signup_password, signup_confirm;
    private Button signup_button;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        signup_email = view.findViewById(R.id.signup_email);
        signup_password = view.findViewById(R.id.signup_password);
        signup_confirm = view.findViewById(R.id.signup_confirm);
        signup_button = view.findViewById(R.id.signup_button);

        databaseHelper = new DatabaseHelper(getActivity()); // Initialize the database helper

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_email.getText().toString().trim();
                String password = signup_password.getText().toString().trim();
                String confirmPassword = signup_confirm.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.doesUserExist(email)) {
                    Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.addUser(email, password);
                    if (isInserted) {
                        Toast.makeText(getActivity(), "Signup successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}

