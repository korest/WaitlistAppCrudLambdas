package com.korest.lambda.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WaiteePutRequest(@JsonProperty(NOTIFY_IN) val notifyIn: Long) {
    companion object {
        const val NOTIFY_IN = "notifyIn"
    }
}