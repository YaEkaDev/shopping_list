package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemByIdUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _canFinish = MutableLiveData<Unit>()
    val canFinish: LiveData<Unit>
        get() = _canFinish

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int){
        val item = getShopItemByIdUseCase.getShopItemById(shopItemId)
        _shopItem.value = item
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            _shopItem.value?.let {
                val itemCopy = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(itemCopy)
                finishWork()
            }
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            val item = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(item)
            finishWork()
        }
    }

    private fun parseName(name: String?):String{
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?) : Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name:String, count: Int): Boolean{
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count<=0){
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }

    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }

    private fun finishWork(){
        _canFinish.value = Unit
    }
}