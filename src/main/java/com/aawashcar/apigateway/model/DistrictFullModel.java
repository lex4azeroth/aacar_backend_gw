package com.aawashcar.apigateway.model;

import java.util.List;

public class DistrictFullModel extends BaseModel {
	private List<ResidentialQuarterModel> resiQuarterModels;

	public List<ResidentialQuarterModel> getResiQuarterModel() {
		return resiQuarterModels;
	}

	public void setResiQuarterModel(List<ResidentialQuarterModel> resiQuarterModels) {
		this.resiQuarterModels = resiQuarterModels;
	}
}
