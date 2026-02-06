package com.ve.kinia.model;

import com.ve.kinia.model.enums.NivelRiesgo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Resultado del motor de scoring crediticio.
 * 
 * Una empresa tiene DOS scores:
 *   1. SCORE COMO PROVEEDOR (puntaje): Para solicitar factoring
 *   2. SCORE COMO DEUDOR (puntajeComoDeudor): Cuando le facturan
 * 
 * Solo UN score puede estar vigente (esVigente=true) por empresa.
 * Al crear uno nuevo, se invalida el anterior automáticamente.
 */
@Entity
@Table(name = "scores", indexes = {
    @Index(name = "idx_scores_empresa", columnList = "empresa_id"),
    @Index(name = "idx_scores_fecha", columnList = "created_at")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // ══════════════════════════════════════════════════════════════
    // SCORE COMO PROVEEDOR
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Min(0) @Max(100)
    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo", nullable = false)
    private NivelRiesgo nivelRiesgo;

    // Desglose por componentes
    @Min(0) @Max(100)
    @Column(name = "puntaje_financiero")
    private Integer puntajeFinanciero;

    @Column(name = "peso_financiero", precision = 5, scale = 2)
    private BigDecimal pesoFinanciero;

    @Min(0) @Max(100)
    @Column(name = "puntaje_historial_pagos")
    private Integer puntajeHistorialPagos;

    @Column(name = "peso_historial_pagos", precision = 5, scale = 2)
    private BigDecimal pesoHistorialPagos;

    @Min(0) @Max(100)
    @Column(name = "puntaje_antiguedad")
    private Integer puntajeAntiguedad;

    @Column(name = "peso_antiguedad", precision = 5, scale = 2)
    private BigDecimal pesoAntiguedad;

    @Min(0) @Max(100)
    @Column(name = "puntaje_sector")
    private Integer puntajeSector;

    @Column(name = "peso_sector", precision = 5, scale = 2)
    private BigDecimal pesoSector;

    @Min(0) @Max(100)
    @Column(name = "puntaje_cumplimiento")
    private Integer puntajeCumplimiento;

    @Column(name = "peso_cumplimiento", precision = 5, scale = 2)
    private BigDecimal pesoCumplimiento;

    @Min(0) @Max(100)
    @Column(name = "puntaje_documentacion")
    private Integer puntajeDocumentacion;

    @Column(name = "peso_documentacion", precision = 5, scale = 2)
    private BigDecimal pesoDocumentacion;

    // ══════════════════════════════════════════════════════════════
    // SCORE COMO DEUDOR
    // ══════════════════════════════════════════════════════════════

    @Min(0) @Max(100)
    @Column(name = "puntaje_como_deudor")
    private Integer puntajeComoDeudor;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo_como_deudor")
    private NivelRiesgo nivelRiesgoComoDeudor;

    @Column(name = "historial_pagos_en_plataforma")
    private Integer historialPagosEnPlataforma;

    // ══════════════════════════════════════════════════════════════
    // AJUSTES
    // ══════════════════════════════════════════════════════════════

    @Column(name = "ajuste_manual")
    @Builder.Default
    private Integer ajusteManual = 0;

    @Column(name = "motivo_ajuste", columnDefinition = "TEXT")
    private String motivoAjuste;

    @Column(name = "ajustado_por")
    private UUID ajustadoPor;

    // ══════════════════════════════════════════════════════════════
    // RESULTADOS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "limite_factoring_sugerido", precision = 18, scale = 2)
    private BigDecimal limiteFactoringSugerido;

    @Column(name = "tasa_descuento_sugerida", precision = 5, scale = 2)
    private BigDecimal tasaDescuentoSugerida;

    // ══════════════════════════════════════════════════════════════
    // EXPLICACIÓN
    // ══════════════════════════════════════════════════════════════

    @Size(max = 500)
    @Column(name = "explicacion_corta", length = 500)
    private String explicacionCorta;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "factores_positivos", columnDefinition = "TEXT[]")
    private List<String> factoresPositivos;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "factores_negativos", columnDefinition = "TEXT[]")
    private List<String> factoresNegativos;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "recomendaciones", columnDefinition = "TEXT[]")
    private List<String> recomendaciones;

    // ══════════════════════════════════════════════════════════════
    // METADATA
    // ══════════════════════════════════════════════════════════════

    @Size(max = 20)
    @Column(name = "version_algoritmo", length = 20)
    @Builder.Default
    private String versionAlgoritmo = "1.0";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametros_usados", columnDefinition = "jsonb")
    private Map<String, Object> parametrosUsados;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_entrada", columnDefinition = "jsonb")
    private Map<String, Object> datosEntrada;

    // ══════════════════════════════════════════════════════════════
    // VIGENCIA
    // ══════════════════════════════════════════════════════════════

    @Column(name = "es_vigente")
    @Builder.Default
    private Boolean esVigente = true;

    @Column(name = "fecha_vigencia_hasta")
    private LocalDate fechaVigenciaHasta;

    // ══════════════════════════════════════════════════════════════
    // AUDITORÍA
    // ══════════════════════════════════════════════════════════════

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Size(max = 50)
    @Column(name = "calculado_por", length = 50)
    @Builder.Default
    private String calculadoPor = "SISTEMA";
}
