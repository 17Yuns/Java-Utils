package com.yiqiyuns.WebUtils;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import okhttp3.Callback;

import java.io.IOException;



/**
 * HTTP 请求工具类
 * 用于发送 GET、POST、PUT、DELETE、PATCH 请求
 *
 * @author 17Yuns
 * @version 1.0
 */
@SuppressWarnings("all")
public class RequestsUtils {
    private final OkHttpClient okHttpClient;

    // 构造方法
    public RequestsUtils() {
        this.okHttpClient = new OkHttpClient.Builder()
                .build();
    }

    // 回调接口，用于通知请求结果
    public interface ResponseCallback {
        void onResponse(String result);
        void onFailure(IOException e);
    }

    /**
     * 发送 GET 请求
     *
     * @param url      请求 URL
     * @param headers  请求头
     * @param callback 回调接口
     */
    public void sendGetRequest(String url, Headers headers, ResponseCallback callback) {
        sendRequest(url, headers, null, "GET", callback);
    }

    /**
     * 发送 POST 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @param callback    回调接口
     */
    public void sendPostRequest(String url, Headers headers, RequestBody requestBody, ResponseCallback callback) {
        sendRequest(url, headers, requestBody, "POST", callback);
    }

    /**
     * 发送 PUT 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @param callback    回调接口
     */
    public void sendPutRequest(String url, Headers headers, RequestBody requestBody, ResponseCallback callback) {
        sendRequest(url, headers, requestBody, "PUT", callback);
    }

    /**
     * 发送 DELETE 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体（可选）
     * @param callback    回调接口
     */
    public void sendDeleteRequest(String url, Headers headers, RequestBody requestBody, ResponseCallback callback) {
        sendRequest(url, headers, requestBody, "DELETE", callback);
    }

    /**
     * 发送 PATCH 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @param callback    回调接口
     */
    public void sendPatchRequest(String url, Headers headers, RequestBody requestBody, ResponseCallback callback) {
        sendRequest(url, headers, requestBody, "PATCH", callback);
    }

    /**
     * 通用的请求处理方法
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @param method      请求方法（GET、POST、PUT、DELETE、PATCH）
     * @param callback    回调接口
     */
    private void sendRequest(String url, Headers headers, RequestBody requestBody, String method, ResponseCallback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .headers(headers);

        // 根据请求方法选择适当的请求类型
        switch (method) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(requestBody);
                break;
            case "PUT":
                requestBuilder.put(requestBody);
                break;
            case "DELETE":
                if (requestBody != null) {
                    requestBuilder.delete(requestBody);
                } else {
                    requestBuilder.delete();
                }
                break;
            case "PATCH":
                requestBuilder.patch(requestBody);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // 异步请求
        okHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                // 请求失败，通知回调
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                try {
                    // 判断响应是否成功
                    if (response.isSuccessful() && response.body() != null) {
                        // 成功响应，返回数据
                        callback.onResponse(response.body().string());
                    } else {
                        // 响应失败
                        callback.onFailure(new IOException("Unexpected code " + response));
                    }
                } finally {
                    // 确保关闭响应体，避免资源泄漏
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * 发送同步 GET 请求
     *
     * @param url     请求 URL
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String sendSyncGetRequest(String url, Headers headers) throws IOException {
        return sendSyncRequest(url, headers, null, "GET");
    }

    /**
     * 发送同步 POST 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String sendSyncPostRequest(String url, Headers headers, RequestBody requestBody) throws IOException {
        return sendSyncRequest(url, headers, requestBody, "POST");
    }

    /**
     * 发送同步 PUT 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String sendSyncPutRequest(String url, Headers headers, RequestBody requestBody) throws IOException {
        return sendSyncRequest(url, headers, requestBody, "PUT");
    }

    /**
     * 发送同步 DELETE 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体（可选）
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String sendSyncDeleteRequest(String url, Headers headers, RequestBody requestBody) throws IOException {
        return sendSyncRequest(url, headers, requestBody, "DELETE");
    }

    /**
     * 发送同步 PATCH 请求
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String sendSyncPatchRequest(String url, Headers headers, RequestBody requestBody) throws IOException {
        return sendSyncRequest(url, headers, requestBody, "PATCH");
    }

    /**
     * 通用的同步请求处理方法
     *
     * @param url         请求 URL
     * @param headers     请求头
     * @param requestBody 请求体
     * @param method      请求方法
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    private String sendSyncRequest(String url, Headers headers, RequestBody requestBody, String method) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .headers(headers);

        // 根据请求方法选择适当的请求类型
        switch (method) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(requestBody);
                break;
            case "PUT":
                requestBuilder.put(requestBody);
                break;
            case "DELETE":
                if (requestBody != null) {
                    requestBuilder.delete(requestBody);
                } else {
                    requestBuilder.delete();
                }
                break;
            case "PATCH":
                requestBuilder.patch(requestBody);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // 执行请求
        try (Response response = okHttpClient.newCall(requestBuilder.build()).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}
