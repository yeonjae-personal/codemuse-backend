package com.lgcns.svcp.prod.ui.prod.dto.category;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtgrLevelDto extends BaseDto {
    private String ctgrTabUuid;
    private String levelNo;
    private String levelDscrCntn;
}
