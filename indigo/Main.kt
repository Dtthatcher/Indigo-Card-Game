package indigo

fun main() {

    val deck = Deck()
    val david = Human()
    val danielle = NPC()
    val jack = Dealer
    val cardPile = CardPile
    val listOfPlayers = arrayOf(david, danielle)
    val game = Game(deck, listOfPlayers, cardPile, jack)
    game.play()
}