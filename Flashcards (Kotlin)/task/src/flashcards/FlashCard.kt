package flashcards

import java.io.File

class FlashCard {
    private val cards = mutableMapOf<String, Pair<String, Int>>()

    fun addCard(term: String, definition: String, mistakes: Int = 0) = run { cards[term] = Pair(definition, mistakes) }

    fun assertExistTern(term: String) =
        if (usingTern(term)) throw FlashCardTermExist(term) else String

    fun assertExistDefinition(definition: String) =
        if (usingDefinition(definition)) throw FlashCardTDefinitionExist(definition) else String

    fun usingTern(term: String) = cards.containsKey(term)

    fun usingDefinition(definition: String) = cards.values.any { it.first == definition }

    fun isCorrect(term: String, definition: String): Boolean {
        val correctValues = cards.getValue(term)
        val isCorrect = correctValues.first == definition
        if (!isCorrect) {
            cards[term] = Pair(correctValues.first, correctValues.second + 1)
        }
        return isCorrect
    }

    fun definitionOf(term: String) = cards[term]?.first ?: ""

    fun cards(): Map<String, Pair<String, Int>> = cards

    fun existDefinition(definition: String): String =
        cards.entries.findLast { definition == it.value.first }?.key ?: ""

    fun removeByTern(term: String): Boolean = cards.remove(term) != null

    fun moreMistakes(): Pair<List<String>, Int>? {
        if (cards.isEmpty()) {
            return null
        }

        val moreMistake = cards.entries.maxByOrNull { it.value.second }!!

        if (moreMistake.value.second == 0) {
            return null
        }

        return Pair(
            cards.keys.filter { cards.getValue(it).second == moreMistake.value.second },
            moreMistake.value.second
        )
    }

    fun import(file: File): Int {
        file.forEachLine {
            val (term, definition, mistakes) = it.split("::")
            addCard(term, definition, mistakes.toInt())
        }

        return file.readLines().size
    }

    fun export(file: File): Int {
        file.writeText("")
        for (entry in cards.entries) {
            file.appendText("${entry.key}::${entry.value.first}::${entry.value.second}\n")
        }

        return cards.size
    }

    fun resetStats() {
        for ((tern, value) in cards.entries) {
            cards[tern] = Pair(value.first, 0)
        }
    }
}
