package com.lgcns.svcp.prod.ui.prod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdStruDWithItemsMDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String baseProdItemNm;
	private String trgtProdItemCd;
	private String trgtProdItemNm;
	private String trgtItemCode;
	private String trgtItemName;
	private String trgtTypeCode;
	private String trgtTypeName;
	private String trgtSubTypeCode;
	private String trgtSubTypeName;
	private String baseItemCode;
	private String baseItemName;
	private String baseTypeCode;
	private String baseTypeName;
	private String baseSubTypeCode;
	private String baseSubTypeName;
	private String sortNo;
	private String validStartDtm;
	private String validEndDtm;
	private String itemValidStartDtm;
	private String itemValidEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private int trgtProdItemCount;
	private int baseProdItemCount;
}
