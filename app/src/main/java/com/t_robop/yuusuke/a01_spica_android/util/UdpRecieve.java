package com.t_robop.yuusuke.a01_spica_android.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpRecieve extends Thread{
    private int port;
    Context context;
    Handler handler = new Handler();

    public UdpRecieve(Context context){
        port = 10000;
        this.context = context;
    }

    public void UdpRecieveStandby(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Threadが動いてる限り回す
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // UDPパケット待ち受け
                        DatagramSocket recvUdpSocket = new DatagramSocket(port);
                        recvUdpSocket.setReuseAddress(true);

                        final byte[] buffer = new byte[2048];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                        // 受信するまでブロック
                        recvUdpSocket.receive(packet);

                        // 受信したデータをトーストで出力
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    String result = new String(buffer, "UTF-8");
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                                } catch(IOException e) {

                                }
                            }
                        });
                    } catch (IOException e) {

                    }
                }
            }
        }).start();
    }
}
