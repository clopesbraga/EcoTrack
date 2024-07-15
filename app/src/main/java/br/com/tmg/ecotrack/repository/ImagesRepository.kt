package br.com.tmg.ecotrack.repository

import android.app.Application
import br.com.tmg.ecotrack.model.database.ImageItemModel

class ImagesRepository(application: Application) {

    private val base = AppDataBase.getDatabase(application.applicationContext).imageItemDao()

    fun save(image: ImageItemModel):Boolean=base.insert(image)>0

    fun getAllImages():List<ImageItemModel> = base.getAll()
}