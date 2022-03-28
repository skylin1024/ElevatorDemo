package com.elevatordemo.android.logic.network

import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.elevatordemo.android.ElevatorDemoApplication



object PositionService {
    fun position(listener: CallbackListener) {
        //声明全局的content
        val application = ElevatorDemoApplication.context
        //声明AMapLocationClient类对象
        var mLocationClient: AMapLocationClient?
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener { mapLocation ->
            if (mapLocation != null) {
                if (mapLocation.errorCode == 0) {
                    //可在其中解析mapLocation获取相应内容。
                    Log.d("mapSuccess", "${mapLocation.address}")
                    //回调消息
                    //listener.onFinish(mapLocation.address)
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e(
                        "mapError",
                        "location Error, ErrCode: ${mapLocation.errorCode},errInfo:${mapLocation.errorInfo}"
                    )
                }
            }
        }
        //在构造AMapLocationClient 之前必须进行合规检查，设置接口之前保证隐私政策合规
        AMapLocationClient.updatePrivacyShow(application, true, true);
        AMapLocationClient.updatePrivacyAgree(application, true);
        //初始化定位
        mLocationClient = AMapLocationClient(application)
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

        val mLocationOption = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
        }

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.isOnceLocationLatest = true

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        Log.d("Test", "3")
    }


}