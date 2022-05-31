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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AddressActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.ModifyAddressActivityV2;
import com.qunshang.wenpaitong.equnshang.data.AddressBean;
import com.qunshang.wenpaitong.equnshang.data.OperateAddressBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class AddressAdapterV2 extends RecyclerView.Adapter<AddressAdapterV2.AddressViewHolder> {

    public List<AddressBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    boolean isSelect = false;

    public AddressAdapterV2(Context context,List<AddressBean.DataBean> data,boolean isSelect){
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
        return new AddressViewHolder(inflater.inflate(R.layout.item_addressv2,parent,false));
    }

    public void setDef(int position){
        ApiManager.Companion.getInstance().getApiAddress().modifyAddress(UserInfoViewModel.INSTANCE.getUserId(), data.get(position).getName(),data.get(position).getPhone(),data.get(position).getSite(),
                "1",data.get(position).getId()).enqueue(new Callback<OperateAddressBean>() {
            @Override
            public void onResponse(Call<OperateAddressBean> call, Response<OperateAddressBean> response) {
                if (response.body() == null){
                    return;
                }
                if (response.body().getStatusCode() == 0){
                    DialogUtil.toast(context,"修改成功");
                    AddressActivityV2 activityV2 = (AddressActivityV2) context;
                    activityV2.loadData();
                }
            }

            @Override
            public void onFailure(Call<OperateAddressBean> call, Throwable t) {

            }
        });
        /*ApiManager.getInstance().getApiAddress().modifyAddress(
                UserInfoViewModel.getUserId(),
                videoname.text.toString(),
                phone.text.toString(),
                address + "," + detail.text.toString(),default,bean.id).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean> {
            override fun onResponse(
                    call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>,
            response: Response<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>
            ) {
                if (response.body()!= null){
                    val body = response.body()
                    if (body?.statusCode == 0){
                        DialogUtil.toast(this@ModifyAddressActivity,"修改成功")
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>, t: Throwable) {

            }

        })*/
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressBean.DataBean bean = data.get(position);
        StringUtils.log("addressId是" + bean.getId());
        if ("0".equals(bean.getIsDefault())){//为默认地址
            holder.img_isdef.setImageDrawable(context.getDrawable(R.mipmap.amallv3_address_isdef_false));
            holder.text_isdef.setText("设为默认");
            holder.text_isdef.setTextColor(context.getResources().getColor(R.color.color_text_isdef_false));
        } else {
            holder.img_isdef.setImageDrawable(context.getDrawable(R.mipmap.amallv3_address_isdef_true));
            holder.text_isdef.setText("已设为默认");
            holder.text_isdef.setTextColor(context.getResources().getColor(R.color.color_text_isdef_true));
        }
        holder.address.setText(bean.getSite());
        //holder.tvTitle.setText(StringUtils.getBriefAddress(bean.getSite()));
        holder.name.setText(bean.getName());
        holder.phone.setText(bean.getPhone());
        holder.modify.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyAddressActivityV2.class);
            intent.putExtra("data",bean);
            context.startActivity(intent);
        });
        holder.text_isdef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDef(position);
            }
        });
        holder.img_isdef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDef(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show((AppCompatActivity) context,"提示","您确定要删除吗","确定","取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        ApiManager.Companion.getInstance().getApiAddress().deleteAddress(String.valueOf(bean.getId())).enqueue(new Callback<OperateAddressBean>() {
                            @Override
                            public void onResponse(Call<OperateAddressBean> call, Response<OperateAddressBean> response) {
                                if (response.body() == null){
                                    return;
                                }
                                OperateAddressBean data = response.body();
                                if (data.getStatusCode() == 0){
                                    DialogUtil.toast(context,"删除成功");
                                    AddressActivityV2 activityV2 = (AddressActivityV2) context;
                                    activityV2.loadData();
                                }
                            }

                            @Override
                            public void onFailure(Call<OperateAddressBean> call, Throwable t) {

                            }
                        });
                        return false;
                    }
                });

                /*ApiManager.getInstance().getApiAddress().deleteAddress(bean.id).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>{
                    override fun onResponse(
                            call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>,
                    response: Response<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>
            ) {
                        val data = response.body()
                        if (data?.statusCode == 0){
                            DialogUtil.toast(this@ModifyAddressActivity,"删除成功")
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>, t: Throwable) {

                    }

                })*/
            }
        });
        holder.layout.setOnClickListener(v -> {
            /*AddressData data = new AddressData();
            data.setId(bean.getId());*/
            if (isSelect){
                EventBus.getDefault().post(bean);
            } else {
                /*Intent intent = new Intent(context, ModifyAddressActivity.class);
                intent.putExtra("data",bean);
                context.startActivity(intent);*/
            }
        });
    }

    public static final int RESULT_CODE = 9584;

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder{

        TextView address;

        TextView name;

        TextView phone;

        ImageView img_isdef;

        TextView text_isdef;

        TextView delete;

        TextView modify;

        View layout;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            img_isdef = itemView.findViewById(R.id.img_isdef);
            text_isdef = itemView.findViewById(R.id.text_isdef);
            delete = itemView.findViewById(R.id.delete);
            modify = itemView.findViewById(R.id.modify);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}