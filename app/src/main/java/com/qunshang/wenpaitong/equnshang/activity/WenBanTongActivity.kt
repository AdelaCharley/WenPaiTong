package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Color

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.umeng.analytics.MobclickAgent
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_wen_ban_tong.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.My16dpBannerImageAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongBannerV2Bean
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongActivity : MyBaseFragment() {

    val wenBanTongShopFragment = WenBanTongShopFragment()

    val wenBanTongCompanyFragment = WenBanTongCompanyFragment()

    val wenBanTongAskFragment = WenBanTongAskFragment()

    val wenBanTongJiGouFragment = PhysicalStoresFragment()

    var index = -1

    lateinit var banner : Banner<WenBanTongBannerV2Bean.DataBean, My16dpBannerImageAdapter<WenBanTongBannerV2Bean.DataBean>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_wen_ban_tong,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //super.onCreate(savedInstanceState)
        //setSystemBarColor(R.color.grey_light)
        val activity = requireActivity() as BaseActivity
        //activity.setSystemBarColor(R.color.black);
        //activity.setSystemBarColor(R.color.black);
        activity.changeToGreyButTranslucent()
        //setContentView(R.layout.activity_wen_ban_tong)
        val map = HashMap<String,String>()
        map.put("userId", UserInfoViewModel.getUserId())
        MobclickAgent.onEvent(context,"enterwenbantong",map)
        //toolbar_back.setOnClickListener { finish() }
        toolbar_back.visibility = View.INVISIBLE
        toolbar_title.text = "认购"
        if (toolbar_title.parent != null){
            if (toolbar_title.parent is View){
                var view = toolbar_title.parent as View
                StringUtils.log("parent的idshig" + view.id)
                view.background = null
            }
        }
        banner = view.findViewById(R.id.banner)
        initView()
        loadBanner()
        val index = 0
            //intent.getIntExtra("index",-1)
        if (index != -1){
            when(index){
                0 -> {
                    showShop()
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (isHidden) {
        } else {
            val activity = requireActivity() as BaseActivity
            activity.changeToGreyButTranslucent()
        }
    }

    fun loadBanner(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadBannerV2().enqueue(object :
            Callback<WenBanTongBannerV2Bean> {
            override fun onResponse(
                call: Call<WenBanTongBannerV2Bean>,
                response: Response<WenBanTongBannerV2Bean>
            ) {
                if (response.body() != null){
                    if (response.body()!!.code == 200){
                        val adapter : My16dpBannerImageAdapter<WenBanTongBannerV2Bean.DataBean> = object : My16dpBannerImageAdapter<WenBanTongBannerV2Bean.DataBean>(requireContext(),response.body()?.data){
                            override fun onBindView(holder: MyBannerViewHolder?, data: WenBanTongBannerV2Bean.DataBean?, position: Int, size: Int) {
                                Glide.with(holder!!.itemView.context)
                                    .load(data?.img)
                                    .into(holder.imageView)
                                holder.itemView.setOnClickListener {
                                    if (StringUtils.isEmpty(data?.path)){
                                        return@setOnClickListener
                                    }
                                    val intent = Intent(requireContext(),WebViewActivity::class.java)
                                    intent.putExtra("title","")
                                    intent.putExtra("url", data?.path)
                                    startActivity(intent)
                                }
                            }
                        }
                        banner
                            .setAdapter(adapter)
                            .addBannerLifecycleObserver(this@WenBanTongActivity)
                            .setIndicator(CircleIndicator(requireContext()))
                    }
                }
            }

            override fun onFailure(call: Call<WenBanTongBannerV2Bean>, t: Throwable) {
                StringUtils.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbb"+t.message)
            }

        })
    }

    fun initView(){
        text_shop.setOnClickListener {
            showShop()
        }
        text_company.setOnClickListener {
            showCompany()
        }
        text_ask.setOnClickListener {
            showAsk()
        }
        jigou.setOnClickListener {
            showJiGou()
        }
        toolbar_right_image.setOnClickListener {
            val intent = Intent(requireContext(),WenBanTongOrderListActivity::class.java)
            startActivity(intent)
        }
        showShop()
    }

    fun showShop(){
        if (index == 0){
            return
        }
        index = 0
        text_shop.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_shop.setTextColor(Color.parseColor("#ffa98f60"))
        text_company.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_company.setTextColor(Color.parseColor("#ff333333"))
        text_ask.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_ask.setTextColor(Color.parseColor("#ff333333"))
        jigou.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        jigou.setTextColor(Color.parseColor("#ff333333"))
        hideAllFragments()
        if (wenBanTongShopFragment.isAdded){
            getMyFragmentManager().beginTransaction().show(wenBanTongShopFragment).commit()
        } else {
            getMyFragmentManager().beginTransaction().add(R.id.container,wenBanTongShopFragment).commit()
        }
    }

    fun showJiGou(){
        if (index == 3){
            return
        }
        index = 3
        jigou.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        jigou.setTextColor(Color.parseColor("#ffa98f60"))
        text_company.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_company.setTextColor(Color.parseColor("#ff333333"))
        text_ask.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_ask.setTextColor(Color.parseColor("#ff333333"))
        text_shop.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_shop.setTextColor(Color.parseColor("#ff333333"))
        hideAllFragments()
        if (wenBanTongJiGouFragment.isAdded){
            getMyFragmentManager().beginTransaction().show(wenBanTongJiGouFragment).commit()
        } else {
            getMyFragmentManager().beginTransaction().add(R.id.container,wenBanTongJiGouFragment).commit()
        }
    }

    fun showCompany(){
        if (index == 1){
            return
        }
        index = 1
        text_shop.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_shop.setTextColor(Color.parseColor("#ff333333"))
        text_company.setTextColor(Color.parseColor("#ffa98f60"))
        text_ask.setTextColor(Color.parseColor("#ff333333"))
        text_company.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_ask.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        jigou.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        jigou.setTextColor(Color.parseColor("#ff333333"))
        hideAllFragments()
        if (wenBanTongCompanyFragment.isAdded){
            getMyFragmentManager().beginTransaction().show(wenBanTongCompanyFragment).commit()
        } else {
            getMyFragmentManager().beginTransaction().add(R.id.container,wenBanTongCompanyFragment).commit()
        }
    }

    fun showAsk(){
        if (index == 2){
            return
        }
        index = 2
        text_shop.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_company.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_ask.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        text_shop.setTextColor(Color.parseColor("#ff333333"))
        text_company.setTextColor(Color.parseColor("#ff333333"))
        text_ask.setTextColor(Color.parseColor("#ffa98f60"))
        jigou.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            18.toFloat()
        )
        jigou.setTextColor(Color.parseColor("#ff333333"))
        hideAllFragments()
        if (wenBanTongAskFragment.isAdded){
            getMyFragmentManager().beginTransaction().show(wenBanTongAskFragment).commit()
        } else {
            getMyFragmentManager().beginTransaction().add(R.id.container,wenBanTongAskFragment).commit()
        }
    }

    fun getMyFragmentManager() : FragmentManager{
        return childFragmentManager
    }

    fun hideAllFragments(){
        val transaction = getMyFragmentManager().beginTransaction()
        if (wenBanTongShopFragment.isAdded && !wenBanTongShopFragment.isHidden){
            transaction.hide(wenBanTongShopFragment).commit()
        }
        if (wenBanTongCompanyFragment.isAdded && !wenBanTongCompanyFragment.isHidden){
            transaction.hide(wenBanTongCompanyFragment).commit()
        }
        if (wenBanTongAskFragment.isAdded && !wenBanTongAskFragment.isHidden){
            transaction.hide(wenBanTongAskFragment).commit()
        }
        if (wenBanTongJiGouFragment.isAdded && !wenBanTongJiGouFragment.isHidden){
            transaction.hide(wenBanTongJiGouFragment).commit()
        }
    }

}