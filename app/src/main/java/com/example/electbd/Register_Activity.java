package com.example.electbd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/*
public class Register_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText rg_username = findViewById(R.id.et_reg_username);
        EditText rg_email = findViewById(R.id.et_email);
        EditText rg_nid = findViewById(R.id.et_nid);
        EditText rg_birthday = findViewById(R.id.et_birth_date);
        EditText rg_phone = findViewById(R.id.et_phone);
        EditText rg_password = findViewById(R.id.et_reg_password);
        EditText rg_confirm_password = findViewById(R.id.et_confirm_password);

        Button btnRegister = findViewById(R.id.btn_SignUp);
        Button btnLogin = findViewById(R.id.btn_SignIn);

        String username = rg_username.getText().toString();
        String email = rg_email.getText().toString();
        String nid = rg_nid.getText().toString();
        String birthday = rg_birthday.getText().toString();
        String phone = rg_phone.getText().toString();
        String password = rg_password.getText().toString();
        String confirm_password = rg_confirm_password.getText().toString();

        btnRegister.setOnClickListener(v->{

                if (password.equals(confirm_password) && !username.isEmpty() && !password.isEmpty() && !nid.isEmpty() && !phone.isEmpty() && !birthday.isEmpty()) {

                    DatabaseHelper dbhelper = new DatabaseHelper(this);
                    boolean isinserted = dbhelper.insertUser(username, email, nid, birthday, phone, password);

                    if (isinserted) {
                        Intent intenttologin = new Intent(Register_Activity.this,MainActivity.class);
                        startActivity(intenttologin);
                    } else {
                        Toast.makeText(Register_Activity.this, "Cannot registered Successfully!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register_Activity.this, "Fill required filled first!", Toast.LENGTH_SHORT).show();

               }


        });
        btnLogin.setOnClickListener(v->{
            Toast.makeText(Register_Activity.this, "signin button clicked", Toast.LENGTH_SHORT).show();
            Intent intenttologin = new Intent(Register_Activity.this, MainActivity.class);
            startActivity(intenttologin);
        });
    }
}
*/

public class Register_Activity extends AppCompatActivity {

    private EditText rg_username, rg_email, rg_id, rg_birthday, rg_phone, rg_password, rg_confirm_password;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize UI components
        initializeUIComponents();

        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            handleRegistration();
        });
        btnLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void initializeUIComponents() {
        rg_username = findViewById(R.id.et_reg_username);
        rg_email = findViewById(R.id.et_email);
        rg_id = findViewById(R.id.et_id);
        rg_birthday = findViewById(R.id.et_birth_date);
        rg_phone = findViewById(R.id.et_phone);
        rg_password = findViewById(R.id.et_reg_password);
        rg_confirm_password = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_sign_in);
    }

    private void handleRegistration() {
        // Extract user inputs
        String username = rg_username.getText().toString().trim();
        String email = rg_email.getText().toString().trim();
        String id = rg_id.getText().toString().trim();
        String birthday = rg_birthday.getText().toString().trim();
        String phone = rg_phone.getText().toString().trim();
        String password = rg_password.getText().toString().trim();
        String confirm_password = rg_confirm_password.getText().toString().trim();

        // Validate inputs
        if (validateInputs(username, email, id, birthday, phone, password, confirm_password)) {
            boolean isInserted = dbHelper.insertUser(username, email, id, birthday, phone, password);

            if (isInserted) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                Toast.makeText(this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String username, String email, String id, String birthday, String phone, String password, String confirm_password) {

        Pattern phonePattern = Pattern.compile("^(\\+88)?01[2-9][0-9]{8}$");
        Pattern emailPattern = Pattern.compile("^(cse)_\\d{16}@lus.ac.bd$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
        Pattern birthdayPattern = Pattern.compile("^(0[1-9]|[1|2][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)[0-9]{2})$");
        Pattern idPattern = Pattern.compile("^\\d{16}$");

        if (username.isEmpty() || id.isEmpty() || birthday.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            if(username.isEmpty()){
                rg_username.setError("Enter Username");
            }
            if (id.isEmpty()){
                rg_id.setError("Enter ID");
            }
            if(birthday.isEmpty()){
                rg_birthday.setError("Enter BirthDate");
            }
            if(phone.isEmpty()){
                rg_phone.setError("Enter Mobile Number");
            }
            if(password.isEmpty()){
                rg_password.setError("Enter Password");
            }
            return false;
        }

        //email validation code

        else if (!emailPattern.matcher(email).matches()) {
            Toast.makeText(this, "Invalid e-mail address,CSE mail only", Toast.LENGTH_SHORT).show();
            rg_email.setError("Enter LU CSE mail");
            return  false;
        }

        // ID validation code
        else if (!idPattern.matcher(id).matches()) {
            Toast.makeText(this, "Invalid ID. Must Have 16 Digits", Toast.LENGTH_SHORT).show();
            rg_id.setError("Must have 16 digits");
            return false;
        }

        //birthday validation code
        else if (!birthdayPattern.matcher(birthday).matches()) {
            Toast.makeText(this, "Enter date correctly!!", Toast.LENGTH_SHORT).show();
            rg_birthday.setError("Enter Correct date");
            return false;
        }

        // Phone number validation code
        else if (!phonePattern.matcher(phone).matches()) {
            Toast.makeText(this, "is this a phone number?", Toast.LENGTH_SHORT).show();
            rg_phone.setError("Enter a valid number");
            return false;
        }

        //Password Validation code
        else if (!passwordPattern.matcher(password).matches()) {
            Toast.makeText(this, "Password must have least of 8 characters", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "atleast 1 upper, 1 lower and 1 number", Toast.LENGTH_SHORT).show();
            rg_password.setError("atleast 8,Uper, lower, number ");
            return false;
        }

        // confirmed password code
        else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void navigateToLogin() {
        Intent intentToLogin = new Intent(Register_Activity.this, MainActivity.class);
        startActivity(intentToLogin);
    }
}


