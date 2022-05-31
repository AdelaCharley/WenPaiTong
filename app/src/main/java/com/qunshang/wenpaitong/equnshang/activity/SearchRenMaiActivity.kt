package com.qunshang.wenpaitong.equnshang.activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_search_ren_mai.*
import kotlinx.android.synthetic.main.activity_search_ren_mai.input
import kotlinx.android.synthetic.main.activity_search_ren_mai.search
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.NewRenMaiFragment
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class SearchRenMaiActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_search_ren_mai)
        img1.setOnClickListener { finish() }
        initView()
    }

    fun initView(){
        search.setOnClickListener {
            doSearch()
        }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()== KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    doSearch()
                } else if (keyCode == KeyEvent.KEYCODE_DEL){
                    if (input.text.length != 0){
                        input.text.delete(input.text.length - 1, input.text.length)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish()
                }
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER){

            } else if (keyCode == KeyEvent.KEYCODE_BACK){
                finish()
            }
            return@setOnKeyListener true
        }
    }

    fun doSearch(){

        val fragment = NewRenMaiFragment(null,input.text.toString())
        supportFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
        StringUtils.log("fjdkjk")
        input.setText("")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun result(result : String){
        if ("newrenmainull".equals(result)){
            usernotexists.visibility = View.VISIBLE
        } else {
            usernotexists.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}