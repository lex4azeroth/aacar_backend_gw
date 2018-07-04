package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.OrderSummary;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class OrderPageServiceImpl implements OrderPageService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${mcw.service.oms.url.prefix}")
	private String omsUrlPrefix;

	@Value("${mcw.service.crm.url.prefix}")
	private String crmUrlPrefix;

	@Value("${mcw.service.ops.url.prefix}")
	private String opsUrlPrefix;

	@Override
	public List<OrderSummaryModel> myOrderSummaryList(String uuid, int limit) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, uuid);
		User user = restTemplate.getForObject(url, User.class);

		int userId = user.getId();
		if (userId > 0) {
			url = omsUrlPrefix + "order/myordersummaries/" + String.valueOf(userId) + "/" + String.valueOf(limit);
			ResponseEntity<OrderSummary[]> orderSummaryResponseEntity =
			                restTemplate.getForEntity(url, OrderSummary[].class);
			OrderSummary[] myOrderSummarys = orderSummaryResponseEntity.getBody();
			List<OrderSummaryModel> orderSummaryModels =
			                EntityMapper.convertOrderSummarysToMyModel(myOrderSummarys);

			int size = orderSummaryModels.size();
			for (int index = 0; index < size; index++) {
				int serviceId = myOrderSummarys[index].getServiceId();
				url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(serviceId);
				WashCarService washCarService = restTemplate.getForObject(url, WashCarService.class);
				orderSummaryModels.get(index).setServiceName(washCarService.getName());
			}

			return orderSummaryModels;
		}
		else {
			throw new RuntimeException(String.format("UUID %s does not exist", uuid));
		}
	}
}
