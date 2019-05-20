package ru.spbstu.polina

import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*


/*
 fileReader for -c flag
 returns last num symbols of the file
 */
class FileReaderBySymbol : ReaderI { //change

    override fun read(fileNames: List<String>, count: Int): String {

        val result = mutableListOf<String>()
        val symbols = ArrayDeque<Char>()
        var num = count

        for (file in fileNames) {
            val reader = RandomAccessFile(file, "r") //r - read, rw -read&write
            val size = File(file).length()
            var i = size - 1 //needed byte to jump
            while (i >= 0 && size - i <= num) {
                reader.seek(i) //jump to byte

                val c = reader.read()
                if (c != -1) {
                    symbols += c.toChar()
                    i--
                    if ('\n' == c.toChar())
                        num++
                    continue
                }
                break
            }
            val adding = symbols.toTypedArray()
                                       .joinToString("")

            result += adding
            symbols.clear()

            if (fileNames.size > 1)
                result += "$file\n".reversed()

            result.toTypedArray().joinToString("")
            result += "\n"

        }
        val index = result.size - 1
        result.removeAt(index)

        return result.joinToString("").reversed()
    }
}

/*
 fileReader for -n flag
 returns last num lines of the file
 */
class FileReaderByString : ReaderI {
    override fun read(fileNames: List<String>, count: Int): String {

        val result = mutableListOf<String>()
        val lines = ArrayDeque<String>()

        for (file in fileNames) {
            var str = StringBuilder()
            val reader = RandomAccessFile(file, "r")
            val size = File(file).length()
            var i = size - 1
            var line = 0
            while (i >= 0 && line < count) {
                reader.seek(i)
                val c = reader.read() //readLines
                if (c == -1) {
                    lines += str.toString()
                    break

                } else if (c == '\n'.toInt()) {
                    line++
                    i--
                    lines += str.toString()
                    str = StringBuilder()
                }
                str.append(c.toChar())
                i--
            }
            lines += str.toString()

            if (count == lines.size)
                lines += "\n"
            if (fileNames.size > 1)
                lines += file.reversed()

            val adding = lines.toTypedArray()
                                     .joinToString("")
                                     .reversed()

            result.add(adding)
            result += "\n"
            lines.clear()
        }
        val index = result.size - 1
        result.removeAt(index)
        return result.joinToString("")
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
                newLine.forEach { char ->
                    d += char
                    if (d.size >= num)
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

