package com.awslambda;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

public class FaceComparer {
    private AmazonRekognition rekognitionClient;
    private ManageCollection mc;
    private AmazonSNS snsClient;
    private AmazonS3 s3Client;

    public FaceComparer() {

        AWSCredentials credentials;
        /**
        https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html#cli-quick-configuration
         **/
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        snsClient = AmazonSNSClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        s3Client = AmazonS3ClientBuilder.standard().withRegion("us-east-1").withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        System.out.println("\nAWS Rekognition Initialized...");

        mc = new ManageCollection(rekognitionClient);

      //  loadTargetImages();

    }

    /**
     * CREATE COLLECTION AND LOAD IMAGED FROM REPOSITORY
     */
    public void loadTargetImages() {
        try {
            mc.createCollection();
            mc.addFacesToCollection();
//            mc.addFacesToCollectionFromS3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * COMPARE IMAGE WITH COLLECTION INDEXED IMAGES
     */
    public String compare(Image source){
        String name = mc.searchFacesByImageResult(source);
        return name;
    }
}
