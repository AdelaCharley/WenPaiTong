package com.qunshang.wenpaitong.equnshang.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.ShareDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_physical_store_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_title
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.StoreVideoAdapter
import com.qunshang.wenpaitong.equnshang.adapter.VendorDetailImageAdapter
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean
import com.qunshang.wenpaitong.equnshang.data.VendorVideoBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*

class PhysicalStoreDetailActivity : BaseActivity(), AMapLocationListener {

    var vendorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_store_detail)
        vendorId = intent.getIntExtra("vendorId",0)
        toolbar_back.setOnClickListener { finish() }
        toolbar_right_image.visibility = View.VISIBLE
        toolbar_right_image.setImageDrawable(getDrawable(R.mipmap.nav_icon_send))
        toolbar_title.setText("机构详情")
        toolbar_right_image.setOnClickListener { showShareDialog() }
        initView()
    }

    lateinit var bean : VendorDetailBean

    fun initView(){
        ApiManager.getInstance().getApiStore().loadVendorDetailData(vendorId).enqueue(object : Callback<VendorDetailBean>{
            override fun onResponse(
                call: Call<VendorDetailBean>,
                response: Response<VendorDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    DialogUtil.toast(this@PhysicalStoreDetailActivity,response.body()!!.msg)
                    return
                }
                root.visibility = View.VISIBLE
                val bean = response.body()!!
                this@PhysicalStoreDetailActivity.bean = bean
                location()
                Glide.with(this@PhysicalStoreDetailActivity)
                    .load(bean.data.mainImage)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(headimage)
                vendortitle.setText(bean.data.vendorName)
                vendordesc.setText(bean.data.vendorDesc)
                opentime.setText("营业时间：" + bean.data.businessTime)
                contacter.setText("联系电话：" + bean.data.vendorPhone)
                detaillocation.setText("详细地址：" + bean.data.vendorLocation)
                layout_album.setOnClickListener {
                    val intent = Intent(this@PhysicalStoreDetailActivity,VendorImageActivity::class.java)
                    intent.putExtra("vendorId",vendorId)
                    startActivity(intent)
                }
                if (response.body()!!.data?.vendorImageList?.size == 0){
                    list_album.visibility = View.GONE
                    album_empty.visibility = View.VISIBLE
                } else {
                    list_album.visibility = View.VISIBLE
                    album_empty.visibility = View.GONE
                    list_album.adapter = VendorDetailImageAdapter(this@PhysicalStoreDetailActivity,response.body()!!.data.vendorImageList)
                }
                phone.setOnClickListener {
                    BottomMenu.show(
                        this@PhysicalStoreDetailActivity, arrayOf("拨打" + bean.data.vendorPhone)
                    ) { text: String?, index: Int ->
                        if (index == 0) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.data.vendorPhone))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<VendorDetailBean>, t: Throwable) {

            }

        })
        ApiManager.getInstance().getApiStore().loadVendorVideos(vendorId).enqueue(object : Callback<VendorVideoBean>{
            override fun onResponse(
                call: Call<VendorVideoBean>,
                response: Response<VendorVideoBean>
            ) {

                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    DialogUtil.toast(this@PhysicalStoreDetailActivity,response.body()!!.msg)
                    return
                }
                if (response.body()!!.data?.size == 0){
                    list_videos.visibility = View.GONE
                    videos_empty.visibility = View.VISIBLE
                } else {
                    list_videos.visibility = View.VISIBLE
                    videos_empty.visibility = View.GONE
                }
                list_videos.adapter = StoreVideoAdapter(this@PhysicalStoreDetailActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<VendorVideoBean>, t: Throwable) {

            }

        })
    }

    fun showShareDialog(){
        if (!this::bean.isInitialized){
            return
        }
        val itemList = ArrayList<ShareDialog.Item>()
        itemList.add(ShareDialog.Item(this ,R.mipmap.wechat,"微信好友"))
        itemList.add(ShareDialog.Item(this ,R.mipmap.friendcircle,"朋友圈"))

        ShareDialog.show(this,itemList, object : ShareDialog.OnItemClickListener{
            override fun onClick(
                shareDialog: ShareDialog?,
                index: Int,
                item: ShareDialog.Item?
            ): Boolean {
                when(index){
                    0 -> {
                        doShare(0)
                    }
                    1 -> {
                        doShare(1)
                    }
                }
                return false
            }
        })
    }

    fun doShare(index : Int){
        Observable.create(ObservableOnSubscribe <Bitmap?> { e ->
            Glide.with(this)
                .asBitmap()
                .load(bean.data.mainImage)
                .centerCrop()
                .override(200, 200)
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        e.onNext(resource)
                    }
                })
        }).subscribeOn(AndroidSchedulers.mainThread())
            .map(object : Function<Bitmap?, ByteArray> {
                override fun apply(bitmap: Bitmap?): ByteArray {
                    return BitmapUtils.bmpToByteArray(bitmap, 32)
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<ByteArray?> {
                @Throws(java.lang.Exception::class)
                override fun accept(bytes: ByteArray?) {

                    /*val imgObj = WXImageObject(bitmapoflayout)
                    val msg = WXMediaMessage()
                    msg.mediaObject = imgObj
                    //val data = BitmapUtils.bmpToByteArray(bitmapoflayout,false)
                    msg.thumbData = bytes
                    val req = SendMessageToWX.Req()
                    req.transaction = "req"
                    req.message = msg
                    if (type == TYPE_WECHAT_SESSION){
                        req.scene = SendMessageToWX.Req.WXSceneSession
                    } else if (type == TYPE_WECHAT_FRIENDCIRCLE){
                        req.scene = SendMessageToWX.Req.WXSceneTimeline
                    }

                    MainActivity.api.sendReq(req)*/


                    val webpage = WXWebpageObject()
                    webpage.webpageUrl = Constants.baseURL + "/equnshang/O2O/index?vendorId=" + vendorId

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                    val msg = WXMediaMessage(webpage)
                    msg.title = bean.data.vendorName
                    msg.description = "刚刚在群票发现一个不错的机构，快来看看~"
                    msg.thumbData = bytes

//构造一个Req

//构造一个Req
                    val req = SendMessageToWX.Req()
                    req.transaction = "req"
                    req.message = msg
                    if (index == 0){
                        req.scene = SendMessageToWX.Req.WXSceneSession
                    } else if (index == 1){
                        req.scene = SendMessageToWX.Req.WXSceneTimeline
                    }

//调用api接口，发送数据到微信

//调用api接口，发送数据到微信
                    MainActivity.api.sendReq(req)
                    CommonUtil.doCompleteTask(3)
                    //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this@ReceivePrizeToFlauntActivity,"分享完毕")
                }
            }, object : Consumer<Throwable?> {
                @Throws(java.lang.Exception::class)
                override fun accept(throwable: Throwable?) {
                    DialogUtil.toast(this@PhysicalStoreDetailActivity,"分享失败")
                }
            })
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GPS) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                DialogUtil.toast(this,"用户打开位置服务")
            } else {
                DialogUtil.toast(this, "用户关闭定位服务")
            }
        }
    }

    lateinit var mLocationClient: AMapLocationClient

    lateinit var mLocationOption : AMapLocationClientOption

    private val REQUEST_CODE_GPS = 1

    fun location(){
        if (!PermissionUtil.isLocServiceEnable(this)){
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_GPS);
            } else {
                DialogUtil.toast(this,"该设备无法使用定位")
            }
        }

        if (!PermissionUtil.checkLocationPermission(this)){
            PermissionUtil.requireLocationPermission(this)
            return
        }
        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //设置是否只定位一次,默认为false
        mLocationOption.isOnceLocation = true
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.isWifiActiveScan = true
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.isMockEnable = false
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.interval = 2000
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    fun load(lat : Double,lon : Double,name : String){
        /*ApiManager.getInstance().getApiStore().loadStoreList(latitude = lat.toString(),longitude = lon.toString()).enqueue(object : Callback<PhysicalStoreBean>{
            override fun onResponse(
                call: Call<PhysicalStoreBean>,
                response: Response<PhysicalStoreBean>
            ) {
                if (response.body() == null){
                    return
                }
                val manage = LinearLayoutManager(this@PhysicalStoreDetailActivity)
                manage.orientation = LinearLayoutManager.VERTICAL
                list.layoutManager = manage
                list.adapter = PhysicalStoreAdapter(this@PhysicalStoreDetailActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<PhysicalStoreBean>, t: Throwable) {

            }

        })*/
        map.setOnClickListener {
            BottomMenu.show(
                this@PhysicalStoreDetailActivity, arrayOf("高德地图","百度地图")
            ) { text: String?, index: Int ->
                when(index){
                    0 -> {
                        if (MapNaviUtils.isGdMapInstalled()) {
                            MapNaviUtils.openGaoDeNavi(this@PhysicalStoreDetailActivity,lat,lon,name,bean.data.latitude,this@PhysicalStoreDetailActivity.bean.data.longitude,bean.data.vendorLocation)
//                            MapUtils.openMap(mContext,"com.autonavi.minimap",new LatLng(31.33260711060764,121.54777721524306,"CCB"));
                        }else {
                            DialogUtil.toast(this@PhysicalStoreDetailActivity,"您还未安装高德地图！请先下载高德地图")
                        }
                    }
                    1 -> {
                        if (MapNaviUtils.isBaiduMapInstalled()){
                            MapNaviUtils.openBaiDuNavi(this@PhysicalStoreDetailActivity,lat,lon,name,bean.data.latitude,this@PhysicalStoreDetailActivity.bean.data.longitude,bean.data.vendorLocation);
//                            MapUtils.openMap(mContext,"com.baidu.BaiduMap",new LatLng(31.33260715160764,121.54777723124306,"CCB"));
                        }else {
                            DialogUtil.toast(this@PhysicalStoreDetailActivity,"您还未安装百度地图！请先下载百度地图")
                        }
                    }
                }
            }
           /* val itemList = ArrayList<ShareDialog.Item>()
            itemList.add(ShareDialog.Item(this, R.drawable.icon_amap, "高德地图"))
            itemList.add(ShareDialog.Item(this, R.drawable.icon_baidumap, "百度地图"))

            ShareDialog.show(this, itemList, object : ShareDialog.OnItemClickListener {
                override fun onClick(
                    shareDialog: ShareDialog?,
                    index: Int,
                    item: ShareDialog.Item?
                ): Boolean {

                }
            })*/
        }
    }

    override fun onLocationChanged(aMapLocation : AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                val lat = aMapLocation.getLatitude();//获取纬度
                val lon = aMapLocation.getLongitude();//获取经度
                var name = aMapLocation.poiName
                if (!isDataLoaded){
                    load(lat,lon,name)
                    mLocationClient.stopLocation()
                    isDataLoaded = true
                }
            }
        }
    }

    var isDataLoaded = false

}