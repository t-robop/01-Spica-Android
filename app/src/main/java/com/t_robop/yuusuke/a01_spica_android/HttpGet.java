package com.t_robop.yuusuke.a01_spica_android;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuusuke on 2018/05/25.
 */

public class HttpGet {

    final String HTTP_STR_FRONT = "http://";
    final String HTTP_STR_BACK = "/?0=";
    String ip;
    String command;

    Request request;
    void setRequest(String ip,String command){
        request = new Request.Builder().url(HTTP_STR_FRONT + ip + HTTP_STR_BACK + command).build();
    }
    void setIpAddress(String ip) {
        this.ip = ip;
        setRequest(this.ip,this.command);
    }
    void setCommand(String command) {
        this.command = command;
        setRequest(this.ip,this.command);
    }

    void sendHttp(){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
            @Override
            public void onFailure(Call call, IOException arg1) {

            }
        });
    }
}
