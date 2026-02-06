package com.ve.kinia.model.enums;

/**
 * Ciclo de vida de una factura en el sistema.
 * 
 * Flujo normal:
 *   PENDIENTE → EN_EVALUACION → APROBADA → ANTICIPADA → EN_COBRANZA → COBRADA
 * 
 * Flujos alternativos:
 *   EN_EVALUACION → RECHAZADA
 *   EN_COBRANZA → VENCIDA → INCOBRABLE
 */
public enum EstadoFactura {
    PENDIENTE("Esperando evaluación"),
    EN_EVALUACION("Siendo evaluada"),
    APROBADA("Aprobada para factoring"),
    RECHAZADA("No califica para factoring"),
    ANTICIPADA("Anticipo otorgado"),
    EN_COBRANZA("En proceso de cobro"),
    COBRADA("Cobrada exitosamente"),
    VENCIDA("Vencida sin cobrar"),
    INCOBRABLE("Marcada como incobrable");

    private final String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
