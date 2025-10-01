package com.lgcns.svcp.prod.ui.prod.dto.publish.item;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.publish.ChngDataStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChngDataListLDto extends BaseDto {
	private long chngDataSeq;
	private String chngDataCode;
	private String chngDataCodeName;
	private String chngDataTypeCode;
	private String chngDataObjUuid;
	private String chngDataItemCode;
	private ChngDataStusCode chngDataStusCode;
	private String chngDataRqstUser;
	private String callApiUrl;
	private String callApiMethod;
	private String callApiQuery;
	private String callApiBody;
}
