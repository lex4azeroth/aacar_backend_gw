package com.aawashcar.apigateway.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.aawashcar.apigateway.model.WechatPayResponse;
import com.aawashcar.apigateway.model.WechatPayResponseModel;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.service.WechatPayService;
import com.aawashcar.apigateway.util.WXPayUtil;
import com.aawashcar.apigateway.util.WechatUtil;
import com.aawashcar.apigateway.util.XMLUtil;

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
	public WechatPayResponseModel getUnifiedOrder(@PathVariable("validid") String validId, @PathVariable("body") String body,
			@PathVariable("total") String total) {

		// String openId = payService.takeOpenId(code);
//		if (StringUtils.isEmpty(validId)) {
//			return "pay/wxpay_error";
//		}
		String notify = "https://www.aawashcar.com/order/wechatnotify";
		// String body = "商品支付内容";
		// String total = "1"; // 单位分
		String out_trade_no = new Date().getTime() + "";

		String res = payService.unifiedorder(notify, validId, body, total, out_trade_no);
		
//		String url = "https://www.aawashcar.com/order/unifiedOrder/" + validId + "/" + body + "/" + String.valueOf(total);
//		payService.wechatPay(model,res,url);
		
		
//		String paySign = MD5(appId=wxd678efh567hg6787&nonceStr=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&package=prepay_id=wx2017033010242291fcfe0db70013231072&signType=MD5&timeStamp=1490840662&key=qazwsxedcrfvtgbyhnujmikolp111111) = 22D9B4E54AB1950F51E0649E8810ACD6
//		String paySign = MD5();
		Object object = XMLUtil.xmlToBean(WechatPayResponse.class, res);
		
		
        
        
		WechatPayResponseModel responseModel = null;
		if (object instanceof WechatPayResponse) {
			WechatPayResponse wechatPay = (WechatPayResponse) object;
			
			String timeStamp = System.currentTimeMillis() / 1000 + "";//时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
//	        String nonceStr =  UUID.randomUUID().toString().replace("-", "");//随机字符串，长度为32个字符以下。
			String nonceStr = wechatPay.getNonce_str();
//	        String _package = "prepay_id="+ MapUtils.getString(txtResult, "prepay_id");//统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
			String _package = "prepay_id=" + wechatPay.getPrepay_id();
	        String signType = "MD5";//签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
	        Map<String, String> signMap = new TreeMap<String, String>();
	        signMap.put("appId",wechatPay.getAppid());
	        signMap.put("timeStamp",timeStamp);
	        signMap.put("nonceStr",nonceStr);
	        signMap.put("package",_package);
	        signMap.put("signType",signType);
	        String paySign = null;
			try {
				paySign = WXPayUtil.generateSignature(signMap, "aawashcar789123aawashcarAA0WAGgP");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};//签名,具体签名方案参见微信公众号支付帮助文档;
	        signMap.put("paySign",paySign);
	        
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
