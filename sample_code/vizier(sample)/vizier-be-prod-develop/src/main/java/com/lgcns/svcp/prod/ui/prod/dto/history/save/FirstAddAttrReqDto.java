package com.lgcns.svcp.prod.ui.prod.dto.history.save;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirstAddAttrReqDto extends BaseDto {
	private String objUuid;
	private String attrUuid;
    private String itemCode;
    private String mctgrItemCode;
    private String lctgrItemCode;
}
