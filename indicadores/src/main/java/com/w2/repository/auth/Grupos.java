package com.w2.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.auth.Grupo;
import com.w2.model.auth.Usuario;



public interface Grupos extends JpaRepository<Grupo, Long> {
	
	List<Grupo> findByUsuariosIn(Usuario usuario);

}