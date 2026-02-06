package com.ve.kinia.model.enums;

/**
 * Estados del proceso KYC (Know Your Customer).
 * 
 * Transiciones permitidas:
 *   PENDIENTE → EN_REVISION | RECHAZADO
 *   EN_REVISION → APROBADO | RECHAZADO | REQUIERE_INFO
 *   REQUIERE_INFO → EN_REVISION | RECHAZADO
 *   APROBADO → (estado final)
 *   RECHAZADO → PENDIENTE (si reaplica)
 */
public enum EstadoKyc {
    PENDIENTE("Pendiente de revisión"),
    EN_REVISION("En proceso de revisión"),
    APROBADO("Verificación aprobada"),
    RECHAZADO("Verificación rechazada"),
    REQUIERE_INFO("Requiere información adicional");

    private final String descripcion;

    EstadoKyc(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
