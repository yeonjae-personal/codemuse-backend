package com.lgcns.svcp.prod.online.prod.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.category.CtgrNodeMDto;
import com.lgcns.svcp.prod.online.prod.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/categories")
@Tag(name = "category-controller", description = "카테고리 컨트롤러")
/**
 * 
 * @Author	: 이훈민(A76485@cnspartners.com)
 * @Date	: 2024. 5. 9.
 */
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping(value = "")
	@Operation(summary = "카테고리 기본 전체 조회 API", description = "카테고리와 연결된 상위카테고리 정보 전체 리스트 조회")
	public ResponseEntity<List<CtgrNodeMDto>> retrieveCtgrNodeMList() {
		List<CtgrNodeMDto> response = categoryService.retrieveCtgrNodeMList();

		return ResponseEntity.ok(response);	
	}

	@GetMapping(value = "/{catgUuid}")
	@Operation(summary = "특정 카테고리UUID로 상세 정보조회 API", description = "카테고리 UUID로 Category Node 조회")
	public ResponseEntity<CtgrNodeMDto> retrieveCatgMByCatgUuid(@Parameter(description ="카테고리 UUID", required = true, example = "0230027b-b136-4011-b0fa-cedd9e2437df") @PathVariable("catgUuid") String catgUuid) {
		CtgrNodeMDto request = new CtgrNodeMDto();
		request.setCtgrNodeUuid(catgUuid);
		CtgrNodeMDto response = categoryService.retrieveCtgrNodeMByCtgrNodeUuid(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/by-offer/{prodItemUuid}")
	@Operation(summary = "특정 오퍼UUID로 상세 정보조회 API", description = "상품에 연결되어 있는 Category leaf node 조회")
	public ResponseEntity<CtgrNodeMDto> retrieveCatgMByProdItemUuid(@Parameter(description ="카테고리 UUID", required = true, example = "13263c65-18a5-4595-8961-431af88557f1") @PathVariable("prodItemUuid") String prodItemUuid) {
		CtgrNodeMDto request = new CtgrNodeMDto();
		request.setObjUuid(prodItemUuid);
		CtgrNodeMDto response = categoryService.retrieveCtgrNodeMByObjUuid(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/tree")
	@Operation(summary = "(화면) 카테고리 트리 전체 조회 API / *분리예정", description = "카테고리와 연결된 상위카테고리 정보 전체 리스트 조회")
	public ResponseEntity<List<Map<String, Object>>> retrieveCatgTreeList() {
		CtgrNodeMDto request = new CtgrNodeMDto();
		List<CtgrNodeMDto> response = categoryService.retrieveCatgTreeList(request);

		Map<String, List<CtgrNodeMDto>> categoryMap = response.stream()
				.collect(Collectors.groupingBy(dto -> dto.getHpstCtgrNodeUuid() != null ? dto.getHpstCtgrNodeUuid() : ""));
		List<Map<String, Object>> tree = buildTree(categoryMap, "");
		
		return ResponseEntity.ok(tree);	
	}

	/**
	 * 
	 * @Author	: 이훈민(A76485@cnspartners.com)
	 * @Date	: 2024. 5. 9.
	 * @MethodName	: buildTree
	 * @Method 설명	: 카테고리 트리 형태 메소드
	 * @Param	:
	 * @return	: List<Map<String,Object>>
	 */
	private List<Map<String, Object>> buildTree(Map<String, List<CtgrNodeMDto>> categoryMap, String parentId) {
		List<Map<String, Object>> result = new ArrayList<>();
		List<CtgrNodeMDto> subCategories = categoryMap.get(parentId);
		
		if (subCategories != null) {
			for (CtgrNodeMDto category : subCategories) {
				LinkedHashMap<String, Object> node = new LinkedHashMap<>();
				node.put("ctgrNodeUuid", category.getCtgrNodeUuid());
				node.put("ctgrNodeName", category.getCtgrNodeName());
				List<Map<String, Object>> children = buildTree(categoryMap, category.getCtgrNodeUuid());
				if (!children.isEmpty()) {
					node.put("children", children);
				}
				result.add(node);
			}
		}
		return result;
	}
}
