package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import kotlinx.android.synthetic.main.fragment_manage_content.*

class ManageContentFragment : MyBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manage_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exit.setOnClickListener { activity?.finish() }
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        titles.add("上传视频")
        titles.add("编辑视频")
        titles.add("作品数据")
        fragments.add(UpLoadVideoFragment())
        fragments.add(EditVideoFragment())
        fragments.add(VideoDataFragment())
        val adapter = BasePagerAdapter(parentFragmentManager,fragments,titles)
        pagers.adapter = adapter
        tabs.setupWithViewPager(pagers)
    }

}