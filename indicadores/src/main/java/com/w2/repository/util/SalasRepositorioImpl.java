package com.w2.repository.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;


@Repository
public class SalasRepositorioImpl implements ObjetoRepositorioCustom {

	
	
	 @PersistenceContext
	 private EntityManager entityManager;

	@Override
	public void detachObject(Object object) {
		// TODO Auto-generated method stub
		
		
		entityManager.detach(object);
	}
	

}
