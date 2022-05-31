package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_view_images.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.PageImageAdapter
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean

class ViewImagesActivity : BaseActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images)
        val images = intent.getSerializableExtra("images") as List<VendorDetailBean.DataBean.Image>
        val index = intent.getIntExtra("index",0)
        toolbar_back.setOnClickListener { finish() }
        viewpager.adapter = PageImageAdapter(this,images)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentindex.setText((position+1).toString() + "/" + images.size.toString())
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        viewpager.setCurrentItem(index)
    }
}