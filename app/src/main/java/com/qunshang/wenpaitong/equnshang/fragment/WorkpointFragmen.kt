package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import kotlinx.android.synthetic.main.fragment_workpoint.*
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class WorkpointFragment : MyBaseFragment() {
    private var userInfo : UserMsgBean.UserInfoBean? = null

    lateinit var fragment_workpoint : WorkpointDetailFragment

    lateinit var fragment_group_ticket : GroupTicketFragment

    lateinit var fragment_private_area : PrivateAreaFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val  activity = requireActivity() as BaseActivity
        activity.changeToGreyButTranslucent()
        return inflater.inflate(R.layout.fragment_workpoint, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userInfo = UserInfoViewModel.getUserInfo()
        initLayout()
    }

    private fun initLayout() {
        if (!UserHelper.isLogin(requireContext())){
            image_user.setImageDrawable(resources.getDrawable(R.mipmap.workpoint_def_headimage))
            pleaselogin.visibility = View.VISIBLE
            foot.visibility = View.GONE
            labelidentity_fans.visibility = View.GONE
            vip.visibility = View.GONE
            labelidentity_vip.visibility = View.GONE
            partner.visibility = View.GONE
            labelidentity_partner.visibility = View.GONE
            videoname.visibility = View.GONE
            image_user.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
            pleaselogin.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
//            startActivity(Intent(context, LoginActivity::class.java))
//            return
        } else {
            Glide.with(this)
                .load(userInfo?.headimage)
                .into(image_user)
            videoname.text = userInfo?.uname
            if ((UserInfoViewModel.getUserInfo()!!.getIs_vip() < 2) and (UserInfoViewModel.getUserInfo()!!.getIs_partner() < 2)){
                foot.visibility = View.VISIBLE
                labelidentity_fans.visibility = View.VISIBLE
            } else if ((UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2) and (UserInfoViewModel.getUserInfo()!!.getIs_partner() < 2)){
                vip.visibility = View.VISIBLE
                labelidentity_vip.visibility = View.VISIBLE
            } else if ((UserInfoViewModel.getUserInfo()!!.getIs_vip() < 2) and (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2)){
                partner.visibility = View.VISIBLE
                labelidentity_partner.visibility = View.VISIBLE
            } else if ((UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2) and (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2)){
                vip.visibility = View.VISIBLE
                labelidentity_vip.visibility = View.VISIBLE
                partner.visibility = View.VISIBLE
                labelidentity_partner.visibility = View.VISIBLE
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3){
                labelidentity_partner.setText("主任")
                Glide.with(requireContext()).load(R.mipmap.image_managepartner).into(partner)
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                labelidentity_partner.setText("总裁")
                Glide.with(requireContext()).load(R.mipmap.image_createpartner).into(partner)
            } else {
                Glide.with(requireContext()).load(R.mipmap.manage_unpartner).into(partner)
            }
        }

        setTabLayout()
        showWorkPoint()
        rule.setOnClickListener {
            /*val intent = Intent(context,WorkPointRuleActivity::class.java)
            startActivity(intent)*/
            startActivity(Intent(requireActivity(),GroupTicketRuleActivity::class.java))
        }
    }

    private fun setTabLayout() {
        amall.setOnClickListener {
            if (!UserHelper.isLogin(requireContext())){
                startActivity(Intent(context,LoginActivity::class.java))
                return@setOnClickListener
            }
            startActivity(Intent(context, WenBanTongActivity::class.java))
        }
        workpoint.setOnClickListener {
            showWorkPoint()
        }
        groupticket.setOnClickListener {
            showGroupTicket()
        }
        privatearea.setOnClickListener {
            showPrivateArea()
        }
    }

    fun showWorkPoint(){
        image_workpoint.visibility = View.VISIBLE
        text_workpoint.setTextColor(resources.getColor(R.color.white))
        workpoint.background = resources.getDrawable(R.mipmap.rectangle9)
        text_groupticket.setTextColor(resources.getColor(R.color.black))
        groupticket.background = null
        text_privatearea.setTextColor(resources.getColor(R.color.black))
        privatearea.background = null

        val tra = childFragmentManager.beginTransaction()
        if (!this::fragment_workpoint.isInitialized){
            fragment_workpoint = WorkpointDetailFragment()
            tra.add(R.id.container,fragment_workpoint)
        }

        hideAllFragments()
        tra.show(fragment_workpoint)
        tra.commit()
    }

    fun showGroupTicket(){
        if (!UserHelper.isLogin(requireContext())){
            startActivity(Intent(context,LoginActivity::class.java))
            return
        }
        image_workpoint.visibility = View.GONE
        text_workpoint.setTextColor(resources.getColor(R.color.black))
        workpoint.background = null
        text_groupticket.setTextColor(resources.getColor(R.color.white))
        groupticket.background = resources.getDrawable(R.mipmap.chengy)
        text_privatearea.setTextColor(resources.getColor(R.color.black))
        privatearea.background = null

        val tra = childFragmentManager.beginTransaction()
        if (!this::fragment_group_ticket.isInitialized){
            fragment_group_ticket = GroupTicketFragment()
            tra.add(R.id.container,fragment_group_ticket)
        }

        hideAllFragments()
        tra.show(fragment_group_ticket)
        tra.commit()
    }

    fun showPrivateArea(){
        if (!UserHelper.isLogin(requireContext())){
            startActivity(Intent(context,LoginActivity::class.java))
            return
        }
        image_workpoint.visibility = View.GONE
        text_workpoint.setTextColor(resources.getColor(R.color.black))
        workpoint.background = null
        text_groupticket.setTextColor(resources.getColor(R.color.black))
        groupticket.background = null
        text_privatearea.setTextColor(resources.getColor(R.color.white))
        privatearea.background = resources.getDrawable(R.mipmap.rectangle10)

        val tra = childFragmentManager.beginTransaction()
        if (!this::fragment_private_area.isInitialized){
            fragment_private_area = PrivateAreaFragment()
            tra.add(R.id.container,fragment_private_area)
        }

        hideAllFragments()
        tra.show(fragment_private_area)

        tra.commit()
    }

    var isFirst = false

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden){
            val  activity = requireActivity() as BaseActivity
            activity.changeToGreyButTranslucent()
        }
        if (!isFirst){
            isFirst = true
            return
        }
        if (!UserHelper.isLogin(requireContext())){
            return
        }
        refresh()
    }

    fun refresh(){
        if (this::fragment_workpoint.isInitialized){
            fragment_workpoint.refresh()
        }
        if (this::fragment_group_ticket.isInitialized){
            fragment_group_ticket.refresh()
        }
        if (this::fragment_private_area.isInitialized){
            fragment_private_area.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        if (UserHelper.isLogin(requireContext())){
            videoname.text = UserInfoViewModel.getUserInfo()!!.uname
            Glide.with(this).load(UserInfoViewModel.getUserInfo()!!.headimage).into(image_user)
        }
    }

    fun hideAllFragments(){
        val transaction = childFragmentManager.beginTransaction();
        if(this::fragment_workpoint.isInitialized){
            transaction.hide(fragment_workpoint);
        }
        if(this::fragment_group_ticket.isInitialized){
            transaction.hide(fragment_group_ticket);
        }
        if(this::fragment_private_area.isInitialized){
            transaction.hide(fragment_private_area);
        }

        transaction.commit();
    }

}