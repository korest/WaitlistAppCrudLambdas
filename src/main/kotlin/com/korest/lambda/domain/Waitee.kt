package com.korest.lambda.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Waitee(@JsonProperty(ID) val id: String,
                  @JsonProperty(WAITLIST_ID) val waitlistId: String,
                  @JsonProperty(NAME) val name: String,
                  @JsonProperty(PHONE_NUMBER) val phoneNumber: String,
                  @JsonProperty(NOTIFY_AT) val notifyAt: Long,
                  @JsonProperty(NOTIFIED_AT) val notifiedAt: Long,
                  @JsonProperty(TIME_TO_LIVE) val timeToLive: Long) {
    companion object {
        const val ID = "id"
        const val WAITLIST_ID = "waitlistId"
        const val NAME = "name"
        const val PHONE_NUMBER = "phoneNumber"
        const val NOTIFY_AT = "notifyAt"
        const val NOTIFIED_AT = "notifiedAt"
        const val TIME_TO_LIVE = "timeToLive"
    }
}