package com.aawashcar.apigateway.util;

import java.util.ArrayList;
import java.util.List;

import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.model.CityModel;
import com.aawashcar.apigateway.model.DefaultAddressModel;
import com.aawashcar.apigateway.model.DistrictModel;
import com.aawashcar.apigateway.model.MainPageInfo;
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

	public static MainPageInfo buildMainPageInfo(User user) {
		MainPageInfo mainPageInfo = new MainPageInfo();
		mainPageInfo.setUser(convertUserToModel(user));
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
}
