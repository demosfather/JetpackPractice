package com.cb.myjetpackpractice

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MyObserver(lifecycle))
        sp=getPreferences(Context.MODE_PRIVATE)
        val countReserved=sp.getInt("count_reserved",0)

        //获取实例，不可以直接创建ViewModel实例，一定要通过ViewModelProvider来获取ViewModel实例，因为ViewModel生命周期比Activity长
        viewModel=ViewModelProvider(this,MainViewModelFactory(countReserved)).get(MainViewModel::class.java)
        plusOneBtn.setOnClickListener{
            viewModel.plusOne()
        }
        ClearBtn.setOnClickListener{
            viewModel.clear()
        }
        viewModel.counter.observe(this,  { count->
            infoText.text=count.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        sp.edit{
            putInt("count_reserved",viewModel.counter.value?:0)
        }
    }
}