package com.nodream.xskj.stewardpro;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseApplication;
import com.nodream.xskj.commonlib.utils.Utils;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by nodream on 2017/11/25.
 */
@ReportsCrashes(
        mailTo = "nodream007@163.com",
        mode = ReportingInteractionMode.DIALOG,
        customReportContent = {
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL,
                ReportField.CUSTOM_DATA,
                ReportField.BRAND,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT,
                ReportField.USER_COMMENT},
        resToastText = R.string.crash_toast_text,
        resDialogText = R.string.crash_dialog_text,
        resDialogTitle = R.string.crash_dialog_title
)
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Utils.isAppDebug()) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
//        final ACRAConfiguration config = new ConfigurationBuilder(this)
//                .build();

    }

    private class CrashReportSender implements ReportSender {
        CrashReportSender() {
            ACRA.getErrorReporter().putCustomData("PLATFROM","ANDROID");
            ACRA.getErrorReporter().putCustomData("BUILD_ID", Build.ID);
            ACRA.getErrorReporter().putCustomData("DEVICE_NAME",Build.PRODUCT);
        }

        @Override
        public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {

//            EmailIntentSender emailIntentSender = new EmailIntentSender(getApplicationContext());
        }
    }
}
