package com.aawashcar.apigateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.Location;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.entity.Worker;
import com.aawashcar.apigateway.entity.WorkerRemark;
import com.aawashcar.apigateway.model.AssignedOrder;
import com.aawashcar.apigateway.model.WasherActionModel;
import com.aawashcar.apigateway.model.WasherActionResponse;
import com.aawashcar.apigateway.model.WasherInfo;
import com.aawashcar.apigateway.model.WasherMainPageInfo;
import com.aawashcar.apigateway.service.WasherPageService;
import com.aawashcar.apigateway.util.OrderStatusCode;

@Service()
public class WasherPageServiceImpl implements WasherPageService {

	@Value("${mcw.service.crm.url.prefix}")
	private String crmUrlPrefix;
	
	@Value("${mcw.service.oms.url.prefix}")
	private String omsUrlPrefix;

	@Value("${mcw.service.ops.url.prefix}")
	private String opsUrlPrefix;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public WasherMainPageInfo login(String validId) {
		int workerId = isWorker(validId);
		WasherMainPageInfo washerMainPageInfo = new WasherMainPageInfo();
		washerMainPageInfo.setWasher(false);
		if (workerId > 0) {
			// is a worker
			washerMainPageInfo.setWasher(true);
			
			String url = opsUrlPrefix + "worker/" + String.valueOf(workerId);
			Worker worker = restTemplate.getForObject(url, Worker.class);
			
			WasherInfo washerInfo = new WasherInfo();
			washerInfo.setFirstName(worker.getFirstName());
			washerInfo.setLevel(worker.getLevel());
			washerInfo.setNickName(worker.getNickName());
			washerInfo.setGender(worker.getGender());
			
			washerMainPageInfo.setWasherInfo(washerInfo);
			
			url = opsUrlPrefix + "worker/assignedorder/" + String.valueOf(workerId);
			Integer orderId = restTemplate.getForObject(url, Integer.class);
			if (orderId.intValue() > 0) {
				url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
				Order order = restTemplate.getForObject(url, Order.class);
				
				AssignedOrder assignedOrder = new AssignedOrder();
				assignedOrder.setAddress(order.getDetailLocation());
				assignedOrder.setBookTime(order.getBookTime());
				
				url = crmUrlPrefix + "user/info/" + String.valueOf(order.getUserId());
				User user = restTemplate.getForObject(url, User.class);
				assignedOrder.setCustomerFirstName(user.getFirstName());
				assignedOrder.setCustomerLastName(user.getLastName());
				assignedOrder.setCustomerPhone(user.getPhoneNumber());
				
				url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(order.getServiceId());
				WashCarService service = restTemplate.getForObject(url, WashCarService.class);
				assignedOrder.setIcon(service.getIconUrl());
				assignedOrder.setServiceName(service.getName());
				
				url = opsUrlPrefix + "location/lal/" + String.valueOf(order.getLocationId());
				Location location = restTemplate.getForObject(url, Location.class);
				assignedOrder.setLatitude(location.getLatitude());
				assignedOrder.setLongitude(location.getLongitude());
				
				assignedOrder.setOrderId(order.getId());
				assignedOrder.setOrderNumber(order.getOrderNumber());
				assignedOrder.setRemarks(order.getRemarks());
				assignedOrder.setStatus(OrderStatusCode.getStatusName(order.getStatusCode()));
				
				washerMainPageInfo.setAssignedOrder(assignedOrder);
			}
		} 
		
		return washerMainPageInfo;
	}
	
	private int isWorker(String validId) {
		User user = getUserId(validId);
		
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		
		return user.getWorkerId();
	}
	
	private User getUserId(String validId) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, validId);
		User user = restTemplate.getForObject(url, User.class);

		return user;
	}

	@Override
	public WasherActionResponse takeOrder(WasherActionModel model) {
		// {orderid}/{remarksid}/{workerid}
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/takeorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		
		WasherActionResponse actionResponse = new WasherActionResponse();
		actionResponse.setStatus(response.getBody());
		return actionResponse;
	}

	@Override
	public WasherActionResponse rejectOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/rejectorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		
		WasherActionResponse actionResponse = new WasherActionResponse();
		actionResponse.setStatus(response.getBody());
		return actionResponse;
	}

	@Override
	public WasherActionResponse completeOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/completeorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		
		WasherActionResponse actionResponse = new WasherActionResponse();
		actionResponse.setStatus(response.getBody());
		return actionResponse;
	}

	@Override
	public WorkerRemark[] listRemarks() {
		String url = opsUrlPrefix + "worker/remarks/listall";
		ResponseEntity<WorkerRemark[]> workeRemarkResponseEntity = restTemplate.getForEntity(url, WorkerRemark[].class);
		return (WorkerRemark[]) workeRemarkResponseEntity.getBody();
	}
}
