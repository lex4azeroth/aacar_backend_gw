package com.aawashcar.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;
import com.aawashcar.apigateway.service.MinePageService;

@RequestMapping("my/")
@ResponseBody
@Controller
public class MinePageController {
	@Autowired
	private MinePageService service;

	@RequestMapping(value = "promotionlist/{uuid}", method = RequestMethod.GET)
	public List<MyPromotionModel> promtionList(@PathVariable("uuid") String uuid) {
		return service.listMyPromotionModels(uuid);
	}
	
	@RequestMapping(value = "couponlist/{uuid}", method = RequestMethod.GET)
	public List<MyCouponModel> couponList(@PathVariable("uuid") String uuid) {
		return service.listMyCouponModels(uuid);
	}
}
