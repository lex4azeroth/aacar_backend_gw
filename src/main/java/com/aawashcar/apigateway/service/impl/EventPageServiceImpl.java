package com.aawashcar.apigateway.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.config.WechatConfig;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.PromotionTransaction;
import com.aawashcar.apigateway.entity.PromotionWeixinTransaction;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponse;
import com.aawashcar.apigateway.model.WechatPayResponseModel;
import com.aawashcar.apigateway.service.EventPageService;
import com.aawashcar.apigateway.service.WechatPayService;
import com.aawashcar.apigateway.util.EntityMapper;
import com.aawashcar.apigateway.util.WXPayUtil;
import com.aawashcar.apigateway.util.XMLUtil;

@Service()
public class EventPageServiceImpl extends BaseService implements EventPageService {
	@Autowired
	private WechatPayService payService;

	@Override
	public List<PromotionModel> listEvents() {
		String url = promUrlPrefix + "promotion/listall";

		ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
		Promotion[] promotions = promotionResponseEntity.getBody();

		return EntityMapper.converPromotionToModel(promotions);
	}

	@Override
	public WechatPayResponseModel purchasePromotion(PromotionModel promotion, String validId) {
		// get user id by openid
		String url = crmUrlPrefix + "user/" + validId;
		User user = restTemplate.getForObject(url, User.class);
		if (user == null) {
			// log error
			WechatPayResponseModel response = new WechatPayResponseModel();
			response.setResult_code("ERROR");
			response.setReturn_msg("User not found");
			return response;
		}

		int userId = user.getId();

		return getUnifiedOrder(validId, userId, promotion.getId(), promotion.getPrice());
	}

	private int recordPromotionTransaction(int userId, int promotionId, double price) {
		// promotiontransaction/{promotionid}/{userid}/{price}
		// find existing promotion transaction by user id, promotion id and
		// price
		String url = promUrlPrefix + "promotion/promotiontransaction/" + String.valueOf(promotionId) + "/"
				+ String.valueOf(userId) + "/" + String.valueOf(price);
		int promotionTransactionId = restTemplate.getForObject(url, Integer.class).intValue();

		if (promotionTransactionId == 0) {
			// not found, add new one
			url = promUrlPrefix + "promotion/promotiontransaction/new";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			PromotionTransaction promotionTransaction = new PromotionTransaction();
			promotionTransaction.setUserId(userId);
			promotionTransaction.setPromotionId(promotionId);
			promotionTransaction.setPrice(price);
			promotionTransaction.setPaied(false);
			HttpEntity<PromotionTransaction> entity = new HttpEntity<PromotionTransaction>(promotionTransaction,
					headers);
			promotionTransactionId = restTemplate.postForObject(url, entity, Integer.class).intValue();
		}

		return promotionTransactionId;
	}

	private WechatPayResponseModel getUnifiedOrder(String validId, int userId, int promotionId, double price) {

		int promotionTransactionId = recordPromotionTransaction(userId, promotionId, price);

		String notify = "https://www.aawashcar.com/event/wechatnotifypromotion/"
				+ String.valueOf(promotionTransactionId);
		String out_trade_no = new Date().getTime() + "";

		String body = "AA洗车套餐号：" + String.valueOf(promotionId);
		DecimalFormat df = new DecimalFormat();
		String total = df.format(price * 100);

		String res = payService.unifiedorder(notify, validId, body, "1", out_trade_no);
		System.out.println(res);
		Object object = XMLUtil.xmlToBean(WechatPayResponse.class, res);

		// TODO check object returns succeed

		WechatPayResponseModel responseModel = null;
		if (object instanceof WechatPayResponse) {
			WechatPayResponse wechatPay = (WechatPayResponse) object;

			String timeStamp = System.currentTimeMillis() / 1000 + "";// 时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
			String nonceStr = wechatPay.getNonce_str();
			String _package = "prepay_id=" + wechatPay.getPrepay_id(); // 统一下单接口返回的
																		// prepay_id
																		// 参数值，提交格式如：prepay_id=*
			String signType = "MD5";// 签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
			Map<String, String> signMap = new TreeMap<String, String>();
			signMap.put("appId", wechatPay.getAppid());
			signMap.put("timeStamp", timeStamp);
			signMap.put("nonceStr", nonceStr);
			signMap.put("package", _package);
			signMap.put("signType", signType);
			String paySign = null;
			try {
				paySign = WXPayUtil.generateSignature(signMap, WechatConfig.getKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;// 签名,具体签名方案参见微信公众号支付帮助文档;

			responseModel = new WechatPayResponseModel();
			responseModel.setTimeStamp(timeStamp);
			responseModel.setAppid(wechatPay.getAppid());
			responseModel.setMch_id(wechatPay.getMch_id());
			responseModel.setNonce_str(wechatPay.getNonce_str());
			responseModel.setPrepay_id(wechatPay.getPrepay_id());
			responseModel.setResult_code(wechatPay.getResult_code());
			responseModel.setReturn_code(wechatPay.getReturn_code());
			responseModel.setTrade_type(wechatPay.getTrade_type());
			responseModel.setReturn_msg(wechatPay.getReturn_msg());
			responseModel.setSign(paySign);
		}

		return responseModel;
	}

	@Override
	public void proccessWechatNotification(WechatNotify notify, int promotionTransactionId) {
		// find promotion id , user id, from prom_promotion_transaction by
		// promotionTransactionId
		String url = promUrlPrefix + "/promotion/purchase/" + String.valueOf(promotionTransactionId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		PromotionWeixinTransaction weixinTransaction = new PromotionWeixinTransaction();
		weixinTransaction.setAppid(notify.getAppid());
		weixinTransaction.setAttach(notify.getAttach());
		weixinTransaction.setBank_type(notify.getBank_type());
		weixinTransaction.setFee_type(notify.getFee_type());
		weixinTransaction.setIs_subscribe(notify.getIs_subscribe());
		weixinTransaction.setMch_id(notify.getMch_id());
		weixinTransaction.setNonce_str(notify.getNonce_str());
		weixinTransaction.setOpenid(notify.getOpenid());
		weixinTransaction.setOut_trade_no(notify.getOut_trade_no());
		weixinTransaction.setResult_code(notify.getResult_code());
		weixinTransaction.setReturn_code(notify.getReturn_code());
		weixinTransaction.setSign(notify.getSign());
		weixinTransaction.setSub_mch_id(notify.getSub_mch_id());
		weixinTransaction.setTime_end(notify.getTime_end());
		weixinTransaction.setTotal_fee(notify.getTotal_fee());
		weixinTransaction.setTrade_type(notify.getTrade_type());
		weixinTransaction.setTransaction_id(notify.getTransaction_id());
		HttpEntity<PromotionWeixinTransaction> entity = new HttpEntity<PromotionWeixinTransaction>(weixinTransaction, headers);
		boolean result = restTemplate.postForObject(url, entity, Boolean.class);
		
		
//		PromotionTransaction transactionPromotion = restTemplate.getForObject(url, PromotionTransaction.class);
//		
//		if (transactionPromotion == null) {
//			// log error
//		}
//		
		// add new relation between user and promotion in prom_r_user_promotion

		// add new relation among user, promotion, service in
		// prom_r_user_consume_service

	}
}
