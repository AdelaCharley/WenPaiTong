package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.webkit.WebSettings
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.Constants

class ArticleDetailActivity : BaseActivity() {

    var articleid = -9999

    var title : String?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("详情")
        articleid = intent.getIntExtra("id",-9999)
        title = intent.getStringExtra("name")
        if (articleid == -9999){
            return
        }
        if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(title)){
            return
        }

        toolbar_title.setText(title)
        webview.settings.javaScriptEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.loadUrl(Constants.baseURL + "/equnshang/groupDetail?id=" + articleid)
        /*ApiManager.getInstance().getApiGroupMain().getArticleDetailBean(articleid).enqueue(object : Callback<ArticleDetailBean>{
            override fun onResponse(
                call: Call<ArticleDetailBean>,
                response: Response<ArticleDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                ToastUtil.toast(this@ArticleDetailActivity,"当前id" + articleid)
                ToastUtil.toast(this@ArticleDetailActivity,response.body()!!.data.gocInfo.content)
                //webview.loadData(response.body()!!.data.gocInfo.content,"text/html","utf-8")
            }

            override fun onFailure(call: Call<ArticleDetailBean>, t : Throwable) {

            }

        })*/
        /*ApiManager.getInstance().getApiGroupMain().getArticleDetailBeanForString(articleid).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.body() == null){
                    return
                }
                //ToastUtil.toast(this@ArticleDetailActivity,"当前id" + articleid)
                //ToastUtil.toast(this@ArticleDetailActivity,response.body()!!.string())
                //webview.loadData(response.body()!!.data.gocInfo.content,"text/html","utf-8")
                val content = //JSONObject
                     response.body()!!.string()//).getJSONObject("data").getJSONObject("gocInfo").getString("content")
                val start = content.indexOf("content\":\"")
                val end = content.indexOf("\"}}")
                val contentresult = content.substring(start,end).substring(10).replace("\\","")
                Log.i("zhangjunaaa",contentresult)
                webview.loadDataWithBaseURL(null, contentresult, "text/html", "utf-8", null)
                //webview.loadData(Html.fromHtml(contentresult).toString(),"text/html","utf-8")
                //ToastUtil.toast(this@ArticleDetailActivity, contentresult)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })*/
    }
}