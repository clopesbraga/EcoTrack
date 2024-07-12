package br.com.tmg.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import br.com.tmg.ecotrack.ui.screens.FotoScreen
import br.com.tmg.ecotrack.ui.screens.HistoricScreen
import br.com.tmg.ecotrack.ui.screens.LoginScreen
import br.com.tmg.ecotrack.ui.screens.MapScreen
import br.com.tmg.ecotrack.ui.theme.EcoTrackTheme
import br.com.tmg.ecotrack.utils.BarOptions

val bottonOptionsBar = listOf(
    BarOptions(name ="loc", icons = Icons.Filled.LocationOn),
    BarOptions(name ="fotos",icons = Icons.Filled.Search),
    BarOptions(name ="hist",icons = Icons.Filled.DateRange),
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
           Scaffold(
               bottomBar={
               MenuBottomBar(navController)
           })
           { innerpading->

               NavHost(
                   modifier = Modifier.fillMaxSize(),
                   navController = navController,
                   startDestination = "loc"

               )
               {
                   composable(route="login") { LoginScreen(navController)}
                   composable(route="loc") { MapScreen() }
                   composable(route="fotos") { FotoScreen() }
                   composable(route="hist") { HistoricScreen() }
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
                        selected = selectedItem == item,
                        onClick = {
                            selectedItem = item
                            val route = when (text) {
                                "loc" -> "loc"
                                "hist" -> "hist"
                                "fotos" -> "fotos"

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



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcoTrackTheme {

    }
}