package com.lgcns.svcp.prod.online.prod.dto;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;

@Data
public class ProdDpndRelDDto extends BasePaginationDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String trgtProdItemCd;
	private String dpndRelDivsCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	public String getBaseUuid() {
	        return this.baseUuid;
	}
	public String setBaseUuid(String baseUuid) {
	        return this.baseUuid = baseUuid;
	}
	public String getTrgtUuid() {
	        return this.trgtUuid;
	}
	public String setTrgtUuid(String trgtUuid) {
	        return this.trgtUuid = trgtUuid;
	}
	public String getBaseProdItemCd() {
	        return this.baseProdItemCd;
	}
	public String setBaseProdItemCd(String baseProdItemCd) {
	        return this.baseProdItemCd = baseProdItemCd;
	}
	public String getTrgtProdItemCd() {
	        return this.trgtProdItemCd;
	}
	public String setTrgtProdItemCd(String trgtProdItemCd) {
	        return this.trgtProdItemCd = trgtProdItemCd;
	}
	public String getDpndRelDivsCd() {
	        return this.dpndRelDivsCd;
	}
	public String setDpndRelDivsCd(String dpndRelDivsCd) {
	        return this.dpndRelDivsCd = dpndRelDivsCd;
	}
	public String getValdStrtDtm() {
	        return this.valdStrtDtm;
	}
	public String setValdStrtDtm(String valdStrtDtm) {
	        return this.valdStrtDtm = valdStrtDtm;
	}
	public String getValdEndDtm() {
	        return this.valdEndDtm;
	}
	public String setValdEndDtm(String valdEndDtm) {
	        return this.valdEndDtm = valdEndDtm;
	}
	public String getRgstUsr() {
	        return this.rgstUsr;
	}
	public String setRgstUsr(String rgstUsr) {
	        return this.rgstUsr = rgstUsr;
	}
	public String getRgstDtm() {
	        return this.rgstDtm;
	}
	public String setRgstDtm(String rgstDtm) {
	        return this.rgstDtm = rgstDtm;
	}
	public String getUpdUsr() {
	        return this.updUsr;
	}
	public String setUpdUsr(String updUsr) {
	        return this.updUsr = updUsr;
	}
	public String getUpdDtm() {
	        return this.updDtm;
	}
	public String setUpdDtm(String updDtm) {
	        return this.updDtm = updDtm;
	}
}
