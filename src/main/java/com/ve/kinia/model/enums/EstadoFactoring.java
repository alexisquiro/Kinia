package com.ve.kinia.model.enums;

/**
 * Estados de una solicitud de factoring.
 * 
 * Flujo normal:
 *   BORRADOR → ENVIADA → EN_REVISION → APROBADA → DESEMBOLSADA → EN_COBRANZA → LIQUIDADA
 */
public enum EstadoFactoring {
    BORRADOR("En construcción"),
    ENVIADA("Enviada para revisión"),
    EN_REVISION("Siendo revisada"),
    APROBADA("Aprobada, pendiente desembolso"),
    RECHAZADA("Rechazada"),
    DESEMBOLSADA("Dinero transferido"),
    EN_COBRANZA("En proceso de cobro"),
    LIQUIDADA("Completamente cobrada"),
    CANCELADA("Cancelada por usuario");

    private final String descripcion;

    EstadoFactoring(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
