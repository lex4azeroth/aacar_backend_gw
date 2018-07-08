package com.aawashcar.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.entity.WorkerRemark;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.WasherActionModel;
import com.aawashcar.apigateway.model.WasherActionResponse;
import com.aawashcar.apigateway.model.WasherMainPageInfo;
import com.aawashcar.apigateway.service.WasherPageService;

@RequestMapping("washer/")
@ResponseBody
@Controller
public class WasherPageController {

	@Autowired
	private WasherPageService service;
	
	@RequestMapping(value="main/login/{validid}", method = RequestMethod.GET)
	public WasherMainPageInfo login(@PathVariable("validid") String validId) {
		return service.login(validId);
	}
	
	@RequestMapping(value="main/takeorder", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public WasherActionResponse takeOrder(@RequestBody WasherActionModel actionModel) {
		return service.takeOrder(actionModel);
	}
	
	@RequestMapping(value="main/rejectorder", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public WasherActionResponse rejectOrder(@RequestBody WasherActionModel actionModel) {
		return service.rejectOrder(actionModel);
	}
	
	@RequestMapping(value="main/completeorder", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public WasherActionResponse completeOrder(@RequestBody WasherActionModel actionModel) {
		return service.completeOrder(actionModel);
	}
	
	@RequestMapping(value="remarks/listall", method = RequestMethod.GET)
	public WorkerRemark[] listRemarks() {
		return service.listRemarks();
	}
	
	@RequestMapping(value="remarks/accept/list", method = RequestMethod.GET)
	public WorkerRemark[] listAcceptRemarks() {
		return service.listAccpetRemarks();
	}
	
	@RequestMapping(value="remarks/reject/list", method = RequestMethod.GET)
	public WorkerRemark[] listRejectRemarks() {
		return service.listRejectRemarks();
	}
	
	@RequestMapping(value="remarks/complete/list", method = RequestMethod.GET)
	public WorkerRemark[] listCompleteRemarks() {
		return service.listCompleteRemarks();
	}
	
	@RequestMapping(value="order/detail/{id}", method = RequestMethod.GET)
	public OrderDetailModel getOrderDetail(@PathVariable("id") int id) {
		return service.orderDetail(id);
	}
}
