package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Registro de pagos recibidos contra una factura.
 */
@Entity
@Table(name = "pagos_recibidos", indexes = {
    @Index(name = "idx_pagos_factura", columnList = "factura_id"),
    @Index(name = "idx_pagos_fecha", columnList = "fecha_pago")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PagoRecibido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "monto", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Size(max = 3)
    @Column(name = "moneda", length = 3)
    @Builder.Default
    private String moneda = "VES";

    @Column(name = "tasa_cambio", precision = 18, scale = 4)
    private BigDecimal tasaCambio;

    @Size(max = 100)
    @Column(name = "referencia", length = 100)
    private String referencia;

    @Size(max = 50)
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Size(max = 100)
    @Column(name = "banco_origen", length = 100)
    private String bancoOrigen;

    @Column(name = "es_pago_interno")
    @Builder.Default
    private Boolean esPagoInterno = false;

    @Column(name = "compensacion_automatica")
    @Builder.Default
    private Boolean compensacionAutomatica = false;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "registrado_por")
    private UUID registradoPor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
