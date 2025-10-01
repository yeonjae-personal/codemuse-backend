package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.MCTGR_ITEM_CODE;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.context.RequestContextHolder;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.ui.prod.dto.common.structure.ItemStructureDto;

import lombok.RequiredArgsConstructor;

@Service("uiCommonService")
@RequiredArgsConstructor
public class UiCommonService {
    private final CommonDao commonDao;

    public String generateNextItemCode(Map<String, String> params) {
        return generateNextItemCode(params.get(ITEM_CODE));
    }

    public String generateNextItemCode(String itemCode) {
        String objCode = commonDao.select("Ui-common.generateNextItemCode",
                Collections.singletonMap(ITEM_CODE, itemCode));
//        if (objCode == null) {
//            throw new BusinessException("Item code not found!");
//        }
        RequestContextHolder.setCode(objCode);
        return objCode;
    }

	public List<ItemStructureDto> retreiveItemStructure(String itemCode, String mctgrItemCode) {
        Map<String, String> param = new HashMap<>();
        param.put(ITEM_CODE, itemCode);
        param.put(MCTGR_ITEM_CODE, mctgrItemCode);
        return commonDao.selectList("Ui-common.retreiveItemStructure", param);
    }
}
