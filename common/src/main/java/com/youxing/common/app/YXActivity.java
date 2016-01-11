package com.youxing.common.app;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class YXActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YXApplication.instance().activityOnCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        YXApplication.instance().activityOnResume(this);
    }

    @Override
    protected void onPause() {
        YXApplication.instance().activityOnPause(this);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        YXApplication.instance().activityOnDestory(this);

        super.onDestroy();
    }

    // ********* URL Mapping ********** //

    public Intent urlMap(Intent intent) {
        Application app = getApplication();
        if (app instanceof YXApplication) {
            return ((YXApplication) app).urlMap(intent);
        } else {
            return intent;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent = urlMap(intent);
        intent.putExtra("_from", getMyUrl());
        intent.putExtra("_startTime", SystemClock.elapsedRealtime());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent,
                                          int requestCode) {
        intent = urlMap(intent);
        intent.putExtra("_from", getMyUrl());
        intent.putExtra("_startTime", SystemClock.elapsedRealtime());
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    public void startActivity(String urlSchema) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)));
    }

    public void startActivityForResult(String urlSchema, int requestCode) {
        startActivityForResult(
                new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)),
                requestCode);
    }

    private final static HashMap<String, String> manifestUrlMapping = new HashMap<String, String>();

    protected String getMyUrl() {
        if (getIntent().getDataString() != null)
            return getIntent().getDataString();

        String myClassName = getClass().getName();
        String manifestUrl = manifestUrlMapping.get(myClassName);
        if (manifestUrl != null)
            return manifestUrl;
        try {
            AssetManager am = createPackageContext(getPackageName(), 0)
                    .getAssets();
            XmlResourceParser xml = am
                    .openXmlResourceParser("AndroidManifest.xml");
            int eventType = xml.getEventType();
            String inActivity = null;
            boolean inIntentFilter = false;
            xmlloop: while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (inIntentFilter && myClassName.equals(inActivity)) {
                            if (xml.getName().equals("data")) {
                                String scheme = xml
                                        .getAttributeValue(
                                                "http://schemas.android.com/apk/res/android",
                                                "scheme");
                                String host = xml
                                        .getAttributeValue(
                                                "http://schemas.android.com/apk/res/android",
                                                "host");
                                if (scheme != null && host != null
                                        && !scheme.startsWith("http")) {
                                    manifestUrl = scheme + "://" + host;
                                    break xmlloop;
                                }
                            }
                        }
                        if (xml.getName().equals("activity")) {
                            inActivity = xml.getAttributeValue(
                                    "http://schemas.android.com/apk/res/android",
                                    "name");
                            if (inActivity != null && inActivity.startsWith(".")) {
                                inActivity = getPackageName() + inActivity;
                            }
                        }
                        if (xml.getName().equals("intent-filter")) {
                            inIntentFilter = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xml.getName().equals("activity")) {
                            inActivity = null;
                        }
                        if (xml.getName().equals("intent-filter")) {
                            inIntentFilter = false;
                        }
                        break;
                }
                eventType = xml.nextToken();
            }
        } catch (Exception e) {
        }

        if (manifestUrl == null) {
            manifestUrl = "class://" + myClassName;
        }
        manifestUrlMapping.put(myClassName, manifestUrl);
        return manifestUrl;
    }

    // *************** UI操作 (空方法定义，子类实现) ***************

    public void showLoadingDialog(Context context) {
    }

    public void showLoadingDialog(Context context, String msg,
                                  DialogInterface.OnCancelListener cancelListener) {
    }

    public void showDialog(Context context, String message) {
    }

    public void showDialog(Context context, String message, DialogInterface.OnClickListener okListener) {
    }

    public void showDialog(Context context, String title, String message, String ok) {
    }

    public void showDialog(Context context, String title,
                           String message, String ok,
                           DialogInterface.OnClickListener okListener, String cancel,
                           DialogInterface.OnClickListener cancelListener) {
    }

    public void dismissDialog() {
    }

}
