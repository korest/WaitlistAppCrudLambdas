package com.korest.lambda.handler.waitee

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.korest.lambda.domain.Waitee
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse

class WaiteeDeleteHandler(waitlistsTable: Table, val waiteesTable: Table) : WaiteeHttpHandler(waitlistsTable) {
    override fun handleInternal(accountId: String, waitlistId: String,
                                event: AwsProxyRequest): ApiGatewayResponse {
        val waiteeId = event.pathParameters?.get(PATH_WAITEE_ID)
                ?: throw IllegalArgumentException("Waitee id is null")
        val deleteSpec = DeleteItemSpec()
                .withPrimaryKey(Waitee.ID, waiteeId, Waitee.WAITLIST_ID, waitlistId)

        waiteesTable.deleteItem(deleteSpec)

        return ApiGatewayResponse.build {
            statusCode = 204
            objectBody = MessageResponse("Waitee with id: $waiteeId was deleted")
        }
    }
}