package com.elevatordemo.android.ui.elevator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.elevatordemo.android.logic.Repository
import com.elevatordemo.android.logic.model.ElevatorResponse


class ElevatorViewModel:ViewModel() {
    //电梯检测数据是否检测成功
    private val startLiveData = MutableLiveData<Any?>()


    var startResult = Transformations.switchMap(startLiveData){
        Repository.start()
    }

    /**
     * startLiveData发生变化调用Repository.start()
     */
    fun start(){
        startLiveData.value = startLiveData.value
    }

    /**
     * 重置startResult的值
     */
    fun clear(){
        startResult = MutableLiveData("")
    }




}