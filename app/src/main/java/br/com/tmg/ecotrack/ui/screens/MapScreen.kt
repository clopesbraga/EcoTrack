package br.com.tmg.ecotrack.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import br.com.tmg.ecotrack.viewmodel.ImagesViewModel
import org.koin.androidx.compose.koinViewModel

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(showBottomBar: MutableState<Boolean>) {

    showBottomBar.value = true

    val viewModel = koinViewModel<ImagesViewModel>()

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition
            .fromLatLngZoom(currentLocation?:LatLng(0.0, 0.0),5f)
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        coroutineScope.launch {
            val location = viewModel.getCurrentLocation()
            currentLocation = location?.let {LatLng(it.latitude, it.longitude) }
        }
    }

    Box(
        Modifier.fillMaxSize(),
    ) {

        GoogleMap(

            cameraPositionState = cameraPositionState,

            ) {
            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Your Location",
                    snippet = "You are here"
                )
            }
        }

    }
    TakePicture(viewModel)
}


@Composable
private fun TakePicture(viewModel: ImagesViewModel) {


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
        } else {
            Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    Box(contentAlignment = Alignment.CenterEnd) {
        ExtendedFloatingActionButton(
            onClick = {
                val permissionchekResult = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                )

                if (permissionchekResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }


            }) {
            Icon(Icons.Filled.Edit, "Tirar Foto")
            Text(text = "Tirar Foto")
        }


        if (capturedImageUri.path?.isNotEmpty() == true) {
            viewModel.saveImage(capturedImageUri)
        }

    }


}

private fun Context.createImageFile(): File {

    val timestamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
    val imageFileName = "EcoTrack_${timestamp}"
    val image = File.createTempFile(imageFileName, ".jpg", externalCacheDir)

    return image
}
