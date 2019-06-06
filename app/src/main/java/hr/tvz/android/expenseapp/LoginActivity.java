package hr.tvz.android.expenseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import hr.tvz.android.mvpexample.R;
import hr.tvz.android.expenseapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView registerTextView;
    private ProgressBar progressBar;
    private boolean ok;
    private TextView test;
    int exists = 0;
    Long userId;
    String username;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FlowManager.init(this);

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        registerTextView = findViewById(R.id.login_register);
        progressBar = findViewById(R.id.progress);
        test = findViewById(R.id.login_test);

        usernameEditText.setError(null);
        passwordEditText.setError(null);
         ok = true;

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(View v){
        final User loggedUser = new User();
        ok = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<List<User>> call = jsonPlaceholderApi.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(!response.isSuccessful()){
                   test.setText("Code: " + response.code());
                    return;
                }

                List<User> users = response.body();
                String encryptedPassword = encryptPAssword(passwordEditText.getText().toString());

                for(User u : users){
                    if(u.getUsername().equals(usernameEditText.getText().toString()) && u.getPassword().equals(encryptedPassword)){
                        userId = u.getId();
                        username = u.getUsername();
                        email = u.getEmail();
                        exists++;
                    }
                }

                if(usernameEditText.getText().toString().isEmpty()){
                    usernameEditText.setError("Enter username!");
                    ok = false;
                }
                if(passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("Enter password!");
                    ok = false;
                }

                if (ok) {
                    if(exists != 0){
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,"Login successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"User does not exist!", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                test.setText(t.getMessage());
            }
        });
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
