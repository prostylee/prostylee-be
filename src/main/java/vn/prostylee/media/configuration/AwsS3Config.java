//package vn.prostylee.media.configuration;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
///**
// * This holds aws configurations.
// */
//@Configuration
//@Component
//public class AwsS3Config {
//
////    @Value("${app.aws.accessKey}")
////    private String accessKey;
////    @Value("${app.aws.secretKey}")
////    private String secretKey;
////    @Bean
////    public AmazonS3 awsS3Client() {
////        AWSCredentials credentials =
////                new BasicAWSCredentials(accessKey,secretKey);
////        return AmazonS3ClientBuilder
////                .standard()
////                .withCredentials(new AWSStaticCredentialsProvider(credentials))
////                .withRegion(Regions.AP_SOUTHEAST_1)
////                .build();
////    }
//}