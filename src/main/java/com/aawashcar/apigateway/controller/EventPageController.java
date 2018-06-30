package com.aawashcar.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.service.EventPageService;

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
}
