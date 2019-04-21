package ru.spbstu.polina

import org.kohsuke.args4j.Option
import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import java.lang.Exception

class ParserNew(args: List<String>) {

    @Option(name = "-c", metaVar = "num", usage = "Last Symbols")
    var c = -1

    @Option(name = "-n", metaVar = "num", usage = "Last Lines")
    var n = -1

    @Option(name = "-o", metaVar = "oFile", usage = "Output file name")
    var outFileName: String? = null

    @Argument(multiValued = true,usage = "Input fileNames")
    var fileNames: MutableList<String> = mutableListOf()

    init {
        val parser = CmdLineParser(this)
        try {
            parser.parseArgument(args)
        } catch (e: CmdLineException) {
           System.err.println(e.message)
            parser.printUsage(System.err)
        }

        when {
            c == -1 && n == -1 -> n = 10
        }

        when {
            c != -1 && n != -1 -> throw Exception("Flags '-c' and '-n'can not be used at the same time")
        }
    }

    fun parse(): InputData {
        var count = 0
        if (n > 0) {
            count = n
        } else c
        val useLines = n > 0
        return InputData(fileNames, count, useLines, outFileName)
    }

}