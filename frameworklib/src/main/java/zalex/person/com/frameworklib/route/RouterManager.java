package zalex.person.com.frameworklib.route;

import android.app.Application;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.noah.mgtv.toolslib.logger.Logger;

import zalex.person.com.frameworklib.BuildConfig;

/**
 * Created by zhouweinan on 2018/4/16.
 */

public class RouterManager {

    public static void init(Application application) {
        if (BuildConfig.DEBUG) {
            Logger.d("framework", "router debug");
            ARouter.openDebug();
            ARouter.openLog();
            ARouter.printStackTrace();
        }
        ARouter.init(application);
    }

    public static RouterBuilder buildRouter() {
        return new RouterBuilder();
    }

    private static void navigation(RouterBuilder routerBuilder) {
        Postcard postcard = ARouter.getInstance().build(routerBuilder.mUri);
        if (routerBuilder.mBundle != null) {
            postcard.with(routerBuilder.mBundle);
        }
        if (routerBuilder.mIsGreenChannel) {
            postcard.greenChannel();
        }
        postcard.navigation();
    }


    public static class RouterBuilder {

        private String mUri;

        private Bundle mBundle;

        private boolean mIsGreenChannel;//跳过拦截器

        private RouterBuilder() {
        }

        public RouterBuilder uri(String url) {
            mUri = url;
            return this;
        }

        public RouterBuilder Bundle(Bundle bundle) {
            mBundle = bundle;
            return this;
        }

        public RouterBuilder greenChannel() {
            mIsGreenChannel = true;
            return this;
        }

        public void build() {
            RouterManager.navigation(this);
        }

    }

}
