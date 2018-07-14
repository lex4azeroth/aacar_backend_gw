package com.aawashcar.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.service.OrderPageService;

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
}
