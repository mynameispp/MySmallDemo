package com.ffzxnet.mysmall.up_plugin;

import org.json.JSONObject;

import java.util.List;

/**
 * 插件配置文件信息
 * 创建者： feifan.pi 在 2017/4/7.
 */

public class UpgradeInfo {
    public JSONObject manifest;
    public List<UpdateInfo> updates;
}
