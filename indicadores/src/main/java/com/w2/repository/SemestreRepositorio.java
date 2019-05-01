package com.w2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Semestre;

public interface SemestreRepositorio extends JpaRepository<Semestre, Integer>{
	
	
	Semestre findFirstByOrderByIdSemestreDesc();
    Semestre findByAtivoIsTrue();
    List<Semestre> findAllByOrderByIdSemestreDesc();
    
}
