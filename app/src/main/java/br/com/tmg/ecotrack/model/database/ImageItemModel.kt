package br.com.tmg.ecotrack.model.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image")
class ImageItemModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int?=null,

    @ColumnInfo(name = "imageUri")
    val imageUri: String?=null,

    @ColumnInfo(name = "local")
    val local: String?=null,

    @ColumnInfo(name = "data")
    val data: String?=null,

    @ColumnInfo(name = "hora")
    val hora: String?=null

)
