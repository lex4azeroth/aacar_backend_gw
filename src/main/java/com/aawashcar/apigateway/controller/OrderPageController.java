package com.aawashcar.apigateway.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.config.WechatConfig;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.service.WechatPayService;
import com.aawashcar.apigateway.util.WechatUtil;

@RequestMapping("order/")
@ResponseBody
@Controller
public class OrderPageController {

	@Autowired
	private OrderPageService orderPageService;

	@Autowired
	private WechatPayService payService;

	@Autowired
	private RestTemplate restTemplate;

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

	@RequestMapping(value = "deal/", method = RequestMethod.POST)
	public boolean deal() {
		//

		return false;
	}

	/**
	 * 微信支付统一下单接口(https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1&index=1)
	 * 小程序调起支付API(https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/unifiedOrder/{validid}/{body}/{total}", method = RequestMethod.GET)
	public String getUnifiedOrder(@PathVariable("validid") String validId, @PathVariable("body") String body,
			@PathVariable("total") String total) {

		// String openId = payService.takeOpenId(code);
		if (StringUtils.isEmpty(validId)) {
			return "pay/wxpay_error";
		}
		String notify = "https://www.aawashcar.com/order/wechatnotify";
		// String body = "商品支付内容";
		// String total = "1"; // 单位分
		String out_trade_no = new Date().getTime() + "";

		String res = payService.unifiedorder(notify, validId, body, total, out_trade_no);
		return res;
	}

	/**
	 * 微信支付成功后的回调函数
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wechatnotify", method = RequestMethod.POST)
	public String wechatNotify(HttpServletRequest request) {
		System.out.println("WechatNotifing....");
		// 从 request 对象中获取 WechatNotify 对象
		WechatNotify notify = WechatUtil.getNotifyBean(request);
		// 如果 notify 对象不为空 并且 result_code 和 return_code 都为 'SUCCESS' 则表示支付成功
		if (notify != null && WechatConfig.SUCCESS.equals(notify.getResult_code())
				&& WechatConfig.SUCCESS.equals(notify.getReturn_code())) {

			return WechatConfig.NOTIFY_SUCCESS;
		}
		return WechatConfig.NOTIFY_FAIL;
	}
}
