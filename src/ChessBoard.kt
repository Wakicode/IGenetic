import kotlin.random.Random

class ChessBoard : IMutable<MutableList<ChessPiece>,Double> {
    override val geneset = mutableListOf(
            ChessPiece("♜",ChessPieceColor.BLACK,11),
            ChessPiece("♞",ChessPieceColor.BLACK,13),
            ChessPiece("♝",ChessPieceColor.BLACK,15),
            ChessPiece("♛",ChessPieceColor.BLACK,16),
            ChessPiece("♚",ChessPieceColor.BLACK,18),
            ChessPiece("♝",ChessPieceColor.BLACK,15),
            ChessPiece("♞",ChessPieceColor.BLACK,13),
            ChessPiece("♜",ChessPieceColor.BLACK,11),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♟",ChessPieceColor.BLACK,4),
            ChessPiece("♖",ChessPieceColor.WHITE,10),
            ChessPiece("♘",ChessPieceColor.WHITE,12),
            ChessPiece("♗",ChessPieceColor.WHITE,14),
            ChessPiece("♕",ChessPieceColor.WHITE,17),
            ChessPiece("♔",ChessPieceColor.WHITE,19),
            ChessPiece("♗",ChessPieceColor.WHITE,14),
            ChessPiece("♘",ChessPieceColor.WHITE,12),
            ChessPiece("♖",ChessPieceColor.WHITE,10),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5),
            ChessPiece("♙",ChessPieceColor.WHITE,5))

    init {

        geneset.addAll((1..32).map { ChessPiece("□",ChessPieceColor.EMPTY,0) })
    }

    override val target: Double = 64.0
    override var bestParent = createParent(0)
    override var bestFitness: Double = 0.0
    private val values = mutableListOf(11,13,15,16,18,15,13,11,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,10,12,14,17,19,14,12,10)

    override fun isSuccess(): Boolean = bestFitness == 1.0

    var result=""
    override fun start() {
        bestFitness = getFitness(bestParent)
        var childFitness: Double
        var child: MutableList<ChessPiece>
        var attemps=0

        do {
            do {
                child = mutate(bestParent)
                childFitness = getFitness(child)

            } while (bestFitness > childFitness)

            bestFitness = childFitness
            bestParent = child
            result +=toShortString() + "\n\n\n"
            attemps ++
            println(toShortString() + "\n")
        } while (!isSuccess())
        println("Nº de intentos: $attemps")
    }

    override fun createParent(param: Any): MutableList<ChessPiece> {
        // llena el tablero con las 32 piezas + 32 vacias
        return geneset.shuffled().toMutableList()
    }

    override fun getFitness(obj: MutableList<ChessPiece>): Double = obj.mapIndexed { index, chessPiece -> if (values[index] == chessPiece.value) 1.0 else 0.0  }.average()


    override fun mutate(parent: MutableList<ChessPiece>): MutableList<ChessPiece> {
        val parentFitness: Double = getFitness(parent)
        var childFitness: Double
        val child = mutableListOf<ChessPiece>()

        do {
            child.clear()
            child.addAll(parent)

            val startIndex = Random.nextInt(bestParent.size)
            val endIndex = Random.nextInt(bestParent.size)
            val start = parent[startIndex]
            val end = parent[endIndex]
            child[startIndex] = end
            child[endIndex] = start
            childFitness = getFitness(child)

        }while (childFitness<=parentFitness)
        return child
    }

    override fun toString(): String = "${bestParent
            .mapIndexed { index, chessPiece -> chessPiece.toString() + if ((index+1) % 8 ==0) "\n" else "\t" }
            .joinToString("")}\nFitness: $bestFitness"

    private fun toShortString(): String ="${bestParent
                    .mapIndexed { index, chessPiece -> "${if (chessPiece.value==0) "▢" else chessPiece.toString()}${if ((index+1) % 8 == 0) "\n" else "\t"}" }
                    .joinToString("")
        }\nFitness: ${"%.3f".format(bestFitness)}"

}