package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.group.InsertGroupOfferDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTargetReqDto {
    private String chgDeptName;
    private String chgUser;
	private List<OffrDpdcReqDto> addOffrDpdcLst;
	private List<OffrDpdcReqDto> updateOffrDpdcLst;
    private List<InsertGroupOfferDto> insertGroupOfferLst;
}
