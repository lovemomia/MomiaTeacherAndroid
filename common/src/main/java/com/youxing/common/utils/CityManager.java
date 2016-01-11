package com.youxing.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.youxing.common.app.YXApplication;
import com.youxing.common.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理器
 *
 * Created by Jun Deng on 15/8/27.
 */
public class CityManager {

    private static final String TAG = "CityManager";

    private Context context;
    private List<CityChangeListener> listenerList = new ArrayList<CityChangeListener>();
    private City choosedCity;

    public CityManager(Context context) {
        this.context = context;
        this.choosedCity = readCity();
    }

    private static CityManager instance;
    public static CityManager instance() {
        if (instance == null) {
            instance = new CityManager(YXApplication.instance());
        }
        return instance;
    }

    public void addListener(CityChangeListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(CityChangeListener listener) {
        listenerList.remove(listener);
    }

    public void setChoosedCity(City city) {
        if (city.getId() == this.choosedCity.getId()) {
            return;
        }
        this.choosedCity = city;
        saveCity(city);
        for (CityChangeListener listener : listenerList) {
            listener.onCityChanged(city);
        }
    }

    public City getChoosedCity() {
        return choosedCity;
    }

    private City readCity() {
        SharedPreferences sp = sharedPreferences();
        String json = sp.getString("choosedCity", "");
        City city = null;
        if (TextUtils.isEmpty(json)) {
            city = defaultCity();
            saveCity(city);
            return city;
        }
        try {
            city = JSON.parseObject(json, City.class);
        } catch (Exception e) {
            Log.e(TAG, "read choosedCity fail", e);
            return defaultCity();
        }
        return city;
    }

    private City defaultCity() {
        City defaultCity = new City();
        defaultCity.setId(1);
        defaultCity.setName("上海");
        return defaultCity;
    }

    private void saveCity(City city) {
        if (city != null) {
            sharedPreferences().edit().putString("choosedCity", JSON.toJSONString(city)).commit();
        } else {
            sharedPreferences().edit().remove("choosedCity").commit();
        }
    }

    private SharedPreferences sharedPreferences() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public interface CityChangeListener {
        void onCityChanged(City newCity);
    }

}


