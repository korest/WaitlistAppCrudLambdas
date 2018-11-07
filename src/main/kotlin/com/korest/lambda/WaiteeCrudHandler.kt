package com.korest.lambda

import com.amazonaws.HttpMethod
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.handler.waitee.WaiteeDeleteHandler
import com.korest.lambda.handler.waitee.WaiteeGetHandler
import com.korest.lambda.handler.waitee.WaiteePostHandler
import com.korest.lambda.handler.waitee.WaiteePutHandler

class WaiteeCrudHandler : CrudHandler() {

    override fun getHttpHandlers(): Map<String, HttpHandler> {
        return httpHandlers
    }

    companion object {
        private const val WAITLISTS_TABLE_NAME = "waitlistsTableName"
        private const val WAITEES_TABLE_NAME = "waiteesTableName"

        private val waitlistsTable = DynamoDB(
                AmazonDynamoDBClientBuilder.standard()
                        .withRegion(System.getenv(REGION))
                        .build()
        ).getTable(System.getenv(WAITLISTS_TABLE_NAME))

        private val waiteesTable = DynamoDB(
                AmazonDynamoDBClientBuilder.standard()
                        .withRegion(System.getenv(REGION))
                        .build()
        ).getTable(System.getenv(WAITEES_TABLE_NAME))

        private val httpHandlers = mapOf(
                Pair(HttpMethod.GET.name, WaiteeGetHandler(waitlistsTable, waiteesTable)),
                Pair(HttpMethod.POST.name, WaiteePostHandler(waitlistsTable, waiteesTable)),
                Pair(HttpMethod.PUT.name, WaiteePutHandler(waitlistsTable, waiteesTable)),
                Pair(HttpMethod.DELETE.name, WaiteeDeleteHandler(waitlistsTable, waiteesTable))
        )
    }
}