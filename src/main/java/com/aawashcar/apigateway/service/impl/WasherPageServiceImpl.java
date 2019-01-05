package com.aawashcar.apigateway.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.PromotionWithServices;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WasherOrderSummary;
import com.aawashcar.apigateway.entity.Worker;
import com.aawashcar.apigateway.entity.WorkerRemark;
import com.aawashcar.apigateway.model.AssignedOrder;
import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.LocationModel;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.WasherActionModel;
import com.aawashcar.apigateway.model.WasherActionResponse;
import com.aawashcar.apigateway.model.WasherInfo;
import com.aawashcar.apigateway.model.WasherMainPageInfo;
import com.aawashcar.apigateway.model.WasherOrderSummaryModel;
import com.aawashcar.apigateway.service.CapabilityPageService;
import com.aawashcar.apigateway.service.WasherPageService;
import com.aawashcar.apigateway.util.AACodeConsField;
import com.aawashcar.apigateway.util.EntityMapper;
import com.aawashcar.apigateway.util.ServiceUtil;

@Service()
public class WasherPageServiceImpl extends BaseService implements WasherPageService {

	@Autowired
	private CapabilityPageService capabilityService;
	
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
				assignedOrder.setBookTime(EntityMapper.formatTimestamp(order.getBookTime()));

				url = crmUrlPrefix + "user/info/" + String.valueOf(order.getUserId());
				User user = restTemplate.getForObject(url, User.class);
				assignedOrder.setCustomerFirstName(user.getFirstName());
				assignedOrder.setCustomerLastName(user.getLastName());
				assignedOrder.setCustomerPhone(user.getPhoneNumber());
				
				assignedOrder.setServiceName(getServicesName(order.getServiceId()));

//				url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(order.getServiceId());
//				WashCarService service = restTemplate.getForObject(url, WashCarService.class);
//				assignedOrder.setIcon(service.getIconUrl());
//				assignedOrder.setServiceName(service.getName());

//				url = opsUrlPrefix + "location/lal/" + String.valueOf(order.getLocationId());
//				Location location = restTemplate.getForObject(url, Location.class);
//				if (location != null) {
//					assignedOrder.setLatitude(location.getLatitude());
//					assignedOrder.setLongitude(location.getLongitude());
//				}
				
				url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
				LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);
				if (locationModel != null) {
					assignedOrder.setLatitude(locationModel.getLatitude());
					assignedOrder.setLongitude(locationModel.getLongitude());
				}
				
				
				assignedOrder.setOrderId(order.getId());
				assignedOrder.setOrderNumber(order.getOrderNumber());
				assignedOrder.setRemarks(order.getRemarks());

				url = omsUrlPrefix + "order/status/" + String.valueOf(order.getStatusCode());
				String statusName = restTemplate.getForObject(url, String.class);

				assignedOrder.setStatus(statusName);
				assignedOrder.setStatusCode(order.getStatusCode());

				washerMainPageInfo.setAssignedOrder(assignedOrder);
			}
		}

		return washerMainPageInfo;
	}
	
	private String getServicesName(String serviceIds) {
		String[] ids = ServiceUtil.getServiceIDs(serviceIds);
		int length = ids.length;
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < length; index++) {
			String url = capUrlPrefix + "capabilityname/" + ids[index];
			sb.append(restTemplate.getForObject(url, String.class));
			
			if (index != length - 1) {
				sb.append(" + ");
			}
		}
		
		return sb.toString();
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
	public OrderDetailModel orderDetail(int orderId) {

//		City city = null;
//		District district = null;
//		Province province = null;
//		ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
//		WashCarService washCarService = null;

		// get order by order id;
		String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
		Order order = restTemplate.getForObject(url, Order.class);

//		url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(order.getServiceId());
//		washCarService = restTemplate.getForObject(url, WashCarService.class);

		url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
		vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

		url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
		vehicleType = restTemplate.getForObject(url, VehicleType.class);

//		url = opsUrlPrefix + "location/province/" + String.valueOf(order.getProvinceId());
//		province = restTemplate.getForObject(url, Province.class);
//
//		url = opsUrlPrefix + "location/city/" + String.valueOf(order.getCityId());
//		city = restTemplate.getForObject(url, City.class);
//
//		url = opsUrlPrefix + "location/district/" + String.valueOf(order.getDistrictId());
//		district = restTemplate.getForObject(url, District.class);
//
//		url = opsUrlPrefix + "location/resiquarter/" + String.valueOf(order.getResiQuartId());
//		resiQuarter = restTemplate.getForObject(url, ResidentialQuarter.class);

		url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
		Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);

