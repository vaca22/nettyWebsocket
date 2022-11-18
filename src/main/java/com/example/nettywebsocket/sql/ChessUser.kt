package com.example.nettywebsocket.sql

import org.json.JSONArray
import org.json.JSONObject

data class ChessUser(
    var uid:String,
    var name:String,
    var header_url:String
){
    fun toJson(): JSONObject {
        val s=JSONObject()
        s.put("uid",uid)
        s.put("name",name)
        s.put("header_url",header_url)
        return s
    }
}
