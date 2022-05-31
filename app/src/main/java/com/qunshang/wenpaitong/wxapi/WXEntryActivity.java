package com.qunshang.wenpaitong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.MyApplication;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.activity.ParticipateGroupActivity;
import com.qunshang.wenpaitong.equnshang.activity.PhysicalStoreDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    public static IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.Companion.setAPP_STATUS( MyApplication.Companion.getAPP_STATUS_NORMAL());
        api = WXAPIFactory.createWXAPI(this, Constants.Companion.getWECHAT_APPID(), false);
        Intent intent = getIntent();
        api.handleIntent(intent, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onReq(BaseReq req) {
        if(req.getType() == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX && req instanceof ShowMessageFromWX.Req) {
            StringUtils.log("啊啊啊啊啊啊阿军扩多过付军扩军见个分发的卡结构课");
            ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req) req;
            WXMediaMessage mediaMsg = showReq.message;
            String extInfo = mediaMsg.messageExt;
            StringUtils.log(extInfo);
            String localUserId = UserHelper.getCurrentLoginUser(this);
            if (!StringUtils.isEmpty(localUserId)){
                UserInfoViewModel.INSTANCE.setUserId(localUserId);
            }
            try {
                JSONObject object = new JSONObject(extInfo);
                if (StringUtils.isEmpty(UserInfoViewModel.INSTANCE.getUserId())){
                    Constants.Companion.setRemainedOrderGroupSn(object.getString("orderGroupSn"));
                    Constants.Companion.setHaveRemainedOrderGroupSn(true);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                String type = object.getString("type");
                if (type.equals("group")){
                    Intent intent = new Intent(this, ParticipateGroupActivity.class);
                    intent.putExtra("orderGroupSn",object.getString("orderGroupSn"));
                    startActivity(intent);
                    finish();
                } else if (type.equals("wetProduct")){
                    Intent intent = new Intent(this, WenBanTongProductDetailActivity.class);
                    intent.putExtra("productId",object.getInt("productId"));
                    startActivity(intent);
                    finish();
                } else if ("amall".equals(type)){
                    Intent intent = new Intent(this, AMallV3ProductDetailActivity.class);
                    intent.putExtra("productId",Integer.parseInt(object.getString("productId")));
                    startActivity(intent);
                    finish();
                } else if ("O2O".equals(type)){
                    Intent intent = new Intent(this, PhysicalStoreDetailActivity.class);
                    intent.putExtra("vendorId",object.getInt("vendorId"));
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onResp(BaseResp resp) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
