package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem

class ShopListMapper {

    fun mapEntityToDbModel(item: ShopItem) = ShopItemDbModel(
        id = item.id,
        name = item.name,
        count = item.count,
        enabled = item.enabled
    )

    fun mapDbModelToEntity(itemDbModel: ShopItemDbModel) = ShopItem(
        id = itemDbModel.id,
        name = itemDbModel.name,
        count = itemDbModel.count,
        enabled = itemDbModel.enabled
    )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}