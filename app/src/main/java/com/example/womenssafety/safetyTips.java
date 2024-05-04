package com.example.womenssafety;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class safetyTips extends AppCompatActivity {
    TextView  web;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_safety_tips);
        back = findViewById(R.id.Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(safetyTips.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        web= findViewById(R.id.webTips);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.googleadservices.com/pagead/aclk?sa=L&ai=DChcSEwifooaG2MiFAxVBPgYAHXW0A_IYABADGgJ3cw&ase=2&gclid=CjwKCAjw5v2wBhBrEiwAXDDoJWf7BX2Uy3ceyKCUXg4YKNzLyC32UHX4ziXnhXt_7SiF7MrXr7xisRoCERsQAvD_BwE&ohost=www.google.com&cid=CAESVuD2Fc1zeCm6BCNZhdnMa-36n79tTx1BoNKAvKjLRk9wf_TK1SxLr-oyLFito1JZPoxkO6l75d-K0b84S8eX6TESiQHF5FqBElr3R8CPJ5qKSxdkj__M&sig=AOD64_0xQHCP-MYbOry-SVRHTqWb9HdkAg&q&nis=4&adurl&ved=2ahUKEwibu_-F2MiFAxUAYPEDHXRvD4IQ0Qx6BAgKEAE");
            }

            private void gotoUrl(String url) {
                try {
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "No website linked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}