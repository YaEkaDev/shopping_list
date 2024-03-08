package com.example.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        shopItemContainer = binding.shopItemContainerLand
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
        //передача списка по старому методу
        //rvAdapter.shopList = it
            rvAdapter.submitList(it)
        }

        binding.btnAddShopItem.setOnClickListener{
            if (!isOneColumnMode()){
                launchfragment(ShopItemFragment.newInstanceAddItem())
            }
            else {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            }
        }

    }

    override fun onEdidingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOneColumnMode(): Boolean{
        return shopItemContainer == null
    }

    private fun launchfragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainerLand, fragment)
            .addToBackStack(null)
            .commit()
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

            if (!isOneColumnMode()){
                launchfragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
            else {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }


        }
    }

    private fun setupLongClickListener() {
        rvAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}