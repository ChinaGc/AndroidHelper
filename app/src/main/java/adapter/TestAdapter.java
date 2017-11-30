package adapter;

import com.gc.android_helper.app.BaseListViewAdapter;
import com.gc.android_helper.app.BaseViewHolder;

import java.util.List;

/**
 * Created by guocan on 2017/3/6.
 */

public class TestAdapter extends BaseListViewAdapter<String> {
    private TestViewHolder holder;
    public TestAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    protected BaseViewHolder getHolder() {
        holder = new TestViewHolder();//每个条目都是新实例
        return holder;
    }

}
