package nl.altindag.client.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import nl.altindag.client.ClientType
import nl.altindag.client.ClientType.KTOR_OK_HTTP
import nl.altindag.sslcontext.SSLFactory
import okhttp3.ConnectionSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KtorOkHttpClientService(
        @Autowired(required = false)
        sslFactory: SSLFactory?
): KtorHttpClientService(
        HttpClient(OkHttp) {
            sslFactory?.let { factory ->
                engine {
                    config {
                        connectionSpecs(
                                listOf(
                                        ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).apply {
                                            cipherSuites(*factory.sslParameters.cipherSuites)
                                            tlsVersions(*factory.sslParameters.protocols)
                                        }.build()
                                )
                        )

                        sslSocketFactory(factory.sslContext.socketFactory, factory.trustManager.orElseThrow())
                        hostnameVerifier(factory.hostnameVerifier)
                    }
                }
            }
        }
) {

    override fun getClientType(): ClientType = KTOR_OK_HTTP

}
