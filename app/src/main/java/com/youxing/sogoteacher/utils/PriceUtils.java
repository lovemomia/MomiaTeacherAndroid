package com.youxing.sogoteacher.utils;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class PriceUtils {

    public static String formatPriceString(double price) {
        String s = String.valueOf(price);
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}
