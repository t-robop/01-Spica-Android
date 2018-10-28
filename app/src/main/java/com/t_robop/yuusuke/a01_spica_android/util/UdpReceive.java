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

import com.crashlytics.android.Crashlytics;
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
        Crashlytics.log("UdpReceiveStandby");
        //ダイアログの設定
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.receive_udp_dialog);
        dialog.setCancelable(false);
        Button cancelButton = dialog.findViewById(R.id.receive_udp_cancel_button);
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
        dialog.show();
        Crashlytics.log("UdpReceiveStandby: dialog show");
        checkReceive = new Thread(new Runnable() {
            @Override
            public void run() {
                //Threadが動いてる限り回す
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // UDPパケット待ち受け
                        DatagramSocket recvUdpSocket = new DatagramSocket(port);
                        recvUdpSocket.setReuseAddress(true);
                        Crashlytics.log("UdpReceiveStandby: packet 待機");

                        buffer = new byte[2048];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                        // 受信するまでブロック
                        recvUdpSocket.receive(packet);

                        // 受信したデータをトーストで出力
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    Crashlytics.log("UdpReceiveStandby: データを受信");
                                    String result = new String(buffer, "UTF-8");

                                    //結果をトースト表示
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                                    //ダイアログを消す
                                    dialog.dismiss();
                                    Crashlytics.log("UdpReceiveStandby: ダイアログを消す");
                                    //監視しているスレッドを止める
                                    checkReceive.interrupt();
                                    Crashlytics.log("UdpReceiveStandby: スレッドを停止");
                                } catch(IOException e) {
                                    Crashlytics.log("UdpReceiveStandby: 受信後にキャッチ");
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
}