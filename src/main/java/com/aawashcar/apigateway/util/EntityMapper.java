package com.aawashcar.apigateway.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.model.CityModel;
import com.aawashcar.apigateway.model.DefaultAddressModel;
import com.aawashcar.apigateway.model.DistrictModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.model.ProvinceModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;
import com.aawashcar.apigateway.model.ServiceModel;
import com.aawashcar.apigateway.model.UserModel;
import com.aawashcar.apigateway.model.VehicleCategoryModel;
import com.aawashcar.apigateway.model.VehicleTypeModel;

public class EntityMapper {

	public static UserModel convertUserToModel(User user) {
		UserModel userModel = new UserModel();
		userModel.setGender(user.getGenderId());
		userModel.setNickName(user.getNickName());
		return userModel;
	}

	public static List<VehicleCategoryModel> convertVehicleCategoryToModel(VehicleCategory[] vehicleCategories) {
		List<VehicleCategoryModel> categories = new ArrayList<>();
		int size = vehicleCategories.length;
		for (int index = 0; index < size; index++) {
			VehicleCategoryModel model = new VehicleCategoryModel();
			VehicleCategory category = vehicleCategories[index];
			model.setId(category.getId());
			model.setName(category.getName());
			model.setDefault(false);
			categories.add(model);
		}

		return categories;
	}

	public static List<VehicleTypeModel> convertVehicleTypeToModel(VehicleType[] vehicleTypes) {
		List<VehicleTypeModel> types = new ArrayList<>();
		int size = vehicleTypes.length;
		for (int index = 0; index < size; index++) {
			VehicleTypeModel model = new VehicleTypeModel();
			VehicleType type = vehicleTypes[index];
			model.setId(type.getId());
			model.setName(type.getName());
			model.setDefault(false);
			types.add(model);
		}

		return types;
	}
	
	public static List<MyPromotionModel> convertPromotionsToMyModel(Promotion[] promotions) {
		List<MyPromotionModel> myPromotions = new ArrayList<>();
		int size = promotions.length;
		for (int index = 0; index < size; index++) {
			MyPromotionModel model = new MyPromotionModel();
			Promotion promotion = promotions[index];
			model.setDescription(promotion.getDescription());
			model.setDuration(promotion.getDuration());
			model.setId(promotion.getId());
			model.setName(promotion.getName());
			model.setPrice(promotion.getPrice());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(promotion.getCreatedTime());
			calendar.add(Calendar.MONTH, promotion.getDuration());
			model.setValidatedTime(new Timestamp(calendar.getTimeInMillis()));
			
			myPromotions.add(model);
		}
		
		return myPromotions;
	}
	
	public static List<MyCouponModel> convertCouponsToMyModel(Coupon[] coupons) {
		List<MyCouponModel> myCoupons = new ArrayList<>();
		int size = coupons.length;
		for (int index = 0; index < size; index++) {
			MyCouponModel model = new MyCouponModel();
			Coupon coupon = coupons[index];
			model.setDescription(coupon.getDescription());
			model.setDuration(coupon.getDuration());
			model.setId(coupon.getId());
			model.setName(coupon.getName());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(coupon.getCreatedTime());
			calendar.add(Calendar.MONTH, coupon.getDuration());
			model.setValidatedTime(new Timestamp(calendar.getTimeInMillis()));
			
			myCoupons.add(model);
		}
		
		return myCoupons;
	}

	public static CityModel convertCityToModel(City city) {
		CityModel model = new CityModel();
		model.setId(city.getCityId());
		model.setName(city.getName());

		return model;
	}

	public static DistrictModel convertDistrictToModel(District district) {
		DistrictModel model = new DistrictModel();
		model.setId(district.getDistrictId());
		model.setName(district.getName());

		return model;
	}

	public static ProvinceModel convertProvinceToModel(Province province) {
		ProvinceModel model = new ProvinceModel();
		model.setId(province.getProvinceId());
		model.setName(province.getName());

		return model;
	}

	public static ResidentialQuarterModel convertResidentialQuarterToModel(ResidentialQuarter resiQuarter) {
		ResidentialQuarterModel model = new ResidentialQuarterModel();
		model.setId(resiQuarter.getResiQuatId());
		model.setName(resiQuarter.getName());

		return model;
	}

	public static List<ServiceModel> convertServiceToModel(WashCarService[] services) {
		List<ServiceModel> serviceModels = new ArrayList<>();
		int size = services.length;
		for (int index = 0; index < size; index++) {
			ServiceModel model = new ServiceModel();
			WashCarService service = services[index];
			model.setId(service.getId());
			model.setName(service.getName());
			model.setDefault(false);
			serviceModels.add(model);
		}

		return serviceModels;
	}

	public static List<PromotionModel> converPromotionToModel(Promotion[] promotions) {
		List<PromotionModel> promotionModels = new ArrayList<>();
		int size = promotions.length;
		for (int index = 0; index < size; index++) {
			PromotionModel model = new PromotionModel();
			Promotion promotion = promotions[index];
			model.setId(promotion.getId());
			model.setDescription(promotion.getDescription());
			model.setDuration(promotion.getDuration());
			model.setName(promotion.getName());
			model.setPrice(promotion.getPrice());
			promotionModels.add(model);
		}

		return promotionModels;
	}

	public static MainPageInfo buildMainPageInfo(User user, Order order, VehicleCategory[] categories,
	                                             VehicleType[] types,
	                                             WashCarService[] services,
	                                             City city,
	                                             District district,
	                                             Province province,
	                                             ResidentialQuarter resiQuart,
	                                             Vehicle vehicle) {
		MainPageInfo mainPageInfo = new MainPageInfo();
		mainPageInfo.setUser(convertUserToModel(user));
		mainPageInfo.setBookedTime(order.getBookTime());
		mainPageInfo.setServices(convertServiceToModel(services));
		mainPageInfo.setVehicleCategories(convertVehicleCategoryToModel(categories));
		mainPageInfo.setVehicleTypes(convertVehicleTypeToModel(types));
		int size = mainPageInfo.getServices().size();
		for (int index = 0; index < size; index++) {
			ServiceModel serviceModel = mainPageInfo.getServices().get(index);
			if (serviceModel.getId() == order.getServiceId()) {
				serviceModel.setDefault(true);
				break;
			}
		}

		size = mainPageInfo.getVehicleCategories().size();
		for (int index = 0; index < size; index++) {
			VehicleCategoryModel vehicleCategoryModel = mainPageInfo.getVehicleCategories().get(index);
			if (vehicleCategoryModel.getId() == vehicle.getCategoryId()) {
				vehicleCategoryModel.setDefault(true);
				break;
			}
		}

		size = mainPageInfo.getVehicleTypes().size();
		for (int index = 0; index < size; index++) {
			VehicleTypeModel vehicleTypeModel = mainPageInfo.getVehicleTypes().get(index);
			if (vehicleTypeModel.getId() == vehicle.getTypeId()) {
				vehicleTypeModel.setDefault(true);
				break;
			}
		}

		mainPageInfo.setLicense(vehicle.getLicense());
		mainPageInfo.setColor(vehicle.getColor());

		DefaultAddressModel defaultAddress = new DefaultAddressModel();
		defaultAddress.setCity(convertCityToModel(city));
		defaultAddress.setDistrict(convertDistrictToModel(district));
		defaultAddress.setProvicne(convertProvinceToModel(province));
		defaultAddress.setResidentialQuarter(convertResidentialQuarterToModel(resiQuart));
		defaultAddress.setDetailLocation(order.getDetailLocation());

		mainPageInfo.setDefaultAddress(defaultAddress);

		return mainPageInfo;
	}

	public static MainPageInfo buildDefaultMainPageInfo(
	                                                    VehicleCategory[] categories,
	                                                    VehicleType[] types,
	                                                    WashCarService[] services,
	                                                    City city,
	                                                    District district,
	                                                    Province province,
	                                                    ResidentialQuarter resiQuart) {
		MainPageInfo mainPageInfo = new MainPageInfo();
		mainPageInfo.setColor("");
		mainPageInfo.setLicense("");
		mainPageInfo.setBookedTime(null);
		mainPageInfo.setUser(new UserModel());
		mainPageInfo.setVehicleCategories(convertVehicleCategoryToModel(categories));
		mainPageInfo.setVehicleTypes(convertVehicleTypeToModel(types));
		mainPageInfo.setServices(convertServiceToModel(services));

		DefaultAddressModel defaultAddress = new DefaultAddressModel();
		defaultAddress.setCity(convertCityToModel(city));
		defaultAddress.setDistrict(convertDistrictToModel(district));
		defaultAddress.setProvicne(convertProvinceToModel(province));
		defaultAddress.setResidentialQuarter(convertResidentialQuarterToModel(resiQuart));
		defaultAddress.setDetailLocation("");

		mainPageInfo.setDefaultAddress(defaultAddress);
		return mainPageInfo;
	}

	public static void convertOrderModelToOrder(OrderModel model, Order order) {
		order.setBookTime(model.getBookTime());
		order.setOrderTime(model.getOrderTime());;
		order.setCityId(model.getCityId());
		order.setCountyId(1); // 默认为1，上海
		order.setDetailLocation(model.getDetailLocation());
		order.setDistrictId(model.getDistrictId());
		order.setPrice(model.getPrice());
		order.setProvinceId(model.getProvinceId());
		order.setResiQuartId(model.getResidentialQuarterId());
		order.setServiceId(model.getServiceId());
	}
}
