package com.github.mkopylec.furiouscinema.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer

abstract class RestStub implements Closeable {

    protected final WireMockServer server
    private ObjectMapper json = new ObjectMapper().findAndRegisterModules()

    protected RestStub(int port) {
        server = new WireMockServer(port)
        server.start()
    }

    void reset() {
        server.resetAll()
    }

    @Override
    void close() {
        server.stop()
    }

    protected String toJson(Object object) {
        json.writeValueAsString(object)
    }
}
