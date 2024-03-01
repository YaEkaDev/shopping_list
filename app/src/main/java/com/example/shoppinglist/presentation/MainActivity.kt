package com.example.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
        //передача списка по старому методу
        //rvAdapter.shopList = it
            rvAdapter.submitList(it)
        }

        binding.btnAddShopItem.setOnClickListener{
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }

    }
    private fun setupRecyclerView(){
        val rvShopList = binding.rvShopList
        with(rvShopList) {
            rvAdapter = ShopListAdapter()
            adapter = rvAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //старый метод
                    //val item = rvAdapter.shopList[position]
                    val item = rvAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.removeShopItem(item)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        rvAdapter.onShopItemClickListener = {
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        rvAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}