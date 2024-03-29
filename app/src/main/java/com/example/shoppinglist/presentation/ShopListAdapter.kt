package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ShopItemDisabledBinding
import com.example.shoppinglist.databinding.ShopItemEnabledBinding
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    companion object {
        const val VIEW_TYPE_ENABLED = 10
        const val VIEW_TYPE_DISABLED = 20
        const val MAX_POOL_SIZE = 15
    }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

// данная реализация использовалась со старым методом сравнения списков RV через ShopListDiffCallback
// (когда адаптер наследовался от RecyclerView.Adapter)
//    var shopList = listOf<ShopItem>()
//        set(value) {
//            val callBack = ShopListDiffCallBack(shopList, value)
//            val diffResult = DiffUtil.calculateDiff(callBack)
//            diffResult.dispatchUpdatesTo(this)
//            field = value
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType){
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        //старый метод
        // val shopItem = shopList[position]
        val shopItem = getItem(position)
        val binding = holder.binding
        with(holder) {
            when(binding){
                is ShopItemDisabledBinding -> {
                    binding.tvNameShopItem.text = shopItem.name
                    binding.tvCountShopItem.text = shopItem.count.toString()
                }
                is ShopItemEnabledBinding -> {
                    binding.tvNameShopItem.text = shopItem.name
                    binding.tvCountShopItem.text = shopItem.count.toString()
                }
            }
            binding.root.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)
                true
            }
            binding.root.setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //старый метод
        //val item = shopList[position]
        val item = getItem(position)

        return if (item.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED

    }
//нужен для старого метода сравнения списков, в новом методе вся работа со списком скрыта в ListAdapter
//    override fun getItemCount(): Int {
//        return shopList.size
//    }


}