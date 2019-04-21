package ru.spbstu.polina

import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemOutRule
import org.junit.contrib.java.lang.system.TextFromStandardInputStream
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class TailAutoTests {

    @Test
    fun parserTest() {

        var parser = ParserNew(listOf("-c","5", "inputText1.txt", "inputKotlinNum.txt").toList())

        var data = parser.parse()

        assertEquals(listOf("inputText1.txt", "inputKotlinNum.txt"), data.fileNames)

        parser = ParserNew(listOf("-n", "2", "inputText.txt", "inputKotl.txt").toList())
        data = parser.parse()

        assertEquals(listOf("inputText.txt", "inputKotl.txt"), data.fileNames)


    }


    @Test
    fun consoleWriterTest() {
        val writer = ConsoleWriter()
        writer.write("123456789")
        var result = "123456789"
        assertEquals(result, "123456789")

        writer.write("mamma mia!")
        result = "mamma mia!"
        assertEquals(result, "mamma mia!")
    }

    @Test
    fun fileWriterTest() {
        val fileName = "output.txt"
        val writer = FileWriter(fileName)
        writer.write("TEST Text" + "\n" + "Next Line")
        val text = ArrayList<String>()

        for (line in File("output.txt").readLines()) {
            text.add(line)
        }
        val res = text.joinToString("\n")
        assertEquals(res, "TEST Text\nNext Line")
    }


    class ReaderTests {
        private val file = "src/test/testresourses/input/inputKotl"
        private val file1 = "src/test/testresourses/input/inputText1"
        @Rule
        @JvmField
        val systemOut = SystemOutRule().enableLog()!!
        @Rule
        @JvmField
        val systemIn = TextFromStandardInputStream.emptyStandardInputStream()!! //replace console usement

        @Test

        fun fileReaderByStringTest() {
            val reader = FileReaderByString()
            val files = listOf(file, file1)
            val result = "$file\nkotlin2\nkotlin3 kotlin4\n$file1\n1234\n12345"
            assertEquals(result, reader.read(files, 2))
        }

        private val file3 = "src/test/testresourses/input/math"
        @Test
        fun fileReaderBySymbolTest() {
            val reader = FileReaderBySymbol()
            val files = listOf(file, file1, file3)
            val result = "$file3\n88\n$file1\n45\n$file\nn4"
            assertEquals(result, reader.read(files, 2))
        }

        @Test

        fun consoleReaderByStringTest() {
            val reader = ConsoleReaderByString()
            systemIn.provideLines("vrf", "mario cart", "mamma mia!")
            ConsoleWriter().write(reader.read(listOf(), 2))
            assertEquals("mario cart" + System.lineSeparator()
                                + "mamma mia!" + System.lineSeparator(), systemOut.log)

        }


        @Test
        fun consoleReaderBySymbolTest() {
            val reader = ConsoleReaderBySymbol()
            systemIn.provideLines("1", "23", "98765")
            ConsoleWriter().write(reader.read(listOf(), 5))
            assertEquals("\n98765" + System.lineSeparator(), systemOut.log)
        }
    }

}




