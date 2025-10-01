package com.lgcns.svcp.prod.ui.prod.dto.component.update;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class ComponentUpdateResourceReq extends BaseDto {
    private String componentUUID;
    private String resourceUUID;
    private String validStartDtm;
    private String validEndDtm;
    private String workTypeCode;
}
