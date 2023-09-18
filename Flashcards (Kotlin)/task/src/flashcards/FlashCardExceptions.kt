package flashcards

open class FlashCardExceptions(message: String): Exception(message)

class FlashCardTermExist(term: String): FlashCardExceptions("The card \"$term\" already exists.")
class FlashCardTDefinitionExist(definition: String): FlashCardExceptions("The definition \"$definition\" already exists.")