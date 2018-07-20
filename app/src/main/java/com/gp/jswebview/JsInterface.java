package com.gp.jswebview;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by guoping on 2017/5/8.
 */

public class JsInterface {
    private Context mContext;

    public JsInterface(Context context){
        this.mContext = context;
    }

    /**
     * js调有返回的android 方法
     * @return
     */
    @JavascriptInterface
    public String getMessage(){
        return "这句话来自安卓";
    }

    /**
     * js调有返回的android 方法
     * @return
     */
    @JavascriptInterface
    public String exchangeMessage(String message){
        if ("来自js".equals(message)) {
            message = "来自安卓";
        }
        return message;
    }

    /**
     * js调没返回的android 方法
     * @return
     */
    @JavascriptInterface
    public void toastSomething(String s){
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }
}
