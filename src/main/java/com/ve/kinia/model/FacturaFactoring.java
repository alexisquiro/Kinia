package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Tabla pivote entre SolicitudFactoring y Factura.
 * Permite incluir múltiples facturas en una solicitud.
 */
@Entity
@Table(name = "facturas_factoring", indexes = {
    @Index(name = "idx_ff_solicitud", columnList = "solicitud_id"),
    @Index(name = "idx_ff_factura", columnList = "factura_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_solicitud_factura", 
        columnNames = {"solicitud_id", "factura_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FacturaFactoring {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudFactoring solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "monto_aprobado", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoAprobado;

    @Min(0) @Max(100)
    @Column(name = "score_deudor_al_momento")
    private Integer scoreDeudorAlMomento;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
