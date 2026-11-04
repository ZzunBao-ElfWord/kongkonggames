package com.pwyl.modules.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.file.utils.FileUploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里云文件上传
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "阿里云文件上传", tags = {"阿里云文件上传"})
@RequestMapping(value = "/alioss")
@Slf4j
public class AliFileUploadController {


    private final CommonInfoService commonRepository;

    @Autowired
    public AliFileUploadController(CommonInfoService commonRepository) {
        this.commonRepository = commonRepository;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file){
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //        String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            return Result.success().put("data",src);
        }else if("2".equals(value)){
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return Result.success().put("data",url);
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return Result.error(-100,"文件上传失败！");
            }
        }else{
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);
            

            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/pw/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return Result.success().put("data",path + putObjectRequest.getKey());
            } catch (IOException e) {
                return Result.error(-100,"文件上传失败！");
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }

    }




    @RequestMapping(value = "/uploadUniApp", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public String uploadUniApp(@RequestParam("file") MultipartFile file){
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return commonRepository.findOne(72).getValue()+"/"+completePath;
        }else if("2".equals(value)){
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return url;
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return null;
            }
        }else{
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/pw/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return path + putObjectRequest.getKey();
            } catch (IOException e) {
                return null;
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }

    }

    @RequestMapping(value = "/uploadMusic", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public Result uploadMusic(@RequestParam("file") MultipartFile file) {
        File file2=null;
        String url="";
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
                file2 = File.createTempFile("temp",null);
                file.transferTo(file2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            url = commonRepository.findOne(72).getValue()+"/"+completePath;

        }else if("2".equals(value)){
            try
            {

                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                url = http +fileName;
                file2=new File("/www/wwwroot/"+split[1]+fileName);
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return null;
            }
        }else{
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            try {
                file2 = File.createTempFile("temp",null);
                file.transferTo(file2);
                // 指定要上传到 COS 上的路径
                String key = "/pw/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file2);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                url=path + putObjectRequest.getKey();
            } catch (IOException e) {
                return Result.error(-100,"文件上传失败！");
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }
        if("/".equals(url.substring(url.length()-1,url.length()))){
            url=url.substring(0,url.length()-1);
        }
        long trackLength=0;
        try {
            trackLength = getVideoDuration(file2);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取时长异常："+e.getMessage(),e);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("url",url);
        result.put("sec",trackLength);
        return Result.success().put("data",result );
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if ("".equals(file) || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path =format(new Date()) + "/" + uuid;
        return path + suffix;
    }


    private String format(Date date) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }

    /**
     * 获取时长
     *
     * @param pdfFile 音频/视频文件
     * @return
     */
    public  long getVideoDuration(File pdfFile) throws Exception {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        long duration = 0;

        File source = new File(getFilePath() + multipartFile.getOriginalFilename());
        multipartFile.transferTo(source);
        Encoder encoder = new Encoder();
        MultimediaInfo info = encoder.getInfo(source);
        duration = info.getDuration();
        if (source.exists()) {
            source.delete();
        }
        return duration / 1000;
    }


    /**
     * 获取路径
     *
     * @return
     * @throws Exception
     */
    private static String getFilePath() throws Exception {
        //获取当前文件的根路径
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }

        //盘符路径
        StringBuilder codeUrl = new StringBuilder();
        codeUrl.append(path.getAbsolutePath()).append("/static/video/");
        File file = new File(codeUrl.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return codeUrl.toString();
    }


    private static File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String prefix = originalFilename.split("\\.")[0];
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        return file;
    }


}