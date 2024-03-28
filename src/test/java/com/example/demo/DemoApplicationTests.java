package com.example.demo;

import com.example.demo.model.InputOutputData;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {
    @Test
    public void testSerialization() throws InvalidProtocolBufferException {
        InputOutputData.Input input = InputOutputData.Input.newBuilder()
                .setName("老大")
                .setAge(18)
                .build();
        System.out.println("***********序列化*************");
        byte[] bytes = input.toByteArray();
        System.out.println("bytes length is " + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }

        System.out.println("***********反序列化***********");
        InputOutputData.Input input2 = InputOutputData.Input.parseFrom(bytes);
        System.out.println(input2.getName());
    }

    @Test
    public void testHttp() {
        InputOutputData.Input input = InputOutputData.Input.newBuilder()
                .setName("老大")
                .setAge(18)
                .build();

        InputOutputData.Output output =  httpPost(input.toByteArray(), "http://127.0.0.1:8087/test");
        if(output != null){
            System.out.println(output.getResult());
            System.out.println(output);
        }
    }

    private InputOutputData.Output httpPost(byte[] bytes, String url) {
        InputOutputData.Output output = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/x-protobuf");
            conn.connect();
            out = conn.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
            // 反序列化
            in = conn.getInputStream();
            byte[] bytesRe = toByteArray(in);
            output = InputOutputData.Output.parseFrom(bytesRe);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return output;
    }

    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
