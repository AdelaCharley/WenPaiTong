package com.qunshang.wenpaitong.equnshang.utils;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

public class ImageUtil {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private static final OkHttpClient client = new OkHttpClient();

    public static void uploadImage(String userName, File file,Callback callback) {
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("userId", userName)
                .build();
        Request request = new Request.Builder()
                .url( Constants.Companion.getBaseURL() + "/tp/public/index.php/LoginController/uploadImage")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void uploadStoreImage(String type, File file, Callback callback){
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("type", type)
                .build();
        Request request = new Request.Builder()
                .url(Constants.Companion.getBaseURL() + "/tp/public/index.php/O2OController/uploadVendorImg")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void uploadAfterSaleImages(Context context,String orderSn, Uri uri,Callback callback){
        String imagePath = getImagePath(context,uri,null);
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("orderSn", orderSn)
                .build();
        Request request = new Request.Builder()
                .url(Constants.Companion.getBaseURL() + "/tp/public/index.php/AfterSaleController/updateAfterSalePicture")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void uploadExperienceImages(Context context,String newid, Uri uri,Callback callback){
        String imagePath = getImagePath(context,uri,null);
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("newId", newid)
                .build();
        Request request = new Request.Builder()
                .url(Constants.Companion.getBaseURL() + "/tp/public/index.php/ExperienceController/publishExperience")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void uploadIdentityImage(String type, File file, Callback callback){
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", UserInfoViewModel.INSTANCE.getUserId())
                .addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("IDCardType", type)
                .build();
        Request request = new Request.Builder()
                .url(Constants.Companion.getBaseURL() + "/tp/public/index.php/UserController/uploadIDImage")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void upFeedBack(String content, String contact,Callback callback,File file[]){
        RequestBody[] fileBodys = null;
        if (file != null && file.length != 0){
            fileBodys = new RequestBody[file.length];
            for (int i = 0; i < file.length;i++){
                fileBodys[i] = RequestBody.create(MEDIA_TYPE_PNG, file[i]);
            }
        }
        //RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", UserInfoViewModel.INSTANCE.getUserId())
                //.addFormDataPart("image", System.currentTimeMillis() + ".png", fileBody)
                .addFormDataPart("content", content)
                .addFormDataPart("contact", contact);
        if (file != null && fileBodys != null) {
            for (int i = 0;i < file.length;i++){
                String key = "image_" + i;
                builder.addFormDataPart(key,System.currentTimeMillis() + ".png",fileBodys[i]);
            }
        }
        Request request = new Request.Builder()
                .url(Constants.Companion.getBaseURL() + "/tp/public/index.php/Report/feedBack")
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static String getImagePath(Context context,Uri uri, String selection) {
        String path = "";
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
