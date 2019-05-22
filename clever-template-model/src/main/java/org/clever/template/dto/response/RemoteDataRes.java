package org.clever.template.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-21 10:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RemoteDataRes extends BaseResponse {

    private String column1;

    private String column2;

    private String column3;

    private String column4;

    private String column5;

    private String column6;

    private String column7;

    private String column8;

    private String column9;

    public RemoteDataRes() {

    }

    public RemoteDataRes(String key) {
        column1 = key + "column1";
        column2 = key + "column2";
        column3 = key + "column3";
        column4 = key + "column4";
        column5 = key + "column5";
        column6 = key + "column6";
        column7 = key + "column7";
        column8 = key + "column8";
        column9 = key + "column9";
    }
}
