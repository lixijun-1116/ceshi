package com.kgc.wxdemo.controller;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.kgc.wxdemo.config.WxConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: wxdemo
 * @ClassName: WxPayController
 * @Description: TODO(一句话描述该类的功能)
 * @Author: 李茜骏
 * @Date: 2020/6/15 21:35
 */
@Controller
public class WxPayController {

    @Resource
    private WxConfig wxConfig;

    private WXPay wxPay;

    @Resource
    public WXPay setWxPay(WXPayConfig wxPay){
        return this.wxPay = new WXPay(wxConfig);
    }

    //点击购买，生成二维码
    //提供我们的信息，通过微信的方法，得到二维码连接
    @RequestMapping(value = "/createQcCode",produces = "application/json")
    public String createQcCode(HttpServletRequest request) throws Exception {
        String orderNo=createOrderNo();

        //我们的信息
        Map<String,String> data = new HashMap<>();
        //商品名称
        data.put("body","shkhshksd");
        //商品价格
        data.put("total_fee","2");
        //订单编号
        data.put("out_trade_no",orderNo);
        //支付过程去的地址
        data.put("notify_url","123465");
        //使用的方式
        data.put("trade_type","NATIVE");
        //订单号
        data.put("product_id","123");

        //你的信息给他，他加工之后，在给你一些信息
        Map<String, String > resp = wxPay.unifiedOrder(data);
        if (resp.get("return_code").equals(WXPayConstants.SUCCESS)){
            if (resp.get("result_code").equals(WXPayConstants.SUCCESS)){

                //二维码连接
                String code = resp.get("code_url");

                //生成订单，添加数据库


                request.setAttribute("code_url",code);
                request.setAttribute("orderNo",orderNo);
                return "pay";
            }else {
                request.setAttribute("result_code","result_code有问题");
                return "error";
            }
        }else {
            request.setAttribute("errorInfo","return_code有问题");
            return "error";
        }

    }

    @RequestMapping(value = "/notifyAction")
    @ResponseBody
    public String notifyAction(HttpServletRequest request) throws Exception {
        //方法很简单，微信把支付结果给我们发过来，再要一个我们的确认
        //麻烦的地方是给我们的内容是xml，他要的也是xml
        //所以，先把xml换成map,用完以后，再把我们要回传的写成map,转为xml

        //接收这个内容为xml的字符产
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        StringBuilder builder = new StringBuilder();
        String str = null;
        while ((str=reader.readLine()) != null){
            builder.append(str);
        }
        //收到的信息
        String info = builder.toString();
        //这就是他给我们传到支付结果
        Map<String, String> result = WXPayUtil.xmlToMap(info);
        //返回结果验证
        boolean flag = wxPay.isPayResultNotifySignatureValid(result);
        if (flag){
            String returnCode = result.get("return_code");
            System.out.println("return_code"+returnCode);
            if (returnCode.equals(WXPayConstants.SUCCESS)){
                String resultCode = result.get("result_code");
                System.out.println("result_code"+resultCode);
                if (resultCode.equals(WXPayConstants.SUCCESS)){
                    //说明那个人支付成功了
                    //处理我们的业务
                    String orderNo = result.get("out_trade_no");
                    //组织数据，告诉微信收到了
                    Map<String,String> answer = new HashMap<>();
                    answer.put("out_trade_no",orderNo);
                    answer.put("return_msg","OK");
                    answer.put("return_code","SUCCESS");
                    return WXPayUtil.mapToXml(answer);
                }
            }
        }
        return "";
    }



    public String createOrderNo(){
        String rand = (int)Math.random()*100000+"";
        String format = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        return rand + format;
    }
}
