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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ModifyAddressActivity;
import com.qunshang.wenpaitong.equnshang.data.AddressBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    public List<AddressBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    boolean isSelect = false;

    public AddressAdapter(Context context,List<AddressBean.DataBean> data,boolean isSelect){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.isSelect = isSelect;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(Integer i){}

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(inflater.inflate(R.layout.item_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressBean.DataBean bean = data.get(position);
        if ("0".equals(bean.getIsDefault())){
            holder.def.setVisibility(View.GONE);
        }
        holder.tvSite.setText(bean.getSite());
        holder.tvTitle.setText(StringUtils.getBriefAddress(bean.getSite()));
        holder.tvPName.setText(bean.getName());
        holder.tvPPhone.setText(bean.getPhone());
        holder.modify.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyAddressActivity.class);
            intent.putExtra("data",bean);
            context.startActivity(intent);
        });
        holder.layout.setOnClickListener(v -> {
            /*AddressData data = new AddressData();
            data.setId(bean.getId());*/
            if (isSelect){
                EventBus.getDefault().post(bean);
                /*Activity activity = (Activity) context;
                Intent intent = new Intent();
                intent.putExtra("???","???")
                activity.setResult(RESULT_CODE,intent);
                activity.finish();*/
            } else {
                Intent intent = new Intent(context, ModifyAddressActivity.class);
                intent.putExtra("data",bean);
                context.startActivity(intent);
            }
        });
    }

    public static final int RESULT_CODE = 9584;

    /*public static class AddressData {



        public String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }*/

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView def;

        TextView tvTitle;

        TextView tvSite;

        TextView tvPName;

        TextView tvPPhone;

        ImageView modify;

        ViewGroup layout;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_address_title);
            tvSite = itemView.findViewById(R.id.tv_address_site);
            tvPName = itemView.findViewById(R.id.tv_address_user_name);
            tvPPhone = itemView.findViewById(R.id.tv_address_user_phone);
            modify = itemView.findViewById(R.id.modify);
            def = itemView.findViewById(R.id.def);
            layout = itemView.findViewById(R.id.layout_title_bind_address);
        }
    }

}
