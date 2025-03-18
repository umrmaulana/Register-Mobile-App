package com.example.registermobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private TextView tvWelcome, tvBack;
    private EditText etNama, etAlamat, etKota, etTelp, etKodepos, etProvinsi;
    private Button btnSubmit;
    private String email, nama;

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

        // Ambil data dari intent
        email = getIntent().getStringExtra("email");
        nama = getIntent().getStringExtra("nama");

        // Inisialisasi UI
        initViews();

        // Set welcome message
        tvWelcome.setText("Welcome : " + nama + " (" + email + ")");

        // Set listener tombol kembali
        tvBack.setOnClickListener(v -> navigateToHome());

        // Ambil data profil
        getProfil(email);

        // Set listener tombol submit
        btnSubmit.setOnClickListener(v -> updateProfil());
    }

    private void initViews() {
        etNama = findViewById(R.id.etProfile_Nama);
        etAlamat = findViewById(R.id.etProfile_Alamat);
        etKota = findViewById(R.id.etProfile_Kota);
        etTelp = findViewById(R.id.etProfile_Telp);
        etKodepos = findViewById(R.id.etProfile_Kodepos);
        etProvinsi = findViewById(R.id.etProfile_Province);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBack = findViewById(R.id.tvBack);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainEditProfile.this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("nama", nama);
        startActivity(intent);
        finish();
    }

    private void getProfil(String vemail) {
        ServerAPI urlAPI = new ServerAPI();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        api.getProfile(vemail).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        JSONObject json = new JSONObject(response.body().string());
                        if ("1".equals(json.getString("result"))) {
                            JSONObject data = json.getJSONObject("data");
                            etNama.setText(getValidString(data, "nama"));
                            etAlamat.setText(getValidString(data, "alamat"));
                            etKota.setText(getValidString(data, "kota"));
                            etProvinsi.setText(getValidString(data, "provinsi"));
                            etTelp.setText(getValidString(data, "telp"));
                            etKodepos.setText(getValidString(data, "kodepos"));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateProfil() {
        DataPelanggan data = new DataPelanggan();
        data.setNama(etNama.getText().toString().trim());
        data.setAlamat(etAlamat.getText().toString().trim());
        data.setKota(etKota.getText().toString().trim());
        data.setTelp(etTelp.getText().toString().trim());
        data.setKodepos(etKodepos.getText().toString().trim());
        data.setProvinsi(etProvinsi.getText().toString().trim());
        data.setEmail(email);

        ServerAPI urlAPI = new ServerAPI();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        api.updateProfile(data.getNama(), data.getAlamat(), data.getKota(), data.getProvinsi(),
                        data.getTelp(), data.getKodepos(), data.getEmail())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject json = new JSONObject(response.body().string());
                                Toast.makeText(MainEditProfile.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                getProfil(data.getEmail());
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        new AlertDialog.Builder(MainEditProfile.this)
                                .setMessage("Simpan Gagal, Error: " + t.toString())
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                });
    }

    private String getValidString(JSONObject json, String key) {
        try {
            if (json.has(key) && !json.isNull(key)) {
                return json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}