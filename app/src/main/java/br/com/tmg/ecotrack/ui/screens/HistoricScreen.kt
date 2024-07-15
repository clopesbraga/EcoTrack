package br.com.tmg.ecotrack.ui.screens

import android.net.Uri
import androidx.core.net.toUri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import br.com.tmg.ecotrack.viewmodel.ImagesViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HistoricScreen(showBottomBar: MutableState<Boolean>) {

    showBottomBar.value = true

    val viewModel = koinViewModel<ImagesViewModel>()
    val context = LocalContext.current
    val imagelist = viewModel.listImages()

    Column {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(imagelist) { imageitem ->
                val uri = Uri.parse(imageitem.imageUri)
                val bitmap = try {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri.toString().toUri()
                    )
                } catch (e: Exception) {
                    Log.d("BITMAP ERROR", e.toString())
                    null
                }
                bitmap?.let {
                    Card(modifier = Modifier.padding(16.dp)){
                        Row(modifier =Modifier
                            .fillMaxSize(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(100.dp)
                                    .clip(CircleShape)

                            )
                            Column(modifier = Modifier
                                .padding(16.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Local:${imageitem.local}")
                                Text(text = "Data: ${imageitem.data}")
                                Text(text = "Hora: ${imageitem.hora}")
                            }
                        }
                    }


                }


            }
        }
    }
}