package com.way_torg.myapplication.data.network.dto

class CustomerInfoDto() {
    var id:String=""
    var name: String=""
    var address: String=""
    var contact: String=""
    var message: String =""

    constructor(
        id:String,
        name: String,
        address:String,
        contact:String,
        message:String
    ):this(){
        this.id = id
        this.name = name
        this.address = address
        this.contact = contact
        this.message = message
    }
}