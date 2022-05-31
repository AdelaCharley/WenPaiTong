package com.qunshang.wenpaitong.equnshang.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions3.RxPermissions

class PermissionUtil {

    companion object{

        val REQUEST_CODE_PERMISSION = 6958

        fun checkLocationPermission (activity: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            return true
        }

        fun requireLocationPermission(activity : Activity){
            /*if ((ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)*//* or
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)*//*){
                val intent = Intent()
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//设置去向意图
                val uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent)
                DialogUtil.toast(activity,"定位权限被拒绝，请您打开定位权限")
                return
            }*/
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE_PERMISSION)
        }

        fun isLocServiceEnable(context : Context) : Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (gps || network) {
                return true;
            }
            return false;
        }

        fun requireStoragePermission(activity : FragmentActivity){
            /*if ((ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            or (ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)){
                val intent = Intent()
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//设置去向意图
                val uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent)
                DialogUtil.toast(activity,"存储权限被拒绝，请您打开存储权限")
                return
            }*/
            val rxPermissions = RxPermissions(activity)
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                        } else {
                            DialogUtil.toast(activity,"存储权限被拒绝，请您打开存储权限")
                            PermissionUtil.openPermissonInSetting(activity)
                        }
                    }
            /*ActivityCompat.requestPermissions(activity, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE_PERMISSION)*/
        }

        fun openPermissonInSetting(activity: Activity){
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//设置去向意图
            val uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent)
            DialogUtil.toast(activity,"存储权限被拒绝，请您打开存储权限")
            return
        }

        fun checkStoragePermission(activity: Activity) : Boolean {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            return true
        }

        fun requireCameraPermission(activity : Activity){
            ActivityCompat.requestPermissions(activity, arrayOf(
                Manifest.permission.CAMERA),REQUEST_CODE_PERMISSION)
        }

        fun checkSysytemPermission(activity: Activity) : Boolean {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            return true
        }

        fun requireSystemPermission(activity : Activity){
            ActivityCompat.requestPermissions(activity, arrayOf(
                Manifest.permission.READ_PHONE_STATE),REQUEST_CODE_PERMISSION)
        }

        fun checkCameraPermission (activity: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            return true
        }

    }

}