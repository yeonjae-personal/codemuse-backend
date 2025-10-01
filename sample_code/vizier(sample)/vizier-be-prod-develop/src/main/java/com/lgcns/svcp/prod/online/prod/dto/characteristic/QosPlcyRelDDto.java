package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;

@Data
public class QosPlcyRelDDto extends BasePaginationDto {
	private String qosCd;
	private String qosPlcyCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}