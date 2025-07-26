package com.example.currencyconverter.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl

class MainActivity : FragmentActivity() {
    val remoteRatesServise = RemoteRatesServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            ShowCurrency()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCurrency(){

}

@Preview(showBackground = true)
@Composable
fun ListItem(){

}