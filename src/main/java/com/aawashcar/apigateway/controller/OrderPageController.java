package com.aawashcar.apigateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static Logger logger = LogManager.getLogger(OrderPageController.class);

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

	@RequestMapping(value = "listordersindays/{days}", method = RequestMethod.GET)
	public List<OrderDetailWithWasherModel> listOrdersInDays(@PathVariable("days") int days) {
		return orderPageService.listOrderDetailsInDays(days);
	}

	@RequestMapping(value = "pricing/{validid}/{serviceid}/{couponid}/{promotionid}/{originprice}/{vehicletypeid}/{vehiclecategoryid}", method = RequestMethod.GET)
	public Pricing pricing(@PathVariable("validid") String validId,
			@PathVariable("serviceid") String serviceIds, @PathVariable("couponid") int couponId,
			@PathVariable("promotionid") int promotionId, @PathVariable("originprice") double originPrice,
			@PathVariable("vehicletypeid") int vehicleTypeId,
			@PathVariable("vehiclecategoryid") int vehicleCategoryId) {
		return orderPageService.pricing(validId, serviceIds, couponId, promotionId, originPrice, vehicleTypeId,
				vehicleCategoryId);
	}

	@RequestMapping(value = "pay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public WechatPayResponseModel pay(@RequestBody Pricing pricingToPay) {
		System.out.println("origin price = " + pricingToPay.getOriginalPrice() + " discounted price = " + pricingToPay.getDiscountedPrice() + 
				" promtion id = " + pricingToPay.getPromotionId());
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
		StringBuilder sb = new StringBuilder("WechatNotifingOrder....\n");
		sb.append("OrderID: " + String.valueOf(orderId) + "\n");
		// 从 request 对象中获取 WechatNotify 对象
		WechatNotify notify = WechatUtil.getNotifyBean(request);

		sb.append("appid:" + notify.getAppid() + "\n");
		sb.append("attach:" + notify.getAttach() + "\n");
		sb.append("bank_type:" + notify.getBank_type() + "\n");
		sb.append("fee_type:" + notify.getFee_type() + "\n");
		sb.append("is_subscribe:" + notify.getIs_subscribe() + "\n");
		sb.append("mch_id:" + notify.getMch_id() + "\n");
		sb.append("nonce_str:" + notify.getNonce_str() + "\n");
		sb.append("openid:" + notify.getOpenid() + "\n");
		sb.append("out_trade_no:" + notify.getOut_trade_no() + "\n");
		sb.append("sign:" + notify.getSign());
		sb.append("sub_mch_id:" + notify.getSub_mch_id() + "\n");
		sb.append("time_end:" + notify.getTime_end() + "\n");
		sb.append("total_fee:" + notify.getTotal_fee() + "\n");
		sb.append("trade_type:" + notify.getTrade_type() + "\n");
		sb.append("transaction_id:" + notify.getTransaction_id() + "\n");
		
		System.out.println(sb.toString());
		logger.info(sb.toString());
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
