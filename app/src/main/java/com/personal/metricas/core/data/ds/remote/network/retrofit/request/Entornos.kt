package com.personal.metricas.core.data.ds.remote.network.retrofit.request


//DEV
/* val Authorization: String = "Basic RkI2QTY0OTQ1NzNBNEE5MTg3Qzg1MzcxODYxNjdCQTBfTW9iaWxlQW5vbnltb3VzX0FQUElEOjFmM2M3YTFkLWRlZGMtNDFhZC1hYWY5LWFhMjhjMzJjMmEwNQ=="
 val Oracle_Mobile_Backend_Id: String = "f017276c-e16e-40f9-be57-08602a6053d8"*/

/*
//PRO
val Authorization: String = "Basic NTA1NDlGOEMxRUNGNDNGRDg2OTgyMDg0NkE4QjRBMTVfTW9iaWxlQW5vbnltb3VzX0FQUElEOjdkNjVmNDRlLTdmY2MtNDE3NS1iOGFkLTBjNmI4MjAzMmY5Nw=="
val Oracle_Mobile_Backend_Id: String = "c2c7fb6e-cc74-4e34-8b29-1239bc47f036"


//val BASE_URL = "https://FB6A6494573A4A9187C8537186167BA0.mobile.ocp.oraclecloud.com:443/mobile/custom/ApiMaxamWS_DEV/"
val BASE_URL = "https://50549F8C1ECF43FD869820846A8B4A15.mobile.ocp.oraclecloud.com:443/mobile/custom/ApiMaxamWS/"*/

sealed class Entornos(val url: String, val autorizacion: String , val backendOracle: String) {
	data object DEV: Entornos(url = "https://FB6A6494573A4A9187C8537186167BA0.mobile.ocp.oraclecloud.com:443/mobile/custom/ApiMaxamWS_DEV/",
							  autorizacion = "Basic RkI2QTY0OTQ1NzNBNEE5MTg3Qzg1MzcxODYxNjdCQTBfTW9iaWxlQW5vbnltb3VzX0FQUElEOjFmM2M3YTFkLWRlZGMtNDFhZC1hYWY5LWFhMjhjMzJjMmEwNQ==",
							  backendOracle = "f017276c-e16e-40f9-be57-08602a6053d8")

	data object PRO: Entornos(url = "https://50549F8C1ECF43FD869820846A8B4A15.mobile.ocp.oraclecloud.com:443/mobile/custom/ApiMaxamWS/",
							  autorizacion = "Basic NTA1NDlGOEMxRUNGNDNGRDg2OTgyMDg0NkE4QjRBMTVfTW9iaWxlQW5vbnltb3VzX0FQUElEOjdkNjVmNDRlLTdmY2MtNDE3NS1iOGFkLTBjNmI4MjAzMmY5Nw==",
							  backendOracle = "c2c7fb6e-cc74-4e34-8b29-1239bc47f036")




	companion object{
		fun get(entorno: String): Entornos{
			if (entorno.equals("DEV")){
				return DEV
			}

			if (entorno.equals("PRO")){
				return PRO
			}

			return DEV
		}
	}

}