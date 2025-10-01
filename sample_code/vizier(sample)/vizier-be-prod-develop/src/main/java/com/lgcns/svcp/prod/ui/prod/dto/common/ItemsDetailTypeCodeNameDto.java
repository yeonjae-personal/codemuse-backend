package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ItemsDetailTypeCodeNameDto {
	@JsonIgnore
	private String itemTypeNm;
	private String itemDetlTypeCdNm;
	private String itemDetlTypeCd;
}
