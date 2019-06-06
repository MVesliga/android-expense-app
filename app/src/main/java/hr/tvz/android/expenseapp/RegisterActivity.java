package hr.tvz.android.expenseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hr.tvz.android.expenseapp.model.User;
import hr.tvz.android.mvpexample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView loginTextView;
    private boolean ok;

    private TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.signup_first_name);
        lastNameEditText = findViewById(R.id.signup_last_name);
        emailEditText = findViewById(R.id.signup_email);
        usernameEditText = findViewById(R.id.signup_username);
        passwordEditText = findViewById(R.id.signup_password);
        loginTextView = findViewById(R.id.register_login);
        test = findViewById(R.id.register_test);
        ok = true;

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    public void registerUser(View v){
        User user = new User();
        String generatedPassword = encryptPAssword(passwordEditText.getText().toString());
        ok = true;

        if(firstNameEditText.getText().toString().isEmpty()){
            firstNameEditText.setError("Enter first name!");
            ok = false;
        }
        if(lastNameEditText.getText().toString().isEmpty()){
            lastNameEditText.setError("Enter last name!");
            ok = false;
        }
        if(emailEditText.getText().toString().isEmpty()){
            emailEditText.setError("Enter email!");
            ok = false;
        }
        if(usernameEditText.getText().toString().isEmpty()){
            usernameEditText.setError("Enter username!");
            ok = false;
        }
        if(passwordEditText.getText().toString().isEmpty()){
            passwordEditText.setError("Enter password!");
            ok = false;
        }

        if(ok) {
            user.setFirstName(firstNameEditText.getText().toString());
            user.setLastName(lastNameEditText.getText().toString());
            user.setEmail(emailEditText.getText().toString());
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(generatedPassword);

            firstNameEditText.setText("");
            lastNameEditText.setText("");
            emailEditText.setText("");
            usernameEditText.setText("");
            passwordEditText.setText("");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<User> call = jsonPlaceholderApi.createUser(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        test.setText("Code: " + response.code());
                        return;
                    }

                    User userResponse = response.body();

                    test.append("Code: " + response.code() + "\n");
                    test.append("Username: " + userResponse.getUsername() + "\n");
                    test.append("email: " + userResponse.getEmail() + "\n\n ");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    test.setText(t.getMessage());
                }
            });

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
        }
    }

    public String encryptPAssword(String password){
        String generatedPassword = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return generatedPassword;
    }
}