//		url = promUrlPrefix + "promotion/" + String.valueOf(order.getPromotionId());
//		Promotion promotion = restTemplate.getForObject(url, Promotion.class);
//		Promotion[] promotions = { promotion };
		
		url = promUrlPrefix + "promotion/mylistwithservices/" + String.valueOf(order.getUserId());
		ResponseEntity<PromotionWithServices[]> promotionResponseEntity = restTemplate.getForEntity(url, PromotionWithServices[].class);
		PromotionWithServices[] promotions = promotionResponseEntity.getBody();

		url = promUrlPrefix + "coupon/" + String.valueOf(order.getCouponId());
		Coupon coupon = restTemplate.getForObject(url, Coupon.class);
		Coupon[] coupons = { coupon };

		url = opsUrlPrefix + "worker/washedorder/" + String.valueOf(order.getId());
		Worker worker = restTemplate.getForObject(url, Worker.class);

		url = crmUrlPrefix + "user/info/" + String.valueOf(order.getUserId());
		User user = restTemplate.getForObject(url, User.class);
		
		url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
		LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);
		
		String servicesName = getServicesName(order.getServiceId());
		return EntityMapper.buildOrderDetailWithWasher(order, servicesName, vehicle, vehicleCategory, 
				vehicleType, locationModel, coupons, promotions, worker, null, user);

