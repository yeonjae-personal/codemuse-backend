package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.publish.item.ChngDataListLDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubPackageDto {
	private String pubRqstTaskCode;
	private PubRqstTaskMDto pubRqstTaskMDto; // General
	private List<ChngDataListLDto> chngDataLstDtos; // Compose
	private PubAprvMDto pubAprvMDto; // Approval
	private PubPrcsTaskMDto pubPrcsTaskMDto; // Publish
}
