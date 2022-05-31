package com.qunshang.wenpaitong.equnshang.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlin.collections.ArrayList

/**
 * create by libo
 * create on 2020/5/19
 * description 公共viewPageradapter
 */
class BasePagerAdapter(fm: FragmentManager?, private val items: ArrayList<out Fragment>, private val mTitles: ArrayList<String>)
    : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return if (items.size == 0) 0 else items.size
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    fun clearAll(){
        items.removeAll(items)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun saveState(): Parcelable? {
        return null
    }
}