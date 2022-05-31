package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.*
import kotlinx.android.synthetic.main.activity_manange.*
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

class ManageActivity : BaseActivity() {

    val fragments = arrayOf(
        ManageUserFragment(),
        ManageShopFragnent(),
        ManageYongjinFragnent(),
        ManageContentFragment()
    )

    lateinit var linearlayouts : Array<View>

    lateinit var imageviews : Array<ImageView>

    lateinit var texts : Array<TextView>

    lateinit var imageDefault : Array<Int>

    lateinit var imagesSelected : Array<Int>//被选中时的状态

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manange)
        changeToGreyButTranslucent()
        initImages()
        initView()
        if (UserInfoViewModel.getUserInfo() != null){
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() < 2){
                layout_user.visibility = View.GONE
                layout_yongjin.visibility = View.GONE
                linear_bottom.visibility = View.GONE
            }
        }
    }

    fun initImages(){
        imageDefault = arrayOf(
            R.mipmap.manage_user_unselected,
            R.mipmap.manage_shop_unselected,
            R.mipmap.manage_yongjin_unselected,
            R.mipmap.manage_content_unselected
        )
        imagesSelected = arrayOf(
            R.mipmap.manage_user_selected,
            R.mipmap.manage_shop_selected,
            R.mipmap.manage_yongjin_selected,
            R.mipmap.manage_content_selected
        )
    }

    fun initView(){
        //首先初始化view的数据。
        linearlayouts = arrayOf(
            layout_user,
            layout_shop,
            layout_yongjin,
            layout_content)
        imageviews = arrayOf(
            img_user,
            img_shop,
            img_yongjin,
            img_content
        )
        texts = arrayOf(
            text_user,
            text_shop,
            text_yongjin,
            text_content
        )
        for (item in linearlayouts.indices) {
            linearlayouts[item].setOnClickListener(MainLayoutListener((item)))
        }

        if (intent.getIntExtra("index",0) == 0){
            supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
        } else {
            showFragment(intent.getIntExtra("index",0))
        }

    }

    var index = 0

    inner class MainLayoutListener(val index : Int) : View.OnClickListener{

        override fun onClick(v: View?) {
            showFragment(index)
        }

    }

    fun showFragment(index : Int){
        Log.i(Constants.logtag,"currentindex")
        if (this@ManageActivity.index != index){
            imageviews[this@ManageActivity.index].setImageResource(imageDefault[this@ManageActivity.index])
            texts[this@ManageActivity.index].setTextColor(resources.getColor(R.color.black))

            this@ManageActivity.index = index
            imageviews[this@ManageActivity.index].setImageResource(imagesSelected[this@ManageActivity.index])
            texts[this@ManageActivity.index].setTextColor(resources.getColor(R.color.blue))

            hideAllFragments()
            if (fragments[index].isAdded){
                supportFragmentManager.beginTransaction().show(fragments[index]).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
            }

        }
    }

    fun hideAllFragments(){
        val transaction = supportFragmentManager.beginTransaction()
        for (i in fragments.indices){
            if (fragments[i].isAdded && !fragments[i].isHidden){
                transaction.hide(fragments[i]).commit()
            }
        }
    }

}