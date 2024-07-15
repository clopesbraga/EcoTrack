package br.com.tmg.ecotrack.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tmg.ecotrack.model.database.ImageItemModel

@Dao
interface ImageItemDAO {

    @Insert
    fun insert (name: ImageItemModel):Long

    @Query("SELECT * FROM Image")
    fun getAll(): List<ImageItemModel>
}