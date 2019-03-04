package com.t_robop.yuusuke.a01_spica_android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.t_robop.yuusuke.a01_spica_android.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpReceive extends Thread{
    private int port;
    private Context context;
    private Handler handler = new Handler();
    private Handler handlerDismiss = new Handler();
    private Dialog dialog;
    private byte[] buffer;
    private Thread checkReceive;

    final int KILL_DIALOG_TIME = 2000;

    private int udpPort;
    private boolean waiting;


    public UdpReceive(Context context){
        udpPort = 50000;
        this.context = context;
    }


    public void createRecycleUdpSocket(){
        waiting = true;
        new Thread() {
            @Override
            public void run(){
                String address = null;
                try {
                    //waiting = trueの間、ブロードキャストを受け取る
                    while(waiting){
                        //受信用ソケット
                        DatagramSocket receiveUdpSocket = new DatagramSocket(udpPort);
                        byte[] buf = new byte[256];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        //ゲスト端末からのブロードキャストを受け取る
                        //受け取るまでは待ち状態になる
                        receiveUdpSocket.receive(packet);
                        //受信バイト数取得
                        int length = packet.getLength();
                        //受け取ったパケットを文字列にする
                        address = new String(buf, 0, length);
                        //↓③で使用
                        receiveUdpSocket.close();
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void UdpReceiveStandby(){
        Crashlytics.log("UdpReceiveStandby");
        //ダイアログの設定
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.receive_udp_dialog);
        dialog.setCancelable(false);
        final Button cancelButton = dialog.findViewById(R.id.receive_udp_cancel_button);
        final TextView text = dialog.findViewById(R.id.receive_udp_text);
        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Runnable dismiss = new Runnable() {
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        text.setText(R.string.udp_receive_cancel_dialog);
                        cancelButton.setVisibility(View.GONE);

                        UdpSend udp = new UdpSend();
                        udp.UdpSendText(context.getString(R.string.udp_receive_esp_reboot_command));

                        //監視しているスレッドを止める
                        checkReceive.interrupt();

                        handlerDismiss.postDelayed(dismiss, KILL_DIALOG_TIME);
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
                        final DatagramSocket recvUdpSocket = new DatagramSocket(port);
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
                                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                                    recvUdpSocket.close();

                                    //監視しているスレッドを止める
                                    checkReceive.interrupt();
                                    Crashlytics.log("UdpReceiveStandby: スレッドを停止");
                                } catch(IOException e) {
                                    Crashlytics.log("UdpReceiveStandby: 受信後にキャッチ");
                                } finally {
                                    //ダイアログを消す
                                    dialog.dismiss();
                                    Crashlytics.log("UdpReceiveStandby: ダイアログを消す");
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
