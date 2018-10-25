package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.t_robop.yuusuke.a01_spica_android.R;

public class SettingActivity extends AppCompatActivity {

    TextView ipEditText;
    TextView portEditText;

    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ipEditText = findViewById(R.id.ip_edit);
        portEditText = findViewById(R.id.port_edit);

        final SharedPreferences pref = getSharedPreferences("udp_config", Context.MODE_PRIVATE);
        final String ip = pref.getString("ip", "");
        final int port = pref.getInt("port", 10000);
        ipEditText.setText(ip);
        portEditText.setText(String.valueOf(port));


        saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("ip", ipEditText.getText().toString());
                        editor.putInt("port", Integer.parseInt(portEditText.getText().toString()));
                        editor.apply();
                        finish();
                    }
                }
        );

        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
