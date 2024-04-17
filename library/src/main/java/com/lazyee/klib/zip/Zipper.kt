package com.lazyee.klib.zip

import com.lazyee.klib.listener.OnZipListener
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2024/4/16 17:21
 */
class Zipper {

    private var mSourceFileList = mutableListOf<File>()
    private var mTargetZipFile:File? = null
    private var mCharset:Charset = StandardCharsets.UTF_8
    private var mOnZipListener: OnZipListener? = null
    
    constructor(filePath:String){
        mSourceFileList.add(File(filePath))
    }
    
    constructor(file:File){
        mSourceFileList.add(file)
    }
    
//    constructor(filePathList:List<String>){
//        filePathList.forEach{
//            mSourceFileList.add(File(it))
//        }
//    }

    constructor(fileList:List<File>){
        fileList.forEach{
            mSourceFileList.add(it)
        }
    }

    companion object{
        fun from(filePath:String): Zipper {
            return Zipper(filePath)
        }

        fun from(file:File): Zipper {
            return Zipper(file)
        }

        @JvmName("fromFilePathList")
        fun from(filePathList:List<String>): Zipper {
            val fileList = mutableListOf<File>()
            filePathList.forEach{
                fileList.add(File(it))
            }
            return Zipper(fileList)
        }
        @JvmName("fromFileList")
        fun from(fileList:List<File>): Zipper {
            return Zipper(fileList)
        }
    }

    fun target(targetZipFilePath:String): Zipper {
        mTargetZipFile = File(targetZipFilePath)
        return this
    }

    fun target(targetZipFile:File):Zipper{
        mTargetZipFile = targetZipFile
        return this
    }

    fun charset(charset:Charset): Zipper {
        mCharset = charset
        return this
    }

    fun listen(listener:OnZipListener): Zipper {
        mOnZipListener = listener
        return this
    }

    fun excetue(){
        mTargetZipFile?:return
        mOnZipListener?.onZipStart()
        val isExists = mSourceFileList.find { it.exists() } != null
        if(isExists){
            try {
                if(mTargetZipFile!!.exists()){
                    mTargetZipFile!!.delete()
                }

                val fileOutputStream = FileOutputStream(mTargetZipFile!!)
                val zipOutputStream = ZipOutputStream(fileOutputStream)
                realZip("",mSourceFileList ,zipOutputStream)
                zipOutputStream.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        mOnZipListener?.onZipEnd()
    }

    private fun realZip(dirName:String, sourceFileList: List<File>, zipOutputStream: ZipOutputStream){
        sourceFileList.forEach { realZip(dirName,it,zipOutputStream) }
    }


    private fun realZip(parentDirName:String, sourceFile:File, zipOutputStream: ZipOutputStream){
        if(!sourceFile.exists())return
        val zipEntry: ZipEntry
        if(sourceFile.isDirectory){
            val dirName = parentDirName + sourceFile.name + File.separator
            zipEntry = ZipEntry(dirName)
            zipOutputStream.putNextEntry(zipEntry)
            val listFiles = sourceFile.listFiles()?.toList()?: emptyList()
            if(listFiles.isNotEmpty()){
                realZip(dirName,listFiles, zipOutputStream)
            }
            return
        }

        zipEntry = ZipEntry(parentDirName + sourceFile.name)
        zipOutputStream.putNextEntry(zipEntry)
        mOnZipListener?.onZipProgress(zipEntry.name)
        val fileInputStream = FileInputStream(sourceFile)
        val buffer = ByteArray(1024)
        var length:Int
        while (fileInputStream.read(buffer).also { length = it } > 0){
            zipOutputStream.write(buffer,0,length)
        }
    }
}