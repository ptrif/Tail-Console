package ru.spbstu.polina

import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*

class FileReaderBySymbol : ReaderI {

    override fun read(fileNames: List<String>, count: Int): String {

        val result = mutableListOf<String>()
        val lines = ArrayDeque<String>()
        val symbols = ArrayDeque<Char>() //eeehh, what happened to you???
        var num = count

        fileNames.forEach { file ->
            val reader = RandomAccessFile(file, "r") //r - read, rw -read&write
            val size = File(file).length()
            var i = size - 1 //needed byte to jump
            while (i >= 0 && size - i <= num) {
                reader.seek(i) //jump to byte

                val c = reader.read()
                if (c != -1) {
                    symbols.add(c.toChar())
                    i--
                    if ('\n' == c.toChar() || '\r' == c.toChar())
                        num++
                    continue
                }
                break
            }
            val adding = symbols.toTypedArray()
                                .joinToString("")

            lines.add(adding)
            symbols.clear()

            if (fileNames.size > 1)
                lines.add("$file\n".reversed())
            val adding2 = lines.toTypedArray()
                               .joinToString("")
            result.add(adding2)
            result += "\n"

            lines.clear()
        }
        val index = result.size - 1
        result.removeAt(index)

        return result.toTypedArray()
                     .joinToString("")
                     .reversed()
    }
}

class FileReaderByString : ReaderI {
    override fun read(fileNames: List<String>, count: Int): String {

        val result = mutableListOf<String>()
        val lines = ArrayDeque<String>()

        fileNames.forEach { file ->
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


/*
 console string reader by Symbol
 for -c flag
 count == c
 returns a substring of last symbols in console
*/
class ConsoleReaderBySymbol : ReaderI {


    override fun read(fileNames: List<String>, count: Int): String {
        var newLine: String
        val d = ArrayDeque<Char>()
        var num = count
        val input = Scanner(System.`in`)

        while (input.hasNext()) {
            newLine = input.nextLine()
            if (!newLine.isBlank()) {
                for (char in newLine) {
                    d.add(char)
                    if (d.size >= num) d.removeFirst()
                }
                d.addLast('\n')
                num++
                continue
            }
            break
        }
        d.removeLast()
        return d.toTypedArray().joinToString("")
    }

}

/*
 console string reader by String
 for -n flag
 returns a sublist of last lines in console

*/
class ConsoleReaderByString : ReaderI {

    override fun read(fileNames: List<String>, count: Int): String {
        var newLine: String
        val lines = ArrayDeque<String>()
        val input = Scanner(System.`in`)

        while (input.hasNextLine()) {
            newLine = input.nextLine()
            if (newLine.isNotBlank()) {
                if (lines.size == count)
                    lines.removeFirst()
                lines.add(newLine)
                continue
            }
            break
        }
        val sep = System.lineSeparator()
        return lines.toTypedArray().joinToString(sep)

    }


}