package com.w2.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.auth.Usuario;


public interface Usuarios extends JpaRepository<Usuario, Long> {

	Usuario findByLogin(String login);
}