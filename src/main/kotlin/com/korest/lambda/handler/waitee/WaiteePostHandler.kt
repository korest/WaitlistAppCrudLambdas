package com.korest.lambda.handler.waitee

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.korest.lambda.domain.Waitee
import com.korest.lambda.request.WaiteePostRequest
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class WaiteePostHandler(waitlistsTable: Table, val waiteesTable: Table) : WaiteeHttpHandler(waitlistsTable) {
    override fun handleInternal(accountId: String, waitlistId: String, event: AwsProxyRequest): ApiGatewayResponse {
        val waiteeRequest = OBJECT_MAPPER.readValue(event.body, WaiteePostRequest::class.java)
        val waiteeId = UUID.randomUUID().toString().substring(0, 8) // eight characters ID

        val notifyAtEpochMillis = ZonedDateTime.now(ZoneOffset.UTC)
                .plusMinutes(waiteeRequest.notifyIn)
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant()
                .toEpochMilli()

        val timeToLive = ZonedDateTime.now(ZoneOffset.UTC)
                .plusHours(24)
                .toInstant()
                .epochSecond

        val putSpec = Item().withPrimaryKey(Waitee.ID, waiteeId, Waitee.WAITLIST_ID, waitlistId)
                .withString(Waitee.NAME, waiteeRequest.name)
                .withString(Waitee.PHONE_NUMBER, waiteeRequest.phoneNumber)
                .withLong(Waitee.NOTIFY_AT, notifyAtEpochMillis)
                .withLong(Waitee.TIME_TO_LIVE, timeToLive)

        waiteesTable.putItem(putSpec)

        return ApiGatewayResponse.build {
            statusCode = 201
            objectBody = MessageResponse("Waitee with id: '$waiteeId' was created")
        }
    }

}