package indigo

class Game (private val deck: Deck, playerList: Array<Player>,private val tableCards: CardPile, private val dealer: Dealer){

    private var human1Turn = true
    private val human = playerList[0]
    private val npc = playerList[1]
    private lateinit var firstPlayer: Player
    private lateinit var wonLastRound: Player


    private fun printTitle(){
        println("Indigo Card Game")

        while(true) {
            println("Play first?")
            when (readLine()!!) {
                "no" -> { human1Turn = false; firstPlayer = npc; break }
                "yes" -> { human1Turn = true; firstPlayer = human; break }
                else -> continue
            }
        }
    }

    private fun printCardsOnTable(){
        val tcs = tableCards.pile.size

        if (tcs == 0) println("No cards on the table")
        else {
            val topCard = tableCards.pile.last()
            println("$tcs cards on the table, and the top card is $topCard")
        }
    }

    private fun printScore(){
        val score = """
            Score: Player ${human.score} - Computer ${npc.score}
            Cards: Player ${human.numOfCardsWon} - Computer ${npc.numOfCardsWon}
        """.trimIndent()

        when (human1Turn){
            true -> {
                println("Player wins cards")
                println(score)
            }
            false -> {
                println("Computer wins cards")
                println(score)
            }
        }
    }

    private fun printFinalScore(){
        val score = """
            Score: Player ${human.score} - Computer ${npc.score}
            Cards: Player ${human.numOfCardsWon} - Computer ${npc.numOfCardsWon}
        """.trimIndent()
        println(score)
    }

    private fun bonusPointsCheck(){
        when {
            human.numOfCardsWon > npc.numOfCardsWon -> human.score += 3
            human.numOfCardsWon == npc.numOfCardsWon -> firstPlayer.score += 3
            else -> npc.score += 3
        }

    }

    private fun finalCardsCheck(){
        if (tableCards.pile.size > 0){
            dealer.givePileToPlayer(wonLastRound,tableCards)
        }
    }

    private fun redealCheck(){
        if (human.hand.isEmpty()
            && npc.hand.isEmpty()
            && deck.deckOfCards.isNotEmpty()) {
                dealer.dealToPlayer(human, deck)
                dealer.dealToPlayer(npc, deck)
        }
    }

    private fun checkForAllCardsOnTable(tableCards: CardPile): Boolean{
        if (tableCards.pile.size == 52 ) return true
        return false
    }

    private fun checkForRoundWin(): Boolean{
        val topCard = tableCards.pile.last()
        val prevTopCard = tableCards.pile[tableCards.pile.lastIndex - 1]
        if (topCard.rank == prevTopCard.rank || topCard.suit == prevTopCard.suit){
            return true
        }
        return false
    }

    private fun checkForGameOver(): Boolean{
        if (
            human.hand.isEmpty()
            && npc.hand.isEmpty()
            && deck.deckOfCards.isEmpty()
        ) return true
        return false
    }

    fun play(){
        playLoop@while(true){
            when (human1Turn){
                true -> {
                    printCardsOnTable()
                    if (human.addCardToPile(tableCards)) {println("Game Over"); break@playLoop}
                    if (tableCards.pile.size > 1) {
                        if (checkForRoundWin()) {
                            dealer.givePileToPlayer(human, tableCards)
                            wonLastRound = human
                            printScore()
                        }
                    }
                    redealCheck()
                    human1Turn = !human1Turn
                    if (checkForAllCardsOnTable(tableCards)){
                        printCardsOnTable()
                        println("Game Over")
                        bonusPointsCheck()
                        printScore()
                        break@playLoop
                    }
                    if (checkForGameOver()){
                        printCardsOnTable()
                        bonusPointsCheck()
                        finalCardsCheck()
                        printFinalScore()
                        println("Game Over")
                        break@playLoop
                    }
                }
                false -> {
                    printCardsOnTable()
                    npc.addCardToPile(tableCards)
                    if (tableCards.pile.isNotEmpty() && tableCards.pile.size > 1) {
                        if (checkForRoundWin()) {
                            dealer.givePileToPlayer(npc, tableCards)
                            wonLastRound = npc
                            printScore()
                        }
                    }
                    redealCheck()
                    human1Turn = !human1Turn
                    if (checkForAllCardsOnTable(tableCards)){ //see if the pile has all cards
                        printCardsOnTable()
                        bonusPointsCheck()
                        println("Game Over")
                        break@playLoop
                    }
                    if (checkForGameOver()){
                        printCardsOnTable()
                        bonusPointsCheck()
                        finalCardsCheck()
                        printFinalScore()
                        println("Game Over")
                        break@playLoop
                    }
                }
            }
        }
    }

    init {
        printTitle()
        dealer.dealInitialTableCards(tableCards, deck)
        for (i in playerList) dealer.dealToPlayer(i, deck)
    }
}