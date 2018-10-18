package com.t_robop.yuusuke.a01_spica_android.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSend {
    private int port;
    private String ip;
    private InetAddress inetAddress;
    private byte[] buff;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Thread sendTextThread;

    public UdpSend(){
        port = 10000;
        ip = "192.168.0.7";
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