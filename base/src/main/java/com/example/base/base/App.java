package com.example.base.base;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/18 11:33
 * @describe ：
 */
public class App extends MultiDexApplication {
    public final String TAG = getClass().getSimpleName();

    public static Set<Activity> sAllActivities;


    private static App context ;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        //ARouter.init(this);
    }

    public static App getInstant(){
        return context;
    }

    public static void addActivity(Activity act) {
        if (sAllActivities == null) {
            sAllActivities = new HashSet<>();
        }
        sAllActivities.add(act);
    }

    public static void removeActivity(Activity act) {
        if (sAllActivities != null) {
            sAllActivities.remove(act);
        }
    }

    public static void exitApp() {
        if (sAllActivities != null) {
            synchronized (sAllActivities) {
                for (Activity act : sAllActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
