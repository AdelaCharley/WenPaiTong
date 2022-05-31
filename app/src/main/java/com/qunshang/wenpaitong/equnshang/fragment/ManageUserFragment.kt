package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.fragment_manage_user.*
import com.qunshang.wenpaitong.equnshang.activity.BaseActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

class ManageUserFragment : MyBaseFragment() {

    var index = 0

    lateinit var fans : ManageUser_FansFragment

    lateinit var vip : ManageUser_VIPFragment

    lateinit var partner : ManageUser_PartnerFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manage_user, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden){

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as BaseActivity
        if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3){
            identity.setText("当前身份为:主任")
        } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
            identity.setText("当前身份为:总裁")
        } else {
            identity.setText("当前身份为:店主")
        }
        layout_fans.setOnClickListener {
            showFans()
        }
        layout_vip.setOnClickListener {
            showVIP()
        }
        layout_partner.setOnClickListener {
            showPartner()
        }
        showFans(true)
        exit.setOnClickListener { requireActivity().finish() }
    }

    fun hideAll(){
        val transaction = childFragmentManager.beginTransaction()
        if(this::fans.isInitialized){
            transaction.hide(fans);
        }
        if(this::vip.isInitialized){
            transaction.hide(vip);
        }
        if(this::partner.isInitialized){
            transaction.hide(partner);
        }
        transaction.commit();
    }

    fun showFans(isfirst : Boolean = false){
        if ((index != 0) or isfirst){
            layout_fans.background = requireContext().getDrawable(R.drawable.bg_fans_selected)
            image_fans.setImageResource(R.mipmap.manage_fans)
            text_fans.setTextColor(requireContext().getColor(R.color.white))
            layout_vip.background = requireContext().getDrawable(R.mipmap.bg_vip_unselected)
            image_vip.setImageResource(R.mipmap.manage_unvip)
            text_vip.setTextColor(requireContext().getColor(R.color.black))
            layout_partner.background = requireContext().getDrawable(R.mipmap.bg_partner_unselected)
            image_partner.setImageResource(R.mipmap.manage_unpartner)
            text_partner.setTextColor(requireContext().getColor(R.color.black))
            index = 0

            val transaction = childFragmentManager.beginTransaction()
            if(!this::fans.isInitialized){
                fans = ManageUser_FansFragment()
                transaction.add(R.id.container,fans)
            }
            hideAll()
            transaction.show(fans)
            transaction.commit()
        }
    }

    fun showVIP(){
        if (index != 1){
            layout_fans.background = requireContext().getDrawable(R.mipmap.bg_fans_unselected)
            image_fans.setImageResource(R.mipmap.manage_unfans)
            text_fans.setTextColor(requireContext().getColor(R.color.black))
            layout_vip.background = requireContext().getDrawable(R.drawable.bg_vip_selected)
            image_vip.setImageResource(R.mipmap.manage_vip)
            text_vip.setTextColor(requireContext().getColor(R.color.white))
            layout_partner.background = requireContext().getDrawable(R.mipmap.bg_partner_unselected)
            image_partner.setImageResource(R.mipmap.manage_unpartner)
            text_partner.setTextColor(requireContext().getColor(R.color.black))
            index = 1

            val transaction = childFragmentManager.beginTransaction()
            if(!this::vip.isInitialized){
                vip = ManageUser_VIPFragment()
                transaction.add(R.id.container,vip)
            }
            hideAll()
            transaction.show(vip)
            transaction.commit()
        }
    }

    fun showPartner(){
        if (index != 2){
            layout_fans.background = requireContext().getDrawable(R.mipmap.bg_fans_unselected)
            image_fans.setImageResource(R.mipmap.manage_unfans)
            text_fans.setTextColor(requireContext().getColor(R.color.black))
            layout_vip.background = requireContext().getDrawable(R.mipmap.bg_vip_unselected)
            image_vip.setImageResource(R.mipmap.manage_unvip)
            text_vip.setTextColor(requireContext().getColor(R.color.black))
            layout_partner.background = requireContext().getDrawable(R.drawable.bg_partner_selected)
            image_partner.setImageResource(R.mipmap.manage_partner)
            text_partner.setTextColor(requireContext().getColor(R.color.white))
            index = 2

            val transaction = childFragmentManager.beginTransaction()
            if(!this::partner.isInitialized){
                partner = ManageUser_PartnerFragment()
                transaction.add(R.id.container,partner)
            }
            hideAll()
            transaction.show(partner)
            transaction.commit()
        }
    }

}