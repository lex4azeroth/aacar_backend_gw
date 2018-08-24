package com.aawashcar.apigateway.model;

import com.mysql.jdbc.StringUtils;

public class DefaultAddressModel {
	private CityModel city;
	private ProvinceModel provicne;
	private DistrictModel district;
//	private ResidentialQuarterModel residentialQuarter;
	private String detailLocation;

	public CityModel getCity() {
		return city;
	}

	public void setCity(CityModel city) {
		this.city = city;
	}

	public ProvinceModel getProvicne() {
		return provicne;
	}

	public void setProvicne(ProvinceModel provicne) {
		this.provicne = provicne;
	}

	public DistrictModel getDistrict() {
		return district;
	}

	public void setDistrict(DistrictModel district) {
		this.district = district;
	}

//	public ResidentialQuarterModel getResidentialQuarter() {
//		return residentialQuarter;
//	}
//
//	public void setResidentialQuarter(ResidentialQuarterModel residentialQuarter) {
//		this.residentialQuarter = residentialQuarter;
//	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s%s", 
				this.city.getName(), 
				this.district.getName(), 
				this.district.getResiQuarterModel().getName(), 
				StringUtils.isNullOrEmpty(this.detailLocation) ? "" : ", " + this.detailLocation);
	}

}
