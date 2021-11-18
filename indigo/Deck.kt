package indigo

class Deck {

    val ranks = listOf("A","2","3","4","5","6","7","8","9","10","J","Q","K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val hasValue = listOf("A", "10", "J", "Q", "K")
    val deckOfCards = mutableListOf<Card>()

    inner class Card(val rank: String, val suit: String){
        var pointValue = 0
        private fun setPointValue (){
            if (rank in hasValue) pointValue = 1
        }
        init {
            setPointValue()
        }
        override fun toString(): String{
            return "$rank$suit"
        }
    }
    // build the deck of cards
    private fun buildDeck(){
        for (rank in ranks){
            for (suit in suits){
                deckOfCards.add(Card(rank,suit))
            }
        }
    }

    init {
        buildDeck()
        shuffle(deckOfCards)
    }

//    fun reset(){
//
//        deckOfCards.clear()
//        this.buildDeck()
//        println("Card deck is reset.")
//    }

    private fun shuffle(cardDeck: MutableList<Card>){

        val shuffledDeck = mutableListOf<Card>()
        while(cardDeck.isNotEmpty()){
            val random = (Math.random() * cardDeck.size).toInt()
            shuffledDeck.add(cardDeck.removeAt(random))
        }
        cardDeck.addAll(shuffledDeck)
    }

}