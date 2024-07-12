package br.com.tmg.ecotrack.ui.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.tmg.ecotrack.R
import coil.compose.AsyncImagePainter
import coil.compose.rememberImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


import java.util.Objects

//4 PASSO CONFIGURAR O CÓDIGO NO @Composable
@Composable
fun FotoScreen() {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {

            capturedImageUri = uri

        }
    var permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permissão garantida", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else{
            Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    //5 PASSO CRIAR A TELA  e BOTAO PARA PEGAR A IMAGEM
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(onClick = {
            val permissionchekResult= ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA)

            if(permissionchekResult == PackageManager.PERMISSION_GRANTED){
                cameraLauncher.launch(uri)
            }else{
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }

        }) {
            Text(text = "Tirar Foto")
        }

    }
    // 6 PASSO VERIFICANDO A CRIACAO DA IIMAGEM

    if (capturedImageUri.path?.isNotEmpty()==true) {

        Image(
            modifier = Modifier.padding(16.dp,8.dp),
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = null,)

    }else{
        
        Image(
            modifier = Modifier.padding(16.dp, 8.dp),
            painter = painterResource(id = R.drawable.ic_default_image),
            contentDescription = null,
        )
    }
}

fun Context.createImageFile(): File {

    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_${timestamp}_"
    val image = File.createTempFile(imageFileName, ".jpg", externalCacheDir)

    return image
}