package zalex.person.com.citytoollib.application;

import android.content.Context;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

/**
 * Created by zhouweinan on 2018/5/8.
 */

public class CityApplication {

    public static void onCreate(Context context) {
        // 添加中文城市词典
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(context)));
    }
}
