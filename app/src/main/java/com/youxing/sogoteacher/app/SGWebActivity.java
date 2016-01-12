package com.youxing.sogoteacher.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.youxing.sogoteacher.R;

public class SGWebActivity extends SGActivity {

	private static final String TAG = SGWebActivity.class.getSimpleName();

	protected WebView webView;
	protected FrameLayout mask;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setupView();
		webView = (WebView) findViewById(R.id.webview);
		mask = (FrameLayout) findViewById(R.id.mask);

		WebSettings settings = webView.getSettings();
		setupWebSettings(settings);
		webView.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);
		WebChromeClient wcc = createWebChromeClient();
		if (wcc != null)
			webView.setWebChromeClient(wcc);
		WebViewClient wvc = createWebViewClient();
		if (wvc != null)
			webView.setWebViewClient(wvc);
	}

	protected void setupView() {
		setContentView(R.layout.activity_web);
	}

	protected void setupWebSettings(WebSettings settings) {
		settings.setBuiltInZoomControls(false);
		settings.setSaveFormData(false);
		settings.setSavePassword(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(false);
		settings.setUseWideViewPort(true);
	}

	protected WebChromeClient createWebChromeClient() {
		return new DPWebChromeClient();
	}

	protected WebViewClient createWebViewClient() {
		return new DPWebViewClient();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
	}

	protected void openExternalUrl(String url) {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);
		} catch (Exception e) {
			Toast.makeText(this, "无法打开链接\n" + url, Toast.LENGTH_SHORT).show();
		}
	}

	public class DPWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (newProgress < 100) {
			}
			setTitle(view.getTitle());
		}
	}

	public class DPWebViewClient extends WebViewClient {
		private long startMillis = 0;
		private long errorMillis = 0;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

			startMillis = SystemClock.uptimeMillis();
			Log.i(TAG, "WEB: " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			long ds = SystemClock.uptimeMillis() - errorMillis;
			if (ds > 0 && ds < 200)
				return;

			setTitle(webView.getTitle());
			if (mask != null) {
				mask.setVisibility(View.GONE);
				mask.removeAllViews();
			}
			webView.setVisibility(View.VISIBLE);

			long elapse = SystemClock.uptimeMillis() - startMillis;
			Log.i(TAG, "WEB ELAPSE: " + elapse + "ms");
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			errorMillis = SystemClock.uptimeMillis();

			webView.setVisibility(View.INVISIBLE);
			mask.removeAllViews();
			getLayoutInflater().inflate(R.layout.layout_error_item, mask, true);
			mask.findViewById(R.id.btn_retry).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mask.setVisibility(View.GONE);
							webView.reload();
						}
					});
			String errorMsg = "服务暂时不可用，请稍候再试";
			try {
				ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if (ni == null || !ni.isConnected()) {
					errorMsg = "无法连接到服务，请检查网络连接是否可用";
				}
			} catch (Exception e) {
			}
			((TextView) mask.findViewById(android.R.id.text1))
					.setText(errorMsg);
			mask.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (!(url.startsWith("http://") || url.startsWith("https://"))) {
				openExternalUrl(url);
				return true;
			} else if (url.startsWith("http://maps.google.com/")
					|| url.startsWith("http://www.youtube.com/")
					|| url.startsWith("http://market.android.com/")) {
				openExternalUrl(url);
				return true;
			} else if (url.contains("&tag=external")
					|| url.contains("?tag=external")) {
				openExternalUrl(url);
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

}
