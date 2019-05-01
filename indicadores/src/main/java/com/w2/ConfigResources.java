package com.w2;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.w2.initialize.Inicializacao;
import com.w2.initialize.InicializacaoRepositorio;
import com.w2.model.Centro;
import com.w2.repository.CentrosRepositorio;
import com.w2.service.JsonService;

@Configuration
public class ConfigResources extends WebMvcConfigurerAdapter {

	@Autowired
	private InicializacaoRepositorio inicializadores;
	
	@Autowired
	private CentrosRepositorio centros;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/plantas/**")
                .addResourceLocations("file:c:/plantas/");
    }
    
    
    @PostConstruct
    public void inicializar() {
    	
    	
    	List<Inicializacao> inicializar = inicializadores.findAll();
    	
    	if(inicializar.isEmpty()) { 
    		
    			JsonService jsonCurso = new JsonService();
    			JsonNode jsonResultado =  jsonCurso.getTodosCentroJson();
    			
    			ArrayNode centroArray =  (ArrayNode) jsonResultado.at("/data");
    			
    			
    			for(JsonNode centroNode :  centroArray) {
    				
    				Centro centro = new Centro();
    				centro.setIdCentro(centroNode.at("/centro_id").asInt());
    				centro.setNomeCentro(centroNode.at("/centro_nome").asText());
    				centro.setSigla(centroNode.at("/centro_sigla").asText());
    				
    				centros.save(centro);
    				
    			}
  
    			
    			Inicializacao inicializador = new Inicializacao();
				inicializador.setStatus(false);
				
				inicializadores.save(inicializador);
    			
    			System.out.println("VARIAVEIS DEFINIDAS");
    		}
    		
    		
    	
    	
    	
    	System.out.println("SISTEMA PRONTO PARA CONFIGURAÇÂO");
    	
    }

    
}