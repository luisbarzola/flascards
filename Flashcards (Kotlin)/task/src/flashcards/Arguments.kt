package flashcards

class Arguments(args: Array<String>) {
    private val elements = mutableMapOf<String, String>()

    init {
        val (names, values) = args.partition { it.contains('-') }
        for (i in names.indices) {
            elements[names[i]] = values[i]
        }
    }

    fun importFile() = elements["-import"]
    fun exportFile() = elements["-export"]
}