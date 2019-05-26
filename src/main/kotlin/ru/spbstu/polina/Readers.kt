package ru.spbstu.polina

import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*


/*
 fileReader for -c flag
 returns last num symbols of the file
 */
class FileReaderBySymbol : ReaderI {

    override fun read(fileNames: List<String>, count: Int): String {

        val result = mutableListOf<String>()
        val symbols = ArrayDeque<Char>()
        var num: Long

        for (file in fileNames) {
            val reader = RandomAccessFile(file, "r") //r - read
            val size = File(file).length()
            num = size - (count-1).toLong() //needed byte to jump
            while (num >= 0 && size - num <= count) {
                reader.seek(num) //jump to byte

                val c = reader.read()
                if (c != -1) {
                    symbols.add(c.toChar())
                    num--//next byte, right direction
                    if (c.toChar() == '\n')
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
        var num: Long

        for (file in fileNames) {
            var str = StringBuilder()
            val reader = RandomAccessFile(file, "r")
            val size = File(file).length()
            num = size - (count-1).toLong() //needed byte to jump
            var line = 0
            while (num >= 0 && line < count) {
                reader.seek(num)
                val c = reader.read() //readLines
                if (c != -1) {
                    if (c == '\n'.toInt()) {
                        line++
                        num--
                        lines += str.toString()
                        str = StringBuilder()
                    }
                } else {
                    lines += str.toString()
                    break

                }
                str.append(c.toChar())
                num--
            }
            lines += str.toString()

            if (count == lines.size)
                lines += "\n"
            if (fileNames.size > 1)
                lines += file.reversed()

            val adding = lines.toTypedArray()
                    .joinToString("").reversed()


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

