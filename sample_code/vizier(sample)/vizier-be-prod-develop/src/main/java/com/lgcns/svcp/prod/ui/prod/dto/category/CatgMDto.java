package com.lgcns.svcp.prod.ui.prod.dto.category;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatgMDto extends BaseDto {
    private String ctgrNodeUuid;
    private String ctgrTabUuid;
    private String ctgrNodeName;
    private String hpstCtgrNodeUuid;
    private String tclsCtgrYn;
    private String chgDeptName;
    private String chgUser;
    private String ctgrOvwCntn;
    private String useYn;
}
