package com.lgcns.svcp.prod.online.prod.dto.common;

import java.util.List;

import lombok.Data;

@Data
public class OfferInfoDto {
	List<?> pricePlan;
	List<?> baseFee;
	List<?> billingElement;
}
