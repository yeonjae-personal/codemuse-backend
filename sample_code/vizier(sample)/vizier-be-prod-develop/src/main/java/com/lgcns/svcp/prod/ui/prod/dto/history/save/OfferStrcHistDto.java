package com.lgcns.svcp.prod.ui.prod.dto.history.save;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferStrcHistDto extends BaseDto {
    private long workNo;
    private String baseUuid;
    private String trgtUuid;
    private String validStartDtm;
    private String validEndDtm;
    private String workTypeCode;
    private String updUserDeptName;
    private String attrValUpdUser;
}