//		return EntityMapper.buildOrderDetailWithWasher(order, washCarService.getName(), vehicle, vehicleCategory,
//				vehicleType, province, city, district, resiQuarter, coupons, promotions, worker, washCarService, user);
	}

	@Override
	public WasherActionResponse takeOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/takeorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);

		WasherActionResponse actionResponse = new WasherActionResponse();

		int statusCode = codeResponse.getBody().intValue();
		actionResponse.setStatusCode(statusCode);
		actionResponse.setStatus(getStatusName(statusCode));

		return actionResponse;
	}
	
	@Override
	public WasherActionResponse rushOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/rushorder/%s/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId, model.getOrderNumber());
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);

		WasherActionResponse actionResponse = new WasherActionResponse();

		int statusCode = codeResponse.getBody().intValue();
		actionResponse.setStatusCode(statusCode);
		actionResponse.setStatus(getStatusName(statusCode));

		return actionResponse;
	}

	@Override
	public WasherActionResponse rejectOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/rejectorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);

		WasherActionResponse actionResponse = new WasherActionResponse();

		int statusCode = codeResponse.getBody().intValue();
		actionResponse.setStatusCode(statusCode);
		actionResponse.setStatus(getStatusName(statusCode));

		return actionResponse;
	}

	@Override
	public WasherActionResponse arrivedOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/arrivedorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);

		WasherActionResponse actionResponse = new WasherActionResponse();

		int statusCode = codeResponse.getBody().intValue();
		actionResponse.setStatusCode(statusCode);
		actionResponse.setStatus(getStatusName(statusCode));

		return actionResponse;
	}

	@Override
	public WasherActionResponse completeOrder(WasherActionModel model) {
		int workerId = isWorker(model.getValidId());
		String url = opsUrlPrefix + "worker/completeorder/%s/%s/%s";
		url = String.format(url, model.getOrderId(), model.getRemarkId(), workerId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);

		WasherActionResponse actionResponse = new WasherActionResponse();

		int statusCode = codeResponse.getBody().intValue();
		actionResponse.setStatusCode(statusCode);
		actionResponse.setStatus(getStatusName(statusCode));

		return actionResponse;
	}

	private String getStatusName(int code) {
		String url = omsUrlPrefix + "order/status/" + String.valueOf(code);
		ResponseEntity<String> valueResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		return valueResponse.getBody();
	}

	@Override
	public WorkerRemark[] listRemarks() {
		String url = opsUrlPrefix + "worker/remarks/listall";
		ResponseEntity<WorkerRemark[]> workeRemarkResponseEntity = restTemplate.getForEntity(url, WorkerRemark[].class);
		return (WorkerRemark[]) workeRemarkResponseEntity.getBody();
	}

	@Override
	public WorkerRemark[] listAccpetRemarks() {
		String url = opsUrlPrefix + "worker/remarks/accept/list";
		ResponseEntity<WorkerRemark[]> workeRemarkResponseEntity = restTemplate.getForEntity(url, WorkerRemark[].class);
		return (WorkerRemark[]) workeRemarkResponseEntity.getBody();
	}

	@Override
	public WorkerRemark[] listRejectRemarks() {
		String url = opsUrlPrefix + "worker/remarks/reject/list";
		ResponseEntity<WorkerRemark[]> workeRemarkResponseEntity = restTemplate.getForEntity(url, WorkerRemark[].class);
		return (WorkerRemark[]) workeRemarkResponseEntity.getBody();
	}

	@Override
	public WorkerRemark[] listCompleteRemarks() {
		String url = opsUrlPrefix + "worker/remarks/complete/list";
		ResponseEntity<WorkerRemark[]> workeRemarkResponseEntity = restTemplate.getForEntity(url, WorkerRemark[].class);
		return (WorkerRemark[]) workeRemarkResponseEntity.getBody();
	}

	@Override
	public WasherOrderSummaryModel[] listWasherCompletedOrderSummary(String validId, int size) {
		int workerId = isWorker(validId);
		String url = opsUrlPrefix + "worker/orders/completedorderlist/" + String.valueOf(workerId) + "/"
				+ String.valueOf(size);
		ResponseEntity<WasherOrderSummary[]> orderSummaryResponseEntity = restTemplate.getForEntity(url,
				WasherOrderSummary[].class);
		
        WasherOrderSummary[] entities = (WasherOrderSummary[]) orderSummaryResponseEntity.getBody();
        int length = entities.length;
		WasherOrderSummaryModel[] models = new WasherOrderSummaryModel[length];
		for (int index = 0; index < length; index++) {
			models[index] = buildWashOrderSummaryModel(entities[index]);
		}
		
		return models;
	}

	@Override
	public String apply(String validId, String phoneNumber) {
		if (StringUtils.isEmpty(validId) || StringUtils.isEmpty(phoneNumber)) {
			return AACodeConsField.ERROR_INVALID_INPUT_30100;
		}

		if ("null".equals(validId) || ("null").equals(phoneNumber)) {
			return AACodeConsField.ERROR_INVALID_INPUT_30100;
		}

		// add valid id in crm_r_user_uuid
		String url = crmUrlPrefix + "washer/apply/" + validId;
		Integer addValidIdResponse = restTemplate.postForObject(url, null, Integer.class);

		// add phone number in ops_worker
		url = opsUrlPrefix + "worker/apply/" + phoneNumber + "/" + validId;
		Integer addPhonenumberResponse = Integer.valueOf(restTemplate.postForObject(url, null, Integer.class));

		// if (addValidIdResponse.intValue() != HttpStatus.OK.value()
		// || addPhonenumberResponse.intValue() != HttpStatus.OK.value()) {
		// throw new AAInnerServerError("申请注册洗车工失败");
		// }

		if (addValidIdResponse.intValue() > 0 && addPhonenumberResponse.intValue() > 0) {
			return AACodeConsField.SUCCESS_APPLY_WORKER_20200;
		} else {
			return AACodeConsField.ERROR_ALREADY_APPLIED_30000;
		}
	}

	@Override
	public WasherOrderSummaryModel[] listWasherUnCompletedOrderSummary(String validId, int size) {
		int workerId = isWorker(validId);
		String url = opsUrlPrefix + "worker/orders/uncompletedorderlist/" + String.valueOf(workerId) + "/"
				+ String.valueOf(size);
		ResponseEntity<WasherOrderSummary[]> orderSummaryResponseEntity = restTemplate.getForEntity(url,
				WasherOrderSummary[].class);
		
        WasherOrderSummary[] entities = (WasherOrderSummary[]) orderSummaryResponseEntity.getBody();
        int length = entities.length;
		WasherOrderSummaryModel[] models = new WasherOrderSummaryModel[length];
		for (int index = 0; index < length; index++) {
			models[index] = buildWashOrderSummaryModel(entities[index]);
		}
		
		return models;
	}
	
	@Override
	public WasherOrderSummaryModel[] listWasherAvailableOrderSummary(String validId, int size) {
		int workerId = isWorker(validId);
		String url = omsUrlPrefix + "order/availableorderlist/" + String.valueOf(size);
		ResponseEntity<Order[]> ordersResponseEntity = restTemplate.getForEntity(url,
				Order[].class);
		
		Order[] entities = (Order[]) ordersResponseEntity.getBody();
        int length = entities.length;
		WasherOrderSummaryModel[] models = new WasherOrderSummaryModel[length];
		for (int index = 0; index < length; index++) {
			models[index] = buildWashOrderSummaryModelWithOrder(entities[index]);
		}
		
		return models;
	}
	
	private WasherOrderSummaryModel buildWashOrderSummaryModelWithOrder(Order order) {
		Assert.notNull(order, "Washer Order Summary should not be null");
//		String url = omsUrlPrefix + "order/detail/" + String.valueOf(entity.getOrderId());
//		Order order = restTemplate.getForObject(url, Order.class);
		
        String url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
		LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);
		WasherOrderSummaryModel model = new WasherOrderSummaryModel();
