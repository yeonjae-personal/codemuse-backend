package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVolumeDto {
	
	public ItemVolumeDto() {
	}
	
	public ItemVolumeDto(String name, double ration) {
		this.name = name;
		this.ratio = ration;
	}
	
	private String name;
	private double ratio;
}
