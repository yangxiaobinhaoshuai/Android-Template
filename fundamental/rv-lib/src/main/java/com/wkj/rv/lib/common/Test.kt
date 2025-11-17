package com.wkj.rv.lib.common

// TODO To be deleted.
class Test {

    fun test() {

        val adapter = smartAdapter {
            register<String>(layoutId = 0) {
                areItemsSame { old, new -> old == new }
                areContentsSame { old, new -> old == new }
                // 如果内容变化只改名字，给一个 payload
                getChangePayload { old, new ->
                    Any()
                }

                onClick { item, position ->

                }
                onLongClick { item, position ->
                    true
                }
                onBind { item, payloads ->

                }
            }
        }
    }

}