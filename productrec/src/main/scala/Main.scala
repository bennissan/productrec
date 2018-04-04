object Main extends App {

    val sourceFile = args(0)

    val dataMap = DataParser.parse(sourceFile)
    
    for ((k,v) <- dataMap.map) {
        printf("(%s, %d), ", k, v)
    }

    printf("\nThe data map for %s contains %d terms.\n", sourceFile, dataMap.map.size)

}