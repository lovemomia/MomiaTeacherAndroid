package com.youxing.common.utils;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class SignTool {

    /**
     * 对一段字符串签名并返回签名结果
     */
    public static String sign(String source) {
        return sign(source, 0);
    }

    /**
     * 根据业务id，对一段字符串签名并返回签名结果
     */
    public static String sign(String source, int custom) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        String signed = null;
        if (NativeTool.A) {
            signed = NativeTool.ne(source.getBytes(), custom);
        }
        return signed;
    }

    /**
     * 签名指定表单参数，返回签名结果，并自动添加签名
     *
     * @param forms
     * @return 签名结果
     */
    public static void sign(List<NameValuePair> forms) {
        if (forms == null || forms.isEmpty()) {
            return;
        }

        List<String> keys = new ArrayList<String>();
        Map<String, String> kv = new HashMap<String, String>();
        for (NameValuePair pair : forms) {
            if (!pair.getName().equals("logdata")) {
                keys.add(pair.getName());
                kv.put(pair.getName(), pair.getValue());
            }
        }

        Collections.sort(keys);

        StringBuilder sysSign = new StringBuilder();
        for (String key : keys) {
            String value = kv.get(key);
            if (value == null) {
                continue;
            }
            sysSign.append(key + "=" + kv.get(key));
        }

        String signStr = sysSign.toString() + "key=";
        Log.d("sign", signStr);

        String sighed = sign(signStr);
        forms.add(new BasicNameValuePair("sign", sighed));
    }

}
