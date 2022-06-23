package com.example.mytodoapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zp on 2022/4/3 11:22
 */
@Entity(tableName = "FinanceProducts")
class FinanceProducts {

    @PrimaryKey
    var uid = 0

    @ColumnInfo(name = "date")
    val date: Long = 0L

    @ColumnInfo(name = "type")
    val type: ConsumptionType? = null

    @ColumnInfo(name = "money")
    val money: Int = 0

}