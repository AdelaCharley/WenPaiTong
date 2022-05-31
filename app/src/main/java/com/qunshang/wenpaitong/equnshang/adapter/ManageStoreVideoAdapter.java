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

import com.bumptech.glide.Glide;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ManageStoreVideoActivity;
import com.qunshang.wenpaitong.equnshang.activity.WatchVideoActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.VendorModel;
import com.qunshang.wenpaitong.equnshang.data.VendorVideoBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

public class ManageStoreVideoAdapter extends RecyclerView.Adapter<ManageStoreVideoAdapter.ViewHolder> {

    public List<VendorVideoBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public ManageStoreVideoAdapter(Context context,List<VendorVideoBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ManageStoreVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_manage_albumvideo_store,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layout_top.setVisibility(View.GONE);
        VendorVideoBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getVideoPosterUrl()).into(holder.image);
        holder.title.setText(bean.getTitle());
        holder.time.setText(bean.getCreateTime());
        holder.layout_delete.setOnClickListener(v -> {
            MessageDialog.show((AppCompatActivity) context, "", "确认是否删除？", "确认", "取消")
                    .setOkButton(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            ApiManager.Companion.getInstance().getApiStore().deleteVideos(VendorModel.INSTANCE.getVendorId(), bean.getVideoId())
                                    .enqueue(new Callback<BaseHttpBean<String>>() {
                                        @Override
                                        public void onResponse(Call<BaseHttpBean<String>> call, Response<BaseHttpBean<String>> response) {
                                            if (response.body() == null) {
                                                return;
                                            }
                                            if (response.body().getCode() == 200) {
                                                ManageStoreVideoActivity activity = (ManageStoreVideoActivity) context;
                                                activity.loadVideos();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseHttpBean<String>> call, Throwable t) {

                                        }
                                    });
                            return false;
                        }
                    });
        });
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, WatchVideoActivity.class);
            intent.putExtra("data", bean);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View layout_delete;

        View layout_top;

        TextView title;

        TextView time;

        ImageView image;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_delete = itemView.findViewById(R.id.layout_delete);
            layout_top = itemView.findViewById(R.id.layout_top);
            title = itemView.findViewById(R.id.titlestr);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
