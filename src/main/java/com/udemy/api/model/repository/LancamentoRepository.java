package com.udemy.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udemy.api.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	
}
