package com.w2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.w2.security.IndicadoresUserDetailsService;

@EnableWebSecurity
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private IndicadoresUserDetailsService indicadoresUserDetailsService;		

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/login*").anonymous()
		.antMatchers("/fonts/**", "/css/**", "/images/**", "/js/**","/maps/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login").defaultSuccessUrl("/", true)
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true);
		
	}


	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder
		.userDetailsService(indicadoresUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}

}