package nl.pdik.level4.task1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import nl.pdik.level4.task1.ui.screens.HomeScreen
import nl.pdik.level4.task1.ui.screens.Screen
import nl.pdik.level4.task1.ui.theme.MADLevel4Task1Theme
import nl.pdik.level4.task1.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MADLevel4Task1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}


@Composable
private fun GameRoomNavHost(
    navController: NavHostController, modifier: Modifier
) {
    val context = LocalContext.current
    val viewModel: GameViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(route = Screen.HomeScreen.route)
        {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AddGameScreen.route) {
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MADLevel4Task1Theme {

    }
}