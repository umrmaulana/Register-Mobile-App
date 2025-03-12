package com.example.registermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {
    TextView tvWelcome;
    Button imgBtnLogout;
    ImageButton btnEditProfile, btnProduct;
    String email, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = getIntent().getStringExtra("email");
        nama = getIntent().getStringExtra("nama");
        TextView tvNama = (TextView) findViewById(R.id.tv_nama);
        tvNama.setText(getIntent().getStringExtra("nama"));
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        tvEmail.setText(getIntent().getStringExtra("email"));
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnProduct = findViewById(R.id.btnProduct);

        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainEditProfile.class);
                intent.putExtra("email", email);
                intent.putExtra("nama", nama);
                startActivity(intent);
                finish();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("nama", nama);
                startActivity(intent);
                finish();
            }
        });
    }
}