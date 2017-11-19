package com.pl.universalcopy.xposed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.pl.universalcopy.Constant;
import com.pl.universalcopy.WakeUpBR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.pl.universalcopy.Constant.IS_UNIVERSAL_COPY_ENABLE;
import static com.pl.universalcopy.Constant.MONITOR_CLICK;
import static com.pl.universalcopy.Constant.MONITOR_LONG_CLICK;
import static com.pl.universalcopy.Constant.PACKAGE_NAME;
import static com.pl.universalcopy.Constant.SP_NAME;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by dim on 16/10/23.
 */

public class XposedUniversalCopy implements IXposedHookLoadPackage {

    private static final String TAG = "XposedUniversalCopy";

    private final XposedUniversalCopyHandler mUniversalCopyHandler = new XposedUniversalCopyHandler();
    private XposedKeyUpHandler mXposedKeyUpHandler ;
    private final List<Filter> mFilters = new ArrayList<>();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.e("shang", "xposed-packageName:" + loadPackageParam.packageName);
        setXpoedEnable(loadPackageParam);
//        if (!new File("/data/data/"+PACKAGE_NAME).exists())
//            return;
        //  wakeup(loadPackageParam);
        // Logger.d(TAG, loadPackageParam.packageName);


        XSharedPreferences appXSP;
        appXSP = new XSharedPreferences(PACKAGE_NAME, SP_NAME);
        appXSP.makeWorldReadable();
//        boolean enabled =appXSP.getBoolean(IS_UNIVERSAL_COPY_ENABLE,true);
//
//        if (!enabled){
//            return;
//        }
        mFilters.add(new Filter.TextViewValidFilter());
        //优化微信 下的体验。
        if ("com.tencent.mm".equals(loadPackageParam.packageName)) {
            //朋友圈内容拦截。
            mFilters.add(new Filter.WeChatValidFilter(loadPackageParam.classLoader));
            mFilters.add(new Filter.WeChatCellTextViewFilter(loadPackageParam.classLoader));
            mFilters.add(new Filter.WeChatValidNoMeasuredTextViewFilter(loadPackageParam.classLoader));

          //聊天详情中的文字点击事件优化
        }

        mUniversalCopyHandler.setFilters(mFilters);
        // installer  不注入。 防止代码出错。进不去installer 中。
        if (!"de.robv.android.xposed.installer".equals(loadPackageParam.packageName) && !"com.android.systemui".equals(loadPackageParam.packageName)) {
            findAndHookMethod(Activity.class, "onStart",  new UniversalCopyOnStartHook());
            findAndHookMethod(Activity.class, "onStop",  new UniversalCopyOnStopHook());
            if(appXSP.getBoolean(MONITOR_LONG_CLICK,true)){
                findAndHookMethod(Activity.class, "onKeyLongPress", int.class, KeyEvent.class, new ViewonKeyLongPressHooker());
            }
            if(appXSP.getBoolean(MONITOR_CLICK,true)) {
                findAndHookMethod(Activity.class, "onKeyUp", int.class, KeyEvent.class, new ViewonKeyUpHooker());
            }
        }

    }


    private void setXpoedEnable(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        if (loadPackageParam.packageName.equals(PACKAGE_NAME)) {
            findAndHookMethod(loadPackageParam.classLoader.loadClass("com.pl.universalcopy.xposed.XposedEnable"), "isEnable", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return true;
                }
            });
        }
    }

    private Set<String> getLauncherAsWhiteList(Context c) {
        HashSet<String> packages = new HashSet<>();
        PackageManager packageManager = c.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
//        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            packages.add(ri.activityInfo.packageName);
        }
        return packages;
    }

    private Set<String> getInputMethodAsWhiteList(Context context) {
        HashSet<String> packages = new HashSet<>();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getInputMethodList();
        for (InputMethodInfo info : methodList) {
            packages.add(info.getPackageName());
        }
        return packages;
    }
    private boolean isKeyBoardOrLauncher=false;
    private boolean isKeyBoardOrLauncherChecked=false;
    private boolean isKeyBoardOrLauncher(Context context, String packageName) {
        if (isKeyBoardOrLauncherChecked){
            return isKeyBoardOrLauncher;
        }
        if (context == null) {
            isKeyBoardOrLauncher=true;
            isKeyBoardOrLauncherChecked=true;
            return true;
        }
        for (String package_process : getInputMethodAsWhiteList(context)) {
            if (package_process.equals(packageName)) {
                isKeyBoardOrLauncher=true;
                isKeyBoardOrLauncherChecked=true;
                return true;
            }
        }
        for (String package_process : getLauncherAsWhiteList(context)) {
            if (package_process.equals(packageName)) {
                isKeyBoardOrLauncher=true;
                isKeyBoardOrLauncherChecked=true;
                return true;
            }
        }
        isKeyBoardOrLauncher=false;
        isKeyBoardOrLauncherChecked=true;
        return false;
    }

    private class UniversalCopyOnStartHook extends XC_MethodHook {

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Activity activity = (Activity) param.thisObject;
            mUniversalCopyHandler.onStart(activity);
            Intent intent = new Intent(WakeUpBR.UNIVERSAL_COPY_WAKE_UP_ACTION);
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            try {
                activity.sendBroadcast(intent);
            } catch (Throwable e) {
            }
        }
    }


    private class UniversalCopyOnStopHook extends XC_MethodHook {

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Activity activity = (Activity) param.thisObject;
            mUniversalCopyHandler.onStop(activity);
        }
    }

    private class ViewonKeyUpHooker extends XC_MethodHook {

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Activity activity = (Activity) param.thisObject;
            Log.e(TAG, "ViewonKeyUpHooker afterHookedMethod= " + activity);
            if (mXposedKeyUpHandler==null){
                mXposedKeyUpHandler= new XposedKeyUpHandler(activity.getApplicationContext());
            }
            mXposedKeyUpHandler.onKeyEvent((KeyEvent) param.args[1]);
        }
    }

    private class ViewonKeyLongPressHooker extends XC_MethodHook {

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Activity activity = (Activity) param.thisObject;
            Log.e(TAG, "ViewonKeyLongPressHooker afterHookedMethod= " + activity);
            if (mXposedKeyUpHandler==null){
                mXposedKeyUpHandler= new XposedKeyUpHandler(activity.getApplicationContext());
            }
            mXposedKeyUpHandler.onKeyLongPress((KeyEvent) param.args[1]);
        }
    }

}