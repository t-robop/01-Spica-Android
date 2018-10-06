package com.t_robop.yuusuke.a01_spica_android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceive extends Thread{
    private int port;
    private Context context;
    private Handler handler = new Handler();
    private Dialog dialog;

    public UdpReceive(Context context){
        port = 10000;
        this.context = context;
    }

    public void UdpReceiveStandby(){

        //ダイアログの設定
        dialog = new Dialog(context);
        dialog.setTitle("実行中");      //タイトル設定
        dialog.setCancelable(false);
    //    alertDialog.create();
        dialog.show();

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
                                    dialog.dismiss();
                                    Button startButton = (Button)((com.t_robop.yuusuke.a01_spica_android.ScriptActivity) context).findViewById(R.id.start_button);
                                    startButton.setEnabled(true);
                                    startButton.setBackgroundResource(R.drawable.start);
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
