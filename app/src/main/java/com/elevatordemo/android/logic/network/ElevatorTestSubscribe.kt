package com.elevatordemo.android.logic.network



import android.util.Log
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.util.*
import kotlin.collections.ArrayList
import com.hivemq.client.mqtt.datatypes.MqttQos

import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.nio.charset.Charset
import kotlin.concurrent.thread

/**
 * 接收电梯数据的MQTT客户端
 */
object ElevatorTestSubscribe {
    val messages: ArrayList<String> = ArrayList()

    /**
     * 接收订阅主题的消息
     */
    fun receiveMqttMessages(listener: CallbackListener){
        Log.d("elevator","start")
        thread {
            //创建Mqtt客户端
            val client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("175.178.64.23")
                .serverPort(1883)
                .buildBlocking()
            try {
                //连接MQTT broker
                client.connectWith().simpleAuth().username("jack").password("bp9M0O37qR".toByteArray())
                    .applySimpleAuth().keepAlive(60).send()
                client.publishes(MqttGlobalPublishFilter.ALL).use {
                    //订阅并接收消息
                    client.toAsync().subscribeWith().topicFilter("elevator/test/start")
                        .qos(MqttQos.AT_LEAST_ONCE).callback { mqtt5Publish: Mqtt5Publish ->
                            println(mqtt5Publish)
                            messages.add(String(mqtt5Publish.payloadAsBytes, Charset.defaultCharset()))
                        }.send()
                    client.toAsync().subscribeWith().topicFilter("elevator/test/end").qos(MqttQos.AT_LEAST_ONCE).callback {
                        //收到消息，返回并关闭连接
                        // 回调onFinish()方法
                        listener.onFinish(messages)

                        client.disconnect()
                    }.send()
                }
                //休眠30分钟
                java.util.concurrent.TimeUnit.MINUTES.sleep(30)

            }catch (e:Exception){
                // 回调onError()方法
                listener.onError(e.toString())
            }finally {

            }
        }
    }
}