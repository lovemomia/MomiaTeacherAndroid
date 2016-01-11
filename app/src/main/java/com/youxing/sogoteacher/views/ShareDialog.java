package com.youxing.sogoteacher.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youxing.common.app.Constants;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 分享对话框
 *
 * Created by Jun Deng on 15/8/28.
 */
public class ShareDialog extends AlertDialog implements View.OnClickListener {

    private String url;
    private String title;
    private String abstracts;
    private String thumb;

    public ShareDialog(Context context, String url, String title, String abstracts, String thumb) {
        super(context);
        this.url = url;
        this.title = title;
        this.abstracts = abstracts;
        this.thumb = thumb;
    }

    @Override
    public void show() {
        super.show();
        super.setContentView(R.layout.layout_share_dialog);
        setContentView(R.layout.layout_share_dialog);
        findViewById(R.id.wechat_share_friend).setOnClickListener(this);
        findViewById(R.id.wechat_share_timeline).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wechat_share_friend) {
            shareToWechat(0);
            dismiss();
        } else {
            shareToWechat(1);
            dismiss();
        }
    }

    /**
     * @param flag 0:微信好友  1:微信朋友圈
     */
    private void shareToWechat(int flag) {
        // 微信分享
        final IWXAPI api = WXAPIFactory.createWXAPI(getContext(), Constants.WECHAT_APP_ID);
        if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
            ((SGActivity)getContext()).showDialog(getContext(), "使用微信分享功能需要您安装最新的微信客户端");
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = abstracts;
        if (!TextUtils.isEmpty(thumb)) {
            Bitmap bitmap = getNetBitmap(thumb);
            if (bitmap != null) {
                msg.setThumbImage(bitmap);
            }
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    public static Bitmap getNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 4 * 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 4 * 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

}
