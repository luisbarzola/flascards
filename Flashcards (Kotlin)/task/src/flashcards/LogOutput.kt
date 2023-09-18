package flashcards

import java.io.File

class LogOutput {
    private var output = ""

    fun println(message: String) {
        output += "$message\n"
        kotlin.io.println(message)
    }

    fun readln(): String {
        val input = kotlin.io.readln()
        output += "$input\n"
        return input
    }

    fun saveTo(file: File) {
        file.writeText(output)
    }
}