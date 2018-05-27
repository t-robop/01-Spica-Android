package com.t_robop.yuusuke.a01_spica_android;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class UdpSend {
    private AsyncDatagramSocket socket;
    private String ip = "0.0.0.0";
    private int port = 10000;
    private ByteBuffer textBuffer;


    UdpSend() {
        try {
            socket = AsyncServer.getDefault().openDatagram();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void disConnect() {
        try {
            socket.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsyncServer.getDefault().stop();
    }
    void setIpAddres(String ip) {
        this.ip = ip;
    }
    void setSendText(String text) throws UnsupportedEncodingException {
        byte[] strByte = text.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.wrap(strByte);
        this.textBuffer = buffer;
    }
    void setPort(int port) {
        this.port = port;
    }

    void send(){
        socket.send(ip, port, textBuffer);
    }

}
