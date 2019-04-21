package ru.spbstu.polina

import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.RandomAccess

 class FileReader {

     fun read (inputData: InputData) {
        if (inputData.useLines) FileReaderByString()
        if (!inputData.useLines) FileReaderBySymbol()
    }

    val result = mutableListOf<String>()
    val lines = ArrayDeque<String>()
    val symbols = ArrayDeque<Char>()


    // (-n) option
    inner class FileReaderByString : ReaderI {
        override fun read(fileNames: List<String>, count: Int): String {
            for (file in fileNames) {
                var str = StringBuilder()
                val reader = RandomAccessFile(file, "r")
                val size = File(file).length()
                var i = size - 1
                var line = 0
                while (i >= 0 && line < count) {
                    reader.seek(i)
                    val c = reader.read()
                    if (c == '\n'.toInt()) {
                        line++
                        lines.add(str.toString())
                        str = StringBuilder()
                    } else if (c == -1) {
                        lines.add(str.toString())
                        break

                    } else if (c == '\r'.toInt()) {
                        i--
                        continue
                    }
                    str.append(c.toChar())
                    i--
                }
                lines.add(str.toString())
                if (count == lines.size)
                    lines.add("\n")
                if (fileNames.size > 1)
                    lines.add(file.reversed())

                val adding = lines.toTypedArray().joinToString("").reversed()
                result.add(adding)
                result += "\n"
                lines.clear()
            }

            val index = result.size - 1
            result.removeAt(index)
            return result.toTypedArray().joinToString("")
        }
    }

    // for (-c) option
    inner class FileReaderBySymbol : ReaderI {
        override fun read(fileNames: List<String>, count: Int): String {
            for (file in fileNames) {
                val reader = RandomAccessFile(file, "r")
                val size = File(file).length()
                var i = size - 1

                while (i >= 0 && size - i <= count) {
                    reader.seek(i)
                    val c = reader.read()
                    if (c == -1) break
                    symbols.add(c.toChar())
                    i--
                }
                val adding = symbols.toTypedArray().joinToString("")
                lines.add(adding)
                symbols.clear()

                if (fileNames.size > 1) lines.add("$file\r".reversed())
                val adding2 = lines.toTypedArray().joinToString("")
                result.add(adding2)
                result += "\r"
                lines.clear()
            }
            val index = result.size - 1
            result.removeAt(index)
            return result.toTypedArray().joinToString("").reversed()
        }
    }
}