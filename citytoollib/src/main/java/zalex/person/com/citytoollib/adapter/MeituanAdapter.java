package zalex.person.com.citytoollib.adapter;

import android.content.Context;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

import zalex.person.com.citytoollib.R;
import zalex.person.com.citytoollib.model.MeiTuanBean;
import zalex.person.com.citytoollib.utils.CommonAdapter;
import zalex.person.com.citytoollib.utils.ViewHolder;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class MeituanAdapter extends CommonAdapter<MeiTuanBean> {
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final MeiTuanBean cityBean) {
        String city = cityBean.getCity();
        if (!Pinyin.isChinese(city.charAt(0))) {
            city = city.substring(1);
        }
        holder.setText(R.id.tvCity, city);
    }
}