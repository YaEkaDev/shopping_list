package com.example.shoppinglist.domain

class GetShopItemListUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopList(): List<ShopItem>{
       return shopListRepository.getShopList()
    }

}