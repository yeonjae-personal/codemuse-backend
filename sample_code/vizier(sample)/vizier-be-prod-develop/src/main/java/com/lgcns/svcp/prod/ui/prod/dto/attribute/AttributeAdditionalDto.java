package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeAdditionalDto extends BaseDto {
	
	private String attrUuid;
    private String itemCode;
    private String fieldTypeCode;
    private String commGroupCode;
    private Integer sortNo;
    private String useYn;
    private String attrMaxLength;
    private String requiredYn;
    
    @NotEmpty
    private String labelId;
    private String dispTab;
    private String dispCardYn;
    private String advSearchYn;
}
