package model;

public class ItemsCotacao {
private int	idItemsCotacao;
private Cotacao cotacao;
private Producto producto;
private int qty;
private double precoUnitario;
private double subTotal;
private int idP;//improvisado


public int getIdItemsCotacao() {
	return idItemsCotacao;
}
public void setIdItemsCotacao(int idItemsCotacao) {
	this.idItemsCotacao = idItemsCotacao;
}
public Cotacao getCotacao() {
	return cotacao;
}
public void setCotacao(Cotacao cotacao) {
	this.cotacao = cotacao;
}
public Producto getProducto() {
	return producto;
}
public void setProducto(Producto producto) {
	this.producto = producto;
}
public int getQty() {
	return qty;
}
public void setQty(int qty) {
	this.qty = qty;
}
public double getPrecoUnitario() {
	return precoUnitario;
}
public void setPrecoUnitario(double precoUnitario) {
	this.precoUnitario = precoUnitario;
}
public double getSubTotal() {
	return subTotal;
}
public void setSubTotal(double subTotal) {
	this.subTotal = subTotal;
}
public int getIdP() {
	return idP;
}
public void setIdP(int idP) {
	this.idP = idP;
}



}
