package com.example.buildyourself

//import android.content.SharedPreferences
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.stringPreferencesKey
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.buildyourself.ui.theme.BuildYourselfTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MainActivity created")
        Log.i("TAG", "TEST log");


// test the access to database
        val db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
//        db.execSQL("INSERT INTO users (name) VALUES ('Alice')") // <-- 注意，这会每次都插入

        val cursor =
            db.rawQuery("SELECT COUNT(*) FROM users WHERE name = ?", arrayOf<String>("Alice"))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        if (count == 0) {
            db.execSQL("INSERT INTO users (name) VALUES (?)", arrayOf<Any>("Alice"))
        }
        cursor.close()
        Log.i("Data db", "$count");
        val myCursor = db.rawQuery("SELECT * FROM users WHERE name = 'Alice'", null);
        while (myCursor.moveToNext()) {
            val rowNumber = myCursor.columnCount;
            val id = myCursor.getInt(0);
            val name = myCursor.getInt(1);
            Log.i("Database query", "$rowNumber is the $id , $name ");
        }
        myCursor.close();
//        Log.
//        Log.i("Tag", "test log");
        enableEdgeToEdge()
        setContent {
            BuildYourselfTheme {
                var selectedTab by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TabScreen(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy();
        println("Destroy activity");
    }
}

//@Composable
//fun loadUsers(db: SQLiteDatabase) {
//    val cursor = db.rawQuery("SELECT * FROM users", null)
//    val sb = StringBuilder()
//    while (cursor.moveToNext()) {
//        val id = cursor.getInt(0)
//        val name = cursor.getString(1)
//        sb.append(id).append(": ").append(name).append("\n")
//    }
//    cursor.close()
//    val textView = null
//    textView.setText(sb.toString())
//}

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

//@Composable
//fun ScrollingContent() {
//    LazyColumn(modifier = Modifier
//        .fillMaxSize()
//    ) {
//        items(50) { index ->
//            Text(
//                text = "Item $index",
//                modifier = Modifier
//                    .padding(8.dp)
//                    .fillMaxWidth()
//                    .background(Color(0xFF64C8F))
//                    .pointerInput(Unit) {
//                        detectTapGestures (
//                            onTap = {
////                                text = "Clicked";
//                            }
//                        )
//                    }
//            )
//        }
//    }
//}

//数据持久化生命周期，这个数据在退出 app 后会消除；不同 tab 切换都会刷新掉，ScrollingContent 被销毁又重建了
data class ItemData(
    val id: Int,
    var color: MutableState<Color> = mutableStateOf(Color(0xFF64C8FF))
)

@Composable
fun ScrollingContent() {
    val items = remember {
        List(50) { index -> ItemData(id = index) }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.id }) { item ->
            Text(
                text = "Item ${item.id}",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(item.color.value)
                    .clickable {
                        // 修改数据源里的颜色
                        item.color.value = Color(
                            Random.nextFloat(),
                            Random.nextFloat(),
                            Random.nextFloat(),
                            1f
                        )
                    },
                color = Color.White
            )
        }
    }

}

//@Composable
//fun ScrollingContent() {
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(50) { index ->
//            // 为每个 item 创建独立颜色状态
//            val backgroundColor = remember { mutableStateOf(Color(0xFF64C8FF)) }
//
//            Text(
//                text = "Item $index",
//                modifier = Modifier
//                    .padding(8.dp)
//                    .fillMaxWidth()
//                    .background(backgroundColor.value)
//                    .pointerInput(Unit) {
//                        detectTapGestures(
//                            onTap = {
//                                // 点击时随机改变颜色
//                                backgroundColor.value = Color(
//                                    red = Random.nextFloat(),
//                                    green = Random.nextFloat(),
//                                    blue = Random.nextFloat(),
//                                    alpha = 1f
//                                )
//                            }
//                        )
//                    },
//                color = Color.White // 文字颜色
//            )
//        }
//    }
//}

@Composable
fun TaskListScreen(context: Context) {
    // 1️⃣ 状态：保存任务列表
//    持久化问题，tab 切换都会出现丢失状态，要不应该放到全局的，这里是局部状态和对应 UI 组件
//    val tasks = remember { mutableStateListOf("Write report", "Check email") }
    var newTaskText by remember { mutableStateOf("") }
    // TODO:从网络，文件，数据库 api 接口请求过来，本机的 I/O


    val scope = rememberCoroutineScope()
    // 1️⃣ 从 DataStore 读取任务列表，返回 Flow 转 Compose State
    val tasksFlow = context.dataStore.data.map { preferences ->
        preferences[TASKS_KEY]?.toList() ?: emptyList()
    }
    val tasksState by tasksFlow.collectAsState(initial = emptyList())
    val tasks = remember { mutableStateListOf<String>() }

    // 每次 Flow 更新，把最新任务同步到 tasks mutableStateList
    LaunchedEffect(tasksState) {
        tasks.clear()
        tasks.addAll(tasksState)
    }

    // 点击 button 后再弹出来，其实我脑海中有使用体验和想法
    TextField(
        value = newTaskText,
        onValueChange = { newTaskText = it },
        placeholder = { Text("输入任务内容") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    // 2️⃣ 添加任务按钮
    Button(onClick = {
        if (newTaskText.isNotBlank()) {
            tasks.add(newTaskText)
            // 异步保存到 DataStore
            scope.launch {
                context.dataStore.edit { preferences ->
                    preferences[TASKS_KEY] = tasks.toSet()
                }
            }
            newTaskText = "" // 清空输入框
        }
    }) {
        Text("+")
    }

    // 系统 api, 选择文件，windows desktop 中也常见
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            // 用户选择的文件 Uri
        }
    )
    Button(onClick = { launcher.launch(arrayOf("*/*")) }) {
        Text("选择文件")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 3️⃣ 显示任务列表
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(task)
                    IconButton(onClick = {
                        tasks.remove(task)
                        scope.launch {
                            context.dataStore.edit { preferences ->
                                preferences[TASKS_KEY] = tasks.toSet()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }

            }
        }
    }
}

val Context.dataStore by preferencesDataStore("tasks")
val TASKS_KEY = stringSetPreferencesKey("tasks")

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
    TaskListScreen(context = LocalContext.current)
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
            modifier = Modifier
                .size(100.dp, 50.dp)
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

// 内容太多应该划分新的文件夹了，项目架构和常见划分
// 保存数据


