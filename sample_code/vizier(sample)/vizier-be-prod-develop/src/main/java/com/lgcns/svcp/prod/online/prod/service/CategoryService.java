package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.category.CtgrNodeMDto;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class CategoryService {
	@Autowired
	private CommonDao commonDao;

	public CtgrNodeMDto retrieveCtgrNodeMByCtgrNodeUuid(CtgrNodeMDto catgNodeMDto) {
		return commonDao.select("Category.retrieveCtgrNodeMByCtgrNodeUuid", catgNodeMDto);
	}

	public List<CtgrNodeMDto> retrieveCtgrNodeMList() {
		List<CtgrNodeMDto> resultList = commonDao.selectList("Category.retrieveCtgrNodeMList");
		return resultList;
	}
	
	public CtgrNodeMDto retrieveCtgrNodeMByObjUuid(CtgrNodeMDto catgNodeMDto) {
		return commonDao.select("Category.retrieveCtgrNodeMByObjUuid", catgNodeMDto);
	}
	
	public List<CtgrNodeMDto> retrieveCatgTreeList(CtgrNodeMDto catgNodeMDto) {
		List<CtgrNodeMDto> resultList = commonDao.selectList("Category.retrieveCtgrNodeMList", catgNodeMDto);
		return resultList;
	}
}
