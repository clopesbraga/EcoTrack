package br.com.wsworks.listcarswswork.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.material3.OutlinedButtonDefaults
import br.com.tmg.ecotrack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import androidx.compose.ui.text.input.PasswordVisualTransformation as PasswordVisualTransformation1

@Composable
fun LoginScreen(navController: NavHostController, showBottomBar: MutableState<Boolean>) {

    showBottomBar.value = false

    val auth = Firebase.auth
    var username by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF006400)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {

        Column{

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp)
                    .clip(CircleShape)
            )

        }
        Box {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors= OutlinedTextFieldDefaults.colors(Color.Black),
                modifier=Modifier.background(Color.White)

            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.background(Color.Black)) {
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter password") },
                visualTransformation = PasswordVisualTransformation1(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box { SaveLoginButton(navController, username, password, auth) }
    }

}

@Composable
private fun SaveLoginButton(
    navController: NavHostController,
    username: String,
    password: String,
    auth: FirebaseAuth
) {
    FilledTonalButton(
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color.White
        ),
        onClick = {


            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { logintTask ->
                    if (logintTask.isSuccessful) {
                        navController.navigate("map")
                    } else {
                        navController.navigate("map")
                        Log.d("CREATE_ERROR", "USER NOT SAVED -> ${logintTask.exception}")
                    }
                }


    }) {
        Text("Login")
    }
}