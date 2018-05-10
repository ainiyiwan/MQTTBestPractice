package paho.android.mqtt_example.util;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * ================================================
 * 作    者：Luffy（张阳）
 * 版    本：1.0
 * 创建日期：2018/5/1
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyAdapter extends BaseQuickAdapter<User, BaseViewHolder> {


    public MyAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
    }
}
