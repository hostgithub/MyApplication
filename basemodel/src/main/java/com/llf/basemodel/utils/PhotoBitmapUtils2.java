package com.llf.basemodel.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
     * 集合一些图片工具
     *
     * Created by wangjiawei on 2017-05-22.
     */
public class PhotoBitmapUtils2{

    /**
     * 存放拍摄图片的文件夹
     */
    private static final String FILES_NAME = "/MyPhoto";
    /**
     * 获取的时间格式
     */
    public static final String TIME_STYLE = "yyyyMMddHHmmss";
    /**
     * 图片种类
     */
    public static final String IMAGE_TYPE = ".png";
    public static String dateGeshi;
    public static String photoName;
    public static int size;

    // 防止实例化
    private PhotoBitmapUtils2() {
    }

    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private static String getPhoneRootPath(Context context) {
        // 是否有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            return context.getExternalCacheDir().getPath();
        } else {
            // 获取apk包下的缓存路径
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 使图片的名称
     */
    public static String getFileName(String number) {
        String strRand="";
        for(int i=0;i<3;i++){
            strRand += String.valueOf((int)(Math.random() * 10));
        }
        String dateStr= DateUtil.getMyMillon(System.currentTimeMillis());
        dateGeshi= DateUtil.getMillonDate(System.currentTimeMillis());//日期生成格式

        return "AKSR-"+number+"_"+dateStr+strRand+".jpg";
    }

    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    public static String getPhotoFileName(Context context,String number) {

        String dir = FileUtilPic.getSDCardPath()+"TestImage/imagePic/"; //定义一个文件夹储存图片
        FileUtilPic.checkDir(dir); //判断文件夹是否存在，不存在创建

//        File file = new File(getPhoneRootPath(context) + FILES_NAME);
//        // 判断文件是否已经存在，不存在则创建
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        // 设置图片文件名称
//        SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE, Locale.getDefault());
//        Date date = new Date(System.currentTimeMillis());
//        String time = format.format(date);

        // 设置图片文件名称
        String strRand="";
        for(int i=0;i<3;i++){
            strRand += String.valueOf((int)(Math.random() * 10));
        }
        String dateStr= DateUtil.getMyMillon(System.currentTimeMillis());
        dateGeshi= DateUtil.getMillonDate(System.currentTimeMillis());//日期生成格式

        photoName="AKSR-"+number+"_"+dateStr+strRand+".jpg";
        File file = new File(dir,"AKSR-"+number+"_"+dateStr+strRand+".jpg");  //为拍照图片命名规则
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //String photoName = "AKSR-"+number+"_"+dateStr+strRand + IMAGE_TYPE;
        //return file + photoName;
        return file.getAbsolutePath();
    }



    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(Bitmap mbitmap, Context context,String number) {
        FileOutputStream outStream = null;
        String fileName = getPhotoFileName(context,number);
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1; // 图片的大小设置为原来的十分之一
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    /**
     * 把原图按1/10的比例压缩
     */
    public static Bitmap getCompressPhoto(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
        return bitmap;
    }

    /**
     * 处理旋转后的图片
     * @param originpath 原图路径
     * @param context 上下文
     * @return 返回修复完毕后的图片路径
     */
    @SuppressLint("NewApi")
    public static String amendRotatePhoto(String originpath, Context context, String number) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = getCompressPhoto(originpath);

        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);size=bitmap.getByteCount()/1024;

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, context,number);
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }
}
