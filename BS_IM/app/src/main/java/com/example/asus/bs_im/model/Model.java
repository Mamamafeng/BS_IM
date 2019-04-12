package com.example.asus.bs_im.model;

//数据模型全局类
//用于model与controller的数据交换，提高可移植性

import android.content.Context;

import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.model.dao.UserAccountTableDao;
import com.example.asus.bs_im.model.db.DBmanager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    //创建对象
    private static Model model = new Model();
    private Context mcontext;  //上下文对象
    private ExecutorService mexecutorService = Executors.newCachedThreadPool();  //创建线程池
    private UserAccountTableDao userAccountTableDao;
    private DBmanager dBmanager;
    //构造函数
    private Model() {

    }

    //获取单例对象
    public static Model getInstence(){

        return model;
    }

    //初始化
    public void init(Context context){
        mcontext = context;

        //创建用户数据库操作类对象
        userAccountTableDao = new UserAccountTableDao(mcontext);

        //开启全局监听
        GlobalEventListener globalEventListener = new GlobalEventListener(mcontext);
    }

    //获取全局线程池
    public ExecutorService getallthreadpool(){
        return mexecutorService;

    }

    //登录成功后的数据改变
    public void loginSuccess(UserInfo userInfo) {
        if (userInfo == null)
            return;
        if (dBmanager != null)
            dBmanager.close();
        dBmanager = new DBmanager(mcontext,userInfo.getName());
    }

    public DBmanager getdBmanager() {
        return dBmanager;
    }

    public UserAccountTableDao getUserAccountTableDao(){
        return userAccountTableDao;
    }

}
