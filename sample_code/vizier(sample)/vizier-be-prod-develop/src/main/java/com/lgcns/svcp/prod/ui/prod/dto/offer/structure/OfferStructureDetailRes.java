package com.lgcns.svcp.prod.ui.prod.dto.offer.structure;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferStructureDetailRes {
    private String mctgrItemCode;
    private String mctgrItemName;

    private List<String> singleAcceptCode;
    private List<String> multipleAcceptCode;

    @JsonIgnore
    private String mAcceptCode;
    @JsonIgnore
    private String sAcceptCode;

    private List<OfferComponentStructureRes> componentList;

    public List<String> getMultipleAcceptCode() {
        if (multipleAcceptCode != null) {
            return multipleAcceptCode;
        }
        return parseListCode(mAcceptCode);
    }

    public List<String> getSingleAcceptCode() {
        if (singleAcceptCode != null) {
            return singleAcceptCode;
        }
        return parseListCode(sAcceptCode);
    }

    private List<String> parseListCode(String codeStr) {
        return codeStr == null ? null : Arrays.asList(codeStr.split(","));
    }
}
