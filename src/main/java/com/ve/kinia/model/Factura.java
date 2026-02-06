package com.ve.kinia.model;

import com.ve.kinia.model.enums.EstadoFactura;
import com.ve.kinia.model.enums.TipoDeudor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Factura por cobrar subida para factoring.
 * 
 * El deudor puede ser:
 *   - INTERNO: Otra empresa registrada en Kinia (deudorEmpresa)
 *   - EXTERNO: Empresa no registrada (deudorExterno)
 * 
 * Ciclo de vida:
 *   PENDIENTE → EN_EVALUACION → APROBADA → ANTICIPADA → EN_COBRANZA → COBRADA
 */
@Entity
@Table(name = "facturas", indexes = {
    @Index(name = "idx_facturas_empresa", columnList = "empresa_id"),
    @Index(name = "idx_facturas_deudor_empresa", columnList = "deudor_empresa_id"),
    @Index(name = "idx_facturas_deudor_externo", columnList = "deudor_externo_id"),
    @Index(name = "idx_facturas_estado", columnList = "estado"),
    @Index(name = "idx_facturas_vencimiento", columnList = "fecha_vencimiento")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_factura_empresa", 
        columnNames = {"empresa_id", "numero_factura"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ══════════════════════════════════════════════════════════════
    // QUIEN EMITE LA FACTURA
    // ══════════════════════════════════════════════════════════════

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // ══════════════════════════════════════════════════════════════
    // DEUDOR (puede ser INTERNO o EXTERNO)
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_deudor", nullable = false)
    private TipoDeudor tipoDeudor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deudor_empresa_id")
    private Empresa deudorEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deudor_externo_id")
    private DeudorExterno deudorExterno;

    // ══════════════════════════════════════════════════════════════
    // DATOS DE LA FACTURA
    // ══════════════════════════════════════════════════════════════

    @NotBlank
    @Size(max = 50)
    @Column(name = "numero_factura", nullable = false, length = 50)
    private String numeroFactura;

    @Size(max = 50)
    @Column(name = "numero_control", length = 50)
    private String numeroControl;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @NotNull
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    // ══════════════════════════════════════════════════════════════
    // MONTOS
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "subtotal", nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "iva", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal iva = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "total", nullable = false, precision = 18, scale = 2)
    private BigDecimal total;

    @Size(max = 3)
    @Column(name = "moneda", length = 3)
    @Builder.Default
    private String moneda = "VES";

    @Column(name = "tasa_cambio_emision", precision = 18, scale = 4)
    private BigDecimal tasaCambioEmision;

    // ══════════════════════════════════════════════════════════════
    // DATOS DEL DEUDOR (snapshot)
    // ══════════════════════════════════════════════════════════════

    @Size(max = 12)
    @Column(name = "deudor_rif", length = 12)
    private String deudorRif;

    @Size(max = 200)
    @Column(name = "deudor_razon_social", length = 200)
    private String deudorRazonSocial;

    @Min(0) @Max(100)
    @Column(name = "score_deudor_al_subir")
    private Integer scoreDeudorAlSubir;

    // ══════════════════════════════════════════════════════════════
    // ESTADO
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    // ══════════════════════════════════════════════════════════════
    // DOCUMENTO ADJUNTO
    // ══════════════════════════════════════════════════════════════

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_extraidos", columnDefinition = "jsonb")
    private Map<String, Object> datosExtraidos;

    @Column(name = "extraccion_automatica")
    @Builder.Default
    private Boolean extraccionAutomatica = false;

    // ══════════════════════════════════════════════════════════════
    // NOTAS
    // ══════════════════════════════════════════════════════════════

    @Column(name = "concepto", columnDefinition = "TEXT")
    private String concepto;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    // ══════════════════════════════════════════════════════════════
    // RELACIONES
    // ══════════════════════════════════════════════════════════════

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cobranza> cobranzas = new ArrayList<>();

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PagoRecibido> pagosRecibidos = new ArrayList<>();

    @OneToMany(mappedBy = "factura")
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
    public boolean isDeudorInterno() {
        return tipoDeudor == TipoDeudor.INTERNO;
    }

    @Transient
    public boolean isVencida() {
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    @Transient
    public int getDiasParaVencer() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    @Transient
    public BigDecimal getTotalPagado() {
        return pagosRecibidos.stream()
                .map(PagoRecibido::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getSaldoPendiente() {
        return total.subtract(getTotalPagado());
    }

    @PrePersist
    @PreUpdate
    public void validarDeudor() {
        if (tipoDeudor == TipoDeudor.INTERNO && deudorEmpresa == null) {
            throw new IllegalStateException("Deudor interno requiere deudorEmpresa");
        }
        if (tipoDeudor == TipoDeudor.EXTERNO && deudorExterno == null) {
            throw new IllegalStateException("Deudor externo requiere deudorExterno");
        }
        if (empresa != null && deudorEmpresa != null && empresa.getId().equals(deudorEmpresa.getId())) {
            throw new IllegalStateException("No puede facturarse a sí mismo");
        }
    }
}
