package com.lgcns.svcp.prod.ui.prod.dto.component;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class ComponentResourceReq extends BaseDto {
    private String resourceUUID;
    private String componentUUID;
    private String validStartDtm;
    private String validEndDtm;
}