//		model.setId(entity.getOrderId());
		model.setId(order.getId());
		model.setOrderNumber(order.getOrderNumber());
		model.setStatus(order.getStatusCode());
		model.setAddress(locationModel.getDetailAddress());
		model.setBookTime(EntityMapper.formatTimestamp(order.getBookTime()));
		model.setRemarks(locationModel.getAddressRemark());
		
		model.setStatusName(order.getStatus());
		
		String[] serviceIds = order.getServiceId().split(",");
		String serviceName = "";
		for (String serviceId : serviceIds) {
			CapabilityModel capModel = capabilityService.findCapabilityById(Integer.parseInt(serviceId));
			serviceName += capModel.getName();
			serviceName += " ";
		}
		model.setServiceName(serviceName.trim());
	
		return model;
	}
	
	private WasherOrderSummaryModel buildWashOrderSummaryModel(WasherOrderSummary entity) {
		Assert.notNull(entity, "Washer Order Summary should not be null");
		String url = omsUrlPrefix + "order/detail/" + String.valueOf(entity.getOrderId());
		Order order = restTemplate.getForObject(url, Order.class);
		
        url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
		LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);
		WasherOrderSummaryModel model = new WasherOrderSummaryModel();
		model.setId(entity.getOrderId());
		model.setOrderNumber(entity.getOrderNumber());
		model.setStatus(entity.getStatus());
		model.setAddress(locationModel.getDetailAddress());
		model.setBookTime(EntityMapper.formatTimestamp(order.getBookTime()));
		model.setRemarks(locationModel.getAddressRemark());
		
		model.setStatusName(order.getStatus());
		
		String[] serviceIds = order.getServiceId().split(",");
		String serviceName = "";
		for (String serviceId : serviceIds) {
			CapabilityModel capModel = capabilityService.findCapabilityById(Integer.parseInt(serviceId));
			serviceName += capModel.getName();
			serviceName += " ";
		}
		model.setServiceName(serviceName.trim());
	
		return model;
	}
}
