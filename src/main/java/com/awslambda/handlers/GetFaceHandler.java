package com.awslambda.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.awslambda.FaceComparer;
import com.awslambda.model.Face;
import com.awslambda.model.FaceRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GetFaceHandler implements RequestHandler<FaceRequest, Face> {
    @Override
    public Face handleRequest(FaceRequest faceRequest, Context context) {

        FaceComparer faceComparer = new FaceComparer();

        ByteBuffer sourceImageBytes = null;
        try {
            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(new FileInputStream(new File(faceRequest.getPath()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image source = new Image().withBytes(sourceImageBytes);

        String name = faceComparer.compare(source);
        System.out.printf(name);

        return new Face(faceRequest.getId(), faceRequest.getPath());
    }
}
