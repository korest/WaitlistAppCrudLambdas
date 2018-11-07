package com.korest.lambda.handler

import amazonaws.serverless.AwsProxyRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse
import org.apache.logging.log4j.LogManager

abstract class HttpHandler {
    companion object {
        const val PATH_WAITLIST_ID = "waitlistId"

        val LOG = LogManager.getLogger(HttpHandler::class.java)
        val OBJECT_MAPPER = ObjectMapper()
    }

    protected abstract fun handleInternal(accountId: String, event: AwsProxyRequest): ApiGatewayResponse

    fun handle(accountId: String, event: AwsProxyRequest): ApiGatewayResponse {
        return try {
            handleInternal(accountId, event)
        } catch (e: IllegalStateException) {
            LOG.error(e.message, e)
            ApiGatewayResponse.build {
                statusCode = 400
                objectBody = MessageResponse("Bad request")
            }
        } catch (e: HttpException) {
            LOG.error(e.message, e)
            e.response
        } catch (e: Exception) {
            LOG.error(e.message, e)
            ApiGatewayResponse.build {
                statusCode = 500
                objectBody = MessageResponse("Internal error")
            }
        }
    }
}
