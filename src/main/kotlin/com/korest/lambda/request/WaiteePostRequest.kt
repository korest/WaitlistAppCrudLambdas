package com.korest.lambda.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WaiteePostRequest(@JsonProperty(NAME) val name: String,
                             @JsonProperty(PHONE_NUMBER) val phoneNumber: String,
                             @JsonProperty(NOTIFY_IN) val notifyIn: Long) {
    companion object {
        const val NAME = "name"
        const val PHONE_NUMBER = "phoneNumber"
        const val NOTIFY_IN = "notifyIn"
    }
}