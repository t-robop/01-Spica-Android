package com.t_robop.yuusuke.a01_spica_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.t_robop.yuusuke.a01_spica_android.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSend {

    private String ip;
    private int port;
    private InetAddress inetAddress;
    private byte[] buff;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UdpSend(){
        Context context = MyApplication.getInstance();
        SharedPreferences pref = context.getSharedPreferences("udp_config", Context.MODE_PRIVATE);
        this.ip = pref.getString("ip","");
        this.port = pref.getInt("port",10000);
    }

    public void UdpSendText(String sendText){
        try {
            buff = sendText.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Thread sendTextThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inetAddress = InetAddress.getByName(ip);
                    socket = new DatagramSocket();
                    packet = new DatagramPacket(
                            buff,
                            buff.length,
                            inetAddress,
                            port
                    );
                    socket.send(packet);
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("da", "da");
                }
            }
        });
        sendTextThread.start();
    }
}
