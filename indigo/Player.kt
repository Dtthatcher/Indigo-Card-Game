package indigo

abstract class Player {
    abstract var hand: MutableList<Deck.Card>
    abstract var wonCards: MutableList<Deck.Card>
    abstract var score: Int
    abstract var numOfCardsWon: Int

    abstract fun addCardToPile(cardPile: CardPile): Boolean
}

class Human: Player() {

    override var hand = mutableListOf<Deck.Card>()
    override var wonCards = mutableListOf<Deck.Card>()
    override var score = 0
    override var numOfCardsWon = wonCards.size

    override fun addCardToPile(cardPile: CardPile): Boolean{
        for ((index, card) in hand.withIndex()){
            when (index) {
                0 -> print("\nCards in hand: 1)$card ")
                else -> print("${index + 1})$card ")
            }
        }
        while (true){
            val handSize = hand.size
            println("\nChoose a card to play (1-$handSize):" )
            val cardIndexToAdd = readLine()!!
            when {
                cardIndexToAdd.matches("[1-$handSize]".toRegex()) -> {
                    cardPile.pile.add(hand.removeAt(cardIndexToAdd.toInt() - 1))
                    break
                }
                cardIndexToAdd == "exit" -> return true
                else -> continue
            }
        }
        return false
    }
}

class NPC: Player() {

    override var hand = mutableListOf<Deck.Card>()
    override var wonCards = mutableListOf<Deck.Card>()
    override var score = 0
    override var numOfCardsWon = wonCards.size

    private fun printHand(){
        for (card in hand) print("$card ")
    }

    override fun addCardToPile(cardPile: CardPile): Boolean{
        printHand()
        // sub hand of all cards matching either suit or rank
        val sameSuitAsPile = hand.filter { it.suit == cardPile.pile.lastOrNull()?.suit }.toMutableList()
        val sameRankAsPile = hand.filter { it.rank == cardPile.pile.lastOrNull()?.rank }.toMutableList()
        // sub hand of hards that have the same rank inside the hand
        val sameRank = mutableListOf<Deck.Card>()
        // sub hands of all cards with matching suits
        val hearts = mutableListOf<Deck.Card>()
        val diamonds = mutableListOf<Deck.Card>()
        val clubs = mutableListOf<Deck.Card>()
        val spades = mutableListOf<Deck.Card>()
        // suits to check against
        val suits = listOf("♦", "♥", "♠", "♣")
        // print statement for when there is only 1 card in hand. add that card to pile
        fun printAndAdd(h: MutableList<Deck.Card>): Deck.Card {
            println("\nComputer plays ${h[0]}")
            val card = h.removeAt(0)
            cardPile.pile.add(card)
            return card
        }

        fun addCardToPile(list: MutableList<Deck.Card>){
            val num = Math.random() * list.size
            val card = list.removeAt(num.toInt())
            cardPile.pile.add(card)
            hand.remove(card)
            println("\nComputer plays $card")

        }
        // fill sub hands with matching suits
        for ((index, suit) in suits.withIndex()){
            for (card in hand){
                when (index) {
                    0 -> { if (card.suit == suit) diamonds.add(card) }
                    1 -> { if (card.suit == suit) hearts.add(card) }
                    2 -> { if (card.suit == suit) spades.add(card) }
                    3 -> { if (card.suit == suit) clubs.add(card) }
                }
            }
        }
        // create a list of subhands to traverse
        val suitMatches = listOf(hearts, diamonds, clubs, spades)
        val sms: MutableList<Deck.Card>
        var max = 0
        var index1 = 0
        // find the sub hand with most matching suits
        for ((index, i) in suitMatches.withIndex()) {
            if (i.size > max) {max = i.size; index1 = index}
        }
        // set its value to the subhand with most matching suits
        sms = suitMatches[index1]
        // fill same rank with all cards that have matching ranks
        for ((index, card) in hand.withIndex()){
            for (c in (index + 1) until hand.size ){
                if (card.rank == hand[c].rank) sameRank.add(card)
            }
        }
        // conditions for playing hands
        when {
            // only 1 card in hand, play that card
            hand.size == 1 -> printAndAdd(hand)
            // 1 matching suit, or one matching rank, or both 1 and 1
            sameRankAsPile.size == 1 && sameSuitAsPile.size == 0 -> {hand.remove(printAndAdd(sameRankAsPile)); return false}
            sameSuitAsPile.size == 1 && sameRankAsPile.size == 0 -> {hand.remove(printAndAdd(sameSuitAsPile)); return false}
            sameSuitAsPile.size == 1 && sameRankAsPile.size == 1 -> {hand.remove(printAndAdd(sameSuitAsPile)); return false}
            // no cards in pile, if multiple matching suits choose one at random, then ranks, else random card from hand
            cardPile.pile.size == 0 -> return when {
                sms.size > 1 -> {addCardToPile(sms); false }
                sameRank.size > 1 -> {addCardToPile(sameRank); false }
                else -> {addCardToPile(hand); false }
            }
            // if muiltiple matching suits as top card add random one, same for ranks
            sameSuitAsPile.size > 1 -> {addCardToPile(sameSuitAsPile); return false}
            sameRankAsPile.size > 1 -> {addCardToPile(sameRankAsPile); return false}
            // if no matching top card then same conditions as empty pile
            cardPile.pile.size > 0 -> return when {
                sms.size > 0 -> {addCardToPile(sms); false }
                sameRank.size > 0 -> {addCardToPile(sameRank); false }
                else -> {addCardToPile(hand); false }
            }
            // anything else add random card from hand
            else -> {addCardToPile(hand); return false}
        }
        return false
    }

}