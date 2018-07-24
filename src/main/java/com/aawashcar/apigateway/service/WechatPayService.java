package com.aawashcar.apigateway.service;

import org.springframework.ui.Model;

public interface WechatPayService {

	/**
	 * 微信支付统一下单接口
	 * 
	 * @param notifyUrl
	 *            支付成功后回调路径
	 * @param openId
	 *            用户的 openId
	 * @param body
	 *            商品描述
	 * @param total
	 *            支付金额（单位分）
	 * @param out_trade_no
	 *            订单唯一订单号
	 * @return
	 */
	public String unifiedorder(String notifyUrl, String openId, String body, String total, String out_trade_no);

	/**
	 * 调起微信支付
	 * 
	 * @param model
	 * @param res
	 *            预支付订单 字符串
	 * @param url
	 *            微信支付 url
	 */
	public void wechatPay(Model model, String res, String url);

	/**
	 * 微信授权，获取 access_token
	 * 
	 * @return access_token
	 */
	public String getToken();
	
    /**
     * 获取微信 JSAPI 支付的临时票据
     * 
     * @return 临时票据
     */
	public String jsapiTicket();
	
    /**
     * 获取用的 OPENID
     * 
     * @param code 微信认证回调的 code
     * @return
     */
    public String takeOpenId(String code);
    
//    public String wechatPayTest(String res);
}
