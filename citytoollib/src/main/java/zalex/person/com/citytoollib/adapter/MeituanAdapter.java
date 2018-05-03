package zalex.person.com.citytoollib.adapter;

import android.content.Context;

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
        holder.setText(R.id.tvCity, cityBean.getCity());
    }
}