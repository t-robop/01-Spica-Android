package com.t_robop.yuusuke.a01_spica_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    EditText text;
    EditText sendText;
    private UdpSend udp = new UdpSend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.editText);
        text.setText("192.168.0.75");
        sendText = findViewById(R.id.editText2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udp.disConnect();
    }

    public void send(View view) throws UnsupportedEncodingException {
        String ip = text.getText().toString();
        String str = sendText.getText().toString();
        str = System.currentTimeMillis() + str;
        udp.setIpAddres(ip);
        udp.setport(10000);
        udp.setSendText(str);
        udp.send();
    }

}
