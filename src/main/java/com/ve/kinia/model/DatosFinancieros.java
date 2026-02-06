package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Datos financieros de una empresa para un período específico.
 * 
 * Es el INPUT PRINCIPAL para el motor de scoring.
 * Incluye: estado de resultados, balance, flujo de caja, ratios.
 * 
 * Consideraciones Venezuela:
 *   - Se guarda equivalente en USD por inflación
 *   - Se incluye cumplimiento tributario (SENIAT, IVSS, etc.)
 */
@Entity
@Table(name = "datos_financieros", indexes = {
    @Index(name = "idx_financieros_empresa", columnList = "empresa_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_financieros_periodo", 
        columnNames = {"empresa_id", "periodo_ano", "periodo_mes"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DatosFinancieros {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // ══════════════════════════════════════════════════════════════
    // PERÍODO
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Column(name = "periodo_ano", nullable = false)
    private Integer periodoAno;

    @Column(name = "periodo_mes")
    private Integer periodoMes;

    @Column(name = "es_proyeccion")
    @Builder.Default
    private Boolean esProyeccion = false;

    // ══════════════════════════════════════════════════════════════
    // ESTADO DE RESULTADOS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "ingresos_brutos", precision = 18, scale = 2)
    private BigDecimal ingresosBrutos;

    @Column(name = "ingresos_netos", precision = 18, scale = 2)
    private BigDecimal ingresosNetos;

    @Column(name = "costos_operativos", precision = 18, scale = 2)
    private BigDecimal costosOperativos;

    @Column(name = "gastos_administrativos", precision = 18, scale = 2)
    private BigDecimal gastosAdministrativos;

    @Column(name = "utilidad_bruta", precision = 18, scale = 2)
    private BigDecimal utilidadBruta;

    @Column(name = "utilidad_neta", precision = 18, scale = 2)
    private BigDecimal utilidadNeta;

    @Column(name = "ingresos_netos_usd", precision = 18, scale = 2)
    private BigDecimal ingresosNetosUsd;

    @Column(name = "tasa_cambio_usada", precision = 18, scale = 4)
    private BigDecimal tasaCambioUsada;

    // ══════════════════════════════════════════════════════════════
    // BALANCE GENERAL
    // ══════════════════════════════════════════════════════════════

    @Column(name = "activos_totales", precision = 18, scale = 2)
    private BigDecimal activosTotales;

    @Column(name = "activos_corrientes", precision = 18, scale = 2)
    private BigDecimal activosCorrientes;

    @Column(name = "pasivos_totales", precision = 18, scale = 2)
    private BigDecimal pasivosTotales;

    @Column(name = "pasivos_corrientes", precision = 18, scale = 2)
    private BigDecimal pasivosCorrientes;

    @Column(name = "patrimonio", precision = 18, scale = 2)
    private BigDecimal patrimonio;

    @Column(name = "cuentas_por_cobrar", precision = 18, scale = 2)
    private BigDecimal cuentasPorCobrar;

    @Column(name = "cuentas_por_pagar", precision = 18, scale = 2)
    private BigDecimal cuentasPorPagar;

    @Column(name = "inventarios", precision = 18, scale = 2)
    private BigDecimal inventarios;

    // ══════════════════════════════════════════════════════════════
    // FLUJO DE CAJA
    // ══════════════════════════════════════════════════════════════

    @Column(name = "flujo_caja_operativo", precision = 18, scale = 2)
    private BigDecimal flujoCajaOperativo;

    @Column(name = "efectivo_disponible", precision = 18, scale = 2)
    private BigDecimal efectivoDisponible;

    @Column(name = "deuda_bancaria_corto_plazo", precision = 18, scale = 2)
    private BigDecimal deudaBancariaCortoPlazo;

    @Column(name = "deuda_bancaria_largo_plazo", precision = 18, scale = 2)
    private BigDecimal deudaBancariaLargoPlazo;

    // ══════════════════════════════════════════════════════════════
    // RATIOS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "ratio_liquidez", precision = 8, scale = 4)
    private BigDecimal ratioLiquidez;

    @Column(name = "ratio_endeudamiento", precision = 8, scale = 4)
    private BigDecimal ratioEndeudamiento;

    @Column(name = "ratio_cobertura_deuda", precision = 8, scale = 4)
    private BigDecimal ratioCoberturaDeuda;

    @Column(name = "margen_neto", precision = 8, scale = 4)
    private BigDecimal margenNeto;

    @Column(name = "rotacion_cuentas_cobrar")
    private Integer rotacionCuentasCobrar;

    // ══════════════════════════════════════════════════════════════
    // CUMPLIMIENTO TRIBUTARIO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "al_dia_seniat")
    private Boolean alDiaSeniat;

    @Column(name = "al_dia_ivss")
    private Boolean alDiaIvss;

    @Column(name = "al_dia_faov")
    private Boolean alDiaFaov;

    @Column(name = "al_dia_inces")
    private Boolean alDiaInces;

    // ══════════════════════════════════════════════════════════════
    // VERIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "datos_verificados")
    @Builder.Default
    private Boolean datosVerificados = false;

    @Size(max = 100)
    @Column(name = "fuente_datos", length = 100)
    private String fuenteDatos;

    // ══════════════════════════════════════════════════════════════
    // AUDITORÍA
    // ══════════════════════════════════════════════════════════════

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ══════════════════════════════════════════════════════════════
    // MÉTODOS DE UTILIDAD
    // ══════════════════════════════════════════════════════════════

    @Transient
    public BigDecimal calcularRatioLiquidez() {
        if (activosCorrientes == null || pasivosCorrientes == null || 
            pasivosCorrientes.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return activosCorrientes.divide(pasivosCorrientes, 4, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal calcularRatioEndeudamiento() {
        if (pasivosTotales == null || activosTotales == null || 
            activosTotales.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return pasivosTotales.divide(activosTotales, 4, RoundingMode.HALF_UP);
    }

    @Transient
    public boolean isCumplimientoCompleto() {
        return Boolean.TRUE.equals(alDiaSeniat) &&
               Boolean.TRUE.equals(alDiaIvss) &&
               Boolean.TRUE.equals(alDiaFaov) &&
               Boolean.TRUE.equals(alDiaInces);
    }
}
