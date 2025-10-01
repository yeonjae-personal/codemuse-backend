package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChngDataListL {
    private Long ChngDataSeq             ;
    private String ChngDataCode          ;
    private String ChngDataCodeName      ;
    private String ChngDataObjUuid       ;
    private String ChngDataItemCode      ;
    private String ChngDataTypeCode      ;
    private String ChngDataStusCode      ;
    private String ChngDataRqstUser      ;
    private String ChngDataRqstDeptName  ;
    private String CallApiUrl            ;
    private String CallApiMethod         ;
    private String CallApiQuery          ;
    private String CallApiBody           ;
    private String RgstUser              ;
    private String RgstDtm               ;
    private String UpdUser               ;
    private String UpdDtm                ;
}