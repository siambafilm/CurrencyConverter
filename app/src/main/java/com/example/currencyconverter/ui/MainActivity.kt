package com.example.currencyconverter.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.domain.entity.Currency
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.timer

val ratesService = RemoteRatesServiceImpl()

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowCurrency()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCurrency(){
    var currentMainCountry = remember { mutableStateOf("RUB") }
    val currencyState = remember { mutableStateOf(mapOf<String, Double>()) }
    suspend fun updateCurrencyState(ratesService: RemoteRatesServiceImpl) {
        while (true) {
            val rates = ratesService.getRates(currentMainCountry.value, 1.0)
            val updatedMap = mutableMapOf<String, Double>()
            rates.forEach { rate ->
                updatedMap[rate.currency] = rate.value
            }
            currencyState.value = updatedMap
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        updateCurrencyState(ratesService)
    }

    val selectedCardIndex = remember { mutableIntStateOf(0) }

    var countriesCards = remember { mutableStateOf(listOf<CardListModel>()) }

    /*var tempList = mutableListOf<CardListModel>()
    currencyState.value.forEach {
        when(it.key){

        }
    }*/
    fun moveToUp(){
        val selectedCard = countriesCards.value[selectedCardIndex.intValue]
        countriesCards.value = countriesCards.value.toMutableList().apply {
            removeAt(selectedCardIndex.intValue)
            add(0, selectedCard)
        }
    }

    countriesCards.value = listOf(
        CardListModel("RUB", 0.0, currencyState.value["RUB"] ?: 0.0, "Russian Rouble", "₽", R.drawable.ru),
        CardListModel("USD", 0.0, currencyState.value["USD"] ?: 0.0, "US dollar", "$", R.drawable.us),
        CardListModel("EUR", 0.0, currencyState.value["EUR"] ?: 0.0, "Euro", "€", R.drawable.eu),
        CardListModel("GBP", 0.0, currencyState.value["GBP"] ?: 0.0, "Great Britain Pound", "£", R.drawable.br)
    )
    moveToUp()

    // Определите колбэк для обработки нажатия на карточку
    val onCardClick: (Int) -> Unit = { index ->
        if (index >= 0 && index < countriesCards.value.size) {
            selectedCardIndex.intValue = index
        }

        // Вставьте выбранную карточку в начало списка
        if (selectedCardIndex.intValue in countriesCards.value.indices) {
            moveToUp()
            currentMainCountry.value = countriesCards.value[0].country
        }
    }


    LazyColumn {
        itemsIndexed(countriesCards.value) { index, item ->
            ListItem(item) { onCardClick(index) }
        }
    }
}

@Composable
fun ListItem(model: CardListModel, onCardClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.clickable{onCardClick()}) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = model.image),
                    contentDescription = "countryImage",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(5.dp).size(64.dp))
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(model.country)
                    Text(model.currency, fontSize = 12.sp)
                    Text("${model.balance} ${model.sign}", fontSize = 10.sp)
                }
                Spacer(Modifier.weight(1f))
                Text("${model.currencyInt} ${model.sign}",
                    modifier = Modifier.padding(5.dp))
            }
        }
    }
}