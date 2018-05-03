package zalex.person.com.citytoollib.model;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;


public class MeiTuanBean extends BaseIndexPinyinBean {
    private String city;//城市名字

    public MeiTuanBean() {
    }

    public MeiTuanBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public MeiTuanBean setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public String getTarget() {
        return city;
    }
}
