package com.aawashcar.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.DistrictModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.OrderIdModel;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.PriceModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;
import com.aawashcar.apigateway.service.MainPageInfoService;

@RequestMapping("main/")
@ResponseBody
@Controller
public class MainPageController {

	@Autowired
	private MainPageInfoService service;

	@RequestMapping(value = "pageinfo/{validid}", method = RequestMethod.GET)
	public MainPageInfo mainPageInfo(@PathVariable("validid") String validId) {
		return service.getMainPageInfo(validId);
	}

	@RequestMapping(value = "district/{provinceId}/{cityId}", method = RequestMethod.GET)
	public List<DistrictModel> listDistricts(@PathVariable(name = "provinceId") int provinceId,
	                                         @PathVariable(name = "cityId") int cityId) {
		provinceId = (provinceId <= 0) ? 1 : provinceId;
		cityId = (cityId <= 0) ? 1 : cityId;

		return service.listDistricts(provinceId, cityId);
	}

	@RequestMapping(value = "residentialquarter/{provinceId}/{cityId}/{districtId}", method = RequestMethod.GET)
	public List<ResidentialQuarterModel> listResidentialQuarter(@PathVariable(name = "provinceId") int provinceId,
	                                                       @PathVariable(name = "cityId") int cityId,
	                                                       @PathVariable(name = "districtId") int districtId) {
		provinceId = (provinceId <= 0) ? 1 :provinceId;
		cityId = (cityId <= 0) ? 1 : cityId;
		districtId = (districtId <= 0) ? 1 : districtId;
		
		return service.listResidentialQuarters(provinceId, cityId, districtId);
	}
	
	@RequestMapping(value="price/{typeId}/{categoryId}/{serviceId}", method = RequestMethod.GET)
	public PriceModel showPrice(@PathVariable(name="typeId") int typeId, @PathVariable(name="categoryId") int categoryId, @PathVariable(name="serviceId") int serviceId) {
		PriceModel priceModel = new PriceModel();
		
		double price = service.getPrice(typeId, categoryId, serviceId);
		priceModel.setPrice(price);
		
		return priceModel;
	}
	
	@RequestMapping(value="order", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public OrderIdModel newOrder(@RequestBody OrderModel orderModel) {
		int orderId = service.newOrder(orderModel);
		OrderIdModel orderIdModel = new OrderIdModel();
		orderIdModel.setOrderId(orderId);
		return orderIdModel;
	}
}
