package com.korest.lambda.handler.waitlist

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.document.utils.NameMap
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.amazonaws.services.dynamodbv2.model.ReturnValue
import com.korest.lambda.domain.Waitlist
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.request.WaitlistRequest
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse

class WaitlistPutHandler(val waitlistsTable: Table) : HttpHandler() {
    override fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waitlistId = event.pathParameters?.get(PATH_WAITLIST_ID)
                ?: throw IllegalArgumentException("Waitlist id is null")
        val waitlistRequest = OBJECT_MAPPER.readValue(event.body, WaitlistRequest::class.java)

        val updateSpec = UpdateItemSpec()
                .withPrimaryKey(Waitlist.ID, waitlistId, Waitlist.ACCOUNT_ID, accountId)
                .withUpdateExpression("set #name = :name, ${Waitlist.DESCRIPTION} = :desc")
                .withNameMap(
                        NameMap()
                                .with("#name", Waitlist.NAME)
                )
                .withValueMap(
                        ValueMap()
                                .withString(":name", waitlistRequest.name)
                                .withString(":desc", waitlistRequest.description)
                )
                .withReturnValues(ReturnValue.NONE)

        waitlistsTable.updateItem(updateSpec)

        return ApiGatewayResponse.build {
            statusCode = 204
            objectBody = MessageResponse("Waitlist with id: '$waitlistId' was updated")
        }
    }

}