package org.clever.template.service;

import org.clever.template.dto.request.UploadFileReq;
import org.clever.template.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 上传文件存储接口<br/>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016/11/17 22:10 <br/>
 */
public interface IStorageService {


    /**
     * 保存文件，当文件较大时此方法会占用磁盘IO，因为common-fileupload会将上传文件写入硬盘的临时文件<br>
     * <p>
     * <b>注意：如果上传的文件在服务器端存在(通过文件签名判断)，就不会存储文件只会新增文件引用</b>
     *
     * @param uploadFileReq 请求上传参数
     * @param uploadTime    文件上传所用时间
     * @param multipartFile 上传的文件信息
     * @return 返回存储后的文件信息
     * @throws IOException 保存失败抛出异常
     */
    FileInfo saveFile(UploadFileReq uploadFileReq, long uploadTime, MultipartFile multipartFile) throws IOException;

    /**
     * 判断文件在服务端是否存在<br>
     */
    boolean isExists(FileInfo fileInfo);

    /**
     * 打开文件到OutputStream(限制打开文件速度，适用于客户端下载文件) 可以控制打开速度<br>
     * <b>注意：使用此方法会限制打开文件速度(字节/秒)</b>
     *
     * @param fileInfo     文件信息
     * @param outputStream 输出流，用于打开文件
     * @param off          读取起始字节(小于等于0就重头开始读取)
     * @param len          读取长度(小于等于0就读完数据流)
     * @param maxSpeed     最大打开文件速度(字节/秒)(值小于等于0则不限速)
     * @throws IOException 操作失败
     */
    void openFileSpeedLimit(FileInfo fileInfo, OutputStream outputStream, long off, long len, long maxSpeed) throws IOException;
}

