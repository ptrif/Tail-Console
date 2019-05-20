package ru.spbstu.polina

import java.util.stream.Collectors.toList

/*
  main part of the project
  check the flags && if the name "-o" is given  in original string
  then chooses to use one of the methods to do

*/

// what to do with the Kotlin Version in pom.xml???

fun main(args: Array<String>) {
    val inputData = ParserNew(args.toList()).parse()
    val writer: WriterI
    val reader: ReaderI
    val res:String
    when {//use one reader or 2 - didnt got it
        inputData.fileNames.isEmpty() && inputData.useLines -> reader = ConsoleReaderByString()
        inputData.fileNames.isEmpty() && !inputData.useLines -> reader = ConsoleReaderBySymbol()
        inputData.fileNames.isNotEmpty() && inputData.useLines -> reader = FileReaderByString()
        inputData.fileNames.isNotEmpty() && !inputData.useLines -> reader = FileReaderBySymbol()
        else -> throw Exception("Insert Argument!")
    }

    if (inputData.outputFileName != null) {
        writer = FileWriter(inputData.outputFileName)
    } else
        writer = ConsoleWriter()

    res = reader.read(inputData.fileNames, inputData.count)

    writer.write(res)

}