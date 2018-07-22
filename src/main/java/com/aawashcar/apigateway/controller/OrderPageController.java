package com.aawashcar.apigateway.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.util.WXPayUtil;

import net.sf.json.JSONObject;

@RequestMapping("order/")
@ResponseBody
@Controller
public class OrderPageController {

	@Autowired
	private OrderPageService orderPageService;
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "mylist/{validid}/{size}", method = RequestMethod.GET)
	public List<OrderSummaryModel> myOrderSummaries(@PathVariable("validid") String validId,
	                                                @PathVariable("size") int size) {

		return orderPageService.myOrderSummaryList(validId, size);

	}
	
	@RequestMapping(value = "detail/{orderid}/{validid}", method = RequestMethod.GET)
	public OrderDetailModel myOrderDetail(@PathVariable("orderid") int id, 
	                                            @PathVariable("validid") String validId) {
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
	public Pricing pricing(@PathVariable("validid") String validId, 
	                      @PathVariable("orderid") int orderId, 
	                      @PathVariable("couponid") int couponId, 
	                      @PathVariable("promotionid") int promotionId) {
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
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUnifiedOrder", method = RequestMethod.GET)
    @ResponseBody
    public String getUnifiedOrder(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        //拼接请求信息
        String appid = "wx4b5004e03cfdbd7d";//小程序ID ==> 微信分配的小程序ID
        String mch_id = "1503630241";//商户号 ==> 微信支付分配的商户号
        String nonce_str = UUID.randomUUID().toString().replace("-", "");//随机字符串 ==> 随机字符串，长度要求在32位以内。推荐随机数生成算法
        String body = "测试小程序";//商品描述 ==> 商品简单描述，该字段请按照规范传递，具体请见参数规定
        String out_trade_no = UUID.randomUUID().toString().replace("-", "");//商户订单号 ==> 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
        String total_fee = "1";//标价金额 ==> 订单总金额，单位为分，详见支付金额
        String spbill_create_ip = "14.23.150.211";//终端IP ==> APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        String notify_url = "https://jykl.guoweijie.com/app/getPayResponse";//通知地址 ==> 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        String trade_type = "JSAPI";//交易类型 ==> 小程序取值如下：JSAPI，详细说明见参数规定
        String openid = "oEYhV4y0ay93YhRu7yJ4rorArsAI";//用户标识 ==> trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
        //组装请求签名信息
        Map<String, String> signMap = new TreeMap<String, String>();
        signMap.put("appid", appid);
        signMap.put("mch_id", mch_id);
        signMap.put("nonce_str", nonce_str);
        signMap.put("body", body);
        signMap.put("out_trade_no", out_trade_no);
        signMap.put("total_fee", total_fee);
        signMap.put("spbill_create_ip", spbill_create_ip);
        signMap.put("notify_url", notify_url);
        signMap.put("trade_type", trade_type);
        signMap.put("openid", openid);
        String sign = WXPayUtil.generateSignature(signMap, "aawashcar789123aawashcarAA0WAGgP");//签名 ==> 通过签名算法计算得出的签名值，详见签名生成算法
        //组装请求数据
        String postData = "<xml>" +
                "<appid>" + appid + "</appid>" +
                "<mch_id>" + mch_id + "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<body>" + body + "</body>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                "<total_fee>" + total_fee + "</total_fee>" +
                "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<trade_type>" + trade_type + "</trade_type>" +
                "<openid>" + openid + "</openid>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //请求接口
        String result = restTemplate.postForObject(url, postData, String.class);
        System.out.println(String.format("FOR DEBUG ONLY: %s", result));
        
        result = result == null ? "" : result;
        
//        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", postData);
//        
//        //获取接口返回结果
//        Map<String, String> txtResult = WXPayUtil.xmlToMap(jsonObject.optString("txtResult", ""));
        Map<String, String> txtResult = WXPayUtil.xmlToMap(result);
        String return_code = MapUtils.getString(txtResult, "return_code");
        String result_code = MapUtils.getString(txtResult, "result_code");
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {//统一下单接口成功
            String timeStamp = System.currentTimeMillis()/1000+"";//时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
            String nonceStr =  UUID.randomUUID().toString().replace("-", "");//随机字符串，长度为32个字符以下。
            String _package = "prepay_id="+ MapUtils.getString(txtResult, "prepay_id");//统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
            String signType = "MD5";//签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
            signMap = new TreeMap<String, String>();
            signMap.put("appId",appid);
            signMap.put("timeStamp",timeStamp);
            signMap.put("nonceStr",nonceStr);
            signMap.put("package",_package);
            signMap.put("signType",signType);
            String paySign = WXPayUtil.generateSignature(signMap, "aawashcar789123aawashcarAA0WAGgP");;//签名,具体签名方案参见微信公众号支付帮助文档;
            signMap.put("paySign",paySign);
        }else {//统一下单接口失败
            signMap=txtResult;
        }
        return JSONObject.fromObject(signMap).toString();
//        return null;
    }

    /**
     * 支付结果回调
     * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getPayResponse")
    @ResponseBody
    public String getPayResponse(HttpServletRequest request){
        StringBuffer buffer = new StringBuffer();
        try {
            ServletInputStream inputStream = request.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
//            LogUtils.GJ("微信支付回调成功："+buffer.toString());

            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(buffer.toString());
            for (Map.Entry<String, String> ppEntry : stringStringMap.entrySet()) {
//                LogUtils.GJ("key:" + ppEntry.getKey());
//                LogUtils.GJ("value:" + ppEntry.getValue());
            }

        }catch (Exception e){
//            LogUtils.GJ("微信支付回调失败:"+e.getMessage());
        }
        //微信支付，回调返回微信回执
        String responseWeiXin="<xml>"+
        "<return_code><![CDATA[SUCCESS]]></return_code>"+
        "<return_msg><![CDATA[OK]]></return_msg>"+
        "</xml>";
        return responseWeiXin;
    }
}
