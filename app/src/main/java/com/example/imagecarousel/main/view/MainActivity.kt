package com.example.imagecarousel.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imagecarousel.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.imagecarousel.CarouselAdapter
import com.example.imagecarousel.ListItemAdapter
import com.example.imagecarousel.model.Item
import com.example.imagecarousel.main.viewmodel.CarouselViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var listAdapter: ListItemAdapter
    private lateinit var viewModel: CarouselViewModel
    private lateinit var itemAdapter: CarouselAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this).get(CarouselViewModel::class.java)

        //for ImageCarousel
        initImageCarousel()

        //for search view
        initSearchView()

        // for list items below
        initInfoRecyclerView()
    }

    private fun initImageCarousel() {
        binding.carouselView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        itemAdapter = CarouselAdapter()
        itemAdapter.setItems(viewModel.getItemListForCarousel())
        binding.carouselView.adapter = itemAdapter
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselView)
        binding.carouselView.addItemDecoration(CirclePagerIndicatorDecoration())

        binding.carouselView
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val pos: Int =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        changeInfoList(pos)
                    }
                }
            })
    }

    private fun changeInfoList(pos: Int) {
        if(pos != viewModel.getCarouselPosition())
            listAdapter.setList(viewModel.getInfoListForCarousel(pos))
    }

    private fun initSearchView() {
        binding.searchBar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(newText: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filterList =  viewModel.getFilteredList(newText)
                    listAdapter.setList(filterList)
                    if (filterList.isEmpty()) {
                        Toast.makeText(applicationContext, "No Data Found..", Toast.LENGTH_SHORT).show()
                    }
                    return false
                }

            })

    }


    private fun initInfoRecyclerView(){
        binding.infoList.layoutManager = LinearLayoutManager(this)
        listAdapter = ListItemAdapter()
        listAdapter.setList(viewModel.getInfoListForCarousel(viewModel.getCarouselPosition()))
        binding.infoList.adapter = listAdapter
    }

}
