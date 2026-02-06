package com.ve.kinia.model.enums;

/**
 * Tipos de documento de identidad personal.
 */
public enum TipoDocumento {
    CEDULA_V("Cédula Venezolana", "V"),
    CEDULA_E("Cédula Extranjera", "E"),
    PASAPORTE("Pasaporte", "P");

    private final String descripcion;
    private final String prefijo;

    TipoDocumento(String descripcion, String prefijo) {
        this.descripcion = descripcion;
        this.prefijo = prefijo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrefijo() {
        return prefijo;
    }
}
