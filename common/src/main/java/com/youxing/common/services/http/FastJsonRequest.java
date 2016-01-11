package com.youxing.common.services.http;

import com.alibaba.fastjson.JSON;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.youxing.common.services.cache.CacheService;
import com.youxing.common.utils.Log;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 采用fastjson库对结果进行解析的请求类型
 *
 * Created by Jun Deng on 15/6/3.
 */
public class FastJsonRequest<T> extends Request<T> {

    private String url;
    private CacheType cacheType;
    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    private final Map<String, String> headers;

    public FastJsonRequest(int method, String url, CacheType cacheType, Class<T> clazz, Map<String,
            String> headers, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.url = url;
        this.cacheType = cacheType;
        this.clazz = clazz;
        this.listener = listener;
        this.headers = headers;
    }

    public static FastJsonRequest get(String url, CacheType cacheType, Class clazz, Map<String,
            String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        return new FastJsonRequest(Method.GET, url, cacheType, clazz, headers, listener, errorListener);
    }

    public static FastJsonRequest post(String url, Class clazz, Map<String,
            String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        return new FastJsonRequest(Method.POST, url, null, clazz, headers, listener, errorListener);
    }

    @Override
    protected void deliverResponse(T response) {
        this.listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            if (cacheType == CacheType.NORMAL) {
                CacheService.instance().put(url, response.data);
            }

            String json = new String(response.data, "UTF-8");
            Log.d("http", json);

            T object = (T) json;
            if (clazz == null || clazz == String.class) {
                return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
            }

            object = JSON.parseObject(json, clazz);
            return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }


}
