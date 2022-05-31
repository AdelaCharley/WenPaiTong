package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.amallv3_search_top.*
import kotlinx.android.synthetic.main.fragment_a_mall_v3_category.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallSearchResultV3Activity
import com.qunshang.wenpaitong.equnshang.adapter.AMallV3CategoryFirstAdapter
import com.qunshang.wenpaitong.equnshang.data.AMallV3CategoryFirstBean

import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.lang.Exception

class AMallV3CategoryFragment : MyBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
        if (!this::adapter.isInitialized){
            return
        }
        adapter.unbind()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(data : CategoryData){
        if (data.code == 300){
            hideAllFragments()
            showFragment(data.index)
        }
    }

    fun showFragment(index : Int){
        if (fragments[index].isAdded){
            childFragmentManager.beginTransaction().show(fragments[index]).commit()
        } else {
            childFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
        }
    }

    fun hideAllFragments(){
        try {
            val transaction = childFragmentManager.beginTransaction()
            for (i in fragments.indices){
                if (fragments[i].isAdded && !fragments[i].isHidden){
                    transaction.hide(fragments[i]).commit()
                }
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    lateinit var fragments : ArrayList<AMallV3CategorySecondFragment>

    lateinit var adapter: AMallV3CategoryFirstAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a_mall_v3_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()== KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    goSearch()
                } else if (keyCode == KeyEvent.KEYCODE_DEL){
                    if (input.text.length != 0){
                        input.text.delete(input.text.length - 1, input.text.length)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK){
                    requireActivity().finish()
                }
            }

            return@setOnKeyListener true
        }
        back.setOnClickListener { requireActivity().finish() }
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    label_delete_text.visibility = View.INVISIBLE
                } else {
                    label_delete_text.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        label_delete_text.setOnClickListener {
            input.setText("")
        }

        back.setOnClickListener { requireActivity().finish() }

        search.setOnClickListener { goSearch() }

        ApiManager.getInstance().getApiAMallV3().loadFirstCategorys().enqueue(object : Callback<AMallV3CategoryFirstBean>{
            override fun onResponse(
                call: Call<AMallV3CategoryFirstBean>,
                response: Response<AMallV3CategoryFirstBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    fragments = ArrayList<AMallV3CategorySecondFragment>()
                    for (i in response.body()!!.data.indices){
                        fragments.add(AMallV3CategorySecondFragment(response.body()!!.data.get(i).categoryId))
                    }
                    adapter = AMallV3CategoryFirstAdapter(requireContext(),response.body()!!.data)
                    pagetitles.adapter = adapter
                }
            }

            override fun onFailure(call: Call<AMallV3CategoryFirstBean>, t: Throwable) {

            }

        })
    }

    fun goSearch(){
        if (!StringUtils.isEmpty(input.text.toString())){
            val intent = Intent(requireContext(), AMallSearchResultV3Activity::class.java)
            intent.putExtra("keyword",input.text.toString())
            startActivity(intent)
        }
    }

    class CategoryData {

        var code = 0

        var index = 0

    }

}