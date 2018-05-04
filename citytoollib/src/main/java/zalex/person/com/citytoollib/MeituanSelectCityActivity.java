package zalex.person.com.citytoollib;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.noah.mgtv.datalib.hefeng.Basic;
import com.noah.mgtv.datalib.hefeng.HeFengModule;
import com.noah.mgtv.datalib.hefeng.HeWeather;
import com.noah.mgtv.network.NetworkCallback;
import com.noah.mgtv.network.NetworkRequest;
import com.noah.mgtv.network.NetworkRequestFactory;
import com.noah.mgtv.network.constents.RequestConstents;
import com.noah.mgtv.toolslib.IoUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zalex.person.com.citytoollib.adapter.MeituanAdapter;
import zalex.person.com.citytoollib.adapter.MeituanHeaderFooterAdapter;
import zalex.person.com.citytoollib.model.MeiTuanBean;
import zalex.person.com.citytoollib.model.MeituanHeaderBean;
import zalex.person.com.citytoollib.utils.HeaderRecyclerAndFooterWrapperAdapter;
import zalex.person.com.frameworklib.base.BaseActivity;
import zalex.person.com.frameworklib.route.RouterSchema;

@Route(path = RouterSchema.SCHEMA_SELECT_CITIES)
public class MeituanSelectCityActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private MeituanAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源
    private List<MeituanHeaderBean> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<MeiTuanBean> mBodyDatas;
    private SuspensionDecoration mDecoration;
    //右侧边栏导航区域
    private IndexBar mIndexBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meituan);

        initList();
        initRecyclerView();
        initInDexBar();
        initLocalCityDatas();
        loadHotCities();
    }

    private void initList() {
        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        List<String> hotCities = new ArrayList<>();
        mHeaderDatas.add(new MeituanHeaderBean(hotCities, "热门城市", "热"));
        mSourceDatas.addAll(mHeaderDatas);
    }

    private void initRecyclerView() {
        mAdapter = new MeituanAdapter(this, R.layout.meituan_item_select_city, mBodyDatas);
        mHeaderAdapter = new MeituanHeaderFooterAdapter(this, mAdapter);
        mHeaderAdapter.setHeaderView(0, R.layout.meituan_item_header, mHeaderDatas.get(0));
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(mManager = new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHeaderAdapter);
        mRecyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffefefef)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()))
                .setColorTitleFont(getResources().getColor(android.R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initInDexBar() {
        //使用indexBar
        TextView mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());
    }

    /**
     * 组织数据源
     */
    private void initLocalCityDatas() {
        mBodyDatas = new ArrayList<>();
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            String json;
            inputStream = getAssets().open("cities.json");
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] b = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(b);
            json = new String(b, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("provinces");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject province = jsonArray.getJSONObject(i);
                JSONArray cityArray = (JSONArray) province.get("cities");
                for (int j = 0; j < cityArray.length(); j++) {
                    mBodyDatas.add(new MeiTuanBean(cityArray.getString(j)));
                }
            }
            //先排序
            mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
            mAdapter.setDatas(mBodyDatas);
            mSourceDatas.addAll(mBodyDatas);
            mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mSourceDatas);
            mHeaderAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(bufferedInputStream);
            IoUtils.close(inputStream);
        }

    }

    private void loadHotCities() {
        Map<String, Object> query = new HashMap<>();
        query.put("group", "cn");
        query.put("key", RequestConstents.WEATHER_KEY);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest networkRequest = builder.querys(query).build();
        NetworkRequestFactory.getHotCities(networkRequest, mHotCitiesCallback);
    }

    private NetworkCallback<HeFengModule> mHotCitiesCallback = new NetworkCallback<HeFengModule>() {
        @Override
        public void onSuccess(HeFengModule module) {
            MeituanHeaderBean header = mHeaderDatas.get(0);
            List<HeWeather> heWeatherList = module.getHeWeather6();
            for (HeWeather heWeather:heWeatherList) {
                List<Basic> basicList = heWeather.getBasic();
                for (Basic basic : basicList) {
                    header.addCity(basic.getLocation());
                }
            }
            mHeaderAdapter.notifyItemRangeChanged(0, 1);
        }

        @Override
        public void onSuccessInError(HeFengModule module) {

        }

        @Override
        public void onFaile(Throwable e) {

        }
    };
}
