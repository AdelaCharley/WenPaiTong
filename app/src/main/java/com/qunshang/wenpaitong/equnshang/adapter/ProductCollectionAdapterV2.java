package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3ProductStarBean;
import com.qunshang.wenpaitong.equnshang.data.StarProductBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.fragment.ProductStarFragmentV2;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class ProductCollectionAdapterV2 extends RecyclerView.Adapter<ProductCollectionAdapterV2.ViewHolder> {

    public List<AMallV3ProductStarBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    //public HashSet<Integer> selectedForDelete;//这个地方有一个神奇的bug，每当我请求玩网络后，不管HashSet中之前有多少数据，请求之后HashSet都会被置位空.
    //但是当我把他转为非静态后没有用，一样会强行变成空.

    //boolean isAllSelected = false;

    ProductStarFragmentV2 fragmentV2;

    public ProductCollectionAdapterV2(Context context, List<AMallV3ProductStarBean.DataBean> data, ProductStarFragmentV2 fragmentV2){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.fragmentV2 = fragmentV2;
    }

    @NonNull
    @Override
    public ProductCollectionAdapterV2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_user_product_collectionv2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCollectionAdapterV2.ViewHolder holder, int position) {
        AMallV3ProductStarBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText(String.valueOf("" + bean.getVipGroupPrice()));
        holder.originalprice.setText(String.valueOf("￥" + bean.getMarketPrice()));
        holder.amall_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            }
        });
        holder.sale.setText("已售" + bean.getSale());
        holder.status.setText(bean.getTag());
        holder.concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<Integer> set = new HashSet<>();
                set.add(bean.getCollectId());
                String list = StringUtils.analyseSetToString(set);
                ApiManager.Companion.getInstance().getApiMallTest().loadStarProduct(UserInfoViewModel.INSTANCE.getUserId(), String.valueOf(bean.getProductId())).enqueue(
                        new Callback<StarProductBean>() {
                            @Override
                            public void onResponse(Call<StarProductBean> call, Response<StarProductBean> response) {
                                if (response.body() == null){
                                    return;
                                }
                                if (response.body().getCode() == 200){
                                    fragmentV2.loadData();
                                }
                            }

                            @Override
                            public void onFailure(Call<StarProductBean> call, Throwable t) {

                            }
                        });
                    /*override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {
                        val data = response.body()
                        if (data?.code == 200){
                            loadData()
                            //adapter.updateAfterRemove()
                        } else {
                            DialogUtil.toast(requireContext(),data?.msg)
                        }
                    }

                    override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                    }

                })*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        TextView originalprice;

        ViewGroup amall_layout;

        TextView sale;

        TextView concern;

        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amall_layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            sale = itemView.findViewById(R.id.sale);
            status = itemView.findViewById(R.id.status);
            originalprice = itemView.findViewById(R.id.originalprice);
            concern = itemView.findViewById(R.id.concern);
        }
    }

}
