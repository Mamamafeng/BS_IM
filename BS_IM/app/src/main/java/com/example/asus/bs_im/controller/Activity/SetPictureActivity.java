package com.example.asus.bs_im.controller.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class SetPictureActivity extends Activity {

    private ImageView iv_setting_setLogo;
    private Button bt_setting_setLogo;
    private Button bt_setting_return;
    private Bitmap bitmap;
    private LocalBroadcastManager localBroadcastManager;
    private String userId;
    private String picturePath;//图片路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_picture);
        requestWritePermission();
        initView();

        initData();

        intListener();
    }

    private void initData() {
        userId = getIntent().getStringExtra(Constant.USER_ID);
        byte[] p = Model.getInstence().getUserAccountTableDao().getPictureFromSql(userId);
        if (p != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(p,0,p.length,null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            Drawable drawable = bitmapDrawable;
            iv_setting_setLogo.setImageDrawable(drawable);
        }else{
            iv_setting_setLogo.setImageResource(R.drawable.ease_default_avatar);
        }

    }

    private void intListener() {
        iv_setting_setLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        });

        bt_setting_setLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_setting_setLogo.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(iv_setting_setLogo.getDrawingCache());
                iv_setting_setLogo.setDrawingCacheEnabled(false);
                long l = Model.getInstence().getUserAccountTableDao().setPictureToSql(userId,bitmap);
                if (l != -1){
                    Toast.makeText(SetPictureActivity.this,"设置头像成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SetPictureActivity.this,"设置头像失败",Toast.LENGTH_SHORT).show();
                }
                //发送一个头像已更换的广播
                logoChanged();
                finish();
            }
        });

        bt_setting_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                String[] filePathColumn ={MediaStore.Images.Media.DATA};
                Cursor query = getContentResolver().query(uri, filePathColumn, null, null, null);
                query.moveToFirst();
                int columnIndex = query.getColumnIndex(filePathColumn[0]);
                picturePath = query.getString(columnIndex);
                query.close();
                iv_setting_setLogo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        iv_setting_setLogo = findViewById(R.id.iv_setting_setLogo);
        bt_setting_return = findViewById(R.id.bt_setting_return);
        bt_setting_setLogo = findViewById(R.id.bt_setting_setLogo);
    }

    private void requestWritePermission(){
        if (ActivityCompat.checkSelfPermission(SetPictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SetPictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
    private void logoChanged(){
        localBroadcastManager = LocalBroadcastManager.getInstance(SetPictureActivity.this);
        Intent intent = new Intent(Constant.PHOTO_NICK_CHANGED);
        intent.putExtra(Constant.USER_ID,userId);
        localBroadcastManager.sendBroadcast(intent);
    }
}
