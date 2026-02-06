package com.ve.kinia.model.enums;

/**
 * Estado operativo de la empresa en la plataforma.
 */
public enum EstadoEmpresa {
    ACTIVA("Operando normalmente"),
    INACTIVA("Desactivada voluntariamente"),
    SUSPENDIDA("Suspendida temporalmente"),
    BLOQUEADA("Bloqueada por incumplimiento");

    private final String descripcion;

    EstadoEmpresa(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
