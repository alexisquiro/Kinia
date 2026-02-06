package com.ve.kinia.model.enums;

/**
 * Clasificación de archivos subidos al sistema.
 */
public enum TipoDocumentoArchivo {
    RIF_EMPRESA("Documento RIF de la empresa"),
    CEDULA_REPRESENTANTE("Cédula del representante"),
    REGISTRO_MERCANTIL("Registro mercantil"),
    ESTADOS_FINANCIEROS("Estados financieros"),
    ACTA_CONSTITUTIVA("Acta constitutiva"),
    PODER_REPRESENTACION("Poder de representación"),
    REFERENCIA_BANCARIA("Referencia bancaria"),
    REFERENCIA_COMERCIAL("Referencia comercial"),
    FACTURA("Factura para factoring"),
    COMPROBANTE_PAGO("Comprobante de pago"),
    OTRO("Otro documento");

    private final String descripcion;

    TipoDocumentoArchivo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
