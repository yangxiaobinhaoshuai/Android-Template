package me.yangxiaobin.kotlin.codelab.design_pattern


interface Producer {
    suspend fun produce(consumer: Consumer)
}

open class ProducerNode(p: Producer) : Producer by p

interface Consumer {
    suspend fun consume()
}


fun assembleAsync(
    vararg nodes: (Producer) -> ProducerNode,
    finalNode: () -> Producer,
): Producer = nodes.fold(finalNode()) { acc: Producer, p: (Producer) -> ProducerNode -> p.invoke(acc) }
