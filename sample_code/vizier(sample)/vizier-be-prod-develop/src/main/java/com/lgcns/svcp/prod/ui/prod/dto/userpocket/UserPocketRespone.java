package com.lgcns.svcp.prod.ui.prod.dto.userpocket;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPocketRespone {
	
	private String largeItemCode;
	private String largeItemName;
	private Integer sortNo;
	private List<UserPocketDto> datas = new ArrayList<>();
}
