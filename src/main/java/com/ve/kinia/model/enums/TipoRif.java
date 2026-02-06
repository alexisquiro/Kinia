package com.ve.kinia.model.enums;

/**
 * Tipos de RIF venezolano según clasificación SENIAT.
 */
public enum TipoRif {
    J("Persona Jurídica"),
    G("Gubernamental"),
    V("Venezolano Natural"),
    E("Extranjero Natural"),
    P("Pasaporte"),
    C("Comunal");

    private final String descripcion;

    TipoRif(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
