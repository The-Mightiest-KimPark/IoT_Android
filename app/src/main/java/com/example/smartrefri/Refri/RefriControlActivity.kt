package com.example.smartrefri.Refri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.smartrefri.R
import org.eclipse.paho.client.mqttv3.MqttMessage

const val SERVER_URI = "tcp://172.30.1.2:1883"

//const val SERVER_URI = "tcp://192.168.0.122:1883"
const val PUB_TOPIC = "sensors/door/android/open"
const val SUB_TOPIC = "sensors/#"


class RefriControlActivity : AppCompatActivity() {

    val TAG = "RefriControlActivity"
    lateinit var mqttClient: Mqtt


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refri_control)

        val aa = findViewById<ImageView>(R.id.aa)
        aa.setOnClickListener{

            Log.e("들어왔니?","a")
            mqttClient.publish(PUB_TOPIC, "1",0)
        }


        mqttClient = Mqtt(this, SERVER_URI)


        try {
//            mqttClient.setCallback { topic, message ->}
            fun onReceived(s: String, mqttMessage: MqttMessage) {
            val msg = String(mqttMessage.payload)


            }
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
    fun onReceived(topic: String, message: MqttMessage) {
        // 토픽 수신 처리
        val msg = String(message.payload)
    }

}