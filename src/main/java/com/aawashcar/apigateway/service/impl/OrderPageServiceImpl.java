package com.aawashcar.apigateway.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.config.WechatConfig;
import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.CouponTransaction;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Location;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.OrderSummary;
import com.aawashcar.apigateway.entity.OrderTransaction;
import com.aawashcar.apigateway.entity.PointTransaction;
import com.aawashcar.apigateway.entity.Points;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.PromotionWithServices;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.ServiceTransaction;
import com.aawashcar.apigateway.entity.UpdateOrder;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.entity.Worker;
import com.aawashcar.apigateway.exception.AAInnerServerError;
import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.LocationModel;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;
import com.aawashcar.apigateway.model.PromotionService;
import com.aawashcar.apigateway.model.PromotionWithServicesModel;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponse;
import com.aawashcar.apigateway.model.WechatPayResponseModel;
import com.aawashcar.apigateway.service.CapabilityPageService;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.service.WechatPayService;
import com.aawashcar.apigateway.util.EntityMapper;
import com.aawashcar.apigateway.util.ServiceUtil;
import com.aawashcar.apigateway.util.WXPayUtil;
import com.aawashcar.apigateway.util.XMLUtil;

@Service()
public class OrderPageServiceImpl extends BaseService implements OrderPageService {
	@Autowired
	private WechatPayService payService;

	@Autowired
	private CapabilityPageService capabilityService;

	private static final String NO_FEE = "NO_FEE";

	@Override
	public List<OrderSummaryModel> myOrderSummaryList(String validId, int limit) {
		User user = getUserId(validId);
		if (user == null) {
			// log error
			return null;
		}

		int userId = user.getId();
		if (userId > 0) {
			String url = omsUrlPrefix + "order/myordersummaries/" + String.valueOf(userId) + "/"
					+ String.valueOf(limit);
			ResponseEntity<OrderSummary[]> orderSummaryResponseEntity = restTemplate.getForEntity(url,
					OrderSummary[].class);
			OrderSummary[] myOrderSummarys = orderSummaryResponseEntity.getBody();
			List<OrderSummaryModel> orderSummaryModels = EntityMapper.convertOrderSummarysToMyModel(myOrderSummarys);

			int size = orderSummaryModels.size();
			for (int index = 0; index < size; index++) {
				String serviceIds = myOrderSummarys[index].getServiceId();
				// url = capUrlPrefix + "capabilityname/" +
				// String.valueOf(serviceId);
				// String serviceName = restTemplate.getForObject(url,
				// String.class);
				orderSummaryModels.get(index).setServiceName(getServicesName(serviceIds));
			}

			return orderSummaryModels;
		} else {
			// log user does not exist
			return null;
			// throw new RuntimeException(String.format("Id %s does not exist",
			// validId));
		}
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

	@Override
	public OrderDetailModel myOrderDetail(int orderId, String validId) {
		User user = getUserId(validId);

		if (user == null) {
			// log user does not exist
			return null;
			// throw new RuntimeException("user not found");
		}

		// City city = null;
		// District district = null;
		// Province province = null;
		// ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
		// WashCarService washCarService = null;

		if (user.getId() > 0) {

			// get order by order id;
			String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
			Order order = restTemplate.getForObject(url, Order.class);
			//
			// url = opsUrlPrefix + "wasshcarservice/service/" +
			// String.valueOf(order.getServiceId());
			// washCarService = restTemplate.getForObject(url,
			// WashCarService.class);

			url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
			vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

			url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
			vehicleType = restTemplate.getForObject(url, VehicleType.class);

			// url = opsUrlPrefix + "location/province/" +
			// String.valueOf(order.getProvinceId());
			// province = restTemplate.getForObject(url, Province.class);
			//
			// url = opsUrlPrefix + "location/city/" +
			// String.valueOf(order.getCityId());
			// city = restTemplate.getForObject(url, City.class);
			//
			// url = opsUrlPrefix + "location/district/" +
			// String.valueOf(order.getDistrictId());
			// district = restTemplate.getForObject(url, District.class);
			//
			// url = opsUrlPrefix + "location/resiquarter/" +
			// String.valueOf(order.getResiQuartId());
			// resiQuarter = restTemplate.getForObject(url,
			// ResidentialQuarter.class);

			url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
			Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);

			url = promUrlPrefix + "coupon/mylist/" + String.valueOf(user.getId());
			ResponseEntity<Coupon[]> couponResponseEntity = restTemplate.getForEntity(url, Coupon[].class);
			Coupon[] myCoupons = couponResponseEntity.getBody();

			url = promUrlPrefix + "promotion/mylistwithservices/" + String.valueOf(user.getId());
			ResponseEntity<PromotionWithServices[]> promotionResponseEntity = restTemplate.getForEntity(url,
					PromotionWithServices[].class);
			PromotionWithServices[] myPromotionsWithServices = promotionResponseEntity.getBody();

			// url = promUrlPrefix + "promotion/" +
			// String.valueOf(order.getPromotionId());
			// ResponseEntity<Promotion> promotionResponseEntity =
			// restTemplate.getForEntity(url, Promotion.class);
			// Promotion myPromotion = promotionResponseEntity.getBody();
			// Promotion[] myPromotions = {myPromotion};

			url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
			LocationModel location = restTemplate.getForObject(url, LocationModel.class);

			List<PromotionWithServicesModel> promotionWithServicesModels = new ArrayList<>();
			buildPromotionWithServicesList(promotionWithServicesModels, myPromotionsWithServices);
			return EntityMapper.buildOrderDetailInfo(order, getServicesName(order.getServiceId()), vehicle,
					vehicleCategory, vehicleType, location, myCoupons, promotionWithServicesModels);
			// return EntityMapper.buildOrderDetailInfo(order,
			// washCarService.getName(), vehicle,
			// vehicleCategory, vehicleType, province, city, district,
			// resiQuarter, myCoupons, myPromotions);
		}

		return null;
	}

	private void buildPromotionWithServicesList(List<PromotionWithServicesModel> promotionWithServicesModels,
			PromotionWithServices[] promotionsWithServices) {
		int size = promotionsWithServices.length;
		int promotionId = -1;
		PromotionWithServicesModel model = new PromotionWithServicesModel();
		List<PromotionService> promotionServiceList = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			PromotionWithServices item = promotionsWithServices[index];
			if (promotionId != item.getId()) {
				promotionServiceList = new ArrayList<>();
				model = new PromotionWithServicesModel();
				model.setId(item.getId());
				model.setDescription(item.getDescription());
				model.setDuration(item.getDuration());
				model.setName(item.getName());
				model.setPrice(item.getPrice());
				model.setServices(promotionServiceList);

				buildServiceList(promotionServiceList, item);

				promotionWithServicesModels.add(model);
				promotionId = item.getId();

			} else {
				buildServiceList(promotionServiceList, item);
			}
		}
	}

	private void buildServiceList(List<PromotionService> promotionServiceList, PromotionWithServices item) {
		int serviceId = item.getServiceId();
		PromotionService ps = new PromotionService();
		ps.setCount(item.getCount());
		ps.setConsumedCount(item.getConsumedCount());
		ps.setRemainingCount(item.getRemainingCount());
		ps.setServiceId(serviceId);
		CapabilityModel capability = capabilityService.findCapabilityById(serviceId);
		ps.setServiceName(capability.getName());
		ps.setServiceCode(capability.getServiceId());
		promotionServiceList.add(ps);
	}

	private User getUserId(String validId) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, validId);
		User user = restTemplate.getForObject(url, User.class);

		return user;
	}

	@Override
	public OrderDetailWithWasherModel orderDetailWithWasher(int orderId) {
		// City city = null;
		// District district = null;
		// Province province = null;
		// ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
		WashCarService washCarService = null;

		String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
		Order order = restTemplate.getForObject(url, Order.class);

		// url = opsUrlPrefix + "wasshcarservice/service/" +
		// String.valueOf(order.getServiceId());
		// washCarService = restTemplate.getForObject(url,
		// WashCarService.class);

		url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
		vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

		url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
		vehicleType = restTemplate.getForObject(url, VehicleType.class);

		// url = opsUrlPrefix + "location/province/" +
		// String.valueOf(order.getProvinceId());
		// province = restTemplate.getForObject(url, Province.class);
		//
		// url = opsUrlPrefix + "location/city/" +
		// String.valueOf(order.getCityId());
		// city = restTemplate.getForObject(url, City.class);
		//
		// url = opsUrlPrefix + "location/district/" +
		// String.valueOf(order.getDistrictId());
		// district = restTemplate.getForObject(url, District.class);
		//
		// url = opsUrlPrefix + "location/resiquarter/" +
		// String.valueOf(order.getResiQuartId());
		// resiQuarter = restTemplate.getForObject(url,
		// ResidentialQuarter.class);

		url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
		Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);

		url = promUrlPrefix + "coupon/" + String.valueOf(order.getCouponId());
		Coupon coupon = restTemplate.getForObject(url, Coupon.class);
		Coupon[] coupons = { coupon };

		// url = promUrlPrefix + "promotion/" +
		// String.valueOf(order.getPromotionId());
		// Promotion promotion = restTemplate.getForObject(url,
		// Promotion.class);
		// Promotion[] promotions = { promotion };

		url = promUrlPrefix + "promotion/mylistwithservices/" + String.valueOf(order.getUserId());
		ResponseEntity<PromotionWithServices[]> promotionResponseEntity = restTemplate.getForEntity(url,
				PromotionWithServices[].class);
		PromotionWithServices[] promotions = promotionResponseEntity.getBody();

		url = opsUrlPrefix + "worker/washedorder/" + String.valueOf(order.getId());
		Worker worker = restTemplate.getForObject(url, Worker.class);

		url = crmUrlPrefix + "user/info/" + String.valueOf(order.getUserId());
		User user = restTemplate.getForObject(url, User.class);

		url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
		LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);

		return EntityMapper.buildOrderDetailWithWasher(order, getServicesName(order.getServiceId()), vehicle,
				vehicleCategory, vehicleType, locationModel, coupons, promotions, worker, null, user);
		// return EntityMapper.buildOrderDetailWithWasher(order,
		// washCarService.getName(), vehicle, vehicleCategory,
		// vehicleType, province, city, district, resiQuarter, coupons,
		// promotions, worker, washCarService, user);
	}

	@Override
	public List<OrderDetailWithWasherModel> listAllOrderDetails() {
		String url = omsUrlPrefix + "order/listallorders";
		ResponseEntity<Order[]> orderResponseEntity = restTemplate.getForEntity(url, Order[].class);
		Order[] orders = orderResponseEntity.getBody();
		List<OrderDetailWithWasherModel> results = new ArrayList<>();
		int length = orders.length;

		for (int index = 0; index < length; index++) {
			results.add(buildOrderDetailWithWasher(orders[index]));
		}

		return results;
	}

	@Override
	public List<OrderDetailWithWasherModel> listOrderDetailsInDays(int days) {
		String url = omsUrlPrefix + "order/listordersindays/" + String.valueOf(days);
		ResponseEntity<Order[]> orderResponseEntity = restTemplate.getForEntity(url, Order[].class);
		Order[] orders = orderResponseEntity.getBody();
		List<OrderDetailWithWasherModel> results = new ArrayList<>();
		int length = orders.length;

		for (int index = 0; index < length; index++) {
			results.add(buildOrderDetailWithWasher(orders[index]));
		}

		return results;
	}

	private OrderDetailWithWasherModel buildOrderDetailWithWasher(Order order) {
		// City city = null;
		// District district = null;
		// Province province = null;
		// ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
		// WashCarService washCarService = null;

		// String url = opsUrlPrefix + "wasshcarservice/service/" +
		// String.valueOf(order.getServiceId());
		// washCarService = restTemplate.getForObject(url,
		// WashCarService.class);
		String url = null;

		url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
		vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

		url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
		vehicleType = restTemplate.getForObject(url, VehicleType.class);

		// url = opsUrlPrefix + "location/province/" +
		// String.valueOf(order.getProvinceId());
		// province = restTemplate.getForObject(url, Province.class);
		//
		// url = opsUrlPrefix + "location/city/" +
		// String.valueOf(order.getCityId());
		// city = restTemplate.getForObject(url, City.class);
		//
		// url = opsUrlPrefix + "location/district/" +
		// String.valueOf(order.getDistrictId());
		// district = restTemplate.getForObject(url, District.class);
		//
		// url = opsUrlPrefix + "location/resiquarter/" +
		// String.valueOf(order.getResiQuartId());
		// resiQuarter = restTemplate.getForObject(url,
		// ResidentialQuarter.class);

		url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
		Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);

		url = promUrlPrefix + "coupon/" + String.valueOf(order.getCouponId());
		Coupon coupon = restTemplate.getForObject(url, Coupon.class);
		Coupon[] coupons = { coupon };

		// url = promUrlPrefix + "promotion/" +
		// String.valueOf(order.getPromotionId());
		// Promotion promotion = restTemplate.getForObject(url,
		// Promotion.class);
		// Promotion[] promotions = { promotion };
		url = promUrlPrefix + "promotion/mylistwithservices/" + String.valueOf(order.getUserId());
		ResponseEntity<PromotionWithServices[]> promotionResponseEntity = restTemplate.getForEntity(url,
				PromotionWithServices[].class);
		PromotionWithServices[] promotions = promotionResponseEntity.getBody();

		url = opsUrlPrefix + "worker/washedorder/" + String.valueOf(order.getId());
		Worker worker = restTemplate.getForObject(url, Worker.class);

		url = crmUrlPrefix + "user/info/" + String.valueOf(order.getUserId());
		User user = restTemplate.getForObject(url, User.class);

		url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
		LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);

		return EntityMapper.buildOrderDetailWithWasher(order, getServicesName(order.getServiceId()), vehicle,
				vehicleCategory, vehicleType, locationModel, coupons, promotions, worker, null, user);

		// return EntityMapper.buildOrderDetailWithWasher(order,
		// washCarService.getName(), vehicle, vehicleCategory,
		// vehicleType, province, city, district, resiQuarter, coupons,
		// promotions, worker, washCarService, user);
	}

	private double countOriginPrice(String[] ids, int vehicleTypeId, int vehicleCategoryId) {
		String url = opsUrlPrefix + "pricing/price/" + String.valueOf(vehicleTypeId) + "/"
				+ String.valueOf(vehicleCategoryId) + "/%s";

		double originalPrice = 0d;
		int length = ids.length;
		for (int index = 0; index < length; index++) {
			Double price = restTemplate.getForObject(url, Double.class);
			originalPrice += price.doubleValue();
		}

		return originalPrice;
	}

	private double countDiscountedPriceAccordingToPromotions(int userId) {
		String url = promUrlPrefix + "mylistwithservices/" + String.valueOf(userId);
		ResponseEntity<PromotionWithServices[]> promotionsWithServicesResponseEntity = restTemplate.getForEntity(url,
				PromotionWithServices[].class);
		PromotionWithServices[] promotionsWithServices = promotionsWithServicesResponseEntity.getBody();
		return 0d;
	}

	// @Override
	// public Pricing pricing(String validId, int orderId, int couponId, int
	// promotionId) {
	// Pricing pricing = new Pricing();
	//
	// // get user id by valid id
	// User user = getUserId(validId);
	// if (user == null) {
	// throw new AAInnerServerError(String.format("User not found by valid id
	// [%s]", validId));
	// }
	//
	// String url = null;
	// // get origin price by order
	// url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
	// Order order = restTemplate.getForObject(url, Order.class);
	// pricing.setBookedTime(order.getBookTime() == null ? null :
	// EntityMapper.formatTimestamp(order.getBookTime()));
	// url = opsUrlPrefix + "wasshcarservice/service/" +
	// String.valueOf(order.getServiceId());
	// WashCarService washCarService = restTemplate.getForObject(url,
	// WashCarService.class);
	// pricing.setServiceName(washCarService.getName());
	// pricing.setOrderId(order.getId());
	//// pricing.setServiceId(order.getServiceId());
	// pricing.setUserId(order.getUserId());
	//
	// double originPrice = order.getPrice();
	// pricing.setOriginalPrice(order.getPrice());
	// pricing.setDiscountedPrice(originPrice);
	// if (originPrice <= 0) {
	// throw new AAInnerServerError(String.format("Invalid origin price [%d]",
	// originPrice));
	// }
	//
	// if (promotionId > 0) {
	// // check the promotion id matches the service
	// // check promotion still available
	// // check promotion remaining counts available
	// url = promUrlPrefix + "promotion/validpromotion/" +
	// String.valueOf(user.getId()) + "/"
	// + String.valueOf(order.getServiceId()) + "/" +
	// String.valueOf(promotionId);
	// Integer result = restTemplate.getForObject(url, Integer.class);
	// // if (result == null) {
	// //// throw new AAInnerServerError("Failed to validate promotion.");
	// // } else {
	// // pricing.setDiscountedPrice(0d);
	// // pricing.setPromotionId(promotionId);
	// // return pricing;
	// // }
	// if (result != null) {
	// pricing.setDiscountedPrice(0d);
	// pricing.setPromotionId(promotionId);
	// return pricing;
	// }
	// }
	//
	// if (couponId > 0) {
	// url = promUrlPrefix + "coupon/validatecoupon/" +
	// String.valueOf(user.getId()) + "/"
	// + String.valueOf(couponId);
	// Coupon coupon = restTemplate.getForObject(url, Coupon.class);
	// if (coupon != null) {
	// pricing.setCouponId(couponId);
	// } else {
	// throw new AAInnerServerError("Failed to validate coupon.");
	// }
	//
	// if (0 == coupon.getCouponType()) {
	// // free~
	// pricing.setDiscountedPrice(0d);
	//
	// return pricing;
	// } else if (1 == coupon.getCouponType()) {
	// double discounted = originPrice - coupon.getValue();
	// if (discounted < 0) {
	// throw new AAInnerServerError("Invalid dicounted price.");
	// }
	// pricing.setDiscountedPrice(discounted);
	// return pricing;
	// } else if (2 == coupon.getCouponType()) {
	// double discounted = originPrice * ((100 - coupon.getValue()) / 100);
	//
	// if (discounted < 0) {
	// throw new AAInnerServerError("Invalid dicounted price.");
	// }
	// pricing.setDiscountedPrice(discounted);
	// }
	//
	// }
	//
	// return pricing;
	// }

	@Override
	public synchronized WechatPayResponseModel pay(Pricing pricing) {
		// everything happens here needs to be controlled by distribution
		// transaction
		System.out.println(pricing.getPaymentType());
		if (pricing.getPaymentType() == 50) {
			// NO FEE order
			WechatPayResponseModel offLine = new WechatPayResponseModel();
			offLine.setResult_code("SUCCESS");
			offLine.setReturn_msg("OK");
			offLine.setAppid(WechatConfig.getAppid());
			offLine.setMch_id(WechatConfig.getMchid());
			offLine.setReturn_code("SUCCESS");
			offLine.setPrepay_id("prepay_id=NOFEE_" + String.valueOf(System.currentTimeMillis()));
			offLine.setTrade_type("NOFEE");
			offLine.setTimeStamp(System.currentTimeMillis() / 1000 + "");

			updateOrder(pricing, 20);
			recordNoFeeOrderTransaction(offLine, pricing);
			recordPointTransaction(0d, pricing.getUserId(), pricing.getOrderId());
			if (pricing.getPromotionId() != 0) {
				// Original price will be counted as points since promotion
				// services were purchased by user
				recordPoints(pricing.getUserId(), pricing.getOriginalPrice());
				String[] ids = ServiceUtil.getServiceIDs(pricing.getServiceId());
				int length = ids.length;
				for (int index = 0; index < length; index++) {
					recordServiceTransaction(Integer.valueOf(ids[index]), pricing.getUserId(), pricing.getOrderId());
					consumeService(pricing.getUserId(), Integer.valueOf(ids[index]), pricing.getPromotionId());
				}
			}

			if (pricing.getCouponId() != 0) {
				// No points will be counted when a No Fee coupon is used
				recordCouponTransaction(pricing.getCouponId(), pricing.getUserId(), pricing.getOrderId());
				consumeCoupon(pricing.getUserId(), pricing.getCouponId());
			}
			return offLine;
		} else if (pricing.getDiscountedPrice() == 0) {
			// NO FEE order
			WechatPayResponseModel noFee = new WechatPayResponseModel();
			noFee.setResult_code("SUCCESS");
			noFee.setReturn_msg("OK");
			noFee.setAppid(WechatConfig.getAppid());
			noFee.setMch_id(WechatConfig.getMchid());
			noFee.setReturn_code("SUCCESS");
			noFee.setPrepay_id("prepay_id=NOFEE_" + String.valueOf(System.currentTimeMillis()));
			noFee.setTrade_type("NOFEE");
			noFee.setTimeStamp(System.currentTimeMillis() / 1000 + "");

			updateOrder(pricing, 20);
			recordNoFeeOrderTransaction(noFee, pricing);
			recordPointTransaction(0d, pricing.getUserId(), pricing.getOrderId());
			if (pricing.getPromotionId() != 0) {
				// Original price will be counted as points since promotion
				// services were purchased by user
				recordPoints(pricing.getUserId(), pricing.getOriginalPrice());
				String[] ids = ServiceUtil.getServiceIDs(pricing.getServiceId());
				int length = ids.length;
				for (int index = 0; index < length; index++) {
					recordServiceTransaction(Integer.valueOf(ids[index]), pricing.getUserId(), pricing.getOrderId());
					consumeService(pricing.getUserId(), Integer.valueOf(ids[index]), pricing.getPromotionId());
				}
			}

			if (pricing.getCouponId() != 0) {
				// No points will be counted when a No Fee coupon is used
				recordCouponTransaction(pricing.getCouponId(), pricing.getUserId(), pricing.getOrderId());
				consumeCoupon(pricing.getUserId(), pricing.getCouponId());
			}
			return noFee;
		} else {
			updateOrder(pricing, 10);
			return getUnifiedOrder(pricing);
		}
	}

	/**
	 * Increases the count in prom_r_user_consume_service
	 * 
	 * @param userId
	 * @param serviceId
	 * @param promtionId
	 */
	private void consumeService(int userId, int serviceId, int promotionId) {
		String url = promUrlPrefix + "promotion/consumepromotionservice/" + String.valueOf(userId) + "/"
				+ String.valueOf(serviceId) + "/" + String.valueOf(promotionId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		if (codeResponse.getStatusCodeValue() != 1) {
			// Log error
		}
	}

	/**
	 * Disables record in prom_r_user_coupon
	 * 
	 * @param userId
	 * @param couponId
	 */
	private void consumeCoupon(int userId, int couponId) {
		String url = promUrlPrefix + "coupon/consumeone/" + String.valueOf(userId) + "/" + String.valueOf(couponId);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		if (codeResponse.getStatusCodeValue() != 1) {
			// Log error
		}
	}

	private void recordPointTransaction(double amount, int userId, int orderId) {
		String url = omsUrlPrefix + "transaction/recordPointTransaction";
		PointTransaction pointTransaction = new PointTransaction();
		pointTransaction.setAmount(amount);
		pointTransaction.setUserId(userId);
		pointTransaction.setOrderId(orderId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<PointTransaction> entity = new HttpEntity<PointTransaction>(pointTransaction, headers);
		restTemplate.postForObject(url, entity, Integer.class);
	}

	private void recordServicesTransaction(String servicesIds, int userId, int orderId) {
		// String url = omsUrlPrefix + "transaction/recordServiceTransaction";
		// ServiceTransaction serviceTransaction = new ServiceTransaction();
		// serviceTransaction.setOrderId(orderId);
		// serviceTransaction.setServiceId(serviceId);
		// serviceTransaction.setUserId(userId);
		//
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_JSON);
		//
		// HttpEntity<ServiceTransaction> entity = new
		// HttpEntity<ServiceTransaction>(serviceTransaction, headers);
		// restTemplate.postForObject(url, entity, Integer.class);

		String[] ids = ServiceUtil.getServiceIDs(servicesIds);
		int length = ids.length;
		for (int index = 0; index < length; index++) {
			recordServiceTransaction(Integer.valueOf(ids[index]), userId, orderId);
			// consumeService(userId, Integer.valueOf(ids[index]),
			// order.getPromotionId());
		}
	}

	private void recordServiceTransaction(int serviceId, int userId, int orderId) {
		String url = omsUrlPrefix + "transaction/recordServiceTransaction";
		ServiceTransaction serviceTransaction = new ServiceTransaction();
		serviceTransaction.setOrderId(orderId);
		serviceTransaction.setServiceId(serviceId);
		serviceTransaction.setUserId(userId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ServiceTransaction> entity = new HttpEntity<ServiceTransaction>(serviceTransaction, headers);
		restTemplate.postForObject(url, entity, Integer.class);
	}

	private void recordCouponTransaction(int couponId, int userId, int orderId) {
		String url = omsUrlPrefix + "transaction/recordCouponTransaction";
		CouponTransaction couponTransaction = new CouponTransaction();
		couponTransaction.setOrderId(orderId);
		couponTransaction.setCouponId(couponId);
		couponTransaction.setUserId(userId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CouponTransaction> entity = new HttpEntity<CouponTransaction>(couponTransaction, headers);
		restTemplate.postForObject(url, entity, Integer.class);
	}

	private void recordNoFeeOrderTransaction(WechatPayResponseModel payResponse, Pricing pricing) {
		String url = omsUrlPrefix + "transaction/recordOrderTransaction";
		OrderTransaction orderTxn = new OrderTransaction();
		orderTxn.setOrderId(pricing.getOrderId());
		orderTxn.setAmount(pricing.getDiscountedPrice());
		orderTxn.setBankType(NO_FEE);
		if (pricing.getPromotionId() != 0) {
			orderTxn.setFeeType(NO_FEE + "_" + "PROM_ID" + String.valueOf(pricing.getPromotionId()));
		} else if (pricing.getCouponId() != 0) {
			orderTxn.setFeeType(NO_FEE + "_" + "COUP_ID" + String.valueOf(pricing.getCouponId()));
		} else {
			// should not happen, just in case.
			orderTxn.setFeeType(NO_FEE);
		}

		orderTxn.setIsSubscribe("N");
		orderTxn.setMchId(payResponse.getMch_id());
		orderTxn.setNonceStr(payResponse.getNonce_str());
		orderTxn.setOpenid(pricing.getValidId());
		orderTxn.setOutTradeNo(NO_FEE);
		orderTxn.setPaied(false);
		orderTxn.setPayType(1);
		orderTxn.setSign(payResponse.getSign());
		orderTxn.setSubMchId(NO_FEE);
		orderTxn.setTimeEnd(EntityMapper.formatTimestamp(new Timestamp(System.currentTimeMillis())));
		orderTxn.setTotalFee(String.valueOf(pricing.getDiscountedPrice()));
		orderTxn.setTradeType(NO_FEE);
		orderTxn.setTransactionId(NO_FEE + "_" + pricing.getOrderId());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderTransaction> entity = new HttpEntity<OrderTransaction>(orderTxn, headers);
		restTemplate.postForObject(url, entity, Integer.class);
	}

	private void recordFeeOrderTransaction(Pricing pricing) {
		// check if order transaction exists, because the payment might
		// interrupted or cancelled by user or unexpected connection issue.
		String url = omsUrlPrefix + "transaction/findOrderTransactionId/" + String.valueOf(pricing.getOrderId());
		int orderTransactionId = restTemplate.getForObject(url, Integer.class);
		if (orderTransactionId > 0) {
			// exists
			url = omsUrlPrefix + "transaction/updateOrderTransactionAmount/" + String.valueOf(orderTransactionId) + "/"
					+ String.valueOf(pricing.getDiscountedPrice());
			restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		} else {
			// add new one
			OrderTransaction orderTxn = new OrderTransaction();
			orderTxn.setOrderId(pricing.getOrderId());
			orderTxn.setAmount(pricing.getDiscountedPrice());
			orderTxn.setPayType(1);
			orderTxn.setOpenid(pricing.getValidId());

			url = omsUrlPrefix + "transaction/recordOrderTransaction";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<OrderTransaction> entity = new HttpEntity<OrderTransaction>(orderTxn, headers);
			restTemplate.postForObject(url, entity, Integer.class);
		}
	}

	private void completeFeeOrderTransaction(WechatNotify notify, int orderId) {
		String url = omsUrlPrefix + "transaction/updateOrderTransaction";

		OrderTransaction orderTxn = new OrderTransaction();
		orderTxn.setBankType(notify.getBank_type());
		orderTxn.setFeeType(notify.getFee_type());
		orderTxn.setIsSubscribe(notify.getIs_subscribe());
		orderTxn.setMchId(notify.getMch_id());
		orderTxn.setNonceStr(notify.getNonce_str());
		orderTxn.setOutTradeNo(notify.getOut_trade_no());
		orderTxn.setPaied(true);
		orderTxn.setSign(notify.getSign());
		orderTxn.setSubMchId(notify.getSub_mch_id());
		orderTxn.setTimeEnd(notify.getTime_end());
		orderTxn.setTotalFee(notify.getTotal_fee());
		orderTxn.setTradeType(notify.getTrade_type());
		orderTxn.setTransactionId(notify.getTransaction_id());
		orderTxn.setOrderId(orderId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderTransaction> entity = new HttpEntity<OrderTransaction>(orderTxn, headers);
		restTemplate.postForObject(url, entity, Integer.class);
	}

	private void updateOrder(Pricing pricing, int status) {
		String url = omsUrlPrefix + "order/updateorder";
		UpdateOrder orderToUpdate = new UpdateOrder();
		orderToUpdate.setId(pricing.getOrderId());
		orderToUpdate.setDiscountedPrice(pricing.getDiscountedPrice());
		orderToUpdate.setPromotionId(pricing.getPromotionId());
		orderToUpdate.setCouponId(pricing.getCouponId());
		orderToUpdate.setServiceId(pricing.getServiceId());
		orderToUpdate.setStatusCode(status);
		orderToUpdate.setPrice(pricing.getOriginalPrice());
		orderToUpdate.setPaymentType(pricing.getPaymentType());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UpdateOrder> entity = new HttpEntity<UpdateOrder>(orderToUpdate, headers);
		restTemplate.put(url, entity);
	}

	private void recordPoints(int userId, double userPoints) {
		String url = promUrlPrefix + "points/pointid/" + String.valueOf(userId);
		int pointsId = restTemplate.getForObject(url, Integer.class);

		if (pointsId > 0) {
			// exists, update points
			url = promUrlPrefix + "points/pluspoints/" + String.valueOf(userId) + "/" + String.valueOf(userPoints);
			ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		} else {
			// not exists, insert new one
			url = promUrlPrefix + "points/new";
			Points newPoints = new Points();
			newPoints.setUserId(userId);
			newPoints.setUserPoints(userPoints);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Points> entity = new HttpEntity<Points>(newPoints, headers);
			int newPointsId = restTemplate.postForObject(url, entity, Integer.class);
		}
	}

	private void updateOrderStatusOnly(int orderId, int status) {
		String url = omsUrlPrefix + "order/udpatestatus/" + String.valueOf(orderId) + "/" + String.valueOf(status);
		ResponseEntity<Integer> codeResponse = restTemplate.exchange(url, HttpMethod.PUT, null, Integer.class);
		if (codeResponse.getStatusCodeValue() != 1) {
			// Log error
		}
		// String url = omsUrlPrefix + "order/updateorder";
		// UpdateOrder orderToUpdate = new UpdateOrder();
		// orderToUpdate.setStatusCode(status);
		// orderToUpdate.setId(orderId);
		// orderToUpdate.setPrice(null);
		// orderToUpdate.setOrderNumber(null);
		//
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_JSON);
		//
		// HttpEntity<UpdateOrder> entity = new
		// HttpEntity<UpdateOrder>(orderToUpdate, headers);
		// restTemplate.put(url, entity);
	}

	/**
	 * Everything happens here should be controlled by distribution
	 * transaction...
	 */
	@Override
	public void proccessWechatNotification(WechatNotify notify, int orderId) {
		// update order status to 20
		updateOrderStatusOnly(orderId, 20);

		// order_transaction
		completeFeeOrderTransaction(notify, orderId);

		String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
		Order order = restTemplate.getForObject(url, Order.class);

		// point_transaction
		recordPointTransaction(order.getDiscountedPrice(), order.getUserId(), orderId);

		// service_transaction
		recordServicesTransaction(order.getServiceId(), order.getUserId(), orderId);

		// prom_points
		recordPoints(order.getUserId(), order.getDiscountedPrice());

		// prom_r_user_consume_service
		// consumedcount + 1
		if (order.getPromotionId() > 0) {
			// usually should not happen for FEE order, just in case.
			// recordServicesTransaction(order.getServiceId(),
			// order.getUserId(), orderId);
			// consumeServices(order.getUserId(), order.getServiceId(),
			// order.getPromotionId());
			String[] ids = ServiceUtil.getServiceIDs(order.getServiceId());
			int length = ids.length;
			for (int index = 0; index < length; index++) {
				// recordServiceTransaction(Integer.valueOf(ids[index]),
				// order.getUserId(), orderId);
				consumeService(order.getUserId(), Integer.valueOf(ids[index]), order.getPromotionId());
			}
		}

		// prom_r_user_coupon -> disable
		if (order.getCouponId() > 0) {
			recordCouponTransaction(order.getCouponId(), order.getUserId(), orderId);
			consumeCoupon(order.getUserId(), order.getCouponId());
		}
	}

	private WechatPayResponseModel getUnifiedOrder(Pricing pricing) {
		// check if transaction record exists, if true update it
		// else, insert a transaction record into oms_order_transaction with
		// order id;
		recordFeeOrderTransaction(pricing);

		String notify = "https://www.aawashcar.com/v2/order/wechatnotifyorder/" + String.valueOf(pricing.getOrderId());
		String out_trade_no = new Date().getTime() + "";

		String validId = pricing.getValidId();
		String body = "AA洗车订单号：" + String.valueOf(pricing.getOrderId());
		DecimalFormat df = new DecimalFormat();
		String total = df.format(pricing.getDiscountedPrice() * 100);

		// TODO change the price back to total!!!
		String res = payService.unifiedorder(notify, validId, body, "1", out_trade_no);
		System.out.println(res);
		Object object = XMLUtil.xmlToBean(WechatPayResponse.class, res);

		WechatPayResponseModel responseModel = null;
		if (object instanceof WechatPayResponse) {
			WechatPayResponse wechatPay = (WechatPayResponse) object;

			String timeStamp = System.currentTimeMillis() / 1000 + "";// 时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
			String nonceStr = wechatPay.getNonce_str();
			String _package = "prepay_id=" + wechatPay.getPrepay_id(); // 统一下单接口返回的
																		// prepay_id
																		// 参数值，提交格式如：prepay_id=*
			String signType = "MD5";// 签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
			Map<String, String> signMap = new TreeMap<String, String>();
			signMap.put("appId", wechatPay.getAppid());
			signMap.put("timeStamp", timeStamp);
			signMap.put("nonceStr", nonceStr);
			signMap.put("package", _package);
			signMap.put("signType", signType);
			String paySign = null;
			try {
				paySign = WXPayUtil.generateSignature(signMap, WechatConfig.getKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;// 签名,具体签名方案参见微信公众号支付帮助文档;

			responseModel = new WechatPayResponseModel();
			responseModel.setTimeStamp(timeStamp);
			responseModel.setAppid(wechatPay.getAppid());
			responseModel.setMch_id(wechatPay.getMch_id());
			responseModel.setNonce_str(wechatPay.getNonce_str());
			responseModel.setPrepay_id(wechatPay.getPrepay_id());
			responseModel.setResult_code(wechatPay.getResult_code());
			responseModel.setReturn_code(wechatPay.getReturn_code());
			responseModel.setTrade_type(wechatPay.getTrade_type());
			responseModel.setReturn_msg(wechatPay.getReturn_msg());
			responseModel.setSign(paySign);
		}

		return responseModel;
	}

	@Override
	public Pricing pricing(String validId, String serviceIds, int couponId, int promotionId, double originPrice,
			int vehicleTypeId, int vehicleCategoryId) {
		Pricing pricing = new Pricing();

		// get user id by valid id
		User user = getUserId(validId);
		if (user == null) {
			throw new AAInnerServerError(String.format("User not found by valid id [%s]", validId));
		}

		pricing.setValidId(validId);
		pricing.setUserId(user.getId());
		pricing.setOriginalPrice(originPrice);
		pricing.setDiscountedPrice(originPrice);
		if (originPrice <= 0) {
			throw new AAInnerServerError(String.format("Invalid origin price [%d]", originPrice));
		}

		pricing.setServiceName(getServicesName(serviceIds));
		pricing.setServiceId(serviceIds);

		if (promotionId > 0) {
			pricing.setPromotionId(promotionId);
			pricingByPromotion(serviceIds, promotionId, user.getId(), pricing, vehicleTypeId, vehicleCategoryId);
		} else if (couponId > 0) {
			pricing.setCouponId(couponId);
			pricingByCoupon(user.getId(), couponId, pricing, originPrice);
		} else {
			// throw new AAInnerServerError(String.format("Cannot use promotion
			// id and coupon id ", originPrice));
		}

		return pricing;
	}

	private void pricingByPromotion(String serviceIds, int promotionId, int userId, Pricing pricing, int vehicleTypeId,
			int vehicleCategoryId) {
		String url = null;

		// check the promotion id matches the service
		// check promotion still available
		// check promotion remaining counts available
		System.out.println(serviceIds);
		String[] ids = ServiceUtil.getServiceIDs(serviceIds);
		int length = ids.length;
		for (int index = 0; index < length; index++) {
			url = promUrlPrefix + "promotion/validpromotion/" + String.valueOf(userId) + "/" + ids[index] + "/"
					+ String.valueOf(promotionId);

			System.out.println(url);
			Integer result = restTemplate.getForObject(url, Integer.class);

			if (result != null) {
				double price = getPrice(vehicleTypeId, vehicleCategoryId, Integer.valueOf(ids[index]));
				double tempPrice = pricing.getDiscountedPrice() - price;
				pricing.setDiscountedPrice(tempPrice);
			} else {
				System.out.println("invalid promotion");
			}
		}
	}

	private double getPrice(int typeId, int categoryId, int serviceId) {
		String url = opsUrlPrefix + "pricing/price/" + String.valueOf(typeId) + "/" + String.valueOf(categoryId) + "/"
				+ String.valueOf(serviceId);
		return restTemplate.getForObject(url, double.class);
	}

	private void pricingByCoupon(int userId, int couponId, Pricing pricing, double originPrice) {
		String url = promUrlPrefix + "coupon/validatecoupon/" + String.valueOf(userId) + "/" + String.valueOf(couponId);
		Coupon coupon = restTemplate.getForObject(url, Coupon.class);
		if (coupon != null) {
			pricing.setCouponId(couponId);
		} else {
			throw new AAInnerServerError("Failed to validate coupon.");
		}

		if (0 == coupon.getCouponType()) {
			// free~
			pricing.setDiscountedPrice(0d);

		} else if (1 == coupon.getCouponType()) {
			double discounted = originPrice - coupon.getValue();
			if (discounted < 0) {
				throw new AAInnerServerError("Invalid dicounted price.");
			}
			pricing.setDiscountedPrice(discounted);
		} else if (2 == coupon.getCouponType()) {
			double discounted = originPrice * ((100 - coupon.getValue()) / 100);

			if (discounted < 0) {
				throw new AAInnerServerError("Invalid dicounted price.");
			}
			pricing.setDiscountedPrice(discounted);
		}
	}

}
