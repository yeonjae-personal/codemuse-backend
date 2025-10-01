package com.lgcns.svcp.prod.ui.prod.dto.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private String itemCode;
    private String itemName;
    private String cmcdDetlId;
    private String cmcdDetlNm;
    private String middleItemCode;
    private String middleItemName;
    private String largeItemCode;
    private String largeItemName;
    private int sortNo;
    private int middleSortNo;
    private int largeSortNo;
    private String useYn;
    private String strcTypeCode;
}
