package com.lgcns.svcp.prod.ui.prod.dto.analysis;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImpactAnalysisResponseDto {
	private ItemDto parent;
	private List<ItemDto> siblings;
}
