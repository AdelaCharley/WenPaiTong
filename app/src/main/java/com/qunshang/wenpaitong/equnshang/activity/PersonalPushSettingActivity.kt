package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.v3.MessageDialog
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_personal_push_setting.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class PersonalPushSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_push_setting)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("个性化设置")
        if (UserHelper.getPushStatus(this)){
            switcher.isChecked = true
        } else {
            switcher.isChecked = false
        }
        switcher.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener{
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                if (isChecked){
                    UserHelper.setPushStatus(this@PersonalPushSettingActivity,isChecked)
                } else {
                    MessageDialog.show(this@PersonalPushSettingActivity,"提示","关闭后，将无法看到个性化推荐内容","确认","取消")
                        .setOnOkButtonClickListener(object : OnDialogButtonClickListener{
                            override fun onClick(baseDialog: BaseDialog?, v: View?): Boolean {
                                baseDialog?.doDismiss()
                                switcher.isChecked = false
                                UserHelper.setPushStatus(this@PersonalPushSettingActivity,false)
                                return true
                            }

                        })
                        .setOnCancelButtonClickListener(object : OnDialogButtonClickListener{
                            override fun onClick(baseDialog: BaseDialog?, v: View?): Boolean {
                                baseDialog?.doDismiss()
                                switcher.isChecked = true
                                UserHelper.setPushStatus(this@PersonalPushSettingActivity,true)
                                return true
                            }
                        })
                }
            }

        })
    }
}