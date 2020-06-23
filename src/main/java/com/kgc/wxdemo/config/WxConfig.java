package com.kgc.wxdemo.config;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @version v1.0
 * @ProjectName: wxdemo
 * @ClassName: WxConfig
 * @Description: TODO(一句话描述该类的功能)
 * @Author: 李茜骏
 * @Date: 2020/6/15 21:33
 */
@Component
public class WxConfig implements WXPayConfig {
    //AppId
    @Override
    public String getAppID() {
        return "";
    }
    //商品id
    @Override
    public String getMchID() {
        return "";
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 8000;
    }
}
