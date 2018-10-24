package com.aawashcar.apigateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.config.WechatConfig;
import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.model.PromotionWithServicesModel;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponseModel;
import com.aawashcar.apigateway.service.EventPageService;
import com.aawashcar.apigateway.util.WechatUtil;

@RequestMapping("event/")
@ResponseBody
@Controller
public class EventPageController {

	@Autowired
	private EventPageService eventPageService;
	
	@RequestMapping(value = "promotionlist", method = RequestMethod.GET)
	public List<PromotionModel> listPromotions() {
		return eventPageService.listEvents();
	}
	
	@RequestMapping(value = "promotionwithserviceslist", method = RequestMethod.GET)
	public List<PromotionWithServicesModel> listPromotionWithServices() {
		return eventPageService.listEventsWithServices();
	}
	
	@RequestMapping(value = "mypromotionwithserviceslist/{validid}", method = RequestMethod.GET)
	public List<PromotionWithServicesModel> listAvailablePromotionWithServices(@PathVariable("validid") String validId) {
		return eventPageService.listAvailableEventsWithServices(validId);
	}
	
	@RequestMapping(value = "mypromotionlist/{validid}", method = RequestMethod.GET)
	public List<PromotionModel> listAvailablePromotions(@PathVariable("validid") String validId) {
		return eventPageService.listAvailableEvents(validId);
	}
	
	@RequestMapping(value = "purchase/promotion/{validid}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public WechatPayResponseModel purchasePromotion(@PathVariable("validid") String validId, @RequestBody PromotionModel promotion) {
		return eventPageService.purchasePromotion(promotion, validId);
	}
	
	@RequestMapping(value = "wechatnotifypromotion/{promotionTransactionId}", method = RequestMethod.POST)
	public String wechatNotifyOrder(@PathVariable("promotionTransactionId") int promotionTransactionId, HttpServletRequest request) {
		System.out.println("WechatNotifingPromotion....");
		System.out.println("Promotion Transaction ID:" + String.valueOf(promotionTransactionId));
		// 从 request 对象中获取 WechatNotify 对象
		WechatNotify notify = WechatUtil.getNotifyBean(request);

		System.out.println("appid:" + notify.getAppid());
		System.out.println("attach:" + notify.getAttach());
		System.out.println("bank_type:" + notify.getBank_type());
		System.out.println("fee_type:" + notify.getFee_type());
		System.out.println("is_subscribe:" + notify.getIs_subscribe());
		System.out.println("mch_id:" + notify.getMch_id());
		System.out.println("nonce_str:" + notify.getNonce_str());
		System.out.println("openid:" + notify.getOpenid());
		System.out.println("out_trade_no:" + notify.getOut_trade_no());
		System.out.println("sign:" + notify.getSign());
		System.out.println("sub_mch_id:" + notify.getSub_mch_id());
		System.out.println("time_end:" + notify.getTime_end());
		System.out.println("total_fee:" + notify.getTotal_fee());
		System.out.println("trade_type:" + notify.getTrade_type());
		System.out.println("transaction_id:" + notify.getTransaction_id());

		String notifyStatus = WechatConfig.NOTIFY_SUCCESS;
		// 如果 notify 对象不为空 并且 result_code 和 return_code 都为 'SUCCESS' 则表示支付成功
		if (notify != null && WechatConfig.SUCCESS.equals(notify.getResult_code())
				&& WechatConfig.SUCCESS.equals(notify.getReturn_code())) {

			eventPageService.proccessWechatNotification(notify, promotionTransactionId);
		} else {
			notifyStatus = WechatConfig.NOTIFY_FAIL;
		}

		return notifyStatus;
	}
}
