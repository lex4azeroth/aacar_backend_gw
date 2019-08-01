package com.aawashcar.apigateway.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;
import com.aawashcar.apigateway.service.CapabilityPageService;
import com.aawashcar.apigateway.service.MinePageService;

@RequestMapping("my/")
@ResponseBody
@Controller
public class MinePageController {
	@Autowired
	private MinePageService service;
	
	@Autowired
	private CapabilityPageService capService;

	@RequestMapping(value = "promotionlist/{openid}", method = RequestMethod.GET)
	public List<MyPromotionModel> promtionList(@PathVariable("openid") String openId) {
		
		List<CapabilityModel> capModels = capService.listAllCapabilities();
		capModels.stream().forEach(c -> {
			System.out.println(c.getId());
			System.out.println(c.getName());
		});
		Map<Integer, String> serviceNames = 
				capModels.stream().collect(Collectors.toMap(CapabilityModel::getId, CapabilityModel::getName));
		
		Map<Integer, Integer> serviceCategory = 
				capModels.stream().collect(Collectors.toMap(CapabilityModel::getId, CapabilityModel::getCategoryId));
		
		List<MyPromotionModel> models = service.listMyPromotionModels(openId, serviceNames, serviceCategory);
		return models;
	}

	@RequestMapping(value = "couponlist/{openid}", method = RequestMethod.GET)
	public List<MyCouponModel> couponList(@PathVariable("openid") String openId) {
		return service.listMyCouponModels(openId);
	}
}
