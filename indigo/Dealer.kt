package indigo

object Dealer {
    fun dealInitialTableCards(tableCards: CardPile, deck: Deck){
        val cardpile = tableCards.pile
        repeat(4) { cardpile.add(deck.deckOfCards.removeFirst()) }
        println("Initial cards on the table: ${cardpile[0]} ${cardpile[1]} ${cardpile[2]} ${cardpile[3]}")
    }
    fun dealToPlayer(player: Player, deck: Deck){
        val d = deck.deckOfCards
        repeat(6) { player.hand.add(d.removeFirst()) }
    }
    fun givePileToPlayer(player: Player, tableCards: CardPile){
        while (tableCards.pile.isNotEmpty()){
            val card = tableCards.pile.removeLast()
            player.score += card.pointValue
            player.wonCards.add(card)
        }
        player.numOfCardsWon = player.wonCards.size
    }
}