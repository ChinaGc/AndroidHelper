package adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guocan.test.R;
import com.gc.android_helper.app.BaseViewHolder;
import com.gc.android_helper.util.ImageLoader;

/**
 * Created by 郭灿 on 2017/3/27.
 */

public class TestViewHolder extends BaseViewHolder<String> {
    private Context context;

    private ImageLoader imageLoader;

    private ImageView imageView;

    private TextView textView;

    public TestViewHolder() {
        imageLoader = api.getSingleImageLoader();
        imageLoader.setDefaultResId(R.mipmap.default_item);
        imageLoader.setFailedResId(R.mipmap.failed);
    }

    /**
     * 返回ListView单个条目的View
     */
    @Override
    protected View initContentView() {
        View view = View.inflate(api.getContext(), R.layout.list_view_item, null);
        imageView = (ImageView) view.findViewById(R.id.img_item);
        textView = (TextView) view.findViewById(R.id.text_item);
        return view;
    }

    /**
     * 绑定ListView单个条目的数据
     *
     * @param data
     */
    @Override
    protected void bindData(String data, int position) {
        textView.setText("火影忍者动画完结");
        imageLoader.displayImage("http://n1.itc.cn/img8/wb/recom/2017/03/25/149041340639178731.JPEG", imageView);
    }
}
