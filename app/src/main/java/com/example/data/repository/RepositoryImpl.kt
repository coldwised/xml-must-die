package com.example.data.repository

import coil.network.HttpException
import com.example.data.remote.ImagesApi
import com.example.domain.file_provider.FileProvider
import com.example.domain.repository.Repository
import com.example.mdc.R
import com.example.mdc.util.Resource
import com.example.mdc.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ImagesApi,
    private val fileProvider: FileProvider
): Repository {

    private sealed interface DownloadState {
        data class Downloading(val progress: Int) : DownloadState
        object Finished : DownloadState
        data class Failed(val error: Throwable) : DownloadState
    }

    private fun ResponseBody.saveFile(): Flow<DownloadState> {
        return flow {
            emit(DownloadState.Downloading(0))
            val destinationFile = fileProvider.file
            try {
                byteStream().use { inputStream->
                    destinationFile.outputStream().use { outputStream->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }.flowOn(Dispatchers.IO).distinctUntilChanged()
    }
    override fun getImagesUrlList(): Flow<Resource<List<String>>> {
        return flow<Resource<List<String>>> {
            val responseBody = safeApiCall {
                api.loadFile().execute().body()
            }
            when(responseBody) {
                is Resource.Success -> {
                    responseBody.data!!.saveFile().collect { downloadState ->
                        when(downloadState) {
                            is DownloadState.Downloading -> {}
                            is DownloadState.Failed -> {
                                emit(Resource.Error(handleThrowableException(downloadState.error)))
                            }
                            DownloadState.Finished -> {
                                val file = fileProvider.file
                                val lines = file.readLines().reversed()
                                emit(Resource.Success(lines))
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    emit(Resource.Error(responseBody.message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): Resource<T> {
        return try {
            val data = apiCall()
            Resource.Success(data)
        } catch(throwable: Throwable) {
            Resource.Error(handleThrowableException(throwable))
        }
    }

    private fun handleThrowableException(throwable: Throwable): UiText {
        return when(throwable) {
            is HttpException -> {
                val localizedMessage = throwable.localizedMessage
                if(localizedMessage.isNullOrEmpty()) {
                    UiText.StringResource(R.string.unknown_exception)
                }
                else {
                    UiText.DynamicString(localizedMessage)
                }
            }
            is IOException -> {
                UiText.StringResource(R.string.io_exception)
            }
            else -> UiText.StringResource(R.string.unknown_exception)
        }
    }
}