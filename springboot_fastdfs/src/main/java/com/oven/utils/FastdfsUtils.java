package com.oven.utils;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

@Component
public class FastdfsUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传
     */
    public StorePath upload(MultipartFile file) throws IOException {
        // 设置文件信息
        Set<MetaData> mataData = new HashSet<>();
        mataData.add(new MetaData("author", "fastdfs"));
        mataData.add(new MetaData("description", file.getOriginalFilename()));
        // 上传
        return fastFileStorageClient.uploadFile(
                file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                null);
    }

    /**
     * 删除
     */
    public void delete(String path) {
        fastFileStorageClient.deleteFile(path);
    }

    /**
     * 删除
     */
    public void delete(String group, String path) {
        fastFileStorageClient.deleteFile(group, path);
    }

    /**
     * 文件下载
     *
     * @param path     文件路径
     * @param filename 下载的文件命名
     */
    public void download(String path, String filename, HttpServletResponse response) throws IOException {
        // 获取文件
        StorePath storePath = StorePath.parseFromUrl(path);
        if (StringUtils.isBlank(filename)) {
            filename = FilenameUtils.getName(storePath.getPath());
        }
        byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, DEFAULT_CHARSET));
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

}
