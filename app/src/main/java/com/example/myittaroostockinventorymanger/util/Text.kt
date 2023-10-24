package com.example.myittaroostockinventorymanger.util

class Text {

    companion object {
        fun format(num: Int): String {
            return if (num < 2) {
                "Item"
            } else {
                "Items"
            }
        }
    }
}