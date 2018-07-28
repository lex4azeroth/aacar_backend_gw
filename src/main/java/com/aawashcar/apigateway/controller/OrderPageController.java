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
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponseModel;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.util.WechatUtil;

@RequestMapping("order/")
@ResponseBody
@Controller
public class OrderPageController {

	@Autowired
	private OrderPageService orderPageService;

	@RequestMapping(value = "mylist/{validid}/{size}", method = RequestMethod.GET)
	public List<OrderSummaryModel> myOrderSummaries(@PathVariable("validid") String validId,
			@PathVariable("size") int size) {

		return orderPageService.myOrderSummaryList(validId, size);

	}

	@RequestMapping(value = "detail/{orderid}/{validid}", method = RequestMethod.GET)
	public OrderDetailModel myOrderDetail(@PathVariable("orderid") int id, @PathVariable("validid") String validId) {
		return orderPageService.myOrderDetail(id, validId);
	}

	@RequestMapping(value = "detailwithwasher/{orderid}", method = RequestMethod.GET)
	public OrderDetailWithWasherModel myOrderDetail(@PathVariable("orderid") int id) {
		return orderPageService.orderDetailWithWasher(id);
	}

	@RequestMapping(value = "listall", method = RequestMethod.GET)
	public List<OrderDetailWithWasherModel> listAll() {
		return orderPageService.listAllOrderDetails();
	}

	@RequestMapping(value = "pricing/{validid}/{orderid}/{couponid}/{promotionid}", method = RequestMethod.GET)
	public Pricing pricing(@PathVariable("validid") String validId, @PathVariable("orderid") int orderId,
			@PathVariable("couponid") int couponId, @PathVariable("promotionid") int promotionId) {
		return orderPageService.pricing(validId, orderId, couponId, promotionId);
	}

	@RequestMapping(value = "pay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public WechatPayResponseModel pay(@RequestBody Pricing pricingToPay) {
		return orderPageService.pay(pricingToPay);
	}

	/**
	 * 微信支付成功后的回调函数
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wechatnotifyorder/{orderid}", method = RequestMethod.POST)
	public String wechatNotifyOrder(@PathVariable("orderid") int orderId, HttpServletRequest request) {
		System.out.println("WechatNotifing....");
		System.out.println(String.valueOf(orderId));
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

			orderPageService.proccessWechatNotification(notify, orderId);
		} else {
			notifyStatus = WechatConfig.NOTIFY_FAIL;
		}

		return notifyStatus;
	}
}
