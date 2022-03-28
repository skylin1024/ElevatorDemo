package com.elevatordemo.android


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.elevatordemo.android.ui.elevator.ElevatorViewModel
import com.elevatordemo.android.ui.theme.ElevatorDemoTheme
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton


class MainActivity : ComponentActivity() {
    val viewModel by lazy { ViewModelProvider(this)[ElevatorViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElevatorDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var submitButtonState by remember { mutableStateOf(SSButtonState.IDLE) }
                    viewModel.startResult.observe(this){flag ->
                        //判断是否结束接收电梯数据
                        if (submitButtonState == SSButtonState.LOADING){
                            if (flag == "true"){
                                submitButtonState =  SSButtonState.SUCCESS
                            }else if (flag == "false"){
                                submitButtonState = SSButtonState.FAILIURE
                            }

                        }
                    }
                    Column(
                        Modifier.fillMaxSize(),
                        //配置为屏幕中央
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SSJetPackComposeProgressButton(
                            type = SSButtonType.CIRCLE,
                            width = 300.dp,
                            height = 50.dp,
                            onClick = {
                                //Perform action on click of button and make it's state to LOADING
                                submitButtonState = SSButtonState.LOADING
                                viewModel.clear()
                                viewModel.start()
                            },
                            assetColor = White,
                            buttonState = submitButtonState,
                            text = "开始检测"
                        )
                    }
                }
            }
        }
    }
}


