package com.elevatordemo.android.logic.model

/**
 * 电梯测试参数
 *
 */
data class ElevatorResponse(val params: Params) {
    /**
     *
     * @param speed 速度
     * @param acceleration 加速度
     * @param displacement 位移
     *
     */
    data class Params(
        val currentTemperature: String,
        val text: String, val floor: String,
        val height: String, val speed: Double, val acceleration:Double,val displacement:Double
    )
}