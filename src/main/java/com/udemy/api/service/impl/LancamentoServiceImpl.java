package com.udemy.api.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.criterion.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udemy.api.exception.RegraNegocioException;
import com.udemy.api.model.entity.Lancamento;
import com.udemy.api.model.enums.StatusLancamento;
import com.udemy.api.model.enums.TipoLancamento;
import com.udemy.api.model.repository.LancamentoRepository;
import com.udemy.api.service.LancamentoService;



@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}
	@Transactional
	@Override
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) 
	{
		
		Example example = (Example) org.springframework.data.domain.Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING)); 
		
		return repository.findAll();
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) 
	{
		lancamento.setStatus(status);
		atualizar(lancamento);
		
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(""))
		{
			throw new RegraNegocioException("Informe uma descricao valida.");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12)
		{
			throw new RegraNegocioException("Informe um m??s v??lido");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() !=4 )
		{
			throw new  RegraNegocioException("Informe um ano v??lido.");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null)
		{
			throw new  RegraNegocioException("Informe um usu??rio.");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
		{
			throw new  RegraNegocioException("Informe um valor v??lido.");
		}
		
		if(lancamento.getTipo() == null)
		{
			throw new  RegraNegocioException("Informe um tipo de lan??aamento.");
		}
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(receitas == null)
		{
			receitas = BigDecimal.ZERO;
		}
		if(despesas == null)
		{
			despesas = BigDecimal.ZERO;
		}
		return receitas.subtract(despesas);
	}

}
