package com.w2.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.auth.Grupo;
import com.w2.model.auth.Permissao;



public interface Permissoes extends JpaRepository<Permissao, Long> {
	
	List<Permissao> findByGruposIn(Grupo grupo);

}