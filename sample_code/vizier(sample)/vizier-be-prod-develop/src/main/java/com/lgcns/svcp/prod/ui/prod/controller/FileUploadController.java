package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureMDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiMatrixService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/upload")
@Tag(name = "UI-upload-controller", description = "화면 Upload 컨트롤러")
public class FileUploadController {
    private final UiMatrixService uiMatrixService;

    @PostMapping(path = "matrix-management/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<MatrixMeasureMDto> importMatrix(HttpServletRequest request) {
        return uiMatrixService.importMatrix(request);
    }

}
