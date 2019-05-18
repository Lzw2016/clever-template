package org.clever.template.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 18:50 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelModelDto extends BaseRowModel implements Serializable {

    @ExcelProperty(value = {"第1列"}, index = 0)
    @NotBlank(message = "第一列不能为空")
    private String column1;

    @ExcelProperty(value = {"第2列"}, index = 1)
    @NotBlank
    private String column2;

    @ExcelProperty(value = {"第3列"}, index = 2)
    @NotBlank
    @Length(min = 3, max = 5, message = "长度只能是3~5个字符")
    private String column3;

    @ExcelProperty(value = {"第4列"}, index = 3)
    @NotBlank
    private String column4;

    @ExcelProperty(value = {"第5列"}, index = 4)
    @NotBlank
    private String column5;

    @ExcelProperty(value = {"第6列"}, index = 5)
    @NotBlank
    private String column6;

    @ExcelProperty(value = {"第7列"}, index = 6)
    @NotBlank
    private String column7;

    @ExcelProperty(value = {"第8列"}, index = 7)
    @NotBlank
    private String column8;

    @ExcelProperty(value = {"第9列"}, index = 8)
    @NotBlank
    private String column9;
}
