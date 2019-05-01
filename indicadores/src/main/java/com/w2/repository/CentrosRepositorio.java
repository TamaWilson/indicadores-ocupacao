package com.w2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Centro;
import com.w2.model.util.Mesorregioes;

public interface CentrosRepositorio extends JpaRepository<Centro, Integer> {

	Centro findCentroByIdCentro(int idCentro);
	
	List<Centro> findCentroByMesorregiao(Mesorregioes mesorregiao);
	
	Centro findCentroBySiglaLike(String sigla);
}
