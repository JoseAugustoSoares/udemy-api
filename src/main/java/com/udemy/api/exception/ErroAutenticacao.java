package com.udemy.api.exception;

public class ErroAutenticacao extends RuntimeException
{
	public ErroAutenticacao(String mensagem)
	{
		super(mensagem);
	}
}
