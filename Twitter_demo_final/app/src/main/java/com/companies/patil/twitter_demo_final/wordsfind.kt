package com.companies.patil.twitter_demo_final

import android.content.pm.PackageManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import java.io.File
import java.util.*

/*
class Dictionary @Throws(IOException::class)
constructor() {
    private val wordsSet: Set<String>


    init {

        /*val path = System.getProperty("user.dir") + "\\res\\words.txt"

        try {
            val encoded = Files.readAllBytes(Paths.get(path))
           // println(Arrays.toString(encoded))

            encoded.split()
            val words = wordListContents.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            wordsSet = HashSet()
            Collections.addAll(wordsSet, *words)
        } catch (e: IOException) {

        }*/

        val array = Files.readAllBytes(File("/path/to/file").toPath())
       val files= File("words.txt")
        val readBytes = Files.readAllBytes(files.toPath())
        val wordListContents = String(readBytes,"UTF-8")
        val words = wordListContents.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        wordsSet = HashSet()
        Collections.addAll(wordsSet, *words)
    }

    operator fun contains(word: String): Boolean {
        return wordsSet.contains(word)
    }
}*/