package com.gp.jswebview;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private FrameLayout content;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //调用js无参无返回值函数
                    String call = "javascript:javaCallJS()";
                    myWebView.loadUrl(call);
                    return true;
                case R.id.navigation_dashboard:
                    //调用有参无返回值
                    String call1 = "javascript:javaCallJSWithArgs(\"" + "fromJava" + "\")";
                    myWebView.loadUrl(call1);
                    return true;
                case R.id.navigation_notifications:
                    //调用有参有返回值
                    evaluateJavaScript(myWebView,"your");
                    return true;
            }
            return false;
        }

    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void evaluateJavaScript(WebView webView, String ss){
        webView.evaluateJavascript("getYourCar(\"" + ss + "\")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        content = (FrameLayout) findViewById(R.id.content);
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setAppCacheEnabled(true);
        myWebView.addJavascriptInterface(new JsInterface(this), "jscalljava");
        myWebView.setWebChromeClient(new WebChromeClient() {});
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

        });

        myWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult hitTestResult = myWebView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    new AlertDialog.Builder(MainActivity.this)
                            .setItems(new String[]{"查看大图", "保存图片到相册"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String picUrl = hitTestResult.getExtra();
                                    //获取图片
                                    Log.e("picUrl", picUrl);
                                    switch (which) {
                                        case 0:
                                            break;
                                        case 1:
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        });

        myWebView.loadUrl("file:///android_asset/test.html");
    }

    @Override
    protected void onPause() {
        super.onPause();
        myWebView.onPause();
        myWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myWebView.onResume();
        myWebView.resumeTimers();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    private void destroyWebView() {
        synchronized (this) {
            if (content != null) {
                content.removeView(myWebView);
            }
            if (myWebView != null) {
                myWebView.removeAllViews();
                myWebView.destroy();
            }
        }
    }
}
