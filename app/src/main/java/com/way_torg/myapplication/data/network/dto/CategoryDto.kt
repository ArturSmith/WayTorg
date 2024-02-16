package com.way_torg.myapplication.data.network.dto

class CategoryDto() {
    var id: String = ""
    var name: String = ""

    constructor(
        id: String,
        name: String
    ) : this(){
        this.id = id
        this.name = name
    }
}