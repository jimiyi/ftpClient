package com.best.oasis.express.ftp;

import com.best.oasis.express.util.ConfigFileUtil;
import com.best.oasis.express.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Map;

/**
 * FTP restful 客户端
 *
 * @author yiwei
 * @create 2017/8/25
 */
public class FTPClient {
    public static final Logger log = LoggerFactory.getLogger(FTPClient.class.getCanonicalName());

    static RestTemplate restTemplate = (RestTemplate) SpringContext.getBean("restTemplate");

    /**
     * 上传二进制流至ftp服务器
     *
     * @param fileName  文件名
     * @param content   文件的二进制字节
     * @param storePath 存储路径
     * @return
     */
    public static Map upload(final String fileName, byte[] content, String storePath) throws Exception {
        String url = ConfigFileUtil.getValue("ftpService.url") + "/uploadFile";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("storePath", storePath);

        ByteArrayResource arrayResource = new ByteArrayResource(content) {
            @Override
            public String getFilename() throws IllegalStateException {
                return fileName;
            }
        };
        params.add("file", arrayResource);
        params.add("uploadFileName", fileName);
        Map result = restTemplate.postForObject(url, params, Map.class);
        return result;
    }

    /**
     * 上传文件至ftp服务器
     *
     * @param file
     * @param storePath
     * @return
     * @throws Exception
     */
    public static Map upload(final File file, String storePath) throws Exception {
        String url = ConfigFileUtil.getValue("ftpService.url") + "/uploadFile";
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("storePath", storePath);
        params.add("file", resource);
        params.add("uploadFileName", resource.getFilename());
        Map result = restTemplate.postForObject(url, params, Map.class);
        return result;
    }

    /**
     * 往FTP服务器上的文件追加内容
     *
     * @param remoteFile FTP服务器上的文件，包含路径
     * @param content    追加内容的二进制流
     * @return
     * @throws Exception
     */
    public static Map append(final String remoteFile, byte[] content) throws Exception {
        String url = ConfigFileUtil.getValue("ftpService.url") + "/appendFile";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("remoteFile", remoteFile);

        ByteArrayResource arrayResource = new ByteArrayResource(content) {
            @Override
            public String getFilename() throws IllegalStateException {
                return "append";
            }
        };
        params.add("local", arrayResource);
        Map result = restTemplate.postForObject(url, params, Map.class);
        return result;
    }

    public static Map delete(final String remoteFile) throws Exception {
        String url = ConfigFileUtil.getValue("ftpService.url") + "/deleteFile?remoteFile=" + remoteFile;
        Map result = restTemplate.postForObject(url, null, Map.class);
        return result;
    }

    /**
     * 从ftp服务器下载文件
     *
     * @param fileName 文件名
     * @param ftpPath  该文件在ftp的上的路径，注意不包含文件名本身
     * @param destPath 文件本地的存储路径，注意不包含文件名本身,路径必须已存在
     */
    public static void download(final String fileName, String ftpPath, String destPath) {
        String url = ConfigFileUtil.getValue("ftpService.url") + "/downloadFile?path=" + ftpPath + "&fileName=" + fileName;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data;charset=UTF-8");
        headers.setContentType(type);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<byte[]>(headers), byte[].class);
        byte[] result = response.getBody();
        if (result != null) {
            InputStream inputStream = new ByteArrayInputStream(result);
            OutputStream outputStream = null;
            File file = new File(destPath + "/" + fileName);
            try {
                if (!file.exists()) {
                    File dir = new File(destPath);
                    dir.mkdirs();
                    file.createNewFile();
                }
                outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] buf = new byte[4096];
                while ((len = inputStream.read(buf, 0, 4096)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.flush();
            } catch (IOException e) {
                log.error("下载文件异常", e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    log.error("关闭文件流异常", e);
                }
            }
        }
    }
}
