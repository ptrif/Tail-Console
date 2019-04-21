package ru.spbstu.polina

import java.io.File

/*
writers don't depend on flag type
they just write down anything that needs to be written down
super simple classes
 */

class ConsoleWriter : WriterI{
    override fun write(text: String) {
        println(text)
    }
}


class FileWriter(private val outputFileName: String) : WriterI {

    override fun write(text: String) {
        val outputStream = File(outputFileName).bufferedWriter()
        outputStream.write(text)
        outputStream.close()
    }
}