package com.qunshang.wenpaitong.equnshang.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.qunshang.wenpaitong.R;

public class ViewUtil {

    public static void setParagraphSpacing(Context context, TextView tv, String content, int paragraphSpacing, int lineSpacingExtra) {
        if (!content.contains("\n")) {
            tv.setText(content);
            return;
        }
        content = content.replace("\n", "\n\r");

        int previousIndex = content.indexOf("\n\r");
        //记录每个段落开始的index，第一段没有，从第二段开始
        List<Integer> nextParagraphBeginIndexes = new ArrayList<>();
        nextParagraphBeginIndexes.add(previousIndex);
        while (previousIndex != -1) {
            int nextIndex = content.indexOf("\n\r", previousIndex + 2);
            previousIndex = nextIndex;
            if (previousIndex != -1) {
                nextParagraphBeginIndexes.add(previousIndex);
            }
        }
        //获取行高（包含文字高度和行距）
        float lineHeight = tv.getLineHeight();

        //把\r替换成透明长方形（宽:1px，高：字高+段距）
        SpannableString spanString = new SpannableString(content);
        Drawable d = ContextCompat.getDrawable(context, R.drawable.paragraph_space);
        float density = context.getResources().getDisplayMetrics().density;
        //int强转部分为：行高 - 行距 + 段距
        d.setBounds(0, 0, 1, (int) ((lineHeight - lineSpacingExtra * density) / 1.2 + (paragraphSpacing - lineSpacingExtra) * density));

        for (int index : nextParagraphBeginIndexes) {
            // \r在String中占一个index
            spanString.setSpan(new ImageSpan(d), index + 1, index + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(spanString);
    }

}
