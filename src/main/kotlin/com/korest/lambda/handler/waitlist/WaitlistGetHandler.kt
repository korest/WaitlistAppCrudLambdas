package com.korest.lambda.handler.waitlist

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.NameMap
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.korest.lambda.domain.Waitlist
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.response.ApiGatewayResponse


class WaitlistGetHandler(val waitlistsTable: Table) : HttpHandler() {

    companion object {
        const val INDEX_NAME = "accountIdIndexName"
    }

    private val waitlistsAccountIdIndex = waitlistsTable.getIndex(System.getenv(INDEX_NAME))

    override fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val querySpec = QuerySpec()
                .withKeyConditionExpression("${Waitlist.ACCOUNT_ID} = :accId")
                .withProjectionExpression("${Waitlist.ID}, #name, ${Waitlist.DESCRIPTION}")
                .withNameMap(
                        NameMap()
                                .with("#name", Waitlist.NAME)
                )
                .withValueMap(
                        ValueMap()
                                .withString(":accId", accountId)
                )

        val waitlistsJson = waitlistsAccountIdIndex.query(querySpec)
                .joinToString(prefix = "[", postfix = "]") {
                    it.toJSON()
                }

        return ApiGatewayResponse.build {
            statusCode = 200
            rawBody = waitlistsJson
        }
    }

}