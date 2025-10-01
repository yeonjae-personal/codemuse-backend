package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lgcns.svcp.prod.entity.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeDto extends BaseEntity {
	
	private String validCode;
	
	@NotBlank
	private String attrUuid;
	
	@NotBlank
	private String condType;
	
	@NotNull
	private Integer attrNo;
	
	@NotNull
	private String validStartDtm;
	
	private String actionItemCode;
	
	private Integer rangeStartVal;
	private Integer rangeEndVal;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date rangeStartDtm;
	
	private String rangeStartDtmStr;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date rangeEndDtm;
	
	private String rangeEndDtmStr;
	
	private String textCntn;
	
	private String[] multipleValues = new String[] {};
	
	private String validEndDtm;
	
	private String labelId;
	
	@NotBlank
	private String fieldTypeCode;
	
	private String commGroupCode;
	private String itemCodeName;
	private String largeItemCode;
	private String itemCodeAction;
	private Integer seqNo;
	
	private Boolean isExpired;
	private Date validEndDtmOrigin;
	private String requiredYn;
	private String attrMaxLength;
	private String useYn;
}
