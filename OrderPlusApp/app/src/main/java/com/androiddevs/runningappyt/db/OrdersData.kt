package com.androiddevs.runningappyt.db

data class OrdersData(
    var start_point: String = "",
    var finish_point: String = "",
    var product: String = "",
    var number: String = "",
    var deadline: String = "",
    var name: String = "",
    var distance: String = "",
    var price: String = ""
) {
}