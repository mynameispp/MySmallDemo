package com.ffzxnet.mysmall.up_plugin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import net.wequick.small.Small;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 更新，下载插件
 * 创建者： feifan.pi 在 2017/4/7.
 */

public class UpgradeManager {

    private interface OnResponseListener {
        void onResponse(UpgradeInfo info);
    }

    private interface OnUpgradeListener {
        void onUpgrade(boolean succeed);
    }

    private static class ResponseHandler extends Handler {
        private OnResponseListener mListener;

        public ResponseHandler(OnResponseListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mListener.onResponse((UpgradeInfo) msg.obj);
                    break;
            }
        }
    }

    private ResponseHandler mResponseHandler;

    private Context mContext;
    private ProgressDialog mProgressDlg;

    public UpgradeManager(Context context) {
        mContext = context;
    }

    /**
     * 检查更新
     */
    public void checkUpgrade() {
        mProgressDlg = ProgressDialog.show(mContext, "Small", "Checking for updates...");
        requestUpgradeInfo(Small.getBundleVersions(), new OnResponseListener() {
            @Override
            public void onResponse(UpgradeInfo info) {
                mProgressDlg.setMessage("Upgrading...");
                upgradeBundles(info,
                        new OnUpgradeListener() {
                            @Override
                            public void onUpgrade(boolean succeed) {
                                mProgressDlg.dismiss();
                                mProgressDlg = null;
                                String text = succeed ?
                                        "更新成功! Switch to background and back to foreground to see changes."
                                        : "更新失败!";
                                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    /**
     * @param versions
     * @param listener
     */
    private void requestUpgradeInfo(Map versions, OnResponseListener listener) {
        System.out.println(versions); // this should be passed as HTTP parameters
        mResponseHandler = new ResponseHandler(listener);
        new Thread() {
            @Override
            public void run() {
                try {
                    // 请求后台数据
                    URL url = new URL("http://wequick.github.io/small/upgrade/bundles.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    InputStream is = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        sb.append(new String(buffer, 0, length));
                    }

                    // 解析后台返回的数据
                    JSONObject jo = new JSONObject(sb.toString());
                    JSONObject mf = jo.has("manifest") ? jo.getJSONObject("manifest") : null;
                    JSONArray updates = jo.getJSONArray("updates");//插件更新信息
                    int N = updates.length();
                    List<UpdateInfo> infos = new ArrayList<UpdateInfo>(N);
                    for (int i = 0; i < N; i++) {
                        JSONObject o = updates.getJSONObject(i);
                        UpdateInfo info = new UpdateInfo();
                        info.packageName = o.getString("pkg");
                        info.downloadUrl = o.getString("url");
                        infos.add(info);
                    }

                    // 准备更新下载插件
                    UpgradeInfo ui = new UpgradeInfo();
                    ui.manifest = mf;
                    ui.updates = infos;
                    Message.obtain(mResponseHandler, 1, ui).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //下载监听
    private static class DownloadHandler extends Handler {
        private OnUpgradeListener mListener;

        public DownloadHandler(OnUpgradeListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mListener.onUpgrade((Boolean) msg.obj);
                    break;
            }
        }
    }

    private DownloadHandler mHandler;

    private void upgradeBundles(final UpgradeInfo info,
                                final OnUpgradeListener listener) {
        // Just for example, you can do this by OkHttp or something.
        mHandler = new DownloadHandler(listener);
        new Thread() {
            @Override
            public void run() {
                try {
                    // 更新 manifest
                    if (info.manifest != null) {
                        if (!Small.updateManifest(info.manifest, false)) {
                            Message.obtain(mHandler, 1, false).sendToTarget();
                            return;
                        }
                    }
                    // 下载插件
                    List<UpdateInfo> updates = info.updates;
                    for (UpdateInfo u : updates) {
                        // 获取要更新的.so文件存储的路径
                        net.wequick.small.Bundle bundle = Small.getBundle(u.packageName);
                        File file = bundle.getPatchFile();

                        // 开始下载
                        URL url = new URL(u.downloadUrl);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        InputStream is = urlConn.getInputStream();
                        OutputStream os = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) != -1) {
                            os.write(buffer, 0, length);
                        }
                        os.flush();
                        os.close();
                        is.close();

                        // 更新插件配置
                        bundle.upgrade();
                    }

                    Message.obtain(mHandler, 1, true).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message.obtain(mHandler, 1, false).sendToTarget();
                }
            }
        }.start();
    }
}
