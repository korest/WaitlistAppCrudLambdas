package com.korest.lambda.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Waitlist(@JsonProperty(ID) val id: String,
                    @JsonProperty(ACCOUNT_ID) val accountId: String,
                    @JsonProperty(NAME) val name: String,
                    @JsonProperty(DESCRIPTION) val description: String = "") {
    companion object {
        const val ID = "id"
        const val ACCOUNT_ID = "accountId"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}