package com.aawashcar.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.service.OrderPageService;

@RequestMapping("order/")
@ResponseBody
@Controller
public class OrderPageController {

	@Autowired
	private OrderPageService orderPageService;

	@RequestMapping(value = "mylist/{openid}/{size}", method = RequestMethod.GET)
	public List<OrderSummaryModel> myOrderSummaries(@PathVariable("openid") String openId,
	                                                @PathVariable("size") int size) {

		return orderPageService.myOrderSummaryList(openId, size);

	}
}