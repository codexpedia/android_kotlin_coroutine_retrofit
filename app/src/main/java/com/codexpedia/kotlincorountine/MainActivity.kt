package com.codexpedia.kotlincorountine

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codexpedia.kotlincorountine.ui.theme.KotlinCorountineTheme
import com.google.accompanist.flowlayout.FlowRow

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinCorountineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val logMsg by mainViewModel.logMsg.observeAsState("")
                    val logMsgs by mainViewModel.logMsgs.observeAsState(emptyList())

                    MainContent(logMsg, logMsgs) {
                        mainViewModel.onAction(it)
                        Toast.makeText(this, "Action type: $it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(logMsg: String, logMsgs: List<String>, onClick: (type: ActionType) -> Unit) {
    Column() {
        LabelText("Click a button to start download")
        Log.d("pye", logMsg )
        FlowRow() {
            ActionBtn("Blocking") {
                onClick(ActionType.BLOCKING)
            }
            ActionBtn("Background") {
                onClick(ActionType.BACKGROUND)
            }
            ActionBtn("Callbacks") {
                onClick(ActionType.CALLBACKS)
            }
            ActionBtn("Suspend") {
                onClick(ActionType.SUSPEND)
            }
            ActionBtn("Concurrent") {
                onClick(ActionType.CONCURRENT)
            }
            ActionBtn("Not cancelable") {
                onClick(ActionType.NOT_CANCELABLE)
            }
            ActionBtn("Progress") {
                onClick(ActionType.PROGRESS)
            }
            ActionBtn("Channels") {
                onClick(ActionType.CHANNELS)
            }
            ActionBtn("Cancel") {
                onClick(ActionType.CANCEL)
            }
        }
//        LabelText(logMsg)
        Logs(logMsgs)
    }
}

@Composable
fun LabelText(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ActionBtn(text: String, onClick: () -> Unit) {
    val shape = CircleShape
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colors.primary, shape)
    )
    {
        Text(text = text)
    }
}

@Composable
fun Logs(messages: List<String>) {
    LazyColumn {
        items(messages) { message ->
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    KotlinCorountineTheme {
        MainContent("", listOf("Hello")) {

        }
    }
}
