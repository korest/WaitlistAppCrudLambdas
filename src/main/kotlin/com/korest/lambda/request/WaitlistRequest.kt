package com.korest.lambda.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WaitlistRequest(@JsonProperty(NAME) val name: String,
                           @JsonProperty(DESCRIPTION) val description: String) {
    companion object {
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}