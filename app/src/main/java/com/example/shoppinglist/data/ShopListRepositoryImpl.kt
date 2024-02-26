package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10){
            val item = ShopItem("Name $i", i, true)
            addShopItem(item)
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        shopList.add(item)
        updateList()
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItemById(item.id)
        shopList.remove(oldItem)
        addShopItem(item)
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find {
            it.id == id
        } ?: throw RuntimeException("Element $id not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>>{
        return shopListLiveData
    }

    override fun removeShopItem(item: ShopItem) {
        shopList.remove(item)
        updateList()
    }

    private fun updateList() {
        shopListLiveData.value = shopList.toList()
    }
}