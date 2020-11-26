package com.yankaizhang.spring.webmvc.multipart.commons;

import com.yankaizhang.spring.core.LinkedMultiValueMap;
import com.yankaizhang.spring.core.MultiValueMap;
import com.yankaizhang.spring.util.StringUtils;
import com.yankaizhang.spring.webmvc.multipart.MultipartFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 这个Support类定义了使用apache-commons解析上传文件的基本操作
 * 根据这个类，可以继续扩充功能，所以这个类是抽象类
 * @author dzzhyk
 */
public abstract class CommonsFileUploadSupport {

    private static final String DEFAULT_CHARACTER_ENCODING = "utf-8";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 上传文件对象工厂
     */
    private final DiskFileItemFactory fileItemFactory;

    /**
     * 上传文件对象
     */
    private final FileUpload fileUpload;


    /**
     * 实例化工厂
     */
    public CommonsFileUploadSupport() {
        this.fileItemFactory = newFileItemFactory();
        this.fileUpload = newFileUpload(getFileItemFactory());
    }

    public DiskFileItemFactory getFileItemFactory() {
        return this.fileItemFactory;
    }

    public FileUpload getFileUpload() {
        return this.fileUpload;
    }

    /**
     * 上传文件最大大小
     * 单位：Byte
     * 默认-1表示无限大
     */
    public void setMaxUploadSize(long maxUploadSize) {
        this.fileUpload.setSizeMax(maxUploadSize);
    }

    /**
     * 设置每个文件的最大大小
     */
    public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
        this.fileUpload.setFileSizeMax(maxUploadSizePerFile);
    }

    /**
     * 设置在将上传内容写入磁盘之前允许的最大大小（以字节为单位）
     * 超过此数量仍将接收上载的文件，但不会存储在内存中
     * 根据Commons FileUpload，默认值为10240
     */
    public void setMaxInMemorySize(int maxInMemorySize) {
        this.fileItemFactory.setSizeThreshold(maxInMemorySize);
    }

    /**
     * 设置默认编码
     * 其实我这里就直接写死了utf-8
     */
    public void setDefaultEncoding(String defaultEncoding) {
        this.fileUpload.setHeaderEncoding(DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * 获取默认编码
     */
    protected String getDefaultEncoding() {
        String encoding = getFileUpload().getHeaderEncoding();
        if (encoding == null) {
            encoding = DEFAULT_CHARACTER_ENCODING;
        }
        return encoding;
    }

    protected DiskFileItemFactory newFileItemFactory() {
        return new DiskFileItemFactory();
    }

    /**
     * 创建上传文件实例的工厂方法
     * 需要子类实现
     */
    protected abstract FileUpload newFileUpload(FileItemFactory fileItemFactory);


    /**
     * 解析FileItem为内置的包装类MultipartParsingResult对象
     */
    protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
        MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<>();
        Map<String, String[]> multipartParameters = new HashMap<>();
        Map<String, String> multipartParameterContentTypes = new HashMap<>();

        // 解压FileItem对象
        for (FileItem fileItem : fileItems) {
            if (fileItem.isFormField()) {
                String value;
                String partEncoding = determineEncoding(fileItem.getContentType(), encoding);
                try {
                    value = fileItem.getString(partEncoding);
                }
                catch (UnsupportedEncodingException ex) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Could not decode multipart item '" + fileItem.getFieldName() +
                                "' with encoding '" + partEncoding + "': using platform default");
                    }
                    value = fileItem.getString();
                }
                String[] curParam = multipartParameters.get(fileItem.getFieldName());
                if (curParam == null) {
                    // simple form field
                    multipartParameters.put(fileItem.getFieldName(), new String[] {value});
                }
                else {
                    // array of simple form fields
                    String[] newParam = StringUtils.addStringToArray(curParam, value);
                    multipartParameters.put(fileItem.getFieldName(), newParam);
                }
                multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
            }
            else {
                // multipart file field
                CommonsMultipartFile file = createMultipartFile(fileItem);
                multipartFiles.add(file.getName(), file);
                logger.debug("Part '" + file.getName() + "', size " + file.getSize() +
                        " bytes, filename='" + file.getOriginalFilename() + "'" +
                        ", storage=" + file.getStorageDescription()
                );
            }
        }
        return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
    }

    /**
     * 根据FileItem对象创建一个CommonsMultipartFile包装类对象
     */
    protected CommonsMultipartFile createMultipartFile(FileItem fileItem) {
        return new CommonsMultipartFile(fileItem);
    }

    /**
     * 清理在解析上传文件过程中产生的MultipartFile对象
     */
    protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
        for (List<MultipartFile> files : multipartFiles.values()) {
            for (MultipartFile file : files) {
                if (file instanceof CommonsMultipartFile) {
                    CommonsMultipartFile cmf = (CommonsMultipartFile) file;
                    cmf.getFileItem().delete();
                    logger.debug(
                        "Cleaning up part '" + cmf.getName() +
                                "', filename '" + cmf.getOriginalFilename() + "'" +
                                ", stored " + cmf.getStorageDescription()
                    );
                }
            }
        }
    }

    /**
     * 判断编码
     */
    private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
        return defaultEncoding;
    }


    /**
     * 内置包装类MultipartParsingResult
     * multipartFiles 是文件名称对应的文件对象
     * multipartParameters 随着文件上传请求传来的参数map
     * multipartParameterContentTypes 附加请求参数的ContentMap
     */
    protected static class MultipartParsingResult {

        private final MultiValueMap<String, MultipartFile> multipartFiles;

        private final Map<String, String[]> multipartParameters;

        private final Map<String, String> multipartParameterContentTypes;

        public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles,
                                      Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {

            this.multipartFiles = mpFiles;
            this.multipartParameters = mpParams;
            this.multipartParameterContentTypes = mpParamContentTypes;
        }

        public MultiValueMap<String, MultipartFile> getMultipartFiles() {
            return this.multipartFiles;
        }

        public Map<String, String[]> getMultipartParameters() {
            return this.multipartParameters;
        }

        public Map<String, String> getMultipartParameterContentTypes() {
            return this.multipartParameterContentTypes;
        }
    }

}
