package com.personal.requestmobility.endpoints.data.ds.remote.servicio

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface EndPointRemotos {

	@GET
	suspend fun get(
		@HeaderMap
		headers: Map<String, String>,
		@Url
		url: String,
	): ResponseBody

}