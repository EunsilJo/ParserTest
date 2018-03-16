package com.github.eunsiljo.parsertest

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.eunsiljo.parsertest.model.JSearchImageResult
import com.github.eunsiljo.parsertest.model.KSearchImageResult
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.view.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val sample: String = "sample.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        //java
        javaGson.button?.run {
            setText(R.string.gson)
            setOnClickListener {
                progress.visibility = View.VISIBLE
                var startMillis = System.currentTimeMillis()
                fromJsonWithGson(MainActivity::class.java, JSearchImageResult::class.java, sample)
                progress.visibility = View.GONE
                javaGson.text.text = (System.currentTimeMillis() - startMillis).toString()
            }
        }
        javaJackson.button?.run {
            setText(R.string.jackson)
            setOnClickListener {
                var startMillis = System.currentTimeMillis()
                fromJsonWithJackson(MainActivity::class.java, JSearchImageResult::class.java, sample)
                javaJackson.text.text = (System.currentTimeMillis() - startMillis).toString()
            }
        }

        //kotlin
        kotlinGson.button?.run {
            setText(R.string.gson)
            setOnClickListener {
                var startMillis = System.currentTimeMillis()
                fromJsonWithGson(MainActivity::class.java, KSearchImageResult::class.java, sample)
                kotlinGson.text.text = (System.currentTimeMillis() - startMillis).toString()
            }
        }
        kotlinJackson.button?.run {
            setText(R.string.jackson)
            setOnClickListener {
                var startMillis = System.currentTimeMillis()
                fromJsonWithJacksonKotlin(MainActivity::class.java, KSearchImageResult::class.java, sample)
                kotlinJackson.text.text = (System.currentTimeMillis() - startMillis).toString()
            }
        }

        //big
        bigGson.button?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        bigJackson.button?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }

        //small
        smallGson.button?.run {
            setText(R.string.gson)
            //setOnClickListener()
        }
        smallJackson.button?.run {
            setText(R.string.jackson)
            //setOnClickListener()
        }
    }

    inner class AsyncTaskExample: AsyncTask<Class<*>, Integer, Long>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg classes: Class<*>?): Long {

        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            progress.visibility = View.GONE
        }
    }

    private val gson: Gson = Gson()
    private val jackson: ObjectMapper = ObjectMapper()
    private val jacksonWithKotlin = ObjectMapper().registerModule(KotlinModule())

    @Throws(IOException::class)
    private fun <T> fromJsonWithGson(testClass: Class<*>, classOfT: Class<T>, subPath: String): T {
        return gson.fromJson(getStringFromResource(testClass, subPath), classOfT)
    }

    @Throws(IOException::class)
    private fun <T> fromJsonWithJackson(testClass: Class<*>, classOfT: Class<T>, subPath: String): T {
        return jackson.readValue(getStringFromResource(testClass, subPath), classOfT)
    }

    @Throws(IOException::class)
    private fun <T> fromJsonWithJacksonKotlin(testClass: Class<*>, classOfT: Class<T>, subPath: String): T {
        return jacksonWithKotlin.readValue(getStringFromResource(testClass, subPath), classOfT)
    }

    private fun openResource(testClass: Class<*>, subPath: String): InputStream {
        return testClass.getResourceAsStream("/" + subPath)
    }

    @Throws(IOException::class)
    private fun getStringFromResource(testClass: Class<*>, subPath: String): String {
        val b = StringBuilder()
        openResource(testClass, subPath).let {
            BufferedReader(InputStreamReader(it)).let {
                var line: String? = it.readLine()
                while (line != null){
                    b.append(line)
                    line = it.readLine()
                }
                //String line;
                //while ((line = r.readLine()) != null) {
                //    b.append(line);
                //}
                return b.toString()
            }
        }
    }
}
