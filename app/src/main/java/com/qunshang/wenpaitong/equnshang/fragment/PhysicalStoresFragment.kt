package com.qunshang.wenpaitong.equnshang.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tbruyelle.rxpermissions3.RxPermissions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_physical_stores.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.PhysicalStoreAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.PhysicalStoreBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.lang.Exception

class PhysicalStoresFragment : MyBaseFragment(), AMapLocationListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_physical_stores, container, false)
    }

    lateinit var mLocationClient: AMapLocationClient

    lateinit var mLocationOption : AMapLocationClientOption

    private val REQUEST_CODE_GPS = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner = view.findViewById(R.id.banner)
        location()
        loadData()
    }

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    fun loadData(){
        ApiManager.getInstance().getApiStore().getVendorBanner().enqueue(object : Callback<BaseHttpListBean<String>>{
            override fun onResponse(
                call: Call<BaseHttpListBean<String>>,
                response: Response<BaseHttpListBean<String>>
            ) {
                val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(response.body()!!.data){
                    override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                        Glide.with(holder!!.itemView.context)
                            .load(data)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                            .into(holder.imageView)
                    }
                }
                banner
                    .setAdapter(adapter)
                    .addBannerLifecycleObserver(requireActivity()).indicator =
                    CircleIndicator(requireContext())
            }

            override fun onFailure(call: Call<BaseHttpListBean<String>>, t: Throwable) {

            }

        })
        //supportFragmentManager.beginTransaction().add(R.id.container,PhysicalStoresFragment()).commit()
        //location()
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GPS) {
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //DialogUtil.toast(this,"用户打开位置服务")
            } else {
                //DialogUtil.toast(this, "用户关闭定位服务")
            }
        }
    }

    fun location(){
        if (!PermissionUtil.isLocServiceEnable(requireContext())){
            DialogUtil.toast(requireContext(),"您未开启定位服务，请您开启定位服务")
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            try {
                startActivityForResult(intent, REQUEST_CODE_GPS)
            } catch (e : Exception){
                e.printStackTrace()
            }
            return
        }

        if (!PermissionUtil.checkLocationPermission(requireActivity())){
            //PermissionUtil.requireLocationPermission(requireActivity())
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        dolocation()
                    } else {
                        DialogUtil.toast(requireContext(),"定位权限被拒绝，请您手动打开定位权限")
                    }
                }
            return
        }
        dolocation()
    }

    fun dolocation(){
        mLocationClient = AMapLocationClient(requireActivity().applicationContext)
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
        StringUtils.log("开始了")
    }

    fun load(lat : Double,lon : Double){
        ApiManager.getInstance().getApiStore().loadStoreList(latitude = lat.toString(),longitude = lon.toString()).enqueue(object :
            Callback<PhysicalStoreBean> {
            override fun onResponse(
                call: Call<PhysicalStoreBean>,
                response: Response<PhysicalStoreBean>
            ) {
                if (response.body() == null){
                    return
                }
                val manage = LinearLayoutManager(requireContext())
                manage.orientation = LinearLayoutManager.VERTICAL
                list.layoutManager = manage
                list.adapter = PhysicalStoreAdapter(requireContext(),response.body()!!.data)
            }

            override fun onFailure(call: Call<PhysicalStoreBean>, t: Throwable) {

            }

        })
    }

    override fun onLocationChanged(aMapLocation : AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                val lat = aMapLocation.getLatitude();//获取纬度
                val lon = aMapLocation.getLongitude();//获取经度
                if (!isDataLoaded){
                    load(lat,lon)
                    mLocationClient.stopLocation()
                    isDataLoaded = true
                }
            }
        }
    }

    var isDataLoaded = false

}