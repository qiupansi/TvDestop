package com.diyo.activity.desktop.weather;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    private static URL url = null;
    private String filePath = null;
    private String urlPath = null;
    /**
     * 根据URL下载文件,前提是这个文件当中的内容是文�?函数的返回�?就是文本当中的内�?
     * 1.创建URL对象
     * 2.通过URL对象,创建HttpURLConnection对象
     * 3.得到InputStream
     * 4.从InputStream当中读取数据
     * @param urlStr
     * @return
     */
    public static String getContent(String urlStr){
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(getInputStreamFromURL(urlStr), "UTF-8"), 100000);
            while( (line = buffer.readLine()) != null){
                sb.append(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 根据URL得到输入流
     * @param urlStr
     * @return
     */
    public static InputStream getInputStreamFromURL(String urlStr) {
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        try {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection)url.openConnection();
            inputStream = urlConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}  
