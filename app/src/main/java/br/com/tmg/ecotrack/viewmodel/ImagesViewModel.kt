package br.com.tmg.ecotrack.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.tmg.ecotrack.model.database.ImageItemModel
import br.com.tmg.ecotrack.repository.ImagesRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume


class ImagesViewModel(application: Application) : AndroidViewModel(application) {


    private val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    private val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

    private val repository: ImagesRepository by inject(ImagesRepository::class.java)
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application.applicationContext)

    fun saveImage(image: Uri) {

        val imageitem = ImageItemModel(
            imageUri = image.toString(),
            local = "local",
            data = currentDate,
            hora = currentTime,
        )
        repository.save(imageitem)
        imageitem.imageUri?.let { saveImageToMediaStore(it.toUri()) }
    }

    fun listImages(): List<ImageItemModel> {
        return repository.getAllImages()
    }

    private fun saveImageToMediaStore(imageUri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "EcoTrack_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EcoTrack")
        }

        viewModelScope.launch {
            try {
                val resolver = getApplication<Application>().contentResolver
                val imageUriInMediaStore =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                imageUriInMediaStore?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        getApplication<Application>().contentResolver.openInputStream(imageUri)
                            ?.use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions appropriately (e.g., log the error, show a message to the user)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    getApplication(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (hasLocationPermission()) {
            return suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
            }
        }
        return null
    }

}