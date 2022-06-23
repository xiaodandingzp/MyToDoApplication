package com.example.mytodoapplication.data

/**
 * Created by zp on 2022/4/16 16:36
 */
enum class ConsumptionType(val type: Int = 0) {
    //    一日三餐
    MEAL(1),

    //    垃圾食品（一定要少吃呀）
    SNACK(2),

    //    衣服
    CLOTHES(3),

    //    生活用品
    SUPPLIES(4),

    //    社交呀
    SOCIAL(5),

    //    娱乐呀
    ENTERTAINMENT(6)
}