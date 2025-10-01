package com.lgcns.svcp.prod.ui.prod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Menu {
	private Integer menuId;
	private String menuNm;
	private Integer hposMenuId;
	private Integer menuLv;
	private Integer sortNo;
	private String useYn;
	private String pagePath;
	private String menuIconPath;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}
