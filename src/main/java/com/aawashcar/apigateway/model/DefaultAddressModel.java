package com.aawashcar.apigateway.model;

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

}
