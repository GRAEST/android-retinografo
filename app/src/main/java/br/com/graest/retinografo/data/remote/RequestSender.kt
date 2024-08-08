package br.com.graest.retinografo.data.remote

import br.com.graest.retinografo.data.remote.dto.LoginDTO
import br.com.graest.retinografo.data.remote.util.NetworkError
import br.com.graest.retinografo.data.remote.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class RequestSender(
    private val httpClient: HttpClient,
) {
    suspend fun sendLoginInfo(loginDTO: LoginDTO): Result<String, NetworkError> {
        val response = try {
            httpClient.post(
                urlString = "https://76e79302a360.ngrok.app/api/account/login/"
            ) {
                contentType(ContentType.Application.Json)
                setBody(loginDTO)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when(response.status.value) {
            in 200..299 -> {
                //allow them to enter app
                //val status = response.body<String>()
                Result.Success("May Enter App")
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}