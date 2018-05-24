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
    final String HTTP_STR_FRONT = "http://";
    final String HTTP_STR_BACK = "/?0=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.editText);
        text.setText("172.20.10.6");

    }

    public void front(View view) {
        String ip = text.getText().toString();
        final Request request = new Request.Builder()
                .url(HTTP_STR_FRONT + ip + HTTP_STR_BACK + "FORWARD")
                .build();
        httpRequest(request);
    }
    public void left(View view) {
        String ip = text.getText().toString();
        final Request request = new Request.Builder()
                .url(HTTP_STR_FRONT + ip + HTTP_STR_BACK + "LEFT")
                .build();
        httpRequest(request);
    }
    public void right(View view) {
        String ip = text.getText().toString();
        final Request request = new Request.Builder()
                .url(HTTP_STR_FRONT + ip + HTTP_STR_BACK + "RIGHT")
                .build();
        httpRequest(request);
    }
    public void back(View view) {
        String ip = text.getText().toString();
        final Request request = new Request.Builder()
                .url(HTTP_STR_FRONT + ip + HTTP_STR_BACK + "BACK")
                .build();
        httpRequest(request);
    }
    void httpRequest(Request request){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
            @Override
            public void onFailure(Call call, IOException arg1) {

            }
        });
    }
}
