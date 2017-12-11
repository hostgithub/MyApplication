package com.llf.basemodel.commonactivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.llf.basemodel.R;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.spinner.LQRPhotoSelectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class TakePhotoActivity extends BaseActivity {

    Button upload;
    private TextView mTvPath;
    private TextView mTvUri;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private ImageView mIvPic;


    private String mCurrentPhotoPath;
    private int RESULT_LOAD_IMAGE=1;
    private int RESULT_CAMERA_IMAGE=2;
    private  File sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");

    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照请求码
    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写权限
            Manifest.permission.CAMERA//照相权限
    };
    private Uri imageUri;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_spinner;
    }

    @Override
    protected void initView() {
        upload=(Button) findViewById(R.id.btn_upload);
        mTvPath = (TextView) findViewById(R.id.tvPath);
        mTvUri = (TextView) findViewById(R.id.tvUri);
        mIvPic = (ImageView) findViewById(R.id.ivPic);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopueWindow();
            }
        });

        init();
    }


    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                mTvPath.setText(outputFile.getAbsolutePath());
                mTvUri.setText(outputUri.toString());
                Glide.with(TakePhotoActivity.this).load(outputUri).into(mIvPic);
            }
        }, false);//true裁剪，false不裁剪

        //        mLqrPhotoSelectUtils.setAuthorities("com.lqr.lqrnativepicselect.fileprovider");
        //        mLqrPhotoSelectUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }


    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        //popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(getImagePickerIntent(), RESULT_LOAD_IMAGE);
                // 3、调用从图库选取图片方法
                PermissionGen.needPermission(TakePhotoActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、调用拍照方法
                PermissionGen.with(TakePhotoActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }


    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == RESULT_OK && null != data){
//            switch (requestCode){
//                case 1:
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                final String picturePath = cursor.getString(columnIndex);
//                //上传
//                Log.e("---------图片原地址1------",picturePath);
//                    // 得到修复后的照片路径filepath
//                    final String filepath = PhotoBitmapUtils2.amendRotatePhoto(picturePath, this,"");
//                    Log.e("---------图片文件地址2------",filepath);
//                    if (filepath != null) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
//                        mIvPic.setImageBitmap(bitmap);
////                        iv_image.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Intent intent = new Intent(GzclActivity.this, ImageDetailsActivity.class);
////                                intent.putExtra("tag", "gzcl");
////                                intent.putExtra("image_path", filepath);
////                                GzclActivity.this.startActivity(intent);
////                            }
////                        });
//                    }
//                cursor.close();
//                    break;
//                case 2:
//                    try {
//                        Uri xiangji_uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), sdcardTempFile.getAbsolutePath(), null, null));
//                        System.out.println(xiangji_uri.getPath());
//                        ContentResolver xiangji_cr = this.getContentResolver();
//                        //im = BitmapFactory.decodeStream(xiangji_cr.openInputStream(xiangji_uri));
//                    } catch (FileNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    //将bitmap转化为png格式
    public File saveMyBitmap(Bitmap mBitmap){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = null;
        try {
            file = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            FileOutputStream out=new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file;
    }


    /**
     * 获取打开照程序界面的Intent
     */
    public static Intent getOpenCameraIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    /**
     * 获取跳转至相册选择界面的Intent
     */
    public static Intent getImagePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + TakePhotoActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
}
