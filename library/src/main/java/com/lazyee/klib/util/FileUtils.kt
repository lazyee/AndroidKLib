package com.lazyee.klib.util

import android.content.Context
import android.os.StatFs
import android.text.TextUtils
import com.lazyee.klib.listener.OnFileDownloadListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:与文件相关的工具类
 * Date: 2022/6/22 13:22
 */
object FileUtils {

    /**
     * Return the total size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the total size of file system
     */
    fun getFsTotalSize(anyPathInFs: String): Long{
        if(TextUtils.isEmpty(anyPathInFs))return 0L
        val statFs = StatFs(anyPathInFs)
        var blockSize:Long = 0
        var totalSize:Long = 0
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            blockSize = statFs.blockSizeLong
            totalSize = statFs.blockCountLong
        }else{
            blockSize = statFs.blockSize.toLong()
            totalSize = statFs.blockCount.toLong()
        }
        return blockSize * totalSize
    }

    /**
     * Return the available size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the available size of file system
     */
    fun getFsAvailableSize(anyPathInFs: String): Long {
        if(TextUtils.isEmpty(anyPathInFs))return 0L
        val statFs = StatFs(anyPathInFs)
        var blockSize:Long = 0
        var availableSize:Long = 0
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            blockSize = statFs.blockSizeLong
            availableSize = statFs.availableBlocksLong
        }else{
            blockSize = statFs.blockSize.toLong()
            availableSize = statFs.availableBlocks.toLong()
        }

        return blockSize * availableSize
    }

    /**
     * 复制assets文件到制定路径
     */
    fun copyAssetsFile(context: Context, assetsFilePath:String, targetFilePath: String){
        copyAssetsFile(context,assetsFilePath,File(targetFilePath))
    }

    /**
     * 复制assets文件到制定路径
     */
    fun copyAssetsFile(context: Context,assetsFilePath: String,targetFile:File){
        val assetsX5CoreInputStream = context.assets.open(assetsFilePath)
        val buffer = ByteArray(1024)

        val targetFileOutputStream = FileOutputStream(targetFile)
        var len = 0
        while (assetsX5CoreInputStream.read(buffer).also { len = it } != -1){
            targetFileOutputStream.write(buffer,0,len)
        }

        assetsX5CoreInputStream.close()
        targetFileOutputStream.close()
    }

    suspend fun download(downloadFileUrl:String?,outFile:File):Boolean{
        if(TextUtils.isEmpty(downloadFileUrl)) return false
        val url: URL
        val connection: HttpURLConnection
        try {
            //统一资源
            url = URL(downloadFileUrl)
            //打开链接
            connection = url.openConnection() as HttpURLConnection
            //设置链接超时
            connection.connectTimeout = 4000
            //设置允许得到服务器的输入流,默认为true可以不用设置
            connection.doInput = true
            //设置允许向服务器写入数据，一般get方法不会设置，大多用在post方法，默认为false
            connection.doOutput = false
            //设置请求方法
            connection.requestMethod = "GET"
            //设置请求的字符编码
            connection.setRequestProperty("Charset", "utf-8")
            //设置connection打开链接资源
            connection.connect()

            //得到链接的响应码 200为成功
            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false
            }

            //创建一个文件输出流
            val outputStream = FileOutputStream(outFile)
            //得到服务器响应的输入流
            val inputStream = connection.inputStream
            //获取请求的内容总长度
            val contentLength = connection.contentLength
            //创建缓冲输入流对象，相对于inputStream效率要高一些
            val bfi = BufferedInputStream(inputStream)
            //此处的len表示每次循环读取的内容长度
            var len: Int
            //已经读取的总长度
            var total = 0
            //bytes是用于存储每次读取出来的内容
            val bytes = ByteArray(1024)
            while (bfi.read(bytes).also { len = it } != -1) {
                //每次读取完了都将len累加在total里
                total += len
                //通过文件输出流写入从服务器中读取的数据
                outputStream.write(bytes, 0, len)
            }
            //关闭打开的流对象
            outputStream.close()
            inputStream.close()
            bfi.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 递归删除文件or文件夹(包括文件夹下面的所有文件)
     */
    fun delete(filePath:String): Boolean {
        if(TextUtils.isEmpty(filePath)) return false
        return delete(File(filePath))
    }

    /**
     * 递归删除文件or文件夹(包括文件夹下面的所有文件)
     */
    fun delete(file:File):Boolean{
        try {
            if(!file.exists()) return false
            if(file.isFile){
                return file.delete()
            }
            if(file.isDirectory){
                file.listFiles()?.forEach { delete(it) }
                return file.delete()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return false
    }

    /**
     * 简单文件下载
     */
    fun download(downloadFileUrl:String?, outFile: File, listener: OnFileDownloadListener?){
        CoroutineScope(Dispatchers.IO).launch {
            if(TextUtils.isEmpty(downloadFileUrl)){
                listener?.run {
                    withContext(Dispatchers.Main){ onDownloadFailure(Exception("downloadFileUrl is empty string ")) }
                }
                return@launch
            }
            val url: URL
            val connection: HttpURLConnection
            try {
                //统一资源
                url = URL(downloadFileUrl)
                //打开链接
                connection = url.openConnection() as HttpURLConnection
                //设置链接超时
                connection.connectTimeout = 4000
                //设置允许得到服务器的输入流,默认为true可以不用设置
                connection.doInput = true
                //设置允许向服务器写入数据，一般get方法不会设置，大多用在post方法，默认为false
                connection.doOutput = false
                //设置请求方法
                connection.requestMethod = "GET"
                //设置请求的字符编码
                connection.setRequestProperty("Charset", "utf-8")
                //设置connection打开链接资源
                connection.connect()

                //得到链接的响应码 200为成功
                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    listener?.run {
                        withContext(Dispatchers.Main){ onDownloadFailure(Exception("download failed,responseCode:${responseCode}")) }
                    }
                    return@launch
                }

                //创建一个文件输出流
                val outputStream = FileOutputStream(outFile)
                //得到服务器响应的输入流
                val inputStream = connection.inputStream
                //获取请求的内容总长度
                val contentLength = connection.contentLength

                listener?.run {
                    withContext(Dispatchers.Main) { onDownloadStart(contentLength) }
                }

                //创建缓冲输入流对象，相对于inputStream效率要高一些
                val bfi = BufferedInputStream(inputStream)
                //此处的len表示每次循环读取的内容长度
                var len: Int
                //已经读取的总长度
                var total = 0
                //bytes是用于存储每次读取出来的内容
                val bytes = ByteArray(1024)
                while (bfi.read(bytes).also { len = it } != -1) {
                    //每次读取完了都将len累加在total里
                    total += len
                    listener?.run {
                        withContext(Dispatchers.Main){ onDownloading(total,contentLength) }
                    }

                    //通过文件输出流写入从服务器中读取的数据
                    outputStream.write(bytes, 0, len)
                }
                //关闭打开的流对象
                outputStream.close()
                inputStream.close()
                bfi.close()
                listener?.run {
                    withContext(Dispatchers.Main){ onDownloadComplete(outFile) }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                listener?.run {
                    withContext(Dispatchers.Main){ onDownloadFailure(e) }
                }
            }
        }
    }
}