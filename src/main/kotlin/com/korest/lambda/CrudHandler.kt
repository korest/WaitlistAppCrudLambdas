package com.korest.lambda

import amazonaws.serverless.AwsProxyRequest
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.korest.lambda.handler.HttpHandler
import com.korest.lambda.response.ApiGatewayResponse
import com.korest.lambda.response.MessageResponse
import org.apache.logging.log4j.LogManager

abstract class CrudHandler : RequestHandler<AwsProxyRequest, ApiGatewayResponse> {
    override fun handleRequest(input: AwsProxyRequest, context: Context): ApiGatewayResponse {
        LOG.info("Request received body: ${input.body} " +
                "path: ${input.pathParameters} query: ${input.queryStringParameters}")

        return getHttpHandlers()[input.httpMethod]?.handle(input.requestContext.authorizer.principalId, input)
                ?: ApiGatewayResponse.build {
                    statusCode = 405
                    objectBody = MessageResponse("Method not supported")
                }
    }

    abstract fun getHttpHandlers(): Map<String, HttpHandler>

    companion object {
        const val REGION = "region"
        private val LOG = LogManager.getLogger(CrudHandler::class.java)
    }
}