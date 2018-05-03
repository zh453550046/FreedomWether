package zalex.person.com.citytoollib.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import zalex.person.com.citytoollib.R;
import zalex.person.com.citytoollib.model.MeituanHeaderBean;
import zalex.person.com.citytoollib.utils.CommonAdapter;
import zalex.person.com.citytoollib.utils.HeaderRecyclerAndFooterWrapperAdapter;
import zalex.person.com.citytoollib.utils.ViewHolder;

/**
 * Created by zhouweinan on 2018/5/3.
 */

public class MeituanHeaderFooterAdapter extends HeaderRecyclerAndFooterWrapperAdapter {

    private Context mContext;

    public MeituanHeaderFooterAdapter(Context context, RecyclerView.Adapter mInnerAdapter) {
        super(mInnerAdapter);
        mContext = context;
    }

    protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
        final MeituanHeaderBean meituanHeaderBean = (MeituanHeaderBean) o;
        //网格
        RecyclerView recyclerView = holder.getView(R.id.rvCity);
        recyclerView.setAdapter(
                new CommonAdapter<String>(mContext, R.layout.meituan_item_header_item, meituanHeaderBean.getCityList()) {
                    @Override
                    public void convert(ViewHolder holder, final String cityName) {
                        holder.setText(R.id.tvName, cityName);
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mContext, "cityName:" + cityName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }
}
