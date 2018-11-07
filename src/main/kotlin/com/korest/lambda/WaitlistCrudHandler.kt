package com.korest.lambda

import com.amazonaws.HttpMethod
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.handler.waitlist.WaitlistDeleteHandler
import com.korest.lambda.handler.waitlist.WaitlistGetHandler
import com.korest.lambda.handler.waitlist.WaitlistPostHandler
import com.korest.lambda.handler.waitlist.WaitlistPutHandler

class WaitlistCrudHandler : CrudHandler() {

    override fun getHttpHandlers(): Map<String, HttpHandler> {
        return httpHandlers
    }

    companion object {
        private const val TABLE_NAME = "waitlistsTableName";

        private val waitlistsTable = DynamoDB(
                AmazonDynamoDBClientBuilder.standard()
                        .withRegion(System.getenv(REGION))
                        .build()
        ).getTable(System.getenv(TABLE_NAME))

        private val httpHandlers = mapOf(
                Pair(HttpMethod.GET.name, WaitlistGetHandler(waitlistsTable)),
                Pair(HttpMethod.POST.name, WaitlistPostHandler(waitlistsTable)),
                Pair(HttpMethod.PUT.name, WaitlistPutHandler(waitlistsTable)),
                Pair(HttpMethod.DELETE.name, WaitlistDeleteHandler(waitlistsTable))
        )
    }
}
