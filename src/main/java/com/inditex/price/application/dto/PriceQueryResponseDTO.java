package com.inditex.price.application.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la respuesta de consulta de precios
 * Representa los datos de salida del endpoint
 */
@Schema(
    description = "Respuesta con información del precio aplicable",
    example = "{\n" +
             "  \"productId\": 35455,\n" +
             "  \"brandId\": 1,\n" +
             "  \"priceList\": 1,\n" +
             "  \"startDate\": \"2020-06-14T00:00:00\",\n" +
             "  \"endDate\": \"2020-12-31T23:59:59\",\n" +
             "  \"price\": 35.50,\n" +
             "  \"currency\": \"EUR\"\n" +
             "}"
)
public class PriceQueryResponseDTO {

	@Schema(description = "Identificador del producto", example = "35455")
	private Long productId;
	
	@Schema(description = "Identificador de la marca/cadena", example = "1")
	private Long brandId;
	
	@Schema(description = "Identificador de la tarifa aplicada", example = "1")
	private Integer priceList;
	
	@Schema(description = "Precio aplicable", example = "35.50")
	private BigDecimal price;
	
	@Schema(description = "Código de moneda", example = "EUR")
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