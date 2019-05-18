package org.clever.template.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.excel.ExcelData;
import org.clever.common.utils.excel.ExcelDataReader;
import org.clever.common.utils.excel.ExcelDataWriter;
import org.clever.template.model.ExcelModelDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 17:48 <br/>
 */
@Api("Excel")
@RequestMapping("/api/excel")
@RestController
@Slf4j
public class ExcelImportExportController {

    @ApiOperation("Excel数据导入")
    @PostMapping("/import")
    public ExcelData<ExcelModelDto> excelImport(HttpServletRequest request) {
        ExcelDataReader<ExcelModelDto> excelDataReader = ExcelDataReader.readExcel(request, ExcelModelDto.class, 2000, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        return excelDataReader.getExcelData();
    }

    @ApiOperation("Excel数据导出")
    @GetMapping("/export")
    public void excelExport(HttpServletRequest request, HttpServletResponse response) {
        List<ExcelModelDto> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExcelModelDto excelModelDto = new ExcelModelDto();
            excelModelDto.setColumn1("Column1 - " + i);
            excelModelDto.setColumn2("Column2 - " + i);
            excelModelDto.setColumn3("Column3 - " + i);
            excelModelDto.setColumn4("Column4 - " + i);
            excelModelDto.setColumn5("Column5 - " + i);
            excelModelDto.setColumn6("Column6 - " + i);
            excelModelDto.setColumn7("Column7 - " + i);
            excelModelDto.setColumn8("Column8 - " + i);
            excelModelDto.setColumn9("Column9 - " + i);
            list.add(excelModelDto);
        }
        ExcelDataWriter.exportExcel(request, response, ExcelModelDto.class, list);
    }

    @ApiOperation("下载Excel模版")
    @GetMapping("/excel-templates")
    public void downloadExcelTemplates(HttpServletRequest request, HttpServletResponse response) {
        List<ExcelModelDto> list = new ArrayList<>();
        ExcelDataWriter.exportExcel(request, response, "Excel模版.xlsx", ExcelModelDto.class, list);
    }
}
