package com.example.buildyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildyourself.ui.theme.BuildYourselfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildYourselfTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar() }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TabScreen()
                    }
                    val x = innerPadding
//                    InnerContent()
                }
            }
        }
    }
}

@Composable
fun InnerContent() {
    ScrollingContent()
}

@Composable
fun ScrollingContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(50) { index ->
            Text(
                text = "Item $index",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
fun TabScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("DayWork", "Calendar", "TaskR&D")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 根据 selectedTab 显示不同内容
        when (selectedTab) {
            0 -> ScrollingContent() // DayWork
            1 -> CalendarContent()  // Calendar
            2 -> TaskContent()      // TaskR&D
        }
    }
}

@Composable
fun TaskContent() {
//    TODO("Not yet implemented")
//    Text("TaskContent")
    Text("TaskContent")
}

@Composable
fun CalendarContent() {
//    TODO("Not yet implemented")
    Text("CalendarContent")
}


@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth() // 占满底部横向空间
            .background(Color.LightGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly // 按钮等距分布
    ) {
        Button(onClick = {}) { Text("DayWork") }
        Button(onClick = {}) { Text("Calendar") }
        Button(onClick = {}) { Text("TaskR&D") }
    }
}


@Composable
fun MyApp() {

    Column ( modifier = Modifier
        .fillMaxSize()
    ) {
        Box {
            Text(
                text = "Taxy is Here",
                modifier = Modifier
                    .offset(x = 200.dp, y = 200.dp)
                    .background(Color.Green)
            )
            BadCounter()
            Counter()
        }
        // 按钮在底部区域
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.LightGray)
        ) {
            Button(onClick = {}) { Text("DayWork") }
            Button(onClick = {}) { Text("Calendar") }
            Button(onClick = {}) { Text("TaskR&D") }
        }
    }
}



@Composable
fun BadCounter() {
    var count = 0
    Button(onClick = { count++ }) {
        Text("Count: $count") // 永远显示 0
    }
}

@Composable
fun Counter() {
    var count by remember { mutableIntStateOf(0) }
    Button(onClick = { count++ },
            modifier = Modifier.size(100.dp, 50.dp)
                .offset(100.dp, 100.dp)
        ) {
        Text("Count: $count")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuildYourselfTheme {
        Greeting("Android")
    }
}