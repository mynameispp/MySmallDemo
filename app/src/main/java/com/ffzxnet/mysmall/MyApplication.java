package com.ffzxnet.mysmall;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.wequick.small.Small;
import net.wequick.small.webkit.WebView;
import net.wequick.small.webkit.WebViewClient;

/**
 * 创建者： feifan.pi 在 2017/4/7.
 */

public class MyApplication extends Application {
//    public MyApplication() {
//        Small.preSetUp(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        Small.preSetUp(this);
//        Small.setBaseUri("http://code.wequick.net/small-sample/");
//        Small.setWebViewClient(new MyWebViewClient());
//        Small.setLoadFromAssets(BuildConfig.LOAD_FROM_ASSETS);
    }

    private static final class MyWebViewClient extends WebViewClient {

        private ProgressBar mBar;

        @Override
        public void onPageStarted(Context context, WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(Context context, WebView view, String url) {

        }

        @Override
        public void onReceivedError(Context context, WebView view, int errorCode, String description, String failingUrl) {

        }

        @Override
        public void onProgressChanged(Context context, WebView view, int newProgress) {
            super.onProgressChanged(context, view, newProgress);

            final ViewGroup parent = (ViewGroup) view.getParent();
            if (mBar == null) {
                mBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        5);
                parent.addView(mBar, lp);
            }

            if (newProgress == 100) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = mBar.getProgress();
                        while (progress <= 100) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            mBar.setProgress(progress++);
                            mBar.postInvalidate();
                        }

                        parent.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                parent.removeView(mBar);
                                mBar = null;
                            }
                        }, 300);
                    }
                }).start();
            } else {
                mBar.setProgress(newProgress);
            }
        }
    }

}
