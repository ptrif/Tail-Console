package ru.spbstu.polina

/*
really Important Thing that I need to have
*/
data class InputData (val fileNames: List<String>,
                      val count: Int,
                      val useLines: Boolean,
                      val outputFileName: String?)


interface ReaderI {
    fun read(fileNames: List<String>, count:Int):String
}
interface WriterI {
    fun write(text: String)
}