package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3MainBean;
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class GuessProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<AMallV3SearchProductBean.DataBean.ProductBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public AMallV3MainBean bean;

    public GuessProductAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<AMallV3SearchProductBean.DataBean.ProductBean> datas){
        if (datas == null){
            return;
        }
        this.data.addAll(datas);
        notifyDataSetChanged();
    }

    public void addHeader(AMallV3MainBean bean){
        this.bean = bean;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD){
            return new HeadViewHolder(inflater.inflate(R.layout.item_guess_product_head,parent,false));
        }
        return new ViewHolder(inflater.inflate(R.layout.item_guess_product,parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEAD;
        }
        return TYPE_BODY;
    }

    public final int TYPE_HEAD = 0;

    public final int TYPE_BODY = 1;



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (bean == null){
            return;
        }

        if (holder instanceof HeadViewHolder){
            ((HeadViewHolder) holder).categorys.setAdapter(new AMallV3MainCategoryAdapter(context,bean.getData().getCategory()));

            MyBannerImageAdapter<AMallV3MainBean.DataBean.BannerBean> adapter = new MyBannerImageAdapter<AMallV3MainBean.DataBean.BannerBean>(context,bean.getData().getBanner()) {

                @Override
                public void onBindView(MyBannerViewHolder holder, AMallV3MainBean.DataBean.BannerBean data, int position, int size) {
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    // RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(20, 20);
                    Glide.with(context)
                            .load(data.getBannerUrl())
                            //.apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                            .into(holder.imageView);
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                            intent.putExtra("productId", data.getProductId());
                            context.startActivity(intent);
                        }
                    });
                }
            };
            ((HeadViewHolder) holder).banner
                    .setAdapter(adapter)
                    .addBannerLifecycleObserver((LifecycleOwner) context).setIndicator(new CircleIndicator(context));
            ((HeadViewHolder) holder).activitys.setAdapter(new AMallV3ActivityAdapter(context,bean.getData().getActivity()));
            /*((HeadViewHolder) holder).search_layoout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AMallV3SearchActivity.class);
                    context.startActivity(intent);
                }
            });*/
        } else if (holder instanceof GuessProductAdapter.ViewHolder) {
            AMallV3SearchProductBean.DataBean.ProductBean bean = data.get(position - 1);
            Glide.with(context).load(bean.getProductPosterUrl()).into(((ViewHolder) holder).image);
            ((ViewHolder) holder).name.setText(bean.getProductName());
            ((ViewHolder) holder).price.setText("" + bean.getVipGroupPrice());
            ((ViewHolder) holder).originalprice.setText("" + bean.getMarketPrice());
            ((ViewHolder) holder).amall_layout.setOnClickListener(v -> {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId", bean.getProductId());
                context.startActivity(intent);
            });
            ((ViewHolder) holder).tag.setText(bean.getTag());
            StringUtils.log("当前是ad" + position);
            if (position == 0){
                return;
            }
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(((GuessProductAdapter.ViewHolder) holder).image.getLayoutParams());
            if (position % 2 == 0){//左边的
                layoutParams.setMargins(CommonUtil.dp2px(context,8), CommonUtil.dp2px(context,36),CommonUtil.dp2px(context,18),0);
            } else  {//右边的
                layoutParams.setMargins(CommonUtil.dp2px(context,18), CommonUtil.dp2px(context,36),CommonUtil.dp2px(context,8),0);
            }
            ((GuessProductAdapter.ViewHolder) holder).image.setLayoutParams(layoutParams);
        }

    }

    @Override
    public int getItemCount() {
        //return 4;
        Log.i(Constants.Companion.getLogtag(), "总item是" + data.size());
        if (bean == null){
            return data.size();
        }
        return data.size() + 1;
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder{

        Banner<AMallV3MainBean.DataBean.BannerBean, MyBannerImageAdapter<AMallV3MainBean.DataBean.BannerBean>> banner;

        RecyclerView categorys;

        RecyclerView activitys;

        //View search_layoout;

        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            categorys = itemView.findViewById(R.id.categorys);
            activitys = itemView.findViewById(R.id.activitys);
            //search_layoout = itemView.findViewById(R.id.search_layoout);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        TextView originalprice;

        ConstraintLayout amall_layout;

        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amall_layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            tag = itemView.findViewById(R.id.label);
            originalprice = itemView.findViewById(R.id.originalprice);
        }
    }

}
