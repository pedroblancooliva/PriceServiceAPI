package com.inditex.price.application.dto;

import java.math.BigDecimal;

/**
 * DTO para la respuesta de consulta de precios
 * Representa los datos de salida del endpoint
 */
public class PriceQueryResponseDTO {

	private Long productId;
	private Long brandId;
	private Integer priceList;
	private BigDecimal price;
	private String currency;

	public PriceQueryResponseDTO() {
	}

	public PriceQueryResponseDTO(Long productId, Long brandId, Integer priceList,
			BigDecimal price, String currency) {
		this.productId = productId;
		this.brandId = brandId;
		this.priceList = priceList;
		this.price = price;
		this.currency = currency;
	}

	// Getters y setters
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer getPriceList() {
		return priceList;
	}

	public void setPriceList(Integer priceList) {
		this.priceList = priceList;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "PriceQueryResponse{" +
				"productId=" + productId +
				", brandId=" + brandId +
				", priceList=" + priceList +
				", price=" + price +
				", currency='" + currency + '\'' +
				'}';
	}
}