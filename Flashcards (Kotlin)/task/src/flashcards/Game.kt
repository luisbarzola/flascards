package flashcards

import java.io.File

class Game(private val arguments: Arguments) {
    private val flashCard = FlashCard()
    private val logs = LogOutput()

    fun play() {
        executeImport()
        while (true) {
            println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            when(readln()) {
                "add" -> createCard()
                "remove" -> removeCard()
                "ask" -> studyCards ()
                "import" -> {
                    println("File name:")
                    importCards(readln())
                }
                "export" -> {
                    println("File name:")
                    exportCards(readln())
                }
                "log" -> logOutput()
                "hardest card" -> hardestCards()
                "reset stats" -> resetStats()
                "exit" -> return finishGame()
            }
        }
    }

    private fun finishGame() {
        println("Bye bye!")
        arguments.exportFile()?.let { exportCards(it) }
    }

    private fun executeImport() {
        arguments.importFile()?.let { importCards(it) }
    }

    private fun resetStats() {
        flashCard.resetStats()
        println("Card statistics have been reset.")
    }

    private fun hardestCards() {
        val moreMistakes = flashCard.moreMistakes()
        if (moreMistakes == null) {
            println("There are no cards with errors.")
        } else {
            val (errors, countErrors) = moreMistakes
            if (errors.count() == 1) {
                println("The hardest card is \"${errors.first()}\". You have $countErrors errors answering it.")
            } else {
                println("The hardest cards are ${errors.joinToString(", ") { "\"$it\"" }}. You have $countErrors errors answering them.")
            }
        }
    }

    private fun println(message: String) = logs.println(message)

    private fun readln() = logs.readln()

    private fun logOutput() {
        println("File name:")
        logs.saveTo(File(readln()))
        println("The log has been saved.")
    }

    private fun exportCards(fileName: String) {
        val cardsSaved = flashCard.export(File(fileName))
        println("$cardsSaved cards have been saved.")
    }

    private fun importCards(fileName: String) {
        val file = File(fileName)
        if (file.exists()) {
            val numberCardAdded = flashCard.import(file)
            println("$numberCardAdded cards have been loaded.")
        } else {
            print("File not found.")
        }
    }

    private fun removeCard() {
        println("Which card?")
        val term = readln()
        if (flashCard.removeByTern(term)) {
            println("The card has been removed.")
        } else {
            println("Can't remove \"$term\": there is no such card.")
        }
    }

    private fun createCard() {
        println("The card:")
        val term = readln()
        if (flashCard.usingTern(term)) {
            println("The card \"$term\" already exists.\n")
            return
        }

        println("The definition of the card:")
        val definition = readln()
        if (flashCard.usingDefinition(definition)) {
            println("The definition \"$definition\" already exists.\n")
            return
        }

        flashCard.addCard(term, definition)
        println("The pair (\"$term\":\"$definition\") has been added.\n")

    }

    private fun studyCards() {
        println("How many times to ask?")
        val askNumber = readln().toInt()
        for (term in flashCard.cards().keys.shuffled().take(askNumber)) {
            println("Print the definition of \"$term\":")
            val definition = readln()
            val message = if (flashCard.isCorrect(term, definition)) {
                "Correct!"
            } else {
                val rightDefinition = flashCard.definitionOf(term)
                var message = "Wrong. The right answer is \"$rightDefinition\""
                val termOfDefinition = flashCard.existDefinition(definition)

                message += if (termOfDefinition.isNotEmpty()) {
                    ", but your definition is correct for \"$termOfDefinition\"."
                } else {
                    "."
                }

                message
            }

            println(message)
        }
    }
}