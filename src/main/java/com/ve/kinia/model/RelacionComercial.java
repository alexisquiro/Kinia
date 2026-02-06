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
 * Registra el historial de transacciones entre dos empresas del ecosistema.
 * 
 * Esta entidad es CLAVE para el concepto de "ecosistema cerrado":
 *   - Permite calcular el score como DEUDOR basado en datos REALES
 *   - Se actualiza automáticamente con cada factura y pago
 *   - Proporciona métricas de comportamiento comercial
 * 
 * La relación es DIRECCIONAL:
 *   - empresaProveedora → quien emite facturas (vende)
 *   - empresaCliente → quien debe/paga (compra)
 */
@Entity
@Table(name = "relaciones_comerciales", indexes = {
    @Index(name = "idx_relaciones_proveedor", columnList = "empresa_proveedora_id"),
    @Index(name = "idx_relaciones_cliente", columnList = "empresa_cliente_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_relacion_comercial", 
        columnNames = {"empresa_proveedora_id", "empresa_cliente_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RelacionComercial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ══════════════════════════════════════════════════════════════
    // LAS DOS EMPRESAS
    // ══════════════════════════════════════════════════════════════

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_proveedora_id", nullable = false)
    private Empresa empresaProveedora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_cliente_id", nullable = false)
    private Empresa empresaCliente;

    // ══════════════════════════════════════════════════════════════
    // ESTADÍSTICAS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "total_facturas")
    @Builder.Default
    private Integer totalFacturas = 0;

    @Column(name = "total_facturado", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalFacturado = BigDecimal.ZERO;

    @Column(name = "total_pagado", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalPagado = BigDecimal.ZERO;

    @Column(name = "saldo_pendiente", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal saldoPendiente = BigDecimal.ZERO;

    // ══════════════════════════════════════════════════════════════
    // COMPORTAMIENTO DE PAGO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "promedio_dias_pago")
    private Integer promedioDiasPago;

    @Column(name = "facturas_pagadas_a_tiempo")
    @Builder.Default
    private Integer facturasPagadasATiempo = 0;

    @Column(name = "facturas_pagadas_con_mora")
    @Builder.Default
    private Integer facturasPagadasConMora = 0;

    @Column(name = "peor_mora_dias")
    @Builder.Default
    private Integer peorMoraDias = 0;

    // ══════════════════════════════════════════════════════════════
    // FECHAS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "primera_transaccion")
    private LocalDateTime primeraTransaccion;

    @Column(name = "ultima_transaccion")
    private LocalDateTime ultimaTransaccion;

    // ══════════════════════════════════════════════════════════════
    // SCORE DE LA RELACIÓN
    // ══════════════════════════════════════════════════════════════

    @Min(0) @Max(100)
    @Column(name = "score_relacion")
    private Integer scoreRelacion;

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
    public Double getPorcentajePagoATiempo() {
        int total = facturasPagadasATiempo + facturasPagadasConMora;
        if (total == 0) return 0.0;
        return (facturasPagadasATiempo.doubleValue() / total) * 100;
    }
}
