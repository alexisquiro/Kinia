package com.ve.kinia.model;

import com.ve.kinia.model.enums.EstadoFactoring;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Solicitud de factoring (anticipo de facturas).
 * 
 * Flujo:
 *   BORRADOR → ENVIADA → EN_REVISION → APROBADA → DESEMBOLSADA → EN_COBRANZA → LIQUIDADA
 * 
 * Cálculos:
 *   - monto_anticipo = monto_facturas × porcentaje_anticipo
 *   - comision = monto_facturas × tasa_descuento + comision_fija
 *   - monto_a_desembolsar = monto_anticipo - comision
 *   - monto_retenido = monto_facturas - monto_anticipo
 */
@Entity
@Table(name = "solicitudes_factoring", indexes = {
    @Index(name = "idx_factoring_empresa", columnList = "empresa_id"),
    @Index(name = "idx_factoring_estado", columnList = "estado")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_codigo_solicitud", columnNames = {"codigo_solicitud"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SolicitudFactoring {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Size(max = 20)
    @Column(name = "codigo_solicitud", unique = true, length = 20)
    private String codigoSolicitud;

    // ══════════════════════════════════════════════════════════════
    // SCORE AL MOMENTO DE SOLICITAR
    // ══════════════════════════════════════════════════════════════

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_id")
    private Score score;

    @Min(0) @Max(100)
    @Column(name = "score_al_solicitar")
    private Integer scoreAlSolicitar;

    // ══════════════════════════════════════════════════════════════
    // MONTOS
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "monto_facturas_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoFacturasTotal;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    @Column(name = "porcentaje_anticipo", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeAnticipo;

    @NotNull
    @Column(name = "monto_anticipo", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoAnticipo;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    @Column(name = "tasa_descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaDescuento;

    @Column(name = "comision_fija", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal comisionFija = BigDecimal.ZERO;

    @NotNull
    @Column(name = "monto_comision_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoComisionTotal;

    @NotNull
    @Column(name = "monto_a_desembolsar", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoADesembolsar;

    @Column(name = "monto_retenido", precision = 18, scale = 2)
    private BigDecimal montoRetenido;

    // ══════════════════════════════════════════════════════════════
    // FECHAS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "fecha_solicitud", nullable = false)
    @Builder.Default
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "fecha_desembolso")
    private LocalDateTime fechaDesembolso;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    // ══════════════════════════════════════════════════════════════
    // ESTADO
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoFactoring estado = EstadoFactoring.BORRADOR;

    // ══════════════════════════════════════════════════════════════
    // DATOS BANCARIOS PARA DESEMBOLSO
    // ══════════════════════════════════════════════════════════════

    @Size(max = 100)
    @Column(name = "banco_destino", length = 100)
    private String bancoDestino;

    @Size(max = 30)
    @Column(name = "cuenta_destino", length = 30)
    private String cuentaDestino;

    @Size(max = 100)
    @Column(name = "referencia_transferencia", length = 100)
    private String referenciaTransferencia;

    // ══════════════════════════════════════════════════════════════
    // REVISIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "aprobado_por")
    private UUID aprobadoPor;

    @Column(name = "notas_revision", columnDefinition = "TEXT")
    private String notasRevision;

    // ══════════════════════════════════════════════════════════════
    // COBRANZA
    // ══════════════════════════════════════════════════════════════

    @Column(name = "monto_cobrado", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal montoCobrado = BigDecimal.ZERO;

    @Column(name = "fecha_ultimo_cobro")
    private LocalDateTime fechaUltimoCobro;

    @Column(name = "monto_liquidado", precision = 18, scale = 2)
    private BigDecimal montoLiquidado;

    @Column(name = "fecha_liquidacion")
    private LocalDateTime fechaLiquidacion;

    // ══════════════════════════════════════════════════════════════
    // RELACIONES
    // ══════════════════════════════════════════════════════════════

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FacturaFactoring> facturasFactoring = new ArrayList<>();

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
    public BigDecimal getSaldoPorCobrar() {
        return montoFacturasTotal.subtract(montoCobrado);
    }

    @Transient
    public boolean isLiquidada() {
        return estado == EstadoFactoring.LIQUIDADA;
    }

    @Transient
    public int getCantidadFacturas() {
        return facturasFactoring.size();
    }

    public void addFactura(FacturaFactoring facturaFactoring) {
        facturasFactoring.add(facturaFactoring);
        facturaFactoring.setSolicitud(this);
    }
}
