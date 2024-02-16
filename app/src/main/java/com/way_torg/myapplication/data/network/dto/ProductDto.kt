package com.way_torg.myapplication.data.network.dto

import com.way_torg.myapplication.domain.entity.Category

class ProductDto() {
    var id: String = ""
    var name: String = ""
    var category: CategoryDto = CategoryDto("", "")
    var description: String = ""
    var count: Int = 0
    var price: Double = 0.0
    var discount: Double = 0.0
    var pictures: List<String> = emptyList()
    var rating: Double = 0.0

    constructor(
        id: String,
        name: String,
        category: CategoryDto,
        description: String,
        count: Int,
        price: Double,
        discount: Double,
        pictures: List<String>,
        rating: Double
    ) : this() {
        this.id = id
        this.name = name
        this.category = category
        this.description = description
        this.count = count
        this.price = price
        this.discount = discount
        this.pictures = pictures
        this.rating = rating
    }
}