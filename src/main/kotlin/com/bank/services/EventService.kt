package com.bank.services

import com.bank.event.Event
import com.google.common.eventbus.EventBus

open class EventService(private val eventBus: EventBus) {
    open fun send(event: Event) = eventBus.post(event)
}
