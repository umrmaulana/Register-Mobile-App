package com.example.registermobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    TextView tv_kembali;
    EditText ti_nama, ti_email, ti_password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv_kembali = (TextView) findViewById(R.id.tv_kembali);
        tv_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ti_nama = (EditText) findViewById(R.id.ti_nama);
        ti_email = (EditText) findViewById(R.id.ti_email);
        ti_password = (EditText) findViewById(R.id.ti_password);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesSubmit(ti_email.getText().toString(),
                ti_nama.getText().toString(),
                ti_password.getText().toString());
            }
        });
    }

    public boolean isEmailValid (String email){
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }

    void prosesSubmit (String vemail, String vnama, String vpassword){
        ServerAPI urlapi = new ServerAPI();
        String URL = urlapi.BASE_URL;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        if(!isEmailValid(ti_email.getText().toString())){
            AlertDialog.Builder msg = new AlertDialog.Builder(RegisterActivity.this);
            msg.setMessage("Email Tidak Valid").setNegativeButton("Retry",null).create().show();
            return;
        }

        api.register(vemail, vnama, vpassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    JSONObject json = new JSONObject(response.body().string());

                    if(json.getString("status").toString().equals("1")){

                        if(json.getString("result").toString().equals("1")){

                            AlertDialog.Builder msg = new AlertDialog.Builder(RegisterActivity.this);
                            msg.setMessage("Register Berhasil").setNegativeButton("ok", null).create().show();
                            ti_nama.setText("");
                            ti_email.setText("");
                            ti_password.setText("");

                        } else {

                            AlertDialog.Builder msg = new AlertDialog.Builder(RegisterActivity.this);
                            msg.setMessage("Simpan Gagal").setNegativeButton("retry", null).create().show();

                        }
                    } else {

                        AlertDialog.Builder msg = new AlertDialog.Builder(RegisterActivity.this);
                        msg.setMessage("User Sudah Ada").setNegativeButton("retry", null).create().show();

                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Info Register", "Register Gagal"+t.toString());
            }
        });
    }
}