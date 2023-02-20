package nl.pdik.level4.task1.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.grid.items
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.lifecycle.viewmodel.compose.viewModel;
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState;
import androidx.lifecycle.LiveData

import androidx.navigation.NavController
import kotlinx.coroutines.launch
import nl.pdik.level4.task1.viewmodel.GameViewModel
import nl.pdik.level4.task1.R
import nl.pdik.level4.task1.Utils
import nl.pdik.level4.task1.model.Game
import kotlin.streams.toList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: GameViewModel) {
    val context = LocalContext.current
    val games = viewModel.gameBacklog
    val scaffoldState = rememberScaffoldState() // Needed for the Snackbar object.
    val scope = rememberCoroutineScope() // Also needed for the Snackbar object.
    var deletedBacklog by remember { mutableStateOf(false) } // Switch to decide whether Snackbar "undo delete all" option is used.

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.DarkGray,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.app_name))
                        IconButton(onClick = {
                            // Game backlog should be cleared from the database  only after confirmation i.e."UNDO" option NOT
                            // selected on the "Snackbar". For that purpose we have introduced this switch.
                            deletedBacklog = true
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.snackbar_msg),
                                    actionLabel = context.getString(R.string.undo)
                                )
                                if (result != SnackbarResult.ActionPerformed) {
                                    viewModel.deleteGameBacklog()
                                }
                                deletedBacklog = false
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete all games"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddGameScreen.route) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        if (!deletedBacklog) {
            Games(
                context = context,
                games,
                modifier = Modifier.padding(16.dp),
                scaffoldState
            )
        }
    }
}


@Composable
fun Games(
    context: Context,
    games: LiveData<List<Game>>,
    modifier: Modifier,
    scaffoldState: ScaffoldState
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {

        games.value?.let {
            items(items = it.toList(), itemContent = { game ->
                GameCard(context, game, scaffoldState)
            })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameCard(
    context: Context,
    game: Game,
    scaffoldState: ScaffoldState,
    viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd) || dismissState.isDismissed(
            DismissDirection.EndToStart
        )
    ) {
        LaunchedEffect(Unit) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.deleted_game, game.title),
                actionLabel = context.getString(R.string.undo)
            )
            if (result != SnackbarResult.ActionPerformed) {
                viewModel.deleteGame(game)
            } else {
                dismissState.reset()
            }
        }
    }

    SwipeToDismiss(
        state = dismissState,
        background = {},
        dismissContent = {
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.h6,
                        fontStyle = FontStyle.Italic
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = game.platform)
                        Text(text = "Release: " + Utils.dateToString(game.release))
                    }
                }
            }
        },
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
    )
}