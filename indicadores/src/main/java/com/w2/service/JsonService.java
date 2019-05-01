package com.w2.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonService extends RestTemplate {

	private static final String URL_CURSO = "http://x/wservice/cursos/getCursoComDisciplinasPorCurso/?filtro=true";
	private static final String URL_CENTRO = "http:/x/wservice/unidades/getCentroComDadosDeCursos?filtro=true";
	private static final String APP_TOKEN  =  "x";
	private static final String APP_NAME  =  "x";
	private HttpHeaders headers;
	private HttpEntity<?> entity;


	
	public JsonService() { 

		this.headers = new HttpHeaders();

		this.headers.set("appName", APP_NAME);
		this.headers.set("appToken", APP_TOKEN);
		this.headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		entity = new HttpEntity<>(this.headers);


	}


	public JsonNode getCursoJson(int idCurso) { 

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_CURSO).queryParam("atributo", idCurso);

		HttpEntity<JsonNode> response = this.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				JsonNode.class);		

		return response.getBody();
	}


	
	public JsonNode getTodosCursoJson() { 


		HttpEntity<?> entity = new HttpEntity<>(this.headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_CURSO);

		HttpEntity<JsonNode> response = this.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				JsonNode.class);		

		return response.getBody();
	}


	public JsonNode getCentroJson(int idCentro) { 
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_CENTRO).queryParam("atributo", idCentro);

		HttpEntity<JsonNode> response = this.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				JsonNode.class);		
		return response.getBody();
	}


	public JsonNode getTodosCentroJson() { 
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_CENTRO);

		HttpEntity<JsonNode> response = this.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				JsonNode.class);		

		return response.getBody();
	}


	public JsonNode getCentroJson(String siglaCentro) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_CENTRO).queryParam("atributo", siglaCentro);

		HttpEntity<JsonNode> response = this.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				JsonNode.class);		
		
			return response.getBody();
		
	}


	
}