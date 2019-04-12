package com.example.asus.bs_im.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.asus.bs_im.IM;
import com.example.asus.bs_im.R;

import java.util.ArrayList;

public class SpUtils {

    public static final String IS_NEW_INVITE = "is_new_invite";
    private static SharedPreferences im;
    private static SpUtils spUtils = new SpUtils();
    private SpUtils() {
    }

    public static SpUtils getInstance(){

        if (im == null){
            im = IM.getGlobalContext().getSharedPreferences("IM", Context.MODE_PRIVATE);
        }
        return spUtils;
    }
    //保存数据
    public void save(String key,Object values){
        if(values instanceof String){
            im.edit().putString(key,(String)values).commit();
        }else if(values instanceof Boolean){
            im.edit().putBoolean(key,(Boolean) values).commit();
        }else if(values instanceof Integer){
            im.edit().putInt(key, (Integer) values).commit();
        }
    }
    //获取数据
    public String getString(String key,String value){
        return im.getString(key,value);
    }

    //获取bool类型数据
    public boolean getBoolean(String key,Boolean value){
        return im.getBoolean(key,value);
    }

    //获取int类型数据
    public int getInt (String key,Integer value){
        return im.getInt(key,value);
    }

    //将从数据库返回的二进制图片转换为drawable并返回
    public Drawable picture(byte[] bytes){
        Drawable drawable = null ;
        if (bytes != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            drawable = bitmapDrawable;
        }
        return drawable;
    }

    public Bitmap pictureToBitmap(byte[] bytes){
        Bitmap bitmap = null ;
        if (bytes != null){
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
        }
        return bitmap;
    }
}
