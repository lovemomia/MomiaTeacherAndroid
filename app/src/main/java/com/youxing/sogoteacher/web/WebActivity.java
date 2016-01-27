package com.youxing.sogoteacher.web;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youxing.common.app.Constants;
import com.youxing.common.services.account.AccountService;
import com.youxing.sogoteacher.app.SGWebActivity;

import java.net.URLDecoder;

public class WebActivity extends SGWebActivity {
	private String url;
	private String title;
	private boolean openexternal;
	private boolean appendParams;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (getIntent().getData() != null) {
			url = getIntent().getData().getQueryParameter("url");
			title = getIntent().getData().getQueryParameter("title");
			String oe = getIntent().getData().getQueryParameter("openexternal");
			openexternal = "1".equals(oe) || "true".equals(oe);
			if (url != null)
				url = URLDecoder.decode(url);
		} else {
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
			openexternal = getIntent().getBooleanExtra("openExternal", false);
		}
		appendParams = getIntent().getBooleanExtra("appendParams", true);

		if (url == null)
			finish();

		webView.loadUrl(appendUrl(url));
		setTitle(title);

		// set cookie
		String domain = Constants.DEBUG ? "http://m.momia.cn" : "http://m.sogokids.com";
		if (AccountService.instance().isLogin()) {
			synCookies(this, domain, "utoken="
					+ AccountService.instance().account().getToken()
					+ "; path=/; domain=nuomi.com");
		} else {
			synCookies(this, domain,
					"utoken=; path=/; domain=nuomi.com");
		}
	}

	private String appendUrl(String url) {
		if (!appendParams) {
			return url;
		}

		if (url.contains("?")) {
			return url + "&_src=androidapp";
		}
		return url + "&_src=androidapp";
	}

	public void synCookies(Context context, String url, String cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(url, cookies);
		CookieSyncManager.getInstance().sync();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP
				&& webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected WebViewClient createWebViewClient() {
		return new MyWebViewClient();
	}

	public class MyWebViewClient extends DPWebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (openexternal) {
				if (url.equals(WebActivity.this.url))
					return false;
				openExternalUrl(url);
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}
	}
}