package com.gameplat.admin.util;

import com.gameplat.base.common.util.HttpRespBean;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    private static final String DEFAULT_ENCODE = "UTF-8";
    private String httpUrl;
    private boolean isPost;
    private String encode = "UTF-8";
    private StringBuilder paraBuffer = new StringBuilder();
    private Map<String, String> reqHead = new HashMap();

    private HttpClient() {
    }

    public String getEncode() {
        return this.encode;
    }

    public HttpClient setEncode(String encode) {
        this.encode = encode;
        return this;
    }

    public  HttpClient get(String url) {
        return this.setReqMethod(url, false, "UTF-8");
    }

    public HttpClient get(String url, String encode) {
        return this.setReqMethod(url, false, encode);
    }

    public HttpClient post(String url) {
        return this.setReqMethod(url, true, "UTF-8");
    }

    public HttpClient post(String url, String encode) {
        return this.setReqMethod(url, true, encode);
    }

    private HttpClient setReqMethod(String url, boolean isPost, String encode) {
        this.isPost = isPost;
        this.httpUrl = url;
        this.paraBuffer.delete(0, this.paraBuffer.length());
        this.reqHead.clear();
        if (encode == null || "".equals(encode)) {
            encode = "UTF-8";
        }

        this.encode = encode;
        return this;
    }

    public HttpClient addHead(String name, String value) {
        this.reqHead.put(name, value);
        return this;
    }

    private HttpURLConnection connect(String uri) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(uri);
        urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        this.setHead(urlConnection);
        urlConnection.setConnectTimeout(20000);
        urlConnection.setReadTimeout(20000);
        return urlConnection;
    }

    private void postData(HttpURLConnection urlConnection) throws IOException {
        PrintWriter out = null;

        try {
            out = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), this.encode));
            out.write(this.paraBuffer.toString());
        } catch (IOException var7) {
            throw new IOException("post请求异常", var7);
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }

        }

    }

    private void setHead(HttpURLConnection urlConnection) {
        Iterator iter = this.reqHead.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            urlConnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
        }

    }

    public HttpRespBean execute() {
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        String content = "";
        int statusCode = -1;
        if (this.httpUrl != null && !"".equals(this.httpUrl)) {
            try {
                if (this.paraBuffer.length() > 0 && this.paraBuffer.charAt(this.paraBuffer.length() - 1) == '&') {
                    this.paraBuffer.deleteCharAt(this.paraBuffer.length() - 1);
                }

                //LogUtil.debug(this.httpUrl + "?" + this.paraBuffer.toString());
                String uri = this.httpUrl;
                if (!this.isPost) {
                    uri = this.httpUrl + (this.paraBuffer.length() > 0 ? "?" + this.paraBuffer.toString() : "");
                }

                urlConnection = this.connect(uri);
                if (this.isPost) {
                    this.postData(urlConnection);
                }

                statusCode = urlConnection.getResponseCode();
                content = this.readData(urlConnection.getInputStream());
            } catch (Exception var17) {
                //LogUtil.error("请求异常:url=" + this.httpUrl + ",statusCode=" + statusCode + ", content=" + content, var17);

                try {
                    if (urlConnection != null) {
                        statusCode = urlConnection.getResponseCode();
                        content = this.readData(urlConnection.getErrorStream());
                    }
                } catch (IOException var16) {
                    statusCode = -1;
                    var16.printStackTrace();
                    //LogUtil.error("读取http返回错误信息异常", var16);
                }
            } finally {
                if (in != null) {
                    try {
                        ((BufferedReader)in).close();
                        in = null;
                    } catch (IOException var15) {
                        //LogUtil.error("http请求finally出错:" + var15);
                    }
                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            //LogUtil.debug("状态码:" + statusCode + "返回内容:" + content);
            return new HttpRespBean(statusCode, content);
        } else {
           // LogUtil.info("请求地址为空!");
            return null;
        }
    }

    public HttpRespBean sslexecute() {
        String result = null;
        int statusCode = 0;
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = new TrustManager[]{new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init((KeyManager[])null, tm, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            String uri = this.httpUrl;
            if (this.paraBuffer.length() > 0 && this.paraBuffer.charAt(this.paraBuffer.length() - 1) == '&') {
                this.paraBuffer.deleteCharAt(this.paraBuffer.length() - 1);
            }

            if (!this.isPost) {
                uri = this.httpUrl + (this.paraBuffer.length() > 0 ? "?" + this.paraBuffer.toString() : "");
            }

            URL url = new URL(uri);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            String method = this.isPost ? "POST" : "GET";
            httpUrlConn.setRequestMethod(method);
            if ("GET".equalsIgnoreCase(method)) {
                httpUrlConn.connect();
            }

            OutputStream outputStream;
            if (this.paraBuffer.length() > 0) {
                outputStream = httpUrlConn.getOutputStream();
                outputStream.write(this.paraBuffer.toString().getBytes("UTF-8"));
                outputStream.close();
            }

            statusCode = httpUrlConn.getResponseCode();
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;

            while((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            result = buffer.toString();
        } catch (Exception var15) {
            statusCode = -1;
            var15.printStackTrace();
            //LogUtil.error("读取http返回错误信息异常", var15);
        }

        return new HttpRespBean(statusCode, result);
    }

    public String getReqUrl() {
        return this.httpUrl + "?" + this.paraBuffer.toString().substring(0, this.paraBuffer.length() - 1);
    }

    private String readData(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, this.encode));

        String line;
        while((line = in.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

    public HttpClient addPara(String key, Object value) {
        if (key != null && !"".equals(key)) {
            try {
                if (value != null && value.getClass().isArray()) {
                    Object[] values = (Object[])((Object[])value);

                    for(int i = 0; i < values.length; ++i) {
                        this.setPara(key, values[i]);
                    }
                } else {
                    this.setPara(key, value);
                }
            } catch (UnsupportedEncodingException var5) {
                //LogUtil.warn("编码解析异常");
            }
        }

        return this;
    }

    public HttpClient setPara(Map<String, String> paras) {
        Iterator var2 = paras.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            this.addPara(key, paras.get(key));
        }

        return this;
    }

    private void setPara(String key, Object value) throws UnsupportedEncodingException {
        this.paraBuffer.append(key).append("=");
        if (value != null) {
            this.paraBuffer.append(URLEncoder.encode(value.toString(), this.encode));
        }

        this.paraBuffer.append("&");
    }

    public HttpClient postPara(String data) {
        if (data != null && !"".equals(data)) {
            this.paraBuffer.append(data);
        }

        return this;
    }

    public static HttpClient build() {
        return new HttpClient();
    }
}
