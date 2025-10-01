package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.ProdItemMapgMDto;
import com.lgcns.svcp.prod.online.prod.dto.common.ItemCodeRequestDto;
import com.lgcns.svcp.prod.online.prod.dto.common.OfferInfoDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanDto;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class CommonService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private OfferService offerService;
	@Autowired
	private ResourceService resourceService;
	
	public List<ProdItemMapgMDto> retrieveProdItemMapgMList(ProdItemMapgMDto ProdItemMapgMDto) {
		List<ProdItemMapgMDto> resultList = commonDao.selectList("Common.retrieveProdItemMapgMList", ProdItemMapgMDto);
		return resultList;
	}
	
	public String getItemCodeByUuid(String uuid) {
		ItemCodeRequestDto request = new ItemCodeRequestDto();
		
		return commonDao.select("Common.getItemCodeByUuid", request);
	}
	
	public OfferInfoDto retrieveOfferInfo(PricePlanDto pricePlanDto) {
		OfferInfoDto result = new OfferInfoDto();
		
//		String bsfCd = commonDao.select("Common.getBaseFeeCodeByPricePlanCode", pricePlanDto);
//		String blngRscCd = commonDao.select("Common.getBillingElementCodeByPricePlanCode", pricePlanDto);
		
//		result.setPricePlan(offerService.retrievePricePlan());
//		result.setBillingElement(resourceService.retrieveBillingElement());
//		result.setBaseFee(priceService.retrieveBaseFee());
		
		return result;
	}
}
