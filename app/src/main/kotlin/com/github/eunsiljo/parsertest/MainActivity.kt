package com.github.eunsiljo.parsertest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.eunsiljo.parsertest.model.JSearchImageResult
import com.github.eunsiljo.parsertest.model.KSearchImageResult
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.view.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val bigFileName: String = "big.json"
    private val smallFileName: String = "small.json"

    private var gson: Gson = Gson()
    private var jackson: ObjectMapper = ObjectMapper()
    private var jacksonWithKotlin = ObjectMapper().registerModule(KotlinModule())

    private var bigJson: String? = null
    private var smallJson: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bigJson = getStringFromResource(MainActivity::class.java, bigFileName)
        smallJson = getStringFromResource(MainActivity::class.java, smallFileName)

        initLayout()
        initObservable()
    }

    private fun initLayout(){
        //java
        javaGson.button.text = getString(R.string.gson)
        javaJackson.button.text = getString(R.string.jackson)

        //kotlin
        kotlinGson.button.text = getString(R.string.gson)
        kotlinJackson.button.text = getString(R.string.jackson)

        //small
        smallGson.button.text = getString(R.string.gson)
        smallJackson.button.text = getString(R.string.jackson)
    }

    private fun initObservable(){
        //java
        createButtonClickObservable(javaGson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithGson(bigJson, JSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    javaGson.text.text = duringTime.toString()
                }
        createButtonClickObservable(javaJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithJackson(bigJson, JSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    javaJackson.text.text = duringTime.toString()
                }

        //kotlin
        createButtonClickObservable(kotlinGson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithGson(bigJson, KSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    kotlinGson.text.text = duringTime.toString()
                }
        createButtonClickObservable(kotlinJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithJacksonKotlin(bigJson, KSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    kotlinJackson.text.text = duringTime.toString()
                }

        //small
        createButtonClickObservable(smallGson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithGson(smallJson, KSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    smallGson.text.text = duringTime.toString()
                }
        createButtonClickObservable(smallJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.computation())
                .map { startMillis ->
                    fromJsonWithJacksonKotlin(smallJson, KSearchImageResult::class.java)
                    System.currentTimeMillis() - startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { duringTime ->
                    hideProgress()
                    smallJackson.text.text = duringTime.toString()
                }
    }

    private fun createButtonClickObservable(button: View): Observable<Long> {
        return Observable.create { emitter ->
            button.setOnClickListener {
                emitter.onNext(System.currentTimeMillis())
            }
        }
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    @Throws(IOException::class)
    private fun <T> fromJsonWithGson(json: String?, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    @Throws(IOException::class)
    private fun <T> fromJsonWithJackson(json: String?, classOfT: Class<T>): T {
        return jackson.readValue(json, classOfT)
    }

    @Throws(IOException::class)
    private fun <T> fromJsonWithJacksonKotlin(json: String?, classOfT: Class<T>): T {
        return jacksonWithKotlin.readValue(json, classOfT)
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
                return b.toString()
            }
        }
    }
}
