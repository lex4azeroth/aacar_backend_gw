package com.aawashcar.apigateway.service.impl;

import com.aawashcar.apigateway.config.WechatConfig;
import com.aawashcar.apigateway.model.WechatPayModel;
import com.aawashcar.apigateway.service.WechatPayService;
import com.aawashcar.apigateway.util.SignMD5;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class WechatPayServiceImpl implements WechatPayService {
	SignMD5 encoder = new SignMD5();
	RestTemplate restTemplate = new RestTemplate();

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
	public String unifiedorder(String notifyUrl, String openId, String body, String total, String out_trade_no) {
		WechatPayModel xml = new WechatPayModel();
		xml.setAppid(WechatConfig.getAppid());
		xml.setMch_id(WechatConfig.getMchid());
		xml.setNonce_str(encoder.createNonceStr());
		xml.setBody(body);
		xml.setOut_trade_no(out_trade_no);
		xml.setTotal_fee(total);
		xml.setSpbill_create_ip(WechatConfig.getIP());
		xml.setNotify_url(notifyUrl);
		xml.setTrade_type(WechatConfig.TRADE_TYPE);
		xml.setOpenid(openId);
		xml.sign(encoder);
		return restTemplate.postForObject(WechatConfig.UNIFIEDORDER_API, xml, String.class);
	}

	/**
	 * 调起微信支付
	 * 
	 * @param model
	 * @param res
	 *            预支付订单 字符串
	 * @param url
	 *            微信支付 url
	 */
	public void wechatPay(Model model, String res, String url) {
		try {
			Map<String, String> start = new HashMap<>();
			StringBuilder startSign = new StringBuilder();

			Map<String, String> pay = new HashMap<>();
			StringBuilder paySign = new StringBuilder();
			XmlMapper xmlMapper = new XmlMapper();
			JsonNode node = xmlMapper.readTree(res);
			if (StringUtils.equals(node.get("return_code").asText(), WechatConfig.SUCCESS)) {
				// 得到的预支付订单，重新生成微信支付参数
				String prepay_id = node.get("prepay_id").asText();
				String jsapi_ticket = jsapiTicket();
				// 生成 微信支付 config 参数
				start.put("appId", WechatConfig.getAppid());
				start.put("nonceStr", encoder.createNonceStr());
				start.put("timestamp", encoder.createTimeStamp());
				// 生成 config 签名
				startSign.append("jsapi_ticket=").append(jsapi_ticket);
				startSign.append("&noncestr=").append(start.get("nonceStr"));
				startSign.append("&timestamp=").append(start.get("timestamp"));
				startSign.append("&url=").append(url);
				start.put("signature", encoder.encode(startSign.toString()));

				// config信息验证后会执行ready方法的参数
				pay.put("signType", WechatConfig.MD5);
				pay.put("packageStr", "prepay_id=" + prepay_id);
				// 生成支付签名
				paySign.append("appId=").append(start.get("appId"));
				paySign.append("&nonceStr=").append(start.get("nonceStr"));
				paySign.append("&package=").append(pay.get("packageStr"));
				paySign.append("&signType=").append(pay.get("signType"));
				paySign.append("&timeStamp=").append(start.get("timestamp"));
				paySign.append("&key=").append(WechatConfig.getKey());
				pay.put("paySign", encoder.encode(paySign.toString()));
				// 将微信支参数放入 model 对象中以便前端使用
				model.addAttribute("start", start);
				model.addAttribute("pay", pay);
			}
		} catch (Exception e) {
			model.addAttribute("wechatMessage", "微信授权失败!");
			e.printStackTrace();
		}
	}
	
//	public void wechatPayTest(Model model, String res) {
//		try {
//			Map<String, String> start = new HashMap<>();
//			StringBuilder startSign = new StringBuilder();
//
//			Map<String, String> pay = new HashMap<>();
//			StringBuilder paySign = new StringBuilder();
//			XmlMapper xmlMapper = new XmlMapper();
//			JsonNode node = xmlMapper.readTree(res);
//			if (StringUtils.equals(node.get("return_code").asText(), WechatConfig.SUCCESS)) {
//				// 得到的预支付订单，重新生成微信支付参数
//				String prepay_id = node.get("prepay_id").asText();
//				String jsapi_ticket = jsapiTicket();
//				// 生成 微信支付 config 参数
//				start.put("appId", WechatConfig.getAppid());
//				start.put("nonceStr", encoder.createNonceStr());
//				start.put("timestamp", encoder.createTimeStamp());
//				// 生成 config 签名
//				startSign.append("jsapi_ticket=").append(jsapi_ticket);
//				startSign.append("&noncestr=").append(start.get("nonceStr"));
//				startSign.append("&timestamp=").append(start.get("timestamp"));
//				startSign.append("&url=").append(url);
//				start.put("signature", encoder.encode(startSign.toString()));
//
//				// config信息验证后会执行ready方法的参数
//				pay.put("signType", WechatConfig.MD5);
//				pay.put("packageStr", "prepay_id=" + prepay_id);
//				// 生成支付签名
//				paySign.append("appId=").append(start.get("appId"));
//				paySign.append("&nonceStr=").append(start.get("nonceStr"));
//				paySign.append("&package=").append(pay.get("packageStr"));
//				paySign.append("&signType=").append(pay.get("signType"));
//				paySign.append("&timeStamp=").append(start.get("timestamp"));
//				paySign.append("&key=").append(WechatConfig.getKey());
//				pay.put("paySign", encoder.encode(paySign.toString()));
//				// 将微信支参数放入 model 对象中以便前端使用
//				model.addAttribute("start", start);
//				model.addAttribute("pay", pay);
//			}
//		} catch (Exception e) {
//			model.addAttribute("wechatMessage", "微信授权失败!");
//			e.printStackTrace();
//		}
//	}

	/**
	 * 微信授权，获取 access_token
	 * 
	 * @return access_token
	 */
	public String getToken() {
		if (WechatConfig.checkToken()) {
			// 声明 获取 access_token 路径
			StringBuilder tokenBuilder = new StringBuilder();
			tokenBuilder.append(WechatConfig.TOKEN_API);
			tokenBuilder.append("&appid=").append(WechatConfig.getAppid());
			tokenBuilder.append("&secret=").append(WechatConfig.getAppSecret());
			// 获取 token
			Map<?, ?> token = restTemplate.getForObject(tokenBuilder.toString(), Map.class);
			WechatConfig.setToken((String) token.get("access_token"));
		}
		return WechatConfig.getToken();
	}

	/**
	 * 获取微信 JSAPI 支付的临时票据
	 * 
	 * @return 临时票据
	 */
	public String jsapiTicket() {
		if (WechatConfig.checkJsapiTicket()) {
			// 声明 获取临时票据路径
			StringBuilder ticketBuilder = new StringBuilder();
			ticketBuilder.append(WechatConfig.TICKET_API);
			ticketBuilder.append("?access_token=").append(getToken());
			ticketBuilder.append("&type=jsapi");
			// 获取 临时票据
			Map<?, ?> jsapiTicket = restTemplate.getForObject(ticketBuilder.toString(), Map.class);
			WechatConfig.setJsapiTicket((String) jsapiTicket.get("ticket"));
		}
		return WechatConfig.getJsapiTicket();
	}

	/**
	 * 获取用的 OPENID
	 * 
	 * @param code
	 *            微信认证回调的 code
	 * @return
	 */
	public String takeOpenId(String code) {
		// 声明 获取OPENID路径
		StringBuilder openidBuilder = new StringBuilder();
		openidBuilder.append(WechatConfig.OPENID_API);
		openidBuilder.append("?appid=").append(WechatConfig.getAppid());
		openidBuilder.append("&secret=").append(WechatConfig.getAppSecret());
		openidBuilder.append("&code=").append(code);
		openidBuilder.append("&grant_type=authorization_code");
		// 获取 OPENID
		String res = restTemplate.getForObject(openidBuilder.toString(), String.class);
		Map<?, ?> map = JSONArray.parseObject(res, Map.class);
		return String.valueOf(map.get("openid"));
	}
}
