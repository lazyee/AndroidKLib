package com.lazyee.klib.zip

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.lazyee.klib.listener.OnUnZipListener
import com.lazyee.klib.listener.OnZipListener
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.stream.IntStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2024/4/16 17:29
 */
class UnZipper  {

    constructor(zipFile: File){
        mZipFile = zipFile
    }
    constructor(inputStream: InputStream) {
        mInputStream = inputStream
    }


    private var mCharset: Charset = StandardCharsets.UTF_8
    private var mZipFile: File? = null
    private var mTargetUnZipFile:File? = null
    private var mInputStream :InputStream? = null
    private var mOnUnZipListener:OnUnZipListener? = null

    companion object{
        fun from(zipFilePath:String): UnZipper {
            return UnZipper(File(zipFilePath))
        }

        fun from(zipFile: File): UnZipper {
            return UnZipper(zipFile)
        }

        fun fromAssetsFile(context: Context,assetsFilePath: String): UnZipper {
            val inputStream = context.assets.open(assetsFilePath)
            return UnZipper(inputStream)
        }
    }

    fun charset(charset:Charset): UnZipper {
        mCharset = charset
        return this
    }

    fun listen(listener: OnUnZipListener): UnZipper {
        mOnUnZipListener = listener
        return this
    }

    fun target(targetUnZipFilePath:String): UnZipper {
        mTargetUnZipFile = File(targetUnZipFilePath)
        return this
    }

    fun target(targetUnZipFile:File): UnZipper {
        mTargetUnZipFile = targetUnZipFile
        return this
    }

    fun excetue(){
        mOnUnZipListener?.onUnZipStart()
        try {
            if(mInputStream == null){
                mInputStream = FileInputStream(mZipFile)
            }

            val zipInputStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ZipInputStream(mInputStream!!,mCharset)
            } else {
                ZipInputStream(mInputStream!!)
            }
            var entry = zipInputStream.nextEntry

            mkdirs(mTargetUnZipFile!!.absolutePath)
            while (entry != null) {
                unZipFile(zipInputStream,entry)
                entry = zipInputStream.nextEntry
            }
            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mOnUnZipListener?.onUnZipEnd()
    }

    private fun unZipFile(zipInputStream: ZipInputStream, zipEntry: ZipEntry){
        if (zipEntry.isDirectory) {
            var name = zipEntry.name
            name = name.substring(0, name.length - 1)
            mkdirs(mTargetUnZipFile!!.absolutePath + File.separator + name)
            return
        }

        val file = File(mTargetUnZipFile!!.absolutePath + File.separator + zipEntry.name)
        val parentPath = file.parent
        mkdirs(parentPath)
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()

        mOnUnZipListener?.onUnZipProgress(zipEntry.name)
        val out = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (zipInputStream.read(buffer).also { length = it } > 0) {
            out.write(buffer, 0, length)
        }
        out.close()
    }

    private fun mkdirs(dirPath:String?){
        dirPath?:return
        if(TextUtils.isEmpty(dirPath))return
        var realDirPath = dirPath
        if(realDirPath.endsWith(File.separator)){
            realDirPath = realDirPath.substring(0,realDirPath.length - 1)
        }
        val dir = File(realDirPath)
        if(dir.exists())return
        dir.mkdirs()
    }
}