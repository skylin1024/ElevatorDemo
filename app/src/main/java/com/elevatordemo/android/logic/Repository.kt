package com.elevatordemo.android.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.elevatordemo.android.logic.model.ElevatorResponse
import com.elevatordemo.android.logic.network.CallbackListener
import com.elevatordemo.android.logic.network.ElevatorTestSubscribe
import com.google.gson.Gson
import kotlin.coroutines.CoroutineContext

object Repository {


    val elevatorList = ArrayList<ElevatorResponse>()

    //用于对界面上的速度、加速度、位移进行缓存
    val speedList = ArrayList<Double>()
    val accelerationList = ArrayList<Double>()
    val displacementList = ArrayList<Double>()
    private val gson = Gson()
    /**
     * liveData()函数自动构建并返回一个LiveData对象，然后在它的代码块中提供一个挂起函数的上下文
     * liveData()函数的线程参数类型指定成了Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了。
     */
    /**
     * 检测电梯数据
     */
    fun start(): LiveData<String> {
        val flag = MutableLiveData<String>()
        ElevatorTestSubscribe.receiveMqttMessages(object :CallbackListener{
            override fun onFinish(response: List<String>) {
                for (message in response){
                    elevatorList.add(gson.fromJson(message, ElevatorResponse::class.java))
                }
                for (elevator in elevatorList){
                    speedList.add(elevator.params.speed)
                    accelerationList.add(elevator.params.acceleration)
                    displacementList.add(elevator.params.displacement)
                }
                flag.postValue("true")
                Log.d("elevator", speedList.toString())
            }
            override fun onError(e: String) {
                Log.e("elevator",e)
                flag.postValue("false")
            }
        })
        return flag
    }




    /**
     * 在fire()函数的内部会先调用一下liveData()函数，
     * 然后在liveData()函数的代码块中统一进行了try catch处理，并在try语句中调用传入的Lambda表达式中的代码，
     * 最终获取Lambda表达式的执行结果并调用emit()方法发射出去。
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            //通知数据变化
            emit(result)
        }
}