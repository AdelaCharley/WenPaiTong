package com.qunshang.wenpaitong.equnshang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.utils.ViewUtil;

public class TextViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        TextView textView = findViewById(R.id.text);
        ViewUtil.setParagraphSpacing(this,textView,
                "房价的看法巨大空间访客登记肌肤的空间广阔的价格肯定就\\n看JFK大家观看角度看角度看公交卡公交卡的建看\n附近的空间反馈肌肤的开发接口的附近的空间放得开",
                100,10);
    }
}