package com.example.base.util;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.base.rx.RxThrowableConsumer;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/19 14:12
 * @describe ：
 */
public class PermissionUtil {



    /**
     * @param activity
     * @param listener
     * @param mPerms
     * 请求每个权限
     */
    public static void requestPermissionEach(AppCompatActivity activity,EachPermissionListenerImp listener,String... mPerms){

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(mPerms).subscribe(permission -> {
                if (listener!=null){
                    if (permission.granted){
                        listener.onEachGranted(permission.name);
                    }else if (permission.shouldShowRequestPermissionRationale){
                        listener.onEachDeniedWithoutAskNeverAgain(permission.name);
                    }else {
                        listener.onEachDeniedWithAskNeverAgain(permission.name);
                    }
                }
        },new RxThrowableConsumer());

    }
    /**
     * @param fragment
     * @param listener
     * @param mPerms
     * 请求每个权限
     */
    public static void requestPermissionEach(Fragment fragment, EachPermissionListenerImp listener, String... mPerms){

        RxPermissions rxPermissions = new RxPermissions(fragment);
        rxPermissions.requestEach(mPerms).subscribe(permission -> {
            if (listener!=null){
                if (permission.granted){
                    listener.onEachGranted(permission.name);
                }else if (permission.shouldShowRequestPermissionRationale){
                    listener.onEachDeniedWithoutAskNeverAgain(permission.name);
                }else {
                    listener.onEachDeniedWithAskNeverAgain(permission.name);
                }
            }
        },new RxThrowableConsumer());

    }
    /**
     * @param activity
     * @param listener
     * @param mPerms
     * 请求每个权限,返回是否全部同意
     */
    public static void requestPermissionCombined(AppCompatActivity activity,CombinedPermissionListenerImp listener,String... mPerms){

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(mPerms).subscribe(permission -> {
            if (listener!=null){
                if (permission.granted){
                    listener.onCombinedGranted();
                }else if (permission.shouldShowRequestPermissionRationale){
                    listener.onAtLeastOneDeniedWithoutAskNeverAgain();
                }else {
                    listener.onAtLeastOneDeniedWithAskNeverAgain();
                }
            }
        },new RxThrowableConsumer());

    }
    /**
     * @param fragment
     * @param listener
     * @param mPerms
     * 请求每个权限,返回是否全部同意
     */
    public static void requestPermissionCombined(Fragment fragment,CombinedPermissionListenerImp listener,String... mPerms){

        RxPermissions rxPermissions = new RxPermissions(fragment);
        rxPermissions.requestEach(mPerms).subscribe(permission -> {
            if (listener!=null){
                if (permission.granted){
                    listener.onCombinedGranted();
                }else if (permission.shouldShowRequestPermissionRationale){
                    listener.onAtLeastOneDeniedWithoutAskNeverAgain();
                }else {
                    listener.onAtLeastOneDeniedWithAskNeverAgain();
                }
            }
        },new RxThrowableConsumer());

    }


    public interface EachPermissionListener{
        void onEachGranted(String permission);//某个权限同意

        void onEachDeniedWithoutAskNeverAgain (String permission);//某个权限拒绝，但没有勾选不再询问

        void onEachDeniedWithAskNeverAgain (String permission);//某个权限拒绝，勾选不再询问 需要跳转设置
    }

    public static abstract class EachPermissionListenerImp implements EachPermissionListener{
        @Override
        public void onEachGranted(String permission) {

        }

        @Override
        public void onEachDeniedWithoutAskNeverAgain(String permission) {

        }

        @Override
        public void onEachDeniedWithAskNeverAgain(String permission) {

        }
    }


    public interface CombinedPermissionListener{
        void onCombinedGranted();//所有权限同意

        void onAtLeastOneDeniedWithoutAskNeverAgain ();//至少一个权限拒绝，但没有勾选不再询问

        void onAtLeastOneDeniedWithAskNeverAgain ();//至少一权限拒绝，勾选不再询问 需要跳转设置
    }

    public static abstract class  CombinedPermissionListenerImp implements CombinedPermissionListener{
        @Override
        public void onCombinedGranted() {

        }

        @Override
        public void onAtLeastOneDeniedWithoutAskNeverAgain() {

        }

        @Override
        public void onAtLeastOneDeniedWithAskNeverAgain() {

        }
    }
}
