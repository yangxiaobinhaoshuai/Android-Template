package com.wkj.rv.lib.common

// TODO To be deleted.
class Test {

    fun test() {

        val adapter = smartAdapter {
            register<String>(layoutId = 0) {

                getChangePayload

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