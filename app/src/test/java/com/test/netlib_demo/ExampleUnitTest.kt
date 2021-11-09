package com.test.netlib_demo

import com.test.lib.*
import com.test.netlib_demo.tasks.Task1
import com.test.netlib_demo.tasks.Task2
import com.test.netlib_demo.tasks.Task3
import com.test.netlib_demo.tasks.TaskManager
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.Cookie
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun okhttp() {
        val url0 = "https:www.baidu.com"
        val url1 = "https://blog.csdn.net/crazyhacking/article/details/44497293"
        val url2 = "http://publicobject.com/helloworld.txt"

        val cacheControl: CacheControl = CacheControl.Builder()
            .maxStale(5, TimeUnit.SECONDS)
            .maxAge(5, TimeUnit.SECONDS)
            .build()

        val okhttpClient = OkHttpClient.Builder().cookieJar(object : CookieJar {
            var cache: MutableList<Cookie> = mutableListOf()

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                // Http发送请求前回调，Request中设置Cookie
                //过期的Cookie
                val invalidCookies: MutableList<Cookie> = mutableListOf()
                //有效的Cookie
                val validCookies: MutableList<Cookie> = mutableListOf()
                for (cookie in cache) {
                    if (cookie.expiresAt < System.currentTimeMillis()) {
                        //判断是否过期
                        invalidCookies.add(cookie)
                    } else if (cookie.matches(url)) {
                        //匹配Cookie对应url
                        validCookies.add(cookie)
                    }
                }
                //缓存中移除过期的Cookie
                cache.removeAll(invalidCookies)
                //返回List<Cookie>让Request进行设置

                val cusCookie = Cookie.Builder()
                    .hostOnlyDomain(url.host)
                    .name("SESSION").value("wo shi ni baba").build()
                validCookies.add(cusCookie)
                return validCookies
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                // Http请求结束，Response中有Cookie时候回调
                // 内存中缓存Cookie
                cache.addAll(cookies)
            }

        })
            .cache(Cache(File("D:\\cacheeeeeee"), 500 * 1024 * 1024))
//            .addNetworkInterceptor(NetCacheInterceptor())
            .build()

        val request = Request.Builder()
            .url(url2)
            .get()
//            .cacheControl(cacheControl)
            .build()

        val call = okhttpClient.newCall(request)
        val response = call.execute()
        when {
            response.cacheResponse != null -> {
                println("这是 >>> cacheResponse")
            }
            response.networkResponse != null -> {
                println("这是 >>> networkResponse")
            }
            else -> {
                println("这是 >>> others")
            }
        }
        val result = response.body?.string()
        println(result)


//        val response = call.enqueue(object :Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                println("${e.message}")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                when {
//                    response.cacheResponse != null -> {
//                        println("这是 >>> cacheResponse")
//                    }
//                    response.networkResponse != null -> {
//                        println("这是 >>> networkResponse")
//                    }
//                    else -> {
//                        println("这是 >>> others")
//                    }
//                }
//            }
//
//        })


    }

    @Test
    fun tasks() {
        val taskManager = TaskManager()
        taskManager.addTask(Task1())
        taskManager.addTask(Task2())
        taskManager.addTask(Task3())

        taskManager.doAction("Task3", taskManager)
    }


    // 自定义的Okhttp，纯手写
    @Test
    fun testMyOkhttp() {
        val client = MyOkhttpClient.Builder().build()

        val request = MyRequest.Builder().url("http://publicobject.com/helloworld.txt").build()

        val call = client.newCall(request)

        // 异步请求
        call.enqueue(object : MyCallback {
            override fun onFailure(call: MyCall?, exception: IOException?) {
                println(exception?.message)
            }

            override fun onResponse(call: MyCall?, response: MyResponse?) {
                println(response?.string())
            }

        })

//        同步请求
//        val response = call.execute()
//        println("$response")
    }

    @Test
    fun testArrayDeque() {
        val arrayDeque = ArrayDeque<String>()
        arrayDeque.add("你")
        arrayDeque.add("是")
        arrayDeque.add("谁")
        arrayDeque.add("啊")
        arrayDeque.add("!")

//        arrayDeque.forEach {
//            println("$it")
//            arrayDeque.remove(it)
//        }

        println("$arrayDeque")

        val res1v = MyResponse()

        val res2 = res1v

        val ss = "可好看好看"
        val sss = ss

        val hh = 1024

        val hhh = hh
    }
}