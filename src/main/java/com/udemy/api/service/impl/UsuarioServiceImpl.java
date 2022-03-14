package com.udemy.api.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.api.exception.ErroAutenticacao;
import com.udemy.api.exception.RegraNegocioException;
import com.udemy.api.model.entity.Usuario;
import com.udemy.api.model.repository.UsuarioRepository;
import com.udemy.api.service.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;	
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent())
		{
			throw new ErroAutenticacao("usuario nao encontrado.");
		}
		
		if(!usuario.get().getSenha().equals(senha))
		{
			throw new ErroAutenticacao("Senha invalida.");
		}
		
		return null;
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) 
		{
			throw new RegraNegocioException("Já existe um usuario cadastrado com esse e-mail.");
		}
	}
	
	@Override
	public Optional<Usuario> obterPorId(Long id)
	{
		return repository.findById(id);
	}

}
