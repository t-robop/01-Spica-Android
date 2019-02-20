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

    private String ip = "192.168.1.7";
    private int port = 50000;
    private InetAddress inetAddress;
    private byte[] buff;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Thread sendTextThread;



    public UdpSend(){
        Context context = MyApplication.getInstance();
        SharedPreferences pref = context.getSharedPreferences("udp_config", Context.MODE_PRIVATE);
        this.ip = pref.getString("ip","");
        this.port = pref.getInt("port",50000);
    }
    public UdpSend(String ip, int port){
        Context context = MyApplication.getInstance();
        SharedPreferences pref = context.getSharedPreferences("udp_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ip", ip);
        editor.putInt("port", port);
        editor.apply();

        // 読み込み
        this.ip = pref.getString("ip","");
        this.port = pref.getInt("port",50000);
    }

    public void UdpSendText(String sendText){
        try {
            buff = sendText.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sendTextThread = new Thread(new Runnable() {
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
