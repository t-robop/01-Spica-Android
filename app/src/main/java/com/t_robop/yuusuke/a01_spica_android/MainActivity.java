package com.t_robop.yuusuke.a01_spica_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    EditText text;
    final HttpGet http = new HttpGet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.editText);
        text.setText("172.20.10.6");
    }

    public void front(View view) {
        String ip = text.getText().toString();
        http.setRequest(ip,"FORWARD");
        http.sendHttp();
    }
    public void left(View view) {
        String ip = text.getText().toString();
        http.setRequest(ip,"LEFT");
        http.sendHttp();
    }
    public void right(View view) {
        String ip = text.getText().toString();
        http.setRequest(ip,"RIGHT");
        http.sendHttp();
    }
    public void back(View view) {
        String ip = text.getText().toString();
        http.setRequest(ip,"BACK");
        http.sendHttp();
    }
}
