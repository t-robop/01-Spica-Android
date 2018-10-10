package com.t_robop.yuusuke.a01_spica_android.util;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class UdpSendOld {
    private AsyncDatagramSocket socket;
    private String ip = "0.0.0.0";
    private int port = 10000;
    private ByteBuffer textBuffer;


    public UdpSendOld() {
        try {
            socket = AsyncServer.getDefault().openDatagram();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void disConnect() {
        try {
            socket.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsyncServer.getDefault().stop();
    }
    public void setIpAddress(String ip) {
        this.ip = ip;
    }
    public void setSendText(String text) throws UnsupportedEncodingException {
        byte[] strByte = text.getBytes("UTF-8");
        this.textBuffer = ByteBuffer.wrap(strByte);
    }
    public void setPort(int port) {
        this.port = port;
    }

    public void send(){
        socket.send(ip, port, textBuffer);
    }

}
