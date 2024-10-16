package fr.android.mybiblio;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginTabFragment extends Fragment {

    private EditText login_password, login_email;
    private Button login_button;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        login_button = view.findViewById(R.id.login_button);

        databaseHelper = new DatabaseHelper(getActivity()); // Initialize the database helper

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.checkUser(email, password)) {
                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
