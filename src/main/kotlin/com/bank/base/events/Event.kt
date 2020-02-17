package com.bank.base.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type_info")
@JsonSubTypes(
    JsonSubTypes.Type(value = AccountCreatedEvent::class, name = "account_created"),
    JsonSubTypes.Type(value = MoneyCreditedEvent::class, name = "money_credited"),
    JsonSubTypes.Type(value = MoneyDeductedEvent::class, name = "money_debited")
)
abstract class Event(val id: String)