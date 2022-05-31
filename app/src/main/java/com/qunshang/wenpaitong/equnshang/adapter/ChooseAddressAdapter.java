package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ModifyAddressActivity;
import com.qunshang.wenpaitong.equnshang.data.AddressBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.AddressViewHolder> {

    public List<AddressBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    boolean isSelect = false;

    public ChooseAddressAdapter(Context context,List<AddressBean.DataBean> data,boolean isSelect){
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
        return new AddressViewHolder(inflater.inflate(R.layout.item_choose_address,parent,false));
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
                MessageDialog.show((AppCompatActivity) context, "提示", "此信息一旦提交不可更改请确认信息无误！", "确认", "取消").setOkButton(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        EventBus.getDefault().post(bean);
                        return true;
                    }
                });
            } else {
                Intent intent = new Intent(context, ModifyAddressActivity.class);
                intent.putExtra("data",bean);
                context.startActivity(intent);
            }
        });
    }

    public static final int RESULT_CODE = 9584;

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