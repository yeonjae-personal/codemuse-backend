package com.lgcns.svcp.prod.ui.prod.dto.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdCmmGrpDto {
    private String groupCode;
    private String commonCode;
    private String name;
    private String description;
    private String hposCode;
    private String refAttrCntn;
    private String startDate;
    private String endDate;
    private Integer sortNo;
}
