package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Configuración del motor de scoring.
 * Define pesos, umbrales y tasas.
 * Solo una configuración puede estar activa a la vez.
 */
@Entity
@Table(name = "configuracion_scoring")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConfiguracionScoring {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 20)
    @Column(name = "version", length = 20)
    @Builder.Default
    private String version = "1.0";

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // ══════════════════════════════════════════════════════════════
    // PESOS SCORE PROVEEDOR (deben sumar 100)
    // ══════════════════════════════════════════════════════════════

    @Column(name = "peso_financiero", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoFinanciero = new BigDecimal("30.00");

    @Column(name = "peso_historial_pagos", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoHistorialPagos = new BigDecimal("25.00");

    @Column(name = "peso_antiguedad", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoAntiguedad = new BigDecimal("15.00");

    @Column(name = "peso_sector", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoSector = new BigDecimal("10.00");

    @Column(name = "peso_cumplimiento", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoCumplimiento = new BigDecimal("10.00");

    @Column(name = "peso_documentacion", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoDocumentacion = new BigDecimal("10.00");

    // ══════════════════════════════════════════════════════════════
    // PESOS SCORE DEUDOR
    // ══════════════════════════════════════════════════════════════

    @Column(name = "peso_deudor_historial_plataforma", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoDeudorHistorialPlataforma = new BigDecimal("40.00");

    @Column(name = "peso_deudor_financiero", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoDeudorFinanciero = new BigDecimal("30.00");

    @Column(name = "peso_deudor_antiguedad", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoDeudorAntiguedad = new BigDecimal("15.00");

    @Column(name = "peso_deudor_externo", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pesoDeudorExterno = new BigDecimal("15.00");

    // ══════════════════════════════════════════════════════════════
    // UMBRALES DE RIESGO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "umbral_muy_bajo")
    @Builder.Default
    private Integer umbralMuyBajo = 80;

    @Column(name = "umbral_bajo")
    @Builder.Default
    private Integer umbralBajo = 65;

    @Column(name = "umbral_medio")
    @Builder.Default
    private Integer umbralMedio = 50;

    @Column(name = "umbral_alto")
    @Builder.Default
    private Integer umbralAlto = 35;

    // ══════════════════════════════════════════════════════════════
    // TASAS BASE POR NIVEL
    // ══════════════════════════════════════════════════════════════

    @Column(name = "tasa_muy_bajo", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tasaMuyBajo = new BigDecimal("3.50");

    @Column(name = "tasa_bajo", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tasaBajo = new BigDecimal("5.00");

    @Column(name = "tasa_medio", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tasaMedio = new BigDecimal("7.50");

    @Column(name = "tasa_alto", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tasaAlto = new BigDecimal("12.00");

    @Column(name = "tasa_muy_alto", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tasaMuyAlto = new BigDecimal("18.00");

    // ══════════════════════════════════════════════════════════════
    // PORCENTAJES DE ANTICIPO POR NIVEL
    // ══════════════════════════════════════════════════════════════

    @Column(name = "anticipo_muy_bajo", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal anticipoMuyBajo = new BigDecimal("90.00");

    @Column(name = "anticipo_bajo", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal anticipoBajo = new BigDecimal("85.00");

    @Column(name = "anticipo_medio", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal anticipoMedio = new BigDecimal("80.00");

    @Column(name = "anticipo_alto", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal anticipoAlto = new BigDecimal("70.00");

    @Column(name = "anticipo_muy_alto", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal anticipoMuyAlto = new BigDecimal("60.00");

    // ══════════════════════════════════════════════════════════════
    // BONIFICACIONES ECOSISTEMA CERRADO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "bonus_deudor_interno")
    @Builder.Default
    private Integer bonusDeudorInterno = 10;

    @Column(name = "descuento_tasa_deudor_interno", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal descuentoTasaDeudorInterno = new BigDecimal("2.00");

    // ══════════════════════════════════════════════════════════════
    // ESTADO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
