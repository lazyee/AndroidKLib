package com.lazyee.klib.util

import android.content.Context
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
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

    fun unZip(zipFile: File,targetFile:File){
        unzip(FileInputStream(zipFile),targetFile)
    }

    fun zip(sourceFile:File,zipFile: File){
        zip(listOf(sourceFile),zipFile)
    }

    fun zip(sourceFileList: List<File>,zipFile: File){

        try {
            if(zipFile.exists()){
                zipFile.delete()
            }
            val fileOutputStream = FileOutputStream(zipFile)
            val zipOutputStream = ZipOutputStream(fileOutputStream)
            zip("",sourceFileList ,zipOutputStream)
            zipOutputStream.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun zip(dirName:String, sourceFileList: List<File>,zipOutputStream: ZipOutputStream){
        sourceFileList.forEach { zip(dirName,it,zipOutputStream) }
    }


    private fun zip(parentDirName:String,sourceFile:File,zipOutputStream: ZipOutputStream){
        val zipEntry:ZipEntry
        if(sourceFile.isDirectory){
            val dirName = parentDirName + sourceFile.name + File.separator
            zipEntry = ZipEntry(dirName)
            zipOutputStream.putNextEntry(zipEntry)
            val listFiles = sourceFile.listFiles()?.toList()?: emptyList()
            if(listFiles.isNotEmpty()){
                zip(dirName,listFiles, zipOutputStream)
            }
            return
        }

        zipEntry = ZipEntry(parentDirName + sourceFile.name)
        zipOutputStream.putNextEntry(zipEntry)
        val fileInputStream = FileInputStream(sourceFile)
        val buffer = ByteArray(1024)
        var length:Int
        while (fileInputStream.read(buffer).also { length = it } > 0){
            zipOutputStream.write(buffer,0,length)
        }
    }

    fun unZipFromAssets(context: Context, assetName: String, targetFile: File) {
        val dataSource = context.assets.open(assetName)
        unzip(dataSource,targetFile)
    }

    private fun unzip(inputStream: InputStream,targetFile: File){
        try {
            val zipInputStream = ZipInputStream(inputStream)
            var entry = zipInputStream.nextEntry

            mkdirs(targetFile.absolutePath)
            while (entry != null) {
                zipEntryToFile(zipInputStream,entry,targetFile)
                entry = zipInputStream.nextEntry
            }
            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun zipEntryToFile(zipInputStream:ZipInputStream, entry:ZipEntry,targetFile: File){
        if (entry.isDirectory) {
            var name = entry.name
            name = name.substring(0, name.length - 1)
            mkdirs(targetFile.absolutePath + File.separator + name)
            return
        }

        val file = File(targetFile.absolutePath + File.separator + entry.name)
        val parentPath = file.parent
        mkdirs(parentPath)
        createNewFile(file)

        val out = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (zipInputStream.read(buffer).also { length = it } > 0) {
            out.write(buffer, 0, length)
        }
        out.close()
    }

    private fun mkdirs(dirPath:String){
        if(TextUtils.isEmpty(dirPath))return
        var realDirPath = dirPath
        if(realDirPath.endsWith(File.separator)){
            realDirPath = realDirPath.substring(0,realDirPath.length - 1)
        }
        val dir = File(realDirPath)
        if(dir.exists())return
        dir.mkdirs()
    }

    /**
     * 如果有同名文件就直接覆盖
     */
    private fun createNewFile(file:File){
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
    }

}