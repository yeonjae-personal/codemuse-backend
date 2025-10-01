package com.lgcns.svcp.prod.ui.prod.dto.export;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceExportDto {
    private int index;
    private List<GeneralDetailDto> general;
    private List<AdditionalDetailDto> additional;
}
