package me.xlgp.douyinzimu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.designpatterns.ObserverHelper;

public class HttpURLConnectionUtil {

    public static Disposable asyncGet(String httpUrl, Consumer<List<String>> consumer) {
        return Observable.just(httpUrl).map(HttpURLConnectionUtil::doGetList)
                .compose(ObserverHelper.transformer()).subscribe(consumer);
    }

    public static List<String> doGetList(String httpUrl) {
        List<String> list = new ArrayList<>();
        doGet(httpUrl, list);
        return list;
    }

    public static String doGet(String httpUrl, List<String> list) {
        //链接
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();

        if (list == null) list = new ArrayList<>();

        try {
            //创建连接
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setReadTimeout(15000);
            //开始连接
            connection.connect();
            //获取响应数据
            if (connection.getResponseCode() == 200) {
                //获取返回的数据
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp = null;
                    list.clear();
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                        list.add(temp);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭远程连接
            if (connection != null) {
                connection.disconnect();
            }

        }
        return result.toString();
    }
}
