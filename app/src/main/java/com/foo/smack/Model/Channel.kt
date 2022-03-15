package com.foo.smack.Model

/**
 * Created by David Chhetri on 14,March,2022
 */
class Channel(val name: String, val description: String, val id: String) {

    override fun toString(): String {
        //"#" is for chatting purpose
        return "#$name"
    }

}