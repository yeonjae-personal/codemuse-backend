package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMatrixReqDto extends BasePaginationDto {
	private String matrixCodeName;
}
