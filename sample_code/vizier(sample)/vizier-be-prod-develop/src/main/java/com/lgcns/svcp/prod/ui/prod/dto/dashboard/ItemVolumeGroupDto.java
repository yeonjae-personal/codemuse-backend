package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVolumeGroupDto {
	
	private String name;
	private double ratio;
	private Integer total = 0;
	List<ItemVolumeDto> items = new ArrayList<>();
}
