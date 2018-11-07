package com.korest.lambda.handler.waitee

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.korest.lambda.domain.Waitlist
import com.korest.lambda.handler.HttpException
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.handler.ResourceNotFoundException
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse

abstract class WaiteeHttpHandler(val waitlistsTable: Table) : HttpHandler() {

    companion object {
        const val PATH_WAITEE_ID = "waiteeId"
    }

    private fun verifyThatWaitlistExists(accountId: String, waitlistId: String): Boolean {
        val querySpec = QuerySpec()
                .withKeyConditionExpression("${Waitlist.ID} = :id and ${Waitlist.ACCOUNT_ID} = :accId")
                .withProjectionExpression(Waitlist.ID)
                .withValueMap(
                        ValueMap()
                                .withString(":id", waitlistId)
                                .withString(":accId", accountId)
                )

        if (!waitlistsTable.query(querySpec).iterator().hasNext()) {
            throw HttpException(ResourceNotFoundException("Waitlist with id: $waitlistId doesn't exist for $accountId"),
                    ApiGatewayResponse.build {
                        statusCode = 404
                        objectBody = MessageResponse("Waitlist with id: '$waitlistId' is not found")
                    }
            )
        }

        return true
    }

    override fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waitlistId = event.pathParameters?.get(PATH_WAITLIST_ID)
                ?: throw IllegalArgumentException("Waitlist id is null")
        verifyThatWaitlistExists(accountId, waitlistId)
        return handleInternal(accountId, waitlistId, event)
    }

    abstract fun handleInternal(accountId: String, waitlistId: String,
                                event: AwsProxyRequest): ApiGatewayResponse
}