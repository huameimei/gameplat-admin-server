package com.gameplat.admin.util.lottery;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUtils {

    /**
     * Get请求
     *
     * @param url 请求路径
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * Get请求
     *
     * @param url    请求路径
     * @param params 请求参数
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, null);
    }

    /**
     * Get请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        checkUrl(url);
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("httpGet请求目标网址异常", e);
        }

        if (params != null && !params.isEmpty()) {
            uriBuilder.setParameters(covertParams(params));
        }

        URI uri = null;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("httpGet请求参数异常", e);
        }
        HttpGet httpGet = new HttpGet(uri);
        addHeaders(httpGet, headers);

        return execute(httpGet);
    }


    /**
     * postForm 请求
     *
     * @param url 请求路径
     */
    public static String postForm(String url) {
        return postForm(url, null);
    }

    /**
     * postForm 请求
     *
     * @param url    请求路径
     * @param params 请求参数
     */
    public static String postForm(String url, Map<String, String> params) {
        return postForm(url, params, null);
    }

    /**
     * postForm 请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) {
        checkUrl(url);
        HttpPost httpPost = new HttpPost(url);

        if (params != null && !params.isEmpty()) {
            ArrayList<NameValuePair> pairs = covertParams(params);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, UTF_8);
            httpPost.setEntity(entity);
        }

        addHeaders(httpPost, headers);
        return execute(httpPost);
    }


    /**
     * postJson 请求
     *
     * @param url 请求路径
     */
    public static String postJson(String url) {
        return postJson(url, null);
    }

    /**
     * postJson 请求
     *
     * @param url       请求路径
     * @param jsonParam 请求参数
     */
    public static String postJson(String url, String jsonParam) {
        return postJson(url, jsonParam, null);
    }

    /**
     * postJson 请求
     *
     * @param url       请求路径
     * @param jsonParam 请求参数
     * @param headers   请求头
     */
    public static String postJson(String url, String jsonParam, Map<String, String> headers) {
        checkUrl(url);
        HttpPost httpPost = new HttpPost(url);

        if (jsonParam != null && !"".equals(jsonParam)) {
            StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }

        addHeaders(httpPost, headers);
        return execute(httpPost);
    }


    /**
     * 真正执行请求
     */
    private static String execute(HttpRequestBase httpRequest) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(15000).setSocketTimeout(15000).setConnectTimeout(15000).build();
        httpRequest.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            if (response != null) {
                return EntityUtils.toString(response.getEntity(), UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("http 请求异常", e);
        } finally {
            responseClose(response);
            httpClientClose(httpClient);
        }
        return null;
    }

    /**
     * 增加请求头
     */
    private static void addHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
        // 伪装成网页请求
        httpRequest.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpRequest.addHeader(header.getKey(), header.getValue());
            }
        }
    }

    /**
     * 参数转换
     */
    private static ArrayList<NameValuePair> covertParams(Map<String, String> params) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        return nameValuePairs;
    }

    /**
     * 检查目标网址
     */
    private static void checkUrl(String url) {
        String http = "http";
        if (url == null || !url.startsWith(http)) {
            throw new RuntimeException("目标网址不正确");
        }
    }

    /**
     * 释放 httpClient
     */
    private static void httpClientClose(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 释放 response
     */
    private static void responseClose(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
