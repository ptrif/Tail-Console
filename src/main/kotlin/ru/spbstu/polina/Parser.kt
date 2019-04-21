package ru.spbstu.polina

import java.util.*

/*
  Parses the original string and Checks arguments
  flags are written here
  //need it as a class
  flags:
  -o (ofile) - name of the OutputFile -> if doesn't exist write in console
  -c num - where num is Int -> we need to output last num symbols from the file
  -n num - where num is Int -> we need to output last num strings from the file
  if no -c or -n output last 10 strings
  if -c & -n exist at the same time == ERROR
*/


class Parser {


    fun parse(args: Array<String>): InputData {
        val fileNames = ArrayList<String>()
        var outputFileName: String? = null

        var c = 0
        var n = 0
        var i = 0

        while (args.lastIndex > i) {
            when {
                args[i] == "fileN" -> (i + 1..args.lastIndex)
                        .asSequence()
                        .takeWhile { !(args[it] == "n" || args[it] == "c" || args[it] == "o") } //no need
                        .forEach { fileNames.add(args[it]) }
                args[i] == "-c" -> c = args[i + 1].toInt()
                args[i] == "-n" -> n = args[i + 1].toInt()
                args[i] == "-o" -> outputFileName = args[i + 1]
            }
            i++
        }

        when {
            // check same time existence with -c and -n flags

            c != 0 && n != 0 -> throw Exception("ERROR: Flags -c and -n can't be used in the same time! ")

            //check if neither -n nor -c exist in original string

            c == 0 && n == 0 -> n = 10
        }

        val count = if (c <= 0) n else c // if (c <= 0) count=n else count = c
        val useLines = n > 0 //if exist -> true => in  Tail main use AnythingReaderByString


        return InputData(fileNames, count, useLines, outputFileName)
    }


}

