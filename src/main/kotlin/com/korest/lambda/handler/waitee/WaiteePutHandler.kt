package com.korest.lambda.handler.waitee

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.amazonaws.services.dynamodbv2.model.ReturnValue
import com.korest.lambda.domain.Waitee
import com.korest.lambda.request.WaiteePutRequest
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class WaiteePutHandler(waitlistsTable: Table, val waiteesTable: Table) : WaiteeHttpHandler(waitlistsTable) {
    override fun handleInternal(accountId: String, waitlistId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waiteeRequest = OBJECT_MAPPER.readValue(event.body, WaiteePutRequest::class.java)
        val waiteeId = event.pathParameters?.get(PATH_WAITEE_ID)
                ?: throw IllegalArgumentException("Waitee id is null")

        val notifyAtEpochMillis = ZonedDateTime.now(ZoneOffset.UTC)
                .plusMinutes(waiteeRequest.notifyIn)
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant()
                .toEpochMilli()

        val timeToLive = ZonedDateTime.now(ZoneOffset.UTC)
                .plusHours(24)
                .toInstant()
                .epochSecond

        val updateExpression = StringBuilder("set ${Waitee.NOTIFY_AT} = :notifyAt, ${Waitee.TIME_TO_LIVE} = :timeToLive")
        val valueMap = ValueMap()
                .withLong(":notifyAt", notifyAtEpochMillis)
                .withLong(":timeToLive", timeToLive)
        if (waiteeRequest.notifyIn == 0L) {
            updateExpression.append(", ${Waitee.NOTIFIED_AT} = :notifiedAt")
            valueMap[":notifiedAt"] = Instant.now().toEpochMilli()
        }

        val updateSpec = UpdateItemSpec()
                .withPrimaryKey(Waitee.ID, waiteeId, Waitee.WAITLIST_ID, waitlistId)
                .withUpdateExpression(updateExpression.toString())
                .withValueMap(valueMap)
                .withReturnValues(ReturnValue.NONE)

        waiteesTable.updateItem(updateSpec)

        return ApiGatewayResponse.build {
            statusCode = 201
            objectBody = MessageResponse("Waitee with id: '$waiteeId' was updated")
        }
    }

}