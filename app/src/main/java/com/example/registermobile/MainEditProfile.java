package com.example.registermobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEditProfile extends AppCompatActivity {
    TextView tvWelcome, tvBack;
    EditText etNama, etAlamat, etKota, etTelp, etKodepos, etProvinsi;
    String email, nama;
    Button imgBtnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_edit_profile);
        getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = getIntent().getStringExtra("email");
        nama = getIntent().getStringExtra("nama");
        etNama = findViewById(R.id.etProfile_Nama);
        etAlamat = findViewById(R.id.etProfile_Alamat);
        etKota = findViewById(R.id.etProfile_Kota);
        etTelp = findViewById(R.id.etProfile_Telp);
        etKodepos = findViewById(R.id.etProfile_Kodepos);
        etProvinsi = findViewById(R.id.etProfile_Province);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBack = findViewById(R.id.tvBack);
        imgBtnSubmit = findViewById(R.id.btnSubmit);
        tvWelcome.setText("Welcome :" + getIntent().getStringExtra("nama") + "(" + getIntent().getStringExtra("email") + ")");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEditProfile.this, HomeActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("nama", nama);
                startActivity(intent);
                finish();
            }
        });

        getProfil(email);

        imgBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPelanggan data = new DataPelanggan();
                data.setNama(etNama.getText().toString());
                data.setAlamat(etAlamat.getText().toString());
                data.setKota(etKota.getText().toString());
                data.setTelp(etTelp.getText().toString());
                data.setKodepos(etKodepos.getText().toString());
                data.setProvinsi(etProvinsi.getText().toString());
                data.setEmail(email);

                updateProfil(data);
            }
        });
    }

    void getProfil(String vemail){
        ServerAPI urlAPI = new ServerAPI();
        String URL = urlAPI.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        api.getProfile(vemail).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if(json.getString("result").toString().equals("1")){
                        etNama.setText(json.getJSONObject("data").getString("nama"));
                        etAlamat.setText(json.getJSONObject("data").getString("alamat"));
                        etKota.setText(json.getJSONObject("data").getString("kota"));
                        etProvinsi.setText(json.getJSONObject("data").getString("provinsi"));
                        etTelp.setText(json.getJSONObject("data").getString("telp"));
                        etKodepos.setText(json.getJSONObject("data").getString("kodepos"));

                        Log.i("Info Profile",json.getJSONObject("data").getString("nama"));
                    } else {

                    }
                }catch (IOException | JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void updateProfil(DataPelanggan data){
        ServerAPI urlAPI = new ServerAPI();
        String URL = urlAPI.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<ResponseBody> call = api.updateProfile(data.getNama(), data.getAlamat(), data.getKota(), data.getProvinsi(), data.getTelp(), data.getKodepos(), data.getEmail());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    JSONObject json = new JSONObject(response.body().string());
                    Toast.makeText(MainEditProfile.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    getProfil(data.getEmail());
                }catch (JSONException|IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AlertDialog.Builder msg = new AlertDialog.Builder(MainEditProfile.this);
                msg.setMessage("Simpan Gagal, Error : "+t.toString()).setNegativeButton("Retry", null).create().show();
            }
        });
    }
}