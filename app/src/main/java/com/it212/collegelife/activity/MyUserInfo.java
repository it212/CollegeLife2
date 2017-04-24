package com.it212.collegelife.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.sqlite.InviteDBHelp;
import com.it212.collegelife.utils.Constant;
import com.it212.collegelife.utils.FileUtil;
import com.it212.collegelife.utils.Model;
import com.it212.collegelife.utils.SpUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Date;


import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class MyUserInfo extends Activity {
    private static final int PHOTO_REQUEST_CAMERA = 0x002;
    private static final int PHOTO_REQUEST_CUT = 0x003;
    private static final int PHOTO_REQUEST_GALLERY = 0x001;
    public static Integer mBuildVersion = android.os.Build.VERSION.SDK_INT;//当前SDK版本
    private String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();//sd卡路径
    private Button btn_logout;
    private TextView tv_username;
    private TextView tv_nickname;
    private TextView tv_region;
    private TextView tv_mtime;
    private TextView tv_gender;
    private TextView tv_address;
    private ImageView iv_icon;
    private RelativeLayout rl_icon;
    private UserInfo userInfo;
    private Bitmap rawBitmap;
    private PopupWindow popupWindow;
    private LinearLayout activity_my_user_info;
    private MyClickListener myClickListener;
    private String fileName;
    private Uri mPhotoFileUri;
    private String smallFilePath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
         switch (msg.what){
             case 0x000:
                 final File file= (File) msg.obj;
                 Bitmap bm= BitmapFactory.decodeFile(file.getAbsolutePath());
               iv_icon.setImageBitmap(bm);
                 Model.getInstance().getClobeThreadPool().execute(new Runnable() {
                     @Override
                     public void run() {
                         JMessageClient.updateUserAvatar( file, new BasicCallback() {
                             @Override
                             public void gotResult(int i, String s) {
                                 if (i==0){
                                     JMessageClient.getUserInfo(userInfo.getUserName(), new GetUserInfoCallback() {
                                         @Override
                                         public void gotResult(int i, String s, UserInfo userInfo) {
                                             if (i==0){
                                                 runOnUiThread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         setResult(01);
                                                         Toast.makeText(MyUserInfo.this, "更新成功", Toast.LENGTH_LONG).show();
                                                     }
                                                 });
                                             }
                                         }
                                     });
                                 }
                             }
                         });
                     }
                 });
                 break;
         }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_info);
        myClickListener = new MyClickListener();
        activity_my_user_info = (LinearLayout) findViewById(R.id.activity_my_user_info);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_region = (TextView) findViewById(R.id.tv_region);
        tv_mtime = (TextView) findViewById(R.id.tv_mtime);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        rl_icon = (RelativeLayout) findViewById(R.id.rl_icon);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        userInfo = JMessageClient.getMyInfo();
        if (userInfo != null) {
            tv_username.setText(userInfo.getUserName());
            tv_nickname.setText(userInfo.getNickname());
            tv_region.setText(userInfo.getRegion());
            tv_gender.setText("");
            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if (i == 0) {
                        iv_icon.setImageBitmap(bitmap);
                    }
                }
            });
            tv_address.setText(userInfo.getAddress());
            iv_icon.setOnClickListener(myClickListener);
            rl_icon.setOnClickListener(myClickListener);
            btn_logout.setOnClickListener(myClickListener);
        }

    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_icon:
                    newPopwin();
                    break;
                case R.id.rl_icon:
                    intent = new Intent(MyUserInfo.this, UserIcon.class);
                    startActivity(intent);
                    break;
                case R.id.ll_bg:
                    popupWindow.dismiss();
                    break;
                case R.id.tv_img:
                    popupWindow.dismiss();
                    choseHeadImageFromGallery();
                    break;
                case R.id.tv_photo:
                    popupWindow.dismiss();
                    choseHeadImageFromCameraCapture();
                    break;
                case R.id.btn_logout:
                    JMessageClient.logout();
                    iv_icon.setImageResource(R.mipmap.ic_launcher);
                    Toast.makeText(MyUserInfo.this, "退出登陆成功", Toast.LENGTH_SHORT).show();

                    setResult(0x07);
                    finish();
                    break;
            }

        }

    }
    private void newPopwin() {
        popupWindow = new PopupWindow(this);
        popupWindow.setFocusable(true);
        View view = View.inflate(this, R.layout.select_icon_menu, null);
        LinearLayout ll_bg = (LinearLayout) view.findViewById(R.id.ll_bg);
        TextView tv_img = (TextView) view.findViewById(R.id.tv_img);
        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        ll_bg.setOnClickListener(myClickListener);
        tv_img.setOnClickListener(myClickListener);
        tv_photo.setOnClickListener(myClickListener);
        popupWindow.setContentView(view);
        popupWindow.setWidth(Constant.getWidth(this));
        popupWindow.setHeight(Constant.getHeight(this));
        popupWindow.showAtLocation(activity_my_user_info, Gravity.CENTER, 0, 0);
    }
    private static final String IMAGE_FILE_LOCATION = FileUtil.SDPATH+"/CollegeLifeIcon/imgs.jpg";

    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    private void choseHeadImageFromCameraCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, PHOTO_REQUEST_GALLERY);
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        Log.e("uri",uri.toString());
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MyUserInfo.this, "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case PHOTO_REQUEST_CAMERA:
                if (data!=null){
                    Bundle bundle = data.getExtras();
                    //获取相机返回的数据，并转换为图片格式
                    Bitmap bitmap = (Bitmap)bundle.get("data");
                    String name=getPhotoFileName();
                File file=  FileUtil.saveBitmap2File(bitmap,name,100);
                    Message msg=new Message();
                    msg.obj=file;
                    msg.what=0x000;
                    handler.sendMessage(msg);

                }
                break;
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(),480);
                }

                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    Bitmap photo = data.getParcelableExtra("data");
                  File file=  FileUtil.saveBitmap2File(photo,getPhotoFileName(),100);
                    Message msg=new Message();
                    msg.obj=file;
                    msg.what=0x000;
                    handler.sendMessage(msg);
                    iv_icon.setImageBitmap(photo);
                }
                break;
        }
    }
//    private void saveAndsetPic(Intent picdata) {
//        Bundle bundle = picdata.getExtras();
//        if (bundle != null) {
//            Bitmap photo = bundle.getParcelable("data");
//            //保存到SD卡
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] p = baos.toByteArray();
//
//            //将图片名字保存带本
//
//        }
//    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }


}
