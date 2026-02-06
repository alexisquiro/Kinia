package com.ve.kinia.model.enums;

/**
 * Clasificación del sector económico de la empresa.
 * Influye en el cálculo del score.
 */
public enum SectorEconomico {
    COMERCIO_RETAIL("Comercio y Retail"),
    MANUFACTURA("Manufactura"),
    SERVICIOS("Servicios"),
    CONSTRUCCION("Construcción"),
    TECNOLOGIA("Tecnología"),
    ALIMENTOS("Alimentos y Bebidas"),
    SALUD("Salud"),
    TRANSPORTE("Transporte y Logística"),
    AGRICULTURA("Agricultura"),
    TURISMO("Turismo y Hotelería"),
    EDUCACION("Educación"),
    FINANCIERO("Servicios Financieros"),
    OTRO("Otro");

    private final String descripcion;

    SectorEconomico(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
