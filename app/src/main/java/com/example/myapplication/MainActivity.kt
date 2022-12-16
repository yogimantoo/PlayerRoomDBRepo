package com.example.myapplication

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.Player
import com.example.myapplication.ui.theme.PlayerRoomDatabaseRepoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerRoomDatabaseRepoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlayerScreen()
                }
            }
        }
    }
}

@Composable
fun PlayerScreen(modifier: Modifier = Modifier) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let {
        val viewModel: PlayerViewModel = viewModel(
            it,
            "MainViewModel",
            PlayerViewModelFactory(
                LocalContext.current.applicationContext
                        as Application
            )
        )

        ScreenSetup(viewModel)
    }
}

@Composable
fun ScreenSetup(viewModel: PlayerViewModel) {

    val allPlayers by viewModel.allPlayers.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    MainScreen(
        allPlayers = allPlayers,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun MainScreen(
    allPlayers: List<Player>,
    searchResults: List<Player>,
    viewModel: PlayerViewModel
) {
    var playerName by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }

    val onPlayerTextChange = { text: String ->
        playerName = text
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CustomTextField(
            title = "Player Name",
            textState = playerName,
            onTextChange = onPlayerTextChange,
            keyboardType = KeyboardType.Text
        )

    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Button(onClick = {
            viewModel.insertPlayer(
                Player(
                    playerName,
                )
            )
            searching = false
        }) {
            Text("Add")
        }

        Button(onClick = {
            searching = true
            viewModel.findPlayer(playerName)
        }) {
            Text("Search")
        }

        Button(onClick = {
            searching = false
            viewModel.deletePlayer(playerName)
        }) {
            Text("Delete")
        }

        Button(onClick = {
            searching = false
            playerName = ""
        }) {
            Text("Clear")
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allPlayers
            item {
                TitleRow(head1 = "ID", head2 = "Player")
            }

            items(list) { player ->
                PlayerRow(id = player.id, name = player.playerName)
            }
        }
    }
}

@Composable
fun TitleRow(head1: String, head2: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(head1, color = Color.White,
            modifier = Modifier
                .weight(0.1f))
        Text(head2, color = Color.White,
            modifier = Modifier
                .weight(0.2f))
    }
}

@Composable
fun PlayerRow(id: Int, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(id.toString(), modifier = Modifier
            .weight(0.1f))
        Text(name, modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title)},
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlayerRoomDatabaseRepoTheme {
        PlayerScreen()
    }
}