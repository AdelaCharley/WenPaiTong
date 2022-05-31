package com.qunshang.wenpaitong.equnshang.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import com.coremedia.iso.boxes.Container;

public class FileUtils {
    private final static String PREFIX_VIDEO_HANDLER = "vide";
    private final static String PREFIX_AUDIO_HANDLER = "soun";

    public String getPath(Uri uri,Context context) {
        String path;
        if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
            path = uri.getPath();
            return path;
        }
        path = getPath(context, uri);
        return path;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context    The context.
     * @param uri      The Uri to query.
     * @param selection   (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*public static void mergeVideos(List<String> inputVideos, String outputPath) throws IOException {
        try {
            List<Movie> inputMovies = new ArrayList<>();
            for (String input : inputVideos) {
                inputMovies.add(MovieCreator.build(input));
            }

            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();

            for (Movie m : inputMovies) {
                for (Track t : m.getTracks()) {
                    if (PREFIX_AUDIO_HANDLER.equals(t.getHandler())) {
                        audioTracks.add(t);
                    }
                    if (PREFIX_VIDEO_HANDLER.equals(t.getHandler())) {
                        videoTracks.add(t);
                    }
                }
            }

            Movie outputMovie = new Movie();
            if (audioTracks.size() > 0) {
                outputMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                outputMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }

            Container out = new DefaultMp4Builder().build(outputMovie);

            FileChannel fc = new RandomAccessFile(outputPath, "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public static void doCopy(Context context, String assetsPath, String outFileName) throws IOException {
        String[] srcFiles = context.getAssets().list(assetsPath);//for directory
        if (srcFiles == null){
            return;
        }
        if (srcFiles.length != 0){
            //String outFileName = desPath + File.separator + srcFiles[0];
            String inFileName = assetsPath + File.separator + srcFiles[0];
            if (assetsPath.equals("")) {// for first time
                inFileName = srcFiles[0];
            }
            Log.e("tag","========= assets: "+ assetsPath+"  filename: "+srcFiles[0] +" infile: "+inFileName+" outFile: "+outFileName);
            try {
                InputStream inputStream = context.getAssets().open(inFileName);
                copyAndClose(inputStream, new FileOutputStream(outFileName));
            } catch (IOException e) {//if directory fails exception
                e.printStackTrace();
                new File(outFileName).mkdir();
                doCopy(context,inFileName, outFileName);
            }
        }
    }

    private static void closeQuietly(OutputStream out){
        try{
            if(out != null) out.close();;
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private static void closeQuietly(InputStream is){
        try{
            if(is != null){
                is.close();
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private static void copyAndClose(InputStream is, OutputStream out) throws IOException{
        copy(is,out);
        closeQuietly(is);
        closeQuietly(out);
    }

    private static void copy(InputStream is, OutputStream out) throws IOException{
        byte[] buffer = new byte[1024];
        int n = 0;
        while(-1 != (n = is.read(buffer))){
            out.write(buffer,0,n);
        }
    }

    static boolean fileIsExist(String fileName) {
        //传入指定的路径，然后判断路径是否存在
        File file=new File(fileName);
        if (file.exists())
            return true;
        else{
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }

    public static File getFileByUri(Context context,Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context. getContentResolver ();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr . query (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur. moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri . parse ("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File (path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context . getContentResolver ().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor . getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File (path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }


    public static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }

    public static void saveImage(Context context, File file) {
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);

        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        final Uri localUri = Uri.fromFile(file);
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);
    }

    public static ContentValues getImageContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "image/jpeg");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("orientation", Integer.valueOf(0));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }


    /**
     * 保存视频
     * @param context
     * @param file
     */
    public static void saveVideo(Context context, File file) {
        //是否添加到相册
        /*ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));*/
        /*Uri localUri = Uri.parse("file://"+ file.getAbsolutePath());
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);*/
        Uri uri = null;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
//            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        ContentResolver contentResolver = context.getContentResolver();
        uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        if(uri == null){
            StringUtils.log("Insert file to resolver failed.");
            return;
        }

        // 拷贝到指定uri,如果没有这步操作，android11不会在相册显示
        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            FileUtils.copyFile(file.getAbsolutePath(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        scanIntent.setData(uri);
        context.sendBroadcast(scanIntent);
    }

    public static void saveImageToLocal(Context context, File file) {//通知相册
        //是否添加到相册
        /*ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));*/
        /*Uri localUri = Uri.parse("file://"+ file.getAbsolutePath());
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);*/
        Uri uri = null;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        ContentResolver contentResolver = context.getContentResolver();
//        uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if(uri == null){
            StringUtils.log("Insert file to resolver failed.");
            return;
        }

        // 拷贝到指定uri,如果没有这步操作，android11不会在相册显示
        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            FileUtils.copyFile(file.getAbsolutePath(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        scanIntent.setData(uri);
        context.sendBroadcast(scanIntent);
    }

    public static boolean copyFile(String oldPath, OutputStream out) {
        StringUtils.log("oldPath = " + oldPath);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                // 读入原文件
                InputStream inStream = new FileInputStream(oldPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    out.write(buffer, 0, byteread);
                }
                inStream.close();
                out.close();
                return true;
            }
            else {
                StringUtils.log(String.format("文件(%s)不存在。", oldPath));
            }
        }
        catch (Exception e) {
            StringUtils.log("复制单个文件操作出错");
            e.printStackTrace();
        }

        return false;
    }

    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(MediaStore.Video.Media.TITLE, paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        //localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put(MediaStore.Video.Media.DATA,paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

}
