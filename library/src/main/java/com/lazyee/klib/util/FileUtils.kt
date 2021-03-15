package com.lazyee.klib.util

import java.io.*

/**
 * @Author leeorz
 * @Date 2020/10/30-12:11 PM
 * @Description:
 */
object FileUtils {

    /**
     * 将文件内容写入目标文件
     * @param content String 写入的内容
     * @param path String 写入的目标绝对路径
     * @param isAppend Boolean 是否是追加模式
     * @return Boolean
     */
    fun contentWriteToFile(content:String,path:String,isAppend:Boolean = false):Boolean{
        val file = File(path)

        val lastSeparatorIndex = path.lastIndexOf(File.separator)
        if(lastSeparatorIndex == -1){
            return false
        }
        val folder = File(path.substring(0,lastSeparatorIndex))
        if(!folder.exists()){
            folder.mkdirs()//创建文件夹
        }

        val fileOutputStream= FileOutputStream(file,isAppend)
        fileOutputStream.write(content.toByteArray(Charsets.UTF_8))
        fileOutputStream.close()
        return true
    }

    /**
     * 根据文件路径返回的文本内容
     * @param path String 文件绝对路径
     * @return String? 返回文件的内容，可能为null
     */
    fun contentOfPath(path:String): String? {
        val file = File(path)
        if (!file.exists()){
            return null
        }
        if (file.isDirectory){
            return null
        }
        return contentOfInputStream(FileInputStream(file))
    }

    /**
     * 判断文件是否存在
     * @param path String 文件路径
     * @return Boolean 返回是否存在，true为存在
     */
    fun fileIsExists(path:String): Boolean {
        return File(path).exists()
    }

    /**
     * 从流中获取正文
     * @param inputStream InputStream
     * @return String
     */
    fun contentOfInputStream(inputStream:InputStream): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var readCount:Int
        while (inputStream.read(buffer,0,1024).also { readCount = it } != -1){
            byteArrayOutputStream.write(buffer,0,readCount)
        }
        inputStream.close()
        return String(byteArrayOutputStream.toByteArray())
    }

    /**
     * 删除文件或者文件夹
     * @param path String
     */
    fun delete(path:String): Boolean {
        return File(path).delete()
    }
}