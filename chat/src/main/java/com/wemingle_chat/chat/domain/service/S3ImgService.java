package com.wemingle_chat.chat.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ImgService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private static final String GROUP_CHAT = "chat/group/";
    private static final Duration EXPIRY_TIME = Duration.ofMinutes(1L);

    public boolean isAvailableExtension(String extension) {
        try {
            AllowExtensions.valueOf(extension);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isAvailableExtensions(List<String> extensions) {
        return extensions.stream().distinct().toList().stream()
                .map(this::isAvailableExtension)
                .allMatch(isAvailable -> isAvailable.equals(true));
    }

    public List<String> getGroupChatPicsDownloadUrl(List<UUID> picIdList) {
        return picIdList.stream().map(picId -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(GROUP_CHAT + picId).build();
            GetObjectPresignRequest objectPreSignRequest = GetObjectPresignRequest.builder().getObjectRequest(getObjectRequest).signatureDuration(EXPIRY_TIME).build();
            URL url = s3Presigner.presignGetObject(objectPreSignRequest).url();
            s3Presigner.close();
            return url.toString();
        }).toList();

    }
    public String getGroupChatPicUploadUrl(UUID groupChatPicUUID) {
        PutObjectRequest getObjectRequest = PutObjectRequest.builder().bucket(bucket).key(GROUP_CHAT + groupChatPicUUID).build();
        PutObjectPresignRequest objectPreSignRequest = PutObjectPresignRequest.builder().signatureDuration(EXPIRY_TIME).putObjectRequest(getObjectRequest).build();
        URL url = s3Presigner.presignPutObject(objectPreSignRequest).url();
        s3Presigner.close();
        return url.toString();
    }

    public boolean isExistGroupChatPic(UUID groupChatPicUUID) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(GROUP_CHAT + groupChatPicUUID)
                    .build();

            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            return headObjectResponse.sdkHttpResponse().isSuccessful();
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }




    private void verifyImgExist(String keyPath, UUID imgId) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(keyPath + imgId)
                .build();

        s3Client.headObject(headObjectRequest);
        s3Client.close();
    }
}
