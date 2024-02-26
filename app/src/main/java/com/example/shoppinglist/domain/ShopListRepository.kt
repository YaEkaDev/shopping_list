package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    fun addShopItem(item: ShopItem)
    fun editShopItem(item: ShopItem)
    fun getShopItemById(id:Int):ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
    fun removeShopItem(item: ShopItem)
}