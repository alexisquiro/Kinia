package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Registro de gestiones de cobranza realizadas sobre una factura.
 */
@Entity
@Table(name = "cobranzas", indexes = {
    @Index(name = "idx_cobranzas_factura", columnList = "factura_id"),
    @Index(name = "idx_cobranzas_fecha", columnList = "fecha_gestion")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cobranza {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @NotNull
    @Column(name = "fecha_gestion", nullable = false)
    private LocalDateTime fechaGestion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "tipo_gestion", nullable = false, length = 50)
    private String tipoGestion;

    @Size(max = 100)
    @Column(name = "resultado", length = 100)
    private String resultado;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_compromiso_pago")
    private LocalDateTime fechaCompromisoPago;

    @Column(name = "gestion_automatica")
    @Builder.Default
    private Boolean gestionAutomatica = false;

    @Column(name = "realizado_por")
    private UUID realizadoPor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
