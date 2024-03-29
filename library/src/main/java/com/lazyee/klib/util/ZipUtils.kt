package com.lazyee.klib.util

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
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:压缩与解压缩
 * Date: 2023/9/26 09:34
 */
private const val TAG = "[ZipUtils]"
object ZipUtils {

    val GBK = Charset.forName("GBK")

    @JvmName("unZip3")
    fun unZip(zipFilePath: String,targetFilePath: String,charset: Charset = StandardCharsets.UTF_8,listener: OnUnZipListener? = null){
        unZip(File(zipFilePath),File(targetFilePath),charset,listener)
    }

    @JvmName("unZip2")
    fun unZip(zipFile: File,targetFilePath:String,charset: Charset = StandardCharsets.UTF_8,listener: OnUnZipListener? = null){
        unZip(zipFile,File(targetFilePath),charset,listener)
    }

    fun unZip(zipFile: File,targetFile:File,charset: Charset = StandardCharsets.UTF_8,listener:OnUnZipListener? = null){
        if(!zipFile.exists())return
        listener?.onUnZipStart()
        realUnZip(FileInputStream(zipFile),targetFile,charset,listener)
        listener?.onUnZipEnd()
    }

    @JvmName("unZipFromAssets2")
    fun unZipFromAssets(context: Context,assetsFilePath: String,targetFilePath: String,charset: Charset = StandardCharsets.UTF_8,listener: OnUnZipListener? = null){
        unZipFromAssets(context,assetsFilePath,File(targetFilePath),charset,listener)
    }

    fun unZipFromAssets(context: Context, assetsFilePath: String, targetFile: File,charset: Charset = StandardCharsets.UTF_8,listener: OnUnZipListener? = null) {
        listener?.onUnZipStart()
        val dataSource = context.assets.open(assetsFilePath)
        realUnZip(dataSource,targetFile,charset,listener)
        listener?.onUnZipEnd()
    }

    @JvmName("zip3")
    fun zip(sourceFilePath: String,zipFilePath: String,listener: OnZipListener? = null){
        zip(File(sourceFilePath),File(zipFilePath),listener)
    }

    @JvmName("zip2")
    fun zip(sourceFile: File,zipFilePath: String,listener: OnZipListener? = null){
        zip(sourceFile,File(zipFilePath),listener)
    }

    fun zip(sourceFile:File,zipFile: File,listener: OnZipListener? = null){
        zipMultipleFile(listOf(sourceFile),zipFile,listener)
    }

    @JvmName("zipMultipleFile2")
    fun zipMultipleFile(sourceFileList: List<File>, zipFilePath: String, listener: OnZipListener? = null){
        zipMultipleFile(sourceFileList,File(zipFilePath),listener)
    }

    @JvmName("zipMultipleFile3")
    fun zipMultipleFile(sourceFilePathList: List<String>, zipFilePath: String, listener: OnZipListener? = null){
        val sourceFileList = mutableListOf<File>()
        sourceFilePathList.forEach { sourceFileList.add(File(it)) }
        zipMultipleFile(sourceFileList,File(zipFilePath),listener)
    }

    fun zipMultipleFile(sourceFileList: List<File>,zipFile: File,listener: OnZipListener? = null){
        listener?.onZipStart()
        val isExists = sourceFileList.find { it.exists() } != null
        if(isExists){
            try {
                if(zipFile.exists()){
                    zipFile.delete()
                }

                val fileOutputStream = FileOutputStream(zipFile)
                val zipOutputStream = ZipOutputStream(fileOutputStream)
                realZip("",sourceFileList ,zipOutputStream,listener)
                zipOutputStream.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        listener?.onZipEnd()
    }

    private fun realZip(dirName:String, sourceFileList: List<File>,zipOutputStream: ZipOutputStream,listener: OnZipListener? = null){
        sourceFileList.forEach { realZip(dirName,it,zipOutputStream,listener) }
    }


    private fun realZip(parentDirName:String,sourceFile:File,zipOutputStream: ZipOutputStream,listener: OnZipListener? = null){
        if(!sourceFile.exists())return
        val zipEntry:ZipEntry
        if(sourceFile.isDirectory){
            val dirName = parentDirName + sourceFile.name + File.separator
            zipEntry = ZipEntry(dirName)
            zipOutputStream.putNextEntry(zipEntry)
            val listFiles = sourceFile.listFiles()?.toList()?: emptyList()
            if(listFiles.isNotEmpty()){
                realZip(dirName,listFiles, zipOutputStream,listener)
            }
            return
        }

        zipEntry = ZipEntry(parentDirName + sourceFile.name)
        zipOutputStream.putNextEntry(zipEntry)
        listener?.onZipProgress(zipEntry.name)
        val fileInputStream = FileInputStream(sourceFile)
        val buffer = ByteArray(1024)
        var length:Int
        while (fileInputStream.read(buffer).also { length = it } > 0){
            zipOutputStream.write(buffer,0,length)
        }
    }



    private fun realUnZip(inputStream: InputStream,targetFile: File,charset: Charset,listener: OnUnZipListener? = null){
        try {
            val zipInputStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ZipInputStream(inputStream,charset)
            } else {
                ZipInputStream(inputStream)
            }
            var entry = zipInputStream.nextEntry

            mkdirs(targetFile.absolutePath)
            while (entry != null) {
                unZipFile(zipInputStream,entry,targetFile,listener)
                entry = zipInputStream.nextEntry
            }
            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun unZipFile(zipInputStream:ZipInputStream, zipEntry:ZipEntry, targetFile: File, listener: OnUnZipListener? = null){
        if (zipEntry.isDirectory) {
            var name = zipEntry.name
            name = name.substring(0, name.length - 1)
            mkdirs(targetFile.absolutePath + File.separator + name)
            return
        }

        val file = File(targetFile.absolutePath + File.separator + zipEntry.name)
        val parentPath = file.parent
        mkdirs(parentPath)
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()

        listener?.onUnZipProgress(zipEntry.name)
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