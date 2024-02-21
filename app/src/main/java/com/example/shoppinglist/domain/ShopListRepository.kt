package com.example.shoppinglist.domain

interface ShopListRepository {
    fun addShopItem(item: ShopItem)
    fun editShopItem(item: ShopItem)
    fun getShopItemById(id:Int):ShopItem
    fun getShopList(): List<ShopItem>
    fun removeShopItem(item: ShopItem)
}