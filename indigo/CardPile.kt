package indigo

object CardPile {
    val pile = mutableListOf<Deck.Card>()
    val topCard = pile.lastOrNull()
}