package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.oven.config.AlipayConfig;
import com.oven.constants.AppConstants;
import com.oven.entity.Alipay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Resource
    private AlipayConfig alipayConfig;

    @GetMapping("/pay")
    public void pay(Alipay alipay, HttpServletResponse httpResponse) throws Exception {
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getServerUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(),
                AppConstants.FORMAT,
                AppConstants.CHARSET,
                alipayConfig.getAlipayPublicKey(),
                AppConstants.SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", alipay.getTraceNo());  // 我们自己生成的订单编号
        bizContent.put("total_amount", alipay.getTotalAmount()); // 订单的总金额
        bizContent.put("subject", alipay.getSubject());   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toJSONString());

        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            log.info("调用alipay sdk异常：", e);
        }
        httpResponse.setContentType("text/html;charset=" + AppConstants.CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            log.info("=========================== >>> 支付宝异步回调");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                log.info("交易名称: {}", params.get("subject"));
                log.info("交易状态: {}", params.get("trade_status"));
                log.info("支付宝交易凭证号: {}", params.get("trade_no"));
                log.info("商户订单号: {}", params.get("out_trade_no"));
                log.info("交易金额: {}", params.get("total_amount"));
                log.info("买家在支付宝唯一id: {}", params.get("buyer_id"));
                log.info("买家付款时间: {}", params.get("gmt_payment"));
                log.info("买家付款金额: {}", params.get("buyer_pay_amount"));
            }
        }
        return "success";
    }

}
