package fw.android.com.freedomweather.application;

import android.app.Application;
import android.content.Context;

import com.noah.mgtv.imagelib.ImageApplication;
import com.noah.mgtv.network.application.NetworkApplication;
import com.noah.mgtv.toolslib.application.ToolApplication;

import zalex.person.com.frameworklib.application.FrameworkApplicaiton;

/**
 * Created by zhouweinan on 2018/4/27.
 */

public class ModuleApplication {

    public static void onCreate(Application context){
        ToolApplication.onCreate(context);
        ImageApplication.onCreate(context);
        NetworkApplication.onCreate(context);
        FrameworkApplicaiton.onCreate(context);
    }

}
