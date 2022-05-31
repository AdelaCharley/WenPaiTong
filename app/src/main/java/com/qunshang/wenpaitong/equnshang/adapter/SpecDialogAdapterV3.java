package com.qunshang.wenpaitong.equnshang.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV3;

public class SpecDialogAdapterV3 extends RecyclerView.Adapter<SpecDialogAdapterV3.ViewHolder> {

    public List<String> data;

    public Context context;

    public LayoutInflater inflater;

    int currentIndex = -1;

    int index;

    String specvalue = "";

    int totallistcount = 0;

    List<Boolean> isTextCanClickAble;//因为这里不在onBindViewHolder里面写的话，我拿不到界面，拿不到界面就没法知道text是否可以点击，所以这里就只能弄一个List来记录一下.

    public SpecDialogAdapterV3(Context context,List<String> data,int index,int totallistcount){//index用来记录当前是第几个
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.index = index;
        this.totallistcount = totallistcount;//用来判断当前是不是最后的一个规格选择器
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        isTextCanClickAble = new ArrayList<>();
        for (int i = 0;i < data.size();i++){
            isTextCanClickAble.add(true);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_specs_dialogs_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (StringUtils.isEmpty(specvalue)){
            holder.text.setText(data.get(position));
            holder.text.setOnClickListener(v -> {
                onTextClick(position);
                /*if (position == currentIndex){
                    return;
                }
                currentIndex = position;
                ProductsDialogV3.words[index] = data.get(position);
                notifyDataSetChanged();
                if (totallistcount > 1){
                    SepcData data = new SepcData();
                    data.setIndex(index);
                    data.setSpecvalue(this.data.get(position));
                    EventBus.getDefault().post(data);
                } else {
                    ProductBean.DataBean.SkuList selectedSku = StringUtils.getSelectedSku();
                    if (selectedSku != null){
                        EventBus.getDefault().post(selectedSku);
                    }
                }*/
            });
            if (currentIndex == position){
                holder.text.setTextColor(context.getColor(R.color.white));
                holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_blue));
            } else {
                holder.text.setTextColor(context.getColor(R.color.black));
                holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_grey));
            }

        } else {
            holder.text.setText(data.get(position));
            if (index == 0){
                return;
            }
            HashSet<String> useableSku = StringUtils.getUseableSku(index - 1,specvalue);//这是可用的规格
            if (useableSku != null){
                if (useableSku.contains(data.get(position))){
                    holder.text.setOnClickListener(v -> {
                        onTextClick(position);
                    });
                    if (currentIndex == position){
                        holder.text.setTextColor(context.getColor(R.color.white));
                        holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_blue));
                    } else {
                        holder.text.setTextColor(context.getColor(R.color.black));
                        holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_grey));
                    }
                } else {
                    holder.text.setClickable(false);
                    isTextCanClickAble.set(position,false);//如果不可点击，这里设置为false
                    holder.text.setTextColor(context.getColor(R.color.text_grey));
                    holder.text.setBackground(context.getDrawable(R.color.grey_light));
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void defValue(String flag){//设置默认的选择内容
        if ("init".equals(flag) && index == 0){//这个初始化是用来为第一个弄的
            initDef();
        } else if ("destory".equals(flag)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void initDef(){
        isTextCanClickAble.clear();
        for (int i = 0;i < data.size();i++){
            isTextCanClickAble.add(true);
        }
        if (!StringUtils.isEmpty(specvalue)){
            HashSet<String> useableSku = StringUtils.getUseableSku(index - 1,specvalue);//这是可用的规格
            if (useableSku != null){
                for (int i = 0;i < data.size();i++){
                    if (!useableSku.contains(data.get(i))){
                        isTextCanClickAble.set(i,false);//如果不可点击，这里设置为false
                    }
                }
            }
        }
        int index = -9;
        for (int i = 0;i < isTextCanClickAble.size();i++){
            if (isTextCanClickAble.get(i)){
                index = i;
                break;
            }
        }
        if (index != -9){
            currentIndex = -1;
            onTextClick(index);
        }
    }

    public void onTextClick(int position){
        if (position == currentIndex){
            return;
        }
        currentIndex = position;
        ProductsDialogV3.words[index] = data.get(position);
        notifyDataSetChanged();
        if (index == totallistcount - 1){//如果是最后一个
            ProductBean.DataBean.SkuList selectedSku = StringUtils.getSelectedSku();
            EventBus.getDefault().post(selectedSku);
        } else {
            SepcData data = new SepcData();
            data.setIndex(index);
            data.setSpecvalue(this.data.get(position));
            EventBus.getDefault().post(data);
        }
    }

    public static class SepcData{

        int index  = 0;//index用来表示当前是第几层的Spec

        String specvalue = "";//用来标记所选的值

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getSpecvalue() {
            return specvalue;
        }

        public void setSpecvalue(String specvalue) {
            this.specvalue = specvalue;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(SepcData data){
        if (data.getIndex() == index - 1){
            this.specvalue = data.getSpecvalue();
            //ToastUtil.toast(context,"当前是" + data.getSpecvalue());
            initDef();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ViewHolder(@NonNull View view) {
            super(view);
            text = view.findViewById(R.id.text);
        }
    }

}

