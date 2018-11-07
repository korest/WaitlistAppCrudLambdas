package com.korest.lambda.handler

import com.korest.lambda.response.ApiGatewayResponse

class HttpException(exception: Exception, val response: ApiGatewayResponse) : RuntimeException(exception)