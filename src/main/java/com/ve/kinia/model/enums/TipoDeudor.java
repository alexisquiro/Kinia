package com.ve.kinia.model.enums;

/**
 * Indica si el deudor de una factura está registrado en Kinia.
 * 
 * INTERNO: Empresa registrada en la plataforma
 *   - Score REAL verificado
 *   - Historial de pagos en plataforma
 *   - Cobranza automática posible
 *   - MEJORES TASAS
 * 
 * EXTERNO: Empresa NO registrada
 *   - Score ESTIMADO
 *   - Sin historial verificable
 *   - Cobranza manual
 *   - Tasas más altas
 */
public enum TipoDeudor {
    INTERNO("Empresa registrada en Kinia"),
    EXTERNO("Empresa externa");

    private final String descripcion;

    TipoDeudor(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
