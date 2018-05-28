package com.t_robop.yuusuke.a01_spica_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.UnsupportedEncodingException;


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
        udp.setIpAddress(ip);
        udp.setPort(10000);
        udp.setSendText(str);
        udp.send();
    }

}
