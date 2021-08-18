package model;

/**
 * <h1><b>EntradaProducto</b></h1>
 * <p>
 * Esta classe representa o corpo de EntradaProducto, ela é usada quando se faz
 * <br>
 * um entrada do producto,o envio do producto para pratileira, e listagem de
 * EntradaProducto
 * </p>
 * 
 * @see Producto <b>
 * @see Fornecedor
 *      <h3>@author JACINTO BILA TEL:848319153 Email:
 *      jacinto.billa@gmail.com</h3>
 */

public class EntradaProducto {
	private int idEntradaProducto;
	private Producto producto;
	private Fornecedor fornecedor;
	private int quantidade;
	private double valorCompra;
	private double valorVenda;
	private double lucroPrevisto;
	private String validade;
	private String dataRegisto;

	/**
	 * <h2>Contrutor vazio
	 * <h2>
	 */
	public EntradaProducto() {
	}

	/**
	 * <h2>Modificadores de Acesso</h2> <br>
	 * <p>
	 * permite acaptura e insercao de dados no objecto
	 * </p>
	 */

	public int getIdEntradaProducto() {
		return idEntradaProducto;
	}

	public void setIdEntradaProducto(int idEntradaProducto) {
		this.idEntradaProducto = idEntradaProducto;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(double valorCompra) {
		this.valorCompra = valorCompra;
	}

	public double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(double valorVenda) {
		this.valorVenda = valorVenda;
	}

	public double getLucroPrevisto() {
		return lucroPrevisto;
	}

	public void setLucroPrevisto(double lucroPrevisto) {
		this.lucroPrevisto = lucroPrevisto;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public String getDataRegisto() {
		return dataRegisto;
	}

	public void setDataRegisto(String dataRegisto) {
		this.dataRegisto = dataRegisto;
	}

	/**
	 * <h2>ToString</h2>
	 * <p>
	 * Pemite a impressao do objecto
	 * </p>
	 */
	@Override
	public String toString() {
		return "EntradaProducto [idEntradaProducto=" + idEntradaProducto + ", producto=" + producto + ", fornecedor="
				+ fornecedor + ", quantidade=" + quantidade + ", valorCompra=" + valorCompra + ", valorVenda="
				+ valorVenda + ", lucroPrevisto=" + lucroPrevisto + ", validade=" + validade + ", dataRegisto="
				+ dataRegisto + "]";
	}

}
