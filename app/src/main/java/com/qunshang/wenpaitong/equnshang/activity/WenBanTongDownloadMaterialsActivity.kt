package com.qunshang.wenpaitong.equnshang.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.databinding.ActivityWenBanTongDownloadMaterialsBinding
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.adapter.WenBanTongDownloadMaterialsAdapter
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.utils.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * "下载素材"页
 * 入口:[com.qunshang.wenpaitong.equnshang.view.WenBanTongShareDialog]
 */
class WenBanTongDownloadMaterialsActivity: BaseActivity() {
    private lateinit var binding: ActivityWenBanTongDownloadMaterialsBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBtnDownload: Button

    private lateinit var adapter: WenBanTongDownloadMaterialsAdapter
    private lateinit var data: WenBanTongProductBean
    private var selectedMaterials = mutableListOf<Any>() //选择的下载图片

    fun createFile(){
        val activity = this
        if (!PermissionUtil.checkStoragePermission(activity)){
            PermissionUtil.requireStoragePermission(activity)
            return
        }
        val PHOTO_DIR = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + UUID.randomUUID() + ".jpeg"

        val avaterFile = File(PHOTO_DIR)//设置文件名称
        try {
            val fileparent = File(avaterFile.parent)
            StringUtils.log(fileparent.absolutePath)
            if (fileparent.exists()){
                StringUtils.log("已存在了加济k")
            } else {
                if (avaterFile.mkdirs()){
                    StringUtils.log("创建陈宫")
                } else {
                    StringUtils.log("穿件失败")
                }
            }

        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createFile()
        binding = ActivityWenBanTongDownloadMaterialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WaitDialog.show(this,"正在载入")
        EventBus.getDefault().register(this)
        data = intent.getSerializableExtra("data") as WenBanTongProductBean
        initView()
    }

    private fun initView() {
        initViewPager()
        binding.toolbar.toolbarTitle.text = "下载素材"
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        mRecyclerView = binding.recyclerView
        mRecyclerView.layoutManager = GridLayoutManager(this, 3)
        loadBitmaps()
    }

    fun loadBitmaps(){
        /*val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {


            }
        }, 1500)*/
        object : Thread(){
            override fun run() {
                super.run()
                if (fragments.get(0).rootBitmap != null && fragments.get(1).rootBitmap != null && fragments.get(2).rootBitmap != null && fragments.get(3).rootBitmap != null
                    && fragments.get(4).rootBitmap != null && fragments.get(5).rootBitmap != null && fragments.get(5).isQrCodeInited){
                    runOnUiThread { init() }
                } else {
                    Thread.sleep(500)
                    loadBitmaps()
                }
            }
        }.start()
    }

    fun init(){
        WaitDialog.dismiss()
        val list : MutableList<Any> = mutableListOf()

        list.add(fragments.get(0).rootBitmap)
        list.add(fragments.get(1).rootBitmap)
        list.add(fragments.get(2).rootBitmap)
        list.add(fragments.get(3).rootBitmap)
        list.add(fragments.get(4).rootBitmap)
        list.add(fragments.get(5).rootBitmap)
        if (data.data.swiperList?.size != 0){
            for (i in data.data.swiperList?.indices!!){
                list.add(data.data.swiperList?.get(i)!!)
            }
        }
        list.addAll(data.data.detailInfo)
        adapter = WenBanTongDownloadMaterialsAdapter(this, list)
        mRecyclerView.adapter = adapter
        mBtnDownload = binding.btnDownload
        mBtnDownload.setOnClickListener { toDownload() }
    }

    val fragments = ArrayList<BaseShareFragment>()

    fun initViewPager(){

        fragments.add(WenBanTongShare_OneFragment(data))
        fragments.add(WenBanTongShare_TwoFragment(data))
        fragments.add(WenBanTongShare_ThreeFragment(data))
        //fragments.add(WenBanTongShare_FourFragment())
        fragments.add(WenBanTongShare_FiveFragment(data))
        fragments.add(WenBanTongShare_SixFragment(data))
        fragments.add(QrCodeShareFragment(true))
        val titles = ArrayList<String>()
        titles.add("")
        titles.add("")
        titles.add("")
        //titles.add("")
        titles.add("")
        titles.add("")
        val pagerAdapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        binding.viewPager.pageMargin = CommonUtil.dp2px(this,15)
        binding.viewPager.offscreenPageLimit = 6
        binding.viewPager.adapter = pagerAdapter
    }

    private fun toDownload() {
        if (!PermissionUtil.checkStoragePermission(this)) {
            PermissionUtil.requireStoragePermission(this)
            return
        }
        val context = this

        var count = 0
        var max = 0
        WaitDialog.show(context, "正在下载0/" + selectedMaterials.size)
            .setMessage("正在下载0/" + selectedMaterials.size)
            .refreshView()
        for (url in selectedMaterials) {
            if (url is String){
                val filePath =
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + UUID.randomUUID() + ".jpeg"
                StringUtils.log(count.toString() + "-" + filePath)
                FileDownloader.getImpl().create(url)
                    .setPath(filePath)
                    .setListener(object: FileDownloadListener() {
                        override fun pending(task: BaseDownloadTask?,
                                             soFarBytes: Int,
                                             totalBytes: Int) {
                        }

                        override fun progress(task: BaseDownloadTask?,
                                              soFarBytes: Int,
                                              totalBytes: Int) {
                            /*var currrent: Long = soFarBytes.toLong() * 100
                            var total = totalBytes.toLong()
                            WaitDialog.show(context, "正在下载")
                                .setMessage("正在下载" + (currrent / total) + "%")
                                .refreshView()*/
                        }

                        override fun completed(task: BaseDownloadTask?) {
                            Observable.just(1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .doOnNext {
                                    FileUtils.saveImageToLocal(context, File(filePath)) //通知相册
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Consumer<Int>{
                                    override fun accept(t: Int) {
                                        count++
                                        WaitDialog.show(context, "正在下载" + count + "/" + selectedMaterials.size)
                                            .setMessage("正在下载" + count + "/" + selectedMaterials.size)
                                            .refreshView()
                                        if (count == max){
                                            WaitDialog.dismiss()
                                            TipDialog.show(context, "保存成功!", TipDialog.TYPE.SUCCESS)
                                            max = 0
                                            count = 0
                                            selectedMaterials = mutableListOf()
                                            adapter.clearSelected()
                                        }
                                    }

                                })
                        }

                        override fun paused(task: BaseDownloadTask?,
                                            soFarBytes: Int,
                                            totalBytes: Int) {
                        }

                        override fun error(task: BaseDownloadTask?, e: Throwable?) {StringUtils.log("啊啊啊出错了啊" + e?.message)}

                        override fun warn(task: BaseDownloadTask?) {}

                        override fun connected(
                            task: BaseDownloadTask?,
                            etag: String?,
                            isContinue: Boolean,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                            super.connected(task, etag, isContinue, soFarBytes, totalBytes)
                            if (max == 0){
                                max = selectedMaterials.size
                            }
                            //WaitDialog.show(context, "正在下载")
                        }

                    }).start()
            } else if (url is Bitmap){
                if (max == 0){
                    max = selectedMaterials.size
                }
                Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext {
                        val filePath =
                            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + UUID.randomUUID() + ".jpeg"
                        StringUtils.log(count.toString() + "-" + filePath)
                        val avaterFile = File(filePath)//设置文件名称

                        if(avaterFile.exists()){
                            avaterFile.delete()
                        }
                        try {
                            avaterFile.createNewFile()
                            StringUtils.log(filePath)
                            val fos = FileOutputStream(avaterFile)
                            url.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            fos.flush()
                            fos.close()
                            FileUtils.saveImageToLocal(context,avaterFile)//通知相册
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<Int>{
                        override fun accept(t: Int) {
                            count++
                            WaitDialog.show(context, "正在下载" + count + "/" + selectedMaterials.size)
                                .setMessage("正在下载" + count + "/" + selectedMaterials.size)
                                .refreshView()
                            if (count == max){
                                WaitDialog.dismiss()
                                TipDialog.show(context, "保存成功!", TipDialog.TYPE.SUCCESS)
                                max = 0
                                count = 0
                                selectedMaterials = mutableListOf()
                                adapter.clearSelected()
                            }
                        }

                    })

            }
        }
    }


    /**
     * 传入当前选择的下载图片列表
     * 消息发布者:[com.qunshang.wenpaitong.equnshang.adapter.WenBanTongDownloadMaterialsAdapter]
     * @param materials String
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun select(materials: MutableList<Any>) {
        selectedMaterials = materials
        mBtnDownload.isEnabled = materials.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}