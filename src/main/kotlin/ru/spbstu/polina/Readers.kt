@file:Suppress("DEPRECATION")

package ru.spbstu.polina

import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*


/*
 fileReader for -c flag
 returns last num symbols of the file
 */
class FileReaderBySymbol : ReaderI { //выводит то что надо, но порядок файлов не тот

    override fun read(fileNames: List<String>, count: Int): String {
        val result = mutableListOf<String>()
        var num: Long

        for (file in fileNames) {
            val symbols = ArrayDeque<Char>()
            val reader = RandomAccessFile(file, "r")
            val size = File(file).length()
            num = size - (count).toLong() //needed byte to jump
            reader.seek(num)

            while (num >= 0 && size - num <= count) {
                val c = reader.read()
                if (c != -1) {
                    symbols.add(c.toChar())
                    num++
                    if (c.toChar() == '\n')
                        num--
                    continue
                }
                break
            }
            val adding = symbols.joinToString("")

            result.add("$file\n")
            result.add(adding)
            result.add("\n")
            symbols.clear()
        }
        val index = result.size - 1
        result.removeAt(index)

        return result.joinToString("")
    }
}

/*
 fileReader for -n flag
 returns last num lines of the file
 */
class FileReaderByString : ReaderI {

    override fun read(fileNames: List<String>, count: Int): String {
        val lines = ArrayDeque<String>()

        for (file in fileNames) {
            val reader = File(file).bufferedReader()
            var str = StringBuilder()
            val allLines = reader.readLines()
            val size = allLines.size
            var lineNumber = size - count
            lines+=file

            while (lineNumber != size) {
                val line = allLines[lineNumber]
                lineNumber++
                str.append(line)
                lines += str.toString()

                str = StringBuilder()
            }
        }
        return lines.joinToString("\n")
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
            if (newLine != null) {
                for (char in newLine) {
                    d += char
                    if (d.size == num)
                        d.removeFirst()
                }
                d.addLast('\n')
                num++
                continue
            }
            break
        }
        d.removeLast()
        return d.joinToString("")
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
            if (newLine != null) {
                if (lines.size == count)
                    lines.removeFirst()
                lines += newLine
                continue
            }
            break
        }
        val sep = System.lineSeparator()
        return lines.joinToString(sep)
    }
}

