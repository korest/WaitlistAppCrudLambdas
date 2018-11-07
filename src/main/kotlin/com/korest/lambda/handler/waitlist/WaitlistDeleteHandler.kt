package com.korest.lambda.handler.waitlist

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.korest.lambda.domain.Waitlist
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse

class WaitlistDeleteHandler(val waitlistsTable: Table) : HttpHandler() {

    override fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waitlistId = event.pathParameters?.get(PATH_WAITLIST_ID)
                ?: throw IllegalArgumentException("Waitlist id is null")
        val deleteSpec = DeleteItemSpec()
                .withPrimaryKey(Waitlist.ID, waitlistId, Waitlist.ACCOUNT_ID, accountId)

        waitlistsTable.deleteItem(deleteSpec)

        return ApiGatewayResponse.build {
            statusCode = 204
            objectBody = MessageResponse("Waitlist with id: $waitlistId was deleted")
        }
    }
}