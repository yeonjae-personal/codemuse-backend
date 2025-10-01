package com.lgcns.svcp.prod.ui.prod.dto.dashboard.response;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.dashboard.ItemVolumeGroupDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVolumeRespone {
	
	public ItemVolumeRespone() {
	}
	
	public ItemVolumeRespone(int total, List<ItemVolumeGroupDto> data) {
		this.total = total;
		this.data = data;
	}
	
	private int total;
	private List<ItemVolumeGroupDto> data = new ArrayList<>(); 
}
