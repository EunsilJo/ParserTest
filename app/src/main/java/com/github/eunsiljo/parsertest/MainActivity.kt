package com.github.eunsiljo.parsertest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayout()
    }

    fun initLayout(){
        //java
        findViewById<ViewGroup>(R.id.javaGson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        findViewById<ViewGroup>(R.id.javaJackson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }

        //kotlin
        findViewById<ViewGroup>(R.id.kotlinGson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        findViewById<ViewGroup>(R.id.kotlinJackson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }

        //big
        findViewById<ViewGroup>(R.id.bigGson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        findViewById<ViewGroup>(R.id.bigJackson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }

        //small
        findViewById<ViewGroup>(R.id.smallGson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        findViewById<ViewGroup>(R.id.smallJackson).findViewById<Button>(R.id.button)?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }
    }


}
