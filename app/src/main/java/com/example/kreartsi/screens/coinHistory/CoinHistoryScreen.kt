package com.example.kreartsi.screens.coinHistory

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.Black5
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.boxShadow

@Preview(showBackground = true)
@Composable
fun CoinHistoryScreen(
    navController: NavController = rememberNavController(),
    viewModel: CoinHistoryViewModel = hiltViewModel()
) {
    val coins = viewModel.coinOwned.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    viewModel.error.observe(owner) {
        if(it.isNotEmpty())
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    if(isLoading) Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    } else Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        IconVector(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .clickable {
                    navController.navigateUp()
                },
            vector = Icons.Default.ArrowBack,
            tint = Black
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            color = Black
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "My Coins",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700,
                    color = White)
                Spacer(modifier = Modifier.weight(1f))
                ImageDrawable(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(20.dp),
                    res = R.drawable.coins,
                    contentScale = ContentScale.FillHeight)
                Text(
                    text = "$coins",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700,
                    color = White)
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
        CoinHistoryList()
    }
}

@Composable
fun CoinHistoryList(
    viewModel: CoinHistoryViewModel = hiltViewModel()
) {
    val histories = viewModel.histories.collectAsState().value.reversed()

    LazyColumn {
        items(histories.size) {
            val history = histories[it]
            val placeholder = "${history.donatedAmount} coins"
            val status = viewModel.checkIsGifted(history.donorUserID)
            val fromTo = if(status == "earned") "from" else "to"
            val recipientDonor = viewModel.getDonorReceiver(status, history)

            val globalText =
                "You have $status $placeholder $fromTo $recipientDonor"

            val start = globalText.indexOf(placeholder)
            val spanStyles = listOf(
                AnnotatedString.Range(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    start = start,
                    end = start + placeholder.length
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .boxShadow(
                        color = Black5,
                        offset = DpOffset(1.dp, 4.dp),
                        blurRadius = 10.dp,
                        spreadRadius = 5.dp
                    ),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 22.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AnnotatedString(
                            globalText, spanStyles),
                        fontSize = 12.sp,
                        color =  Black
                    )
                    Text(
                        text = viewModel.getTime(history.donationDate),
                        fontSize = 12.sp,
                        color =  Black
                    )
                }
            }
        }
    }
}