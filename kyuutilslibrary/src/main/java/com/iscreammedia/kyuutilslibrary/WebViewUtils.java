
package com.iscreammedia.kyuutilslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 웹뷰 클래스
 */
public class WebViewUtils extends FrameLayout {

	private static final String TAG = WebViewUtils.class.getSimpleName();

	/**
	 * 웹 로딩 시 프로그래스를 띄울지 여부의 Listener
	 */
	public interface WebProgressListener{
		void onProgressShow(boolean showProgress);
	}

	/**
	 * Web 의 Alert를 띄울지 여부의 Listener
	 */
	public interface WebAlertListener{
		void onAlerted(String url, String message, JsResult result);
	}

	/**
	 * 사운드 재생 리스너
	 */
	public interface OnLoadSoundFileListener {
		void OnLoadSoundFile(String fileName);
	}

	private Context context;
	public WebView web;
	private ProgressBar progress;

	private CusWebClient cusWebClient = null;
	private CusWebChromeViewClient chromeViewClient = null;

	private WebProgressListener progressListener = null;	// Custom 프로그래스 D/P 용 리스너
	private WebAlertListener alertLister = null;			// JSAlert 알림 D/P 용 리스너
	private OnLoadSoundFileListener soundLoadListener; // 사운드 재생 리스너

	private boolean isClearHistory = false;

	public WebViewUtils(Context context) {
		super(context);
		init(context);
	}

	public WebViewUtils(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WebViewUtils(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 웹 로딩 시 프로그래스 이벤트를 받을 경우 지정
	 * @param listener 리스너
	 */
	public void setProgressListener(WebProgressListener listener) {
		progressListener = listener;
	}

	/**
	 * 웹 Alert 이벤트를 받을 경우 지정
	 * @param listener 리스너
	 */
	public void setAlertListener(WebAlertListener listener) {
		alertLister = listener;
	}

	/**
	 * 사운드 재생을 할 경우 지정
	 * @param listener 리스너
	 */
	public void setOnLoadSoundFileListener(OnLoadSoundFileListener listener) {
		soundLoadListener = listener;
	}

	/**
	 * 초기화
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;

		web = new WebView(context);

		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setBuiltInZoomControls(false);

		web.getSettings().setSupportZoom(false);
		web.getSettings().setAppCacheEnabled(true);
		cusWebClient = new CusWebClient();
		web.setWebViewClient(cusWebClient);
		chromeViewClient = new CusWebChromeViewClient();
		web.setWebChromeClient(chromeViewClient);

		web.setInitialScale(1);
		web.getSettings().setLoadWithOverviewMode(true);
		web.getSettings().setUseWideViewPort(true);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		web.setVerticalScrollBarEnabled(false);
		web.setHorizontalScrollBarEnabled(false);

		progress = new ProgressBar(context);
		LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;

		// deprecated issue 수정 - beksung : FILL_PARENT => MATCH_PARENT로 수정
		this.addView(web, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.addView(progress, lp);

		web.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
	}

	public WebSettings getSetting() {
		return web.getSettings();
	}

	/**
	 * 자바스크립트 인터페이스 지정
	 * @param obj 오브젝트
	 */
	@SuppressLint("JavascriptInterface")
	public void setJavascriptInterface(Object obj) {
		web.addJavascriptInterface(obj, "android");
	}
	
	/**
	 * UWebView 종료 시 반드시 호출되어야 함
	 */
	public void close() {
		if (cusWebClient != null) {
			cusWebClient = null;
		}

		if (chromeViewClient != null) {
			chromeViewClient = null;
		}

		if (progress != null) {
			progress = null;
		}

		if (web != null) {
			web.destroy();
			web = null;
		}
	}

	/**
	 * URL Web 호출 시 사용
	 * @param url url
	 */
	public void loadUrl(String url) {
		web.loadUrl(url);
	}

	/**
	 * 이전 화면으로 이동 시 호출
	 * @return canGoBack boolean 값
	 */
	public boolean backWeb() {
		if (web.canGoBack()) {
			// web.goBack();
			try {
				web.goBack();
			} catch (Exception e) {
				LogUtils.e(null, TAG, "Exeption during web.goBack()");
			}
			return true;
		}
		return false;
	}

	/**
	 * 이전 History를 삭제할 경우 호출
	 */
	public void clearHistory(){
		
		isClearHistory = true;
		
		if (web != null) {
			web.clearHistory();			
		}
	}

	/**
	 * WebViewClient 를 상속받는 커스텀 웹뷰 클라이언트
	 */
	public class CusWebClient extends WebViewClient {
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			// 페이지 로드 완료 시 History 지우도록 한다.
			if (isClearHistory) {
				if (web != null) {
					web.clearHistory();			
				}
				isClearHistory = false;
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			if (url.startsWith("tel:")) {
				Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				context.startActivity(i);
				return true;
			} else if (url.startsWith("mailto:")) {
				url = url.replaceFirst("mailto:", "");
				url = url.trim();
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("plain/text").putExtra(Intent.EXTRA_EMAIL, new String[]{url});
				context.startActivity(i);
				return true;
			} else if(url.startsWith("file:")) {
				if(soundLoadListener != null) {
					String fileName = url.replaceAll("file:///", "");
					
					fileName = fileName.trim() + ".mp3";
					
					soundLoadListener.OnLoadSoundFile(fileName.trim());
				}
				
				return true;
		    } else {
				web.loadUrl(url);
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			if(progress!=null && progress.isShown()){
				progress.setVisibility(View.GONE);
			} 
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	/**
	 * WebChromeClient를 상속받는 커스텀 WebChromeClient
	 */
	public class CusWebChromeViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				// 외부 프로그래스 숨기기
				if (progressListener != null) {
					progressListener.onProgressShow(false);
				} 
				
				if(progress!=null && progress.isShown()){
					progress.setVisibility(View.GONE);
				} 
				web.setClickable(true);
				web.setVisibility(View.VISIBLE);
				web.requestFocus();
			} else {
				// 외부 프로그래스 보이기
				if (progressListener != null) {
					progressListener.onProgressShow(true);
				} else { 
					if(progress!=null && !progress.isShown()){
						progress.setVisibility(View.VISIBLE);
					}
				}
				web.setClickable(false);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			
			if (alertLister != null) {
				alertLister.onAlerted(url, message, result);
			} else {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				result.confirm();
			}
			return true;
		}
	}
}
