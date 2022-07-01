/*
 * Copyright 2022 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.grpc;

public class RequestValidationInterceptor implements ServerInterceptor {

    private RequestValidatorResolver requestValidatorResolver;

    public RequestValidationInterceptor(RequestValidatorResolver requestValidatorResolver) {
        this.requestValidatorResolver = requestValidatorResolver;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        ServerCallHandler<ReqT, RespT> handler = (call1, headers1) -> {
            call1.request(1);
            return new RequestValidationListener<>(call1, headers1, next, requestValidatorResolver);
        };

        return handler.startCall(call, headers);
    }
}