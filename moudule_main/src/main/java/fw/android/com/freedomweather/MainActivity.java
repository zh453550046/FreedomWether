package fw.android.com.freedomweather;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noah.mgtv.imagelib.ImageDrawView;
import com.noah.mgtv.toolslib.NumberConventer;
import com.noah.mgtv.toolslib.ScreenUtil;
import com.stone.pile.libs.PileLayout;

import zalex.person.com.frameworklib.base.BaseActivity;
import zalex.person.com.frameworklib.route.RouterSchema;

@Route(path = RouterSchema.SCHEMA_MAIN_PAGE)
public class MainActivity extends BaseActivity {
    private NavigationView navigationView;
    private PileLayout pileLayout;
    private ImageDrawView bgDrawView;
    private long backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setViewSize();

        pileLayout = (PileLayout) findViewById(R.id.pileLayout);
        pileLayout.setAdapter(new PileLayout.Adapter() {
            @Override
            public int getLayoutId() {
                // item's layout resource id
                return R.layout.item_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }
                // recycled view bind new position
                viewHolder.date.setText("星期" + NumberConventer.ToConvtZH(position % 7 + 1));
            }

            @Override
            public int getItemCount() {
                // item count
                return 10;
            }

            @Override
            public void displaying(int position) {
                // right displaying the left biggest itemView's position
            }

            @Override
            public void onItemClick(View view, int position) {
                // on item click
            }
        });

    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        bgDrawView = (ImageDrawView) findViewById(R.id.main_bg);

        bgDrawView.setImageURI("res://fw.android.com.freedomweather/" + R.mipmap.main_page_bg);
    }

    private void setViewSize() {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        ViewGroup.LayoutParams navigationLayoutParams = navigationView.getLayoutParams();
        navigationLayoutParams.width = screenWidth / 3 * 2;
        navigationView.setLayoutParams(navigationLayoutParams);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - backTime > 2000) {
                backTime = currentTime;
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
