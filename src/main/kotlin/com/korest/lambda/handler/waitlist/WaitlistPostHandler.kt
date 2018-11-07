package com.korest.lambda.handler.waitlist

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.korest.lambda.domain.Waitlist
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.request.WaitlistRequest
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse
import java.util.*

class WaitlistPostHandler(val waitlistsTable: Table) : HttpHandler() {

    override fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waitlistRequest = OBJECT_MAPPER.readValue(event.body, WaitlistRequest::class.java)
        val waitlistId = UUID.randomUUID().toString().substring(0, 8) // eight characters ID

        val putSpec = Item().withPrimaryKey(Waitlist.ID, waitlistId, Waitlist.ACCOUNT_ID, accountId)
                .withString(Waitlist.NAME, waitlistRequest.name)
                .withString(Waitlist.DESCRIPTION, waitlistRequest.description)
        waitlistsTable.putItem(putSpec)

        return ApiGatewayResponse.build {
            statusCode = 201
            objectBody = MessageResponse("Waitlist with id: '$waitlistId' was created")
        }
    }

}