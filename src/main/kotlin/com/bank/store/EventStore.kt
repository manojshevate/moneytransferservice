package com.bank.store

import com.bank.event.Event


// TODO: Use JSON storage in H2
open class EventStore {

    companion object {
        private var store: MutableMap<String, List<Event>> = mutableMapOf()
    }

    open fun fetchAll(id: String): List<Event> {
        return when {
            store.containsKey(id) -> store[id]!!
            else -> emptyList()
        }
    }

    open fun save(event: Event) {
        if(store.containsKey(event.id)) {
            store[event.id] = store[event.id]!!.plus(listOf(event))
        } else {
            store[event.id] = listOf(event)
        }
    }

    fun clearAll() {
        store.clear()
    }

}


