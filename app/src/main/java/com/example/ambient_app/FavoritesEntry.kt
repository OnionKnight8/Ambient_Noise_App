package com.example.ambient_app

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "favorites_entries")
//@TypeConverters(Converter::class)
data class FavoritesEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @NonNull
    @ColumnInfo(name = "name")
    var name: String?,

    @NonNull
    //@TypeConverters(Converter::class)
    @ColumnInfo(name = "content")
    var content: List<String>,

    @NonNull
    //@TypeConverters(Converter::class)
    @ColumnInfo(name = "volume")
    var volume: List<String>
)
