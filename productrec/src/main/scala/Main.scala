object Main extends App {

    val sourceFile = args(0)

    val tdm = DataParser.parse(sourceFile)
    
    for ((k,v) <- tdm.matrix) {
        print(k + ": " + v + "\n")
    }

    printf("\nThe data map for %s contains %d terms.\n", sourceFile, tdm.matrix.size)

}