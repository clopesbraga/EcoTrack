package br.com.tmg.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.tmg.ecotrack.ui.screens.HistoricScreen
import br.com.tmg.ecotrack.ui.screens.MapScreen
import br.com.tmg.ecotrack.utils.BarOptions
import br.com.wsworks.listcarswswork.ui.screens.LoginScreen

val bottonOptionsBar = listOf(
    BarOptions(name = "Locais", icons = Icons.Filled.LocationOn),
    BarOptions(name = "Historico", icons = Icons.Filled.DateRange),
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val showBottomBar = remember { mutableStateOf(true)}

            Scaffold(
                bottomBar = {
                    if (showBottomBar.value) {MenuBottomBar(navController)}
                })
            { innerpading ->

                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = "login"
                )
                {
                    composable(route = "login") { LoginScreen(navController, showBottomBar) }
                    composable(route = "map") { MapScreen(showBottomBar) }
                    composable(route = "hist") { HistoricScreen(showBottomBar) }
                }
            }
        }
    }
}

@Composable
fun MenuBottomBar(navController: NavHostController) {
    BottomAppBar {
        val actions = @Composable {

            var selectedItem by remember {
                mutableStateOf(bottonOptionsBar.first())
            }
            bottonOptionsBar.forEach { item ->
                val text = item.name
                val icon = item.icons
                NavigationBarItem(
                    alwaysShowLabel = false,
                    selected = selectedItem == item,
                    onClick = {
                        selectedItem = item
                        val route = when (text) {
                            "Locais" -> "loc"
                            "Historico" -> "hist"
                            else -> {
                                "loc"
                            }
                        }
                        navController.navigate(route, navOptions = navOptions {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        })
                    },
                    icon = { Icon(imageVector = icon, contentDescription = null) },
                    label = { Text(text = text) },
                )

            }

        }
        actions()
    }
}