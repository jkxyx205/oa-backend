package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.config.Global;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rick on 2018/3/22.
 */
@Service
public class DocumentHandler {
    public static final String FOLDER_SEPARATOR = "/"; // path分割符

    @Resource
    private ImageHandler imageHandler;

    /***
     * 获取上传文件基本信息
     * @param file
     * @return
     */
    private Document getInfoFromFile(MultipartFile file) {
        Document document = new Document();
        document.setFullName(file.getOriginalFilename());
        document.setFileType(FileType.FILE);
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());

        return document;
    }


    /***
     * 存储文件
     * @param folderPath
     * @param file
     * @return
     */
    public Document store(String folderPath, MultipartFile file) throws IOException {
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String uuidName = UUID.randomUUID().toString();
        String uuidFullName = uuidName + "." +  ext;

        File folder = new File(Global.DOCUMENT + File.separator + folderPath);
        FileUtils.forceMkdir(folder);

        File srcFile = new File(folder, uuidFullName); //图片存储路径

		file.transferTo(srcFile);

        Document doc = getInfoFromFile(file);

        doc.setPath((folderPath+ File.separator + uuidName).replace(File.separator, FOLDER_SEPARATOR));

        if (doc.getContentType().startsWith("image")) {//处理图片文件
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    imageHandler.compress(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return doc;

    }



}
