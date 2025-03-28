package com.JH.JhOnlineJudge.common.utils;

import com.JH.JhOnlineJudge.domain.Image.S3FileUploadException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
public class S3Uploader {
    private final AmazonS3Client amazonS3;
    private final String bucket;

   public S3Uploader(AmazonS3Client amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
       this.amazonS3 = amazonS3;
       this.bucket = bucket;
   }

    public String upload(MultipartFile multipartFile, String dirName)  {

          String originalFileName = multipartFile.getOriginalFilename();

          String uuid = UUID.randomUUID().toString();
          String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

          String fileName = dirName + "/" + uniqueFileName;
          log.info("fileName: {}", fileName);

          try {
              File uploadFile = convert(multipartFile);

              String uploadImageUrl = putS3(uploadFile, fileName);
              removeNewFile(uploadFile);
              return uploadImageUrl;
          } catch (IOException e) {
              throw new S3FileUploadException("upload error : " + e.getMessage());
          }


    }

      private File convert(MultipartFile file) throws IOException {
          String originalFileName = file.getOriginalFilename();
          String uuid = UUID.randomUUID().toString();
          String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

          File convertFile = new File(uniqueFileName);
          if (convertFile.createNewFile()) {
              try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                  fos.write(file.getBytes());
              } catch (IOException e) {
                  log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                  throw new S3FileUploadException(e.getMessage());
              }
              return convertFile;
          }
          throw new S3FileUploadException(String.format("convert error. %s", originalFileName));
      }

      private String putS3(File uploadFile, String fileName) {
          amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                  .withCannedAcl(CannedAccessControlList.PublicRead));
          return amazonS3.getUrl(bucket, fileName).toString();
      }

      private void removeNewFile(File targetFile) {
          if (targetFile.delete()) {
              log.info("파일이 삭제되었습니다.");
          } else {
              log.info("파일이 삭제되지 못했습니다.");
          }
      }

      public void deleteFile(String url) {
          String key = extractPath(url);
          log.info("Deleting file from S3: " + key);
          amazonS3.deleteObject(bucket, key);
      }

      public String updateFile(MultipartFile newFile, String oldFileName, String dirName) {

          log.info("S3 oldFileName: " + oldFileName);
          deleteFile(oldFileName);

          return upload(newFile, dirName);
      }

    private static String extractPath(String urlString) {
           try {
               URL url = new URL(urlString);
               String path = url.getPath();
               return path.substring(1);
           } catch (Exception e) {
               e.printStackTrace();
               return null; // 오류가 발생할 경우 null 반환
           }
       }
}
