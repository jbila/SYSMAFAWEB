package model;

import java.time.LocalDate;

public class  Cotacao {
	private int idCotacao;
	private double valorTotal;
	private Cliente cliente;
	private Utilizador utilizador;
	private LocalDate dataRegisto;
	
	public  Cotacao() {}
	
	
	public Cotacao(int idCotacao, double valorTotal, Cliente cliente, Utilizador utilizador, LocalDate dataRegisto) {
		super();
		this.idCotacao = idCotacao;
		this.valorTotal = valorTotal;
		this.cliente = cliente;
		this.utilizador = utilizador;
		this.dataRegisto = dataRegisto;
	}


	public int getIdCotacao() {
		return idCotacao;
	}
	public void setIdCotacao(int idCotacao) {
		this.idCotacao = idCotacao;
	}
	public double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Utilizador getUtilizador() {
		return utilizador;
	}
	public void setUtilizador(Utilizador utilizador) {
		this.utilizador = utilizador;
	}
	public LocalDate getDataRegisto() {
		return dataRegisto;
	}
	public void setDataRegisto(LocalDate dataRegisto) {
		this.dataRegisto = dataRegisto;
	}

	
}
