package com.qunshang.wenpaitong.equnshang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.DoApplyAfterSaleActivity;
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil;

public class AfterSaleImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Uri> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    int TYPE_ADD = 1;

    int TYPE_IMAGE = 2;

    public List<Uri> getData(){
        return data;
    }

    public AfterSaleImageAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<Uri> uris){
        this.data.addAll(uris);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD){
            return new AddViewHolder(inflater.inflate(R.layout.item_after_sale_add,parent,false));
        }
        return new ViewHolder(inflater.inflate(R.layout.item_after_sale_image,parent,false));
    }

    int remainCount = 3 - data.size();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        remainCount = 3 - data.size();
        if (remainCount < 0){
            remainCount = 0;
        }

        if (holder instanceof AddViewHolder){
            ((AddViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.Companion.checkStoragePermission((Activity) context)){

                    } else {
                        PermissionUtil.Companion.requireStoragePermission((AppCompatActivity) context);
                        return;
                    }
                    Matisse.from((Activity) context)
                            .choose(MimeType.ofImage())
                            .countable(true)
                            .maxSelectable(remainCount)
                            //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .showPreview(false) // Default is `true`
                            .forResult(DoApplyAfterSaleActivity.Companion.getREQUEST_CODE());
                }
            });
        } else if (holder instanceof ViewHolder){
            Glide.with(context).load(data.get(position)).into(((ViewHolder) holder).image);
            ((ViewHolder) holder).close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()){
            return TYPE_ADD;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() < 3){
            return data.size() +1;
        }
        return 3;
    }

    public static class AddViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            close = itemView.findViewById(R.id.close);
        }
    }

}