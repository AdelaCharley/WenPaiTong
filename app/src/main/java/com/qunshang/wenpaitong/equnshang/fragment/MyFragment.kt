package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.FragmentMyBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.equnshang.activity.BaseActivity
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class MyFragment : MyBaseFragment() {

    lateinit var binding : FragmentMyBinding

    lateinit var fragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyBinding.inflate(layoutInflater)
        /*val  activity = requireActivity() as BaseActivity
        activity.setSystemBarColor(R.color.black)*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    fun initData(){
        val username = UserHelper.getCurrentLoginUser(context)
        if (StringUtils.isEmpty(username)){
            fragment = LoginFragment()
            childFragmentManager.beginTransaction().replace(R.id.container,
                fragment
            ).commit()
        } else {
//            fragment = PersonalFragment()
            fragment = CopyrightPersonalFragment()  //个人中心
            childFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
        }
        if (fragment is  LoginFragment){
            val  activity = requireActivity() as BaseActivity
            activity.changeToGreyButTranslucent()
        } else {
            val  activity = requireActivity() as BaseActivity
            activity.changeToDefaultButTranslucent()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshWhenLoginStatusChanged(string: String){
        if (string.equals("logout")){
            Log.i("zhangjuniii","logout")
            initData()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        StringUtils.log("之你够了" + hidden)
        if (!isHidden){
            if (fragment is LoginFragment){
                val  activity = requireActivity() as BaseActivity
                activity.changeToGreyButTranslucent()
            } else if (fragment is CopyrightPersonalFragment){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (fragment as CopyrightPersonalFragment).initLayoutTop()
                }
            } else {
                val  activity = requireActivity() as BaseActivity
                activity.changeToDefaultButTranslucent()
            }
            if (!isfirst){
                if (fragment is PersonalFragment){
                    val personalFragment = fragment as PersonalFragment
                    personalFragment.init()
                } else {
                    //DialogUtil.toast(requireContext(),"aaa")
                }
            } else {
                isfirst = false
            }
        }
    }

    var isfirst = true

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}