package com.quaspecsystems.quaspay.quaspay;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtPassword;
    Button btnSignIn;
    private final String host_server_url = "http://192.168.8.100:8080/quaspec/rest/login";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhoneNumber = findViewById(R.id.et_phoneNumber);
        edtPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.signInButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,)
                String phoneNumber = edtPhoneNumber.getText().toString();
                String password = edtPassword.getText().toString();

                new LoginUser().execute(phoneNumber, password);
            }
        });

    }

    public class LoginUser extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String phoneNumber =  strings[0];
            String password =  strings[1];

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("phone_number", phoneNumber)
                    .add("pass_word", password)
                    .build();

             Request request = new Request.Builder()
                     .url(host_server_url)
                     .post(requestBody)
                     .build();

             Response response = null;
             try{
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String result = response.body().string();
                        if (result.equalsIgnoreCase("login")){
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this, "Phone Number or Password mismatched!", Toast.LENGTH_LONG).show();
                        }
                    }
             }catch (Exception e){
                 e.printStackTrace();
             }
            return null;
        }
    }
}
