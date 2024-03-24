//package kr.co.chunjae.ocrplatform.service;
//
//import com.amazonaws.HttpMethod;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.*;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.*;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class S3Util {
//    private final AmazonS3 s3Client;
//
//    //사용하는 버킷의 이름
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucketName;
//
////  ===================== s3Util ==========================
//
//    /**
//     * @apiNote DOWNLOAD FILE ON ZIP
//     * @author KJE
//     * @request fileNameList, filePathList
//     * @since 2023-06-16
//     * try - with - resources
//     */
//    public void multiDownload(List<String> fileNameList, List<String> filePathList, HttpServletResponse response) throws IOException {
//        List<S3ObjectInputStream> inputList = new ArrayList<>();
//        LocalDateTime nowDate = LocalDateTime.now();
//        String formatDate = nowDate.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//        String zipFileName = "MYZIP_" + formatDate;
//
//        for (String filePath : filePathList) {
//            S3Object fullObject = s3Client.getObject(bucketName, filePath);
//            S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
//            inputList.add(objectInputStream);
//        }
//
//        List<File> fileList = convertListS3ObjectInputStreamToFile(fileNameList, inputList);
//        File zipFile = File.createTempFile(zipFileName, ".zip");
//        zipFile.deleteOnExit();
//        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
//            byte[] buf = new byte[1024];
//            for (File file : fileList) {
//                if (file.exists()) {
//                    try (FileInputStream fis = new FileInputStream(file)) {
//                        ZipEntry zipEntry = new ZipEntry(file.getName());
//                        out.putNextEntry(zipEntry);
//                        int len;
//                        while ((len = fis.read(buf)) > 0) {
//                            out.write(buf, 0, len);
//                        }
//                        out.closeEntry();
//                    }
//                }
//            }
//            for (File file : fileList) {
//                deleteFile(file);
//            }
//        }
//        byte[] bytes = FileCopyUtils.copyToByteArray(zipFile);
//        response.setContentType("application/zip;");
//        response.addHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + ".zip" + "\";");
//        FileCopyUtils.copy(bytes, response.getOutputStream());
//    }
//
//    private void deleteFile(File file) {
//        file.delete();
//    }
//
//
//    /**
//     * @return file List
//     * @apiNote convert list S3Object to File
//     * @author KJE
//     * @request S3ObjectInputStream
//     * @since 2023-06-16
//     */
//    private List<File> convertListS3ObjectInputStreamToFile(List<String> fileNameList, List<S3ObjectInputStream> inputStream) throws IOException {
//        List<File> files = new ArrayList<>();
//        int count = 0;
//        for (S3ObjectInputStream stream : inputStream) {
//            String path = stream.getHttpRequest().getURI().getPath();
//            String[] split = path.split("/");
//            String fileName = fileNameList.get(count);
//            count++;
//            File file = convertS3ObjectInputStreamToFile(stream, createFileName(fileName, count));
//            stream.close();
//            files.add(file);
//        }
//        return files;
//    }
//
//    /**
//     * @return name of file
//     * @apiNote deduplicate name - 파일 이름 중복 방지
//     * @author KJE
//     * @request original file name and count
//     * @since 2023-11-21
//     */
//    private String createFileName(String fileName, int count) {
//        String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
//        String filename = fileName.substring(0, fileName.lastIndexOf("."));
//        return filename + count + "." + extension;
//    }
//
//
//    /**
//     * @return file
//     * @apiNote convert S3Object to File
//     * @author KJE
//     * @request S3ObjectInputStream, file name
//     * @since 2023-06-16
//     * try - with - resources
//     */
//    private File convertS3ObjectInputStreamToFile(S3ObjectInputStream inputStream, String fileName) throws IOException {
//        File file = new File(fileName);
//        try (OutputStream outputStream = new FileOutputStream(file)) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        }
//        return file;
//    }
//
//
//    public void delete(String keyName){
//        this.deleteOb(keyName);
//    }
//
//    private void deleteOb(String keyName) {
//        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, keyName);
//
//
//        s3Client.deleteObject(deleteObjectRequest);
//    }
//
//
//    public String upload(MultipartFile file, String filePath){
//
//        //uuid로 file name 설정
//        String fileName = createUploadFileName(file.getOriginalFilename());
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(file.getSize());
//        objectMetadata.setContentType(file.getContentType());
//
//        //s3에 이미지 업로드
//        try(InputStream inputStream = file.getInputStream()){
//            s3Client.putObject(new PutObjectRequest(bucketName, filePath+fileName, inputStream, objectMetadata)
//                    .withCannedAcl(CannedAccessControlList.BucketOwnerRead));
//        } catch (IOException e){
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
//        }
//
//        // S3에 난수화로 저장한 파일 이름은 DB 저장을 위해 반환
//        return fileName;
//    }
//
//    public List<String> multiUpload(List<MultipartFile> multipartFiles, String filePath){
//        List<String> fileNameList = new ArrayList<>();
//
//        // forEach 구문을 통해 multipartFiles 리스트 순회
//        multipartFiles.forEach(file -> {
//            String fileName = createUploadFileName(file.getOriginalFilename());
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(file.getSize());
//            objectMetadata.setContentType(file.getContentType());
//
//            //순차적으로 s3에 이미지 업로드
//            try(InputStream inputStream = file.getInputStream()){
//                s3Client.putObject(new PutObjectRequest(bucketName, filePath+fileName, inputStream, objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.BucketOwnerRead));
//            } catch (IOException e){
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
//            }
//            //순차적으로 fileNameList 에 추가
//            fileNameList.add(fileName);
//
//        });
//
//        // S3에 난수화로 저장한 파일 이름 리스트는 DB 저장을 위해 반환
//        return fileNameList;
//    }
//
//    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
//    public String createUploadFileName(String fileName){
//        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
//    }
//
//
//    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
//    private String getFileExtension(String fileName){
//        try{
//            return fileName.substring(fileName.lastIndexOf("."));
//        } catch (StringIndexOutOfBoundsException e){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
//        }
//    }
//
//    public void download(String fileName, String filePath, HttpServletResponse response) throws IOException {
//
//        S3Object fullObject = s3Client.getObject(bucketName, filePath);
//        S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
//        File file = convertS3ObjectInputStreamToFile(objectInputStream, fileName);
//        file.deleteOnExit();
//
//        byte[] bytes = FileCopyUtils.copyToByteArray(file);
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
//        response.setHeader("Content-Transfer-Encoding", "binary");
//        FileCopyUtils.copy(bytes, response.getOutputStream());
//
//    }
//
//
//    //presignedUrl 생성
//    public String createSignedUrlForStringPut(String keyName) throws IOException {
//
//        // Set the presigned URL to expire after one hour.
//        java.util.Date expiration = new java.util.Date();
//        long expTimeMillis = Instant.now().toEpochMilli();
//        expTimeMillis += 1000 * 60 * 60;
//        expiration.setTime(expTimeMillis);
//
//        // Generate the presigned URL.
//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                new GeneratePresignedUrlRequest(bucketName, keyName)
//                        .withMethod(HttpMethod.GET)
//                        .withExpiration(expiration);
//        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
//
////            log.info("Pre-Signed URL: " + presignedUrl.toString());
//
//        return presignedUrl.toString();
//    }
//
//}