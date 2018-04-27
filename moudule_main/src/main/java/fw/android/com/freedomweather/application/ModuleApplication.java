package fw.android.com.freedomweather.application;

import android.content.Context;

import com.noah.mgtv.imagelib.ImageApplication;
import com.noah.mgtv.network.application.NetworkApplication;
import com.noah.mgtv.toolslib.application.ToolApplication;

/**
 * Created by zhouweinan on 2018/4/27.
 */

public class ModuleApplication {

    public static void onCreate(Context context){
        ToolApplication.onCreate(context);
        ImageApplication.onCreate(context);
        NetworkApplication.onCreate(context);
    }

}
