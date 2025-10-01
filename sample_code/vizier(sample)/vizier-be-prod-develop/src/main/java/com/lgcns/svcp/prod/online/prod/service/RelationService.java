package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.ProdCstcRelDDto;
import com.lgcns.svcp.prod.online.prod.dto.ProdDpndRelDDto;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class RelationService {
	@Autowired
	private CommonDao commonDao;
	
	public List<ProdDpndRelDDto> retrieveAllProdDpndRelDList(ProdDpndRelDDto prodDpndRelDDto) {
		List<ProdDpndRelDDto> resultList = commonDao.selectList("Relation.retrieveAllProdDpndRelDList", prodDpndRelDDto);
		return resultList;
	}
	
//	public List<ProdDpndRelDDto> retrieveProdDpndRelDList(ProdDpndRelDDto prodDpndRelDDto) {
//		List<ProdDpndRelDDto> resultList = commonDao.selectPagedList("Relation.retrieveProdDpndRelDList", prodDpndRelDDto);
//		return resultList;
//	}
//	
//	public List<ProdDpndRelDDto> retrieveProdDpndRelD(ProdDpndRelDDto prodDpndRelDDto) {
//		List<ProdDpndRelDDto> resultList = commonDao.selectList("Relation.retrieveProdDpndRelD", prodDpndRelDDto);
//		return resultList;
//	}
//	
//	public List<ProdCstcRelDDto> retrieveAllProdCstcRelDList(ProdCstcRelDDto prodCstcRelDDto) {
//		List<ProdCstcRelDDto> resultList = commonDao.selectPagedList("Relation.retrieveAllProdCstcRelDList", prodCstcRelDDto);
//		return resultList;
//	}
//	
//	public List<RatSvcFctrRelDDto> retrieveAllRatSvcFctrRelDList(RatSvcFctrRelDDto ratSvcFctrRelDDto) {
//		List<RatSvcFctrRelDDto> resultList = commonDao.selectPagedList("Relation.retrieveAllRatSvcFctrRelDList", ratSvcFctrRelDDto);
//		return resultList;
//	}
}
