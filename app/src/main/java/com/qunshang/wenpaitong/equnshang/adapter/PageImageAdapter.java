package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean;

public class PageImageAdapter extends PagerAdapter {

    private Context context;//上下文
    private List<VendorDetailBean.DataBean.Image> list;//数据源

    public PageImageAdapter(Context context, List<VendorDetailBean.DataBean.Image> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //加载vp的布局
        View inflate = View.inflate(context, R.layout.item_view_image_pager, null);
        //给布局控件赋值
        ImageView image = inflate.findViewById(R.id.image);
        Glide.with(context).load(list.get(position).getPictureUrl()).into(image);
        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}