package com.ahmed.fynd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HelperFunctions {
    public static byte[] convertGalleryImageToByteArray(Uri image, Context context){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(image);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        catch(Exception e){
            return null;
        }
    }
}
