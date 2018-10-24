package com.aawashcar.apigateway.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.CapabilityTypeModel;
import com.aawashcar.apigateway.model.CapabilityWithCategoryModel;
import com.aawashcar.apigateway.service.CapabilityPageService;

@RequestMapping("cap/")
@ResponseBody
@Controller
public class CapabilityController {
	
	@Autowired
	private CapabilityPageService service;

	@RequestMapping(value="listall", method = RequestMethod.GET)
	public List<CapabilityWithCategoryModel> listAllCapabilities() {
		return listCapabilitiesByCategories(service.listAllCapabilities());
	}
	
	@RequestMapping(value="listtypes", method = RequestMethod.GET)
	public List<CapabilityTypeModel> listCapabilityTypes() {
		return service.listCapabilityTypes();
	}
	
	@RequestMapping(value="listallwithprice/{typeid}/{categoryid}", method = RequestMethod.GET)
	public List<CapabilityWithCategoryModel> listAllWithPrice(@PathVariable("typeid") int typeId, @PathVariable("categoryid") int categoryId) {
		return listCapabilitiesByCategories(service.listAllWithPrice(typeId, categoryId));
	}
	
	private List<CapabilityWithCategoryModel> listCapabilitiesByCategories(List<CapabilityModel> capabilities) {

		HashMap<Integer, List<CapabilityModel>> temp = new HashMap<>();
		capabilities.stream().forEach(capability -> {
			int key = capability.getCategoryId();
			if (temp.containsKey(key)) {
				temp.get(key).add(capability);
			} else {
				List<CapabilityModel> caps = new ArrayList<CapabilityModel>();
				caps.add(capability);
				temp.put(key, caps);
			}
		});
		
		return temp.keySet().stream().map(key -> {
			CapabilityWithCategoryModel m = new CapabilityWithCategoryModel();
			m.setCategoryId(key);
			m.setCategoryName(temp.get(key).get(0).getCategoryName());
			m.setCategoryUrl(temp.get(key).get(0).getCategoryUrl());
			m.setCapabilites(temp.get(key));
			return m;
		}).collect(Collectors.toList());
	}
}
