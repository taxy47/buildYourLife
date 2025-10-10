package com.example.buildyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.buildyourself.ui.theme.BuildYourselfTheme
import kotlinx.coroutines.selects.select
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildYourselfTheme {
                var selectedTab by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it}
                    ) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {

                    }
                    TabScreen(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
            }
        }
    }
}

@Composable
fun InnerContent() {
//    var selectedTab by remember { mutableIntStateOf(0) }
//    Column {
//        TabScreen(
//            selectedTab = selectedTab,
//            onTabSelected = { selectedTab = it }
//        )
//
//        Button(onClick = { selectedTab = 1 }) {
//            Text("跳转到 TaskR&D")
//        }
//    }
//    ScrollingContent()
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
fun TabScreen(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("DayWork", "Calendar", "TaskR&D")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> ScrollingContent()
            1 -> CalendarContent()
            2 -> TaskContent()
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
    MultiGestureBox()
}


@Composable
fun BottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // 占满底部横向空间
            .background(Color.LightGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly, // 按钮等距分布
    ) {
        Button(onClick = { onTabSelected(0) }) { Text("DayWork") }
        Button(onClick = { onTabSelected(1) }) { Text("Calendar") }
        Button(onClick = { onTabSelected(2) }) { Text("TaskR&D") }
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
fun MultiGestureBox() {
    var color by remember { mutableStateOf(Color.Gray) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var message by remember { mutableStateOf("请试试点击、双击、长按或拖动") }

    val animatedScale by animateFloatAsState(targetValue = scale)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(150.dp)
                .scale(animatedScale)
                .background(color)
                // Tap gestures: 单击、双击、长按
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            message = "👆 单击！"
                        },
                        onDoubleTap = {
                            color = Color(
                                Random.nextFloat(),
                                Random.nextFloat(),
                                Random.nextFloat()
                            )
                            message = "💡 双击改变颜色！"
                        },
                        onLongPress = {
                            scale = 1.3f
                            message = "⏱ 长按放大"
                        },
                        onPress = {
                            tryAwaitRelease()
                            scale = 1f // 松开后恢复
                        }
                    )
                }
                // Drag gesture: 拖拽移动
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        message = "➡️ 拖动中..."
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Box", color = Color.White)
        }

        Text(
            text = message,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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