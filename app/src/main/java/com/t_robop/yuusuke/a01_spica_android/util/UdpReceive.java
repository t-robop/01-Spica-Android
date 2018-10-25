package com.t_robop.yuusuke.a01_spica_android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    private byte[] buffer;
    private Thread checkReceive;

    public UdpReceive(Context context){
        port = 10000;
        this.context = context;
    }

    public void UdpReceiveStandby(){

        //ダイアログの設定
        dialog = dialogSettings(dialog);

        checkReceive = new Thread(new Runnable() {
            @Override
            public void run() {
                //Threadが動いてる限り回す
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // UDPパケット待ち受け
                        DatagramSocket recvUdpSocket = new DatagramSocket(port);
                        recvUdpSocket.setReuseAddress(true);

                        buffer = new byte[2048];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                        // 受信するまでブロック
                        recvUdpSocket.receive(packet);

                        // 受信したデータをトーストで出力
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    String result = new String(buffer, "UTF-8");

                                    //結果をトースト表示
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                                    //ダイアログを消す
                                    dialog.dismiss();

                                    //監視しているスレッドを止める
                                    checkReceive.interrupt();

                                } catch(IOException e) {

                                }
                            }
                        });
                    } catch (IOException e) {

                    }
                }
            }
        });

        //スレッドの実行
        checkReceive.start();
    }

    private Dialog dialogSettings(Dialog executionDialog){
        executionDialog = new Dialog(context);
        executionDialog.setContentView(R.layout.receive_udp_dialog);
        executionDialog.setCancelable(false);
        Button cancelButton = executionDialog.findViewById(R.id.receive_udp_cancel_button);
        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UdpSend udp = new UdpSend();
                        udp.UdpSendText(context.getString(R.string.esp_reboot_command));
                        dialog.dismiss();
                    }
                }
        );
        executionDialog.show();
        return executionDialog;
    }
}