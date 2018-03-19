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

    private val bigFileName: String = "sample_big.json"
    private val smallFileName: String = "sample_small.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithGson(MainActivity::class.java, JSearchImageResult::class.java,
                            bigFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    javaGson.text.text = (System.currentTimeMillis() - startMillis).toString()
                }
        createButtonClickObservable(javaJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithJackson(MainActivity::class.java, JSearchImageResult::class.java,
                            bigFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    javaJackson.text.text = (System.currentTimeMillis() - startMillis).toString()
                }

        //kotlin
        createButtonClickObservable(kotlinGson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithGson(MainActivity::class.java, KSearchImageResult::class.java,
                            bigFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    kotlinGson.text.text = (System.currentTimeMillis() - startMillis).toString()
                }
        createButtonClickObservable(kotlinJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithJacksonKotlin(MainActivity::class.java, KSearchImageResult::class.java,
                            bigFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    kotlinJackson.text.text = (System.currentTimeMillis() - startMillis).toString()
                }

        //small
        createButtonClickObservable(smallGson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithGson(MainActivity::class.java, KSearchImageResult::class.java,
                            smallFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    smallGson.text.text = (System.currentTimeMillis() - startMillis).toString()
                }
        createButtonClickObservable(smallJackson.button)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { startMillis ->
                    fromJsonWithJacksonKotlin(MainActivity::class.java, KSearchImageResult::class.java,
                            smallFileName)
                    startMillis
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startMillis ->
                    hideProgress()
                    smallJackson.text.text = (System.currentTimeMillis() - startMillis).toString()
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
