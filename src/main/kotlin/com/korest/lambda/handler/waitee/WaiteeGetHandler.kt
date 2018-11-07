package com.korest.lambda.handler.waitee

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.NameMap
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.korest.lambda.domain.Waitee
import com.korest.lambda.response.ApiGatewayResponse

class WaiteeGetHandler(waitlistsTable: Table, waiteesTable: Table) : WaiteeHttpHandler(waitlistsTable) {

    companion object {
        const val INDEX_NAME = "waitlistIdIndexName"
    }

    private val waiteesWaitlistIdIndex = waiteesTable.getIndex(System.getenv(INDEX_NAME))

    override fun handleInternal(accountId: String, waitlistId: String,
                                event: AwsProxyRequest): ApiGatewayResponse {
        val querySpec = QuerySpec()
                .withKeyConditionExpression("${Waitee.WAITLIST_ID} = :waitlistId")
                .withProjectionExpression("${Waitee.ID}, #name, ${Waitee.PHONE_NUMBER}, ${Waitee.NOTIFY_AT}")
                .withNameMap(
                        NameMap()
                                .with("#name", Waitee.NAME)
                )
                .withValueMap(
                        ValueMap()
                                .withString(":waitlistId", waitlistId)
                )

        val waitlistsJson = waiteesWaitlistIdIndex.query(querySpec)
                .joinToString(prefix = "[", postfix = "]") {
                    it.toJSON()
                }

        return ApiGatewayResponse.build {
            statusCode = 200
            rawBody = waitlistsJson
        }
    }
}