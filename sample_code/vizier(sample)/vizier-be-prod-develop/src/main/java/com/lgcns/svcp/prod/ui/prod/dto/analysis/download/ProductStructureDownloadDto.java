package com.lgcns.svcp.prod.ui.prod.dto.analysis.download;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Data;

@Data
@CustomTitleHeader
public class ProductStructureDownloadDto {
    @Value(name = "impactAnalysis.excel.no")
    private int no;

    @Value(name = "impactAnalysis.excel.offrCd")
    private String offrCd;
    @Value(name = "impactAnalysis.excel.offrNm")
    private String offrNm;

    @Value(name = "impactAnalysis.excel.cmpCd")
    private String cmpCd;
    @Value(name = "impactAnalysis.excel.cmpNm")
    private String cmpNm;
    @Value(name = "impactAnalysis.excel.cmpValdStrtDtm")
    private String cmpValdStrtDtm;
    @Value(name = "impactAnalysis.excel.cmpValdEndDtm")
    private String cmpValdEndDtm;

    @Value(name = "impactAnalysis.excel.svcCd")
    private String svcCd;
    @Value(name = "impactAnalysis.excel.svcNm")
    private String svcNm;
    @Value(name = "impactAnalysis.excel.svcValdStrtDtm")
    private String svcValdStrtDtm;
    @Value(name = "impactAnalysis.excel.svcValdEndDtm")
    private String svcValdEndDtm;
}
