package com.ve.kinia.model;

import com.ve.kinia.model.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa a empresas que NO están registradas en Kinia.
 * 
 * Un deudor externo es "descubierto" cuando una empresa registrada
 * sube una factura cuyo deudor no existe en el sistema.
 * 
 * Diferencias con deudor interno (Empresa):
 *   - Score ESTIMADO vs Score REAL verificado
 *   - Sin historial de pagos en plataforma
 *   - Cobranza manual (no automática)
 *   - Tasas más altas para el proveedor
 * 
 * Puede recibir invitación para registrarse y convertirse en interno.
 */
@Entity
@Table(name = "deudores_externos", indexes = {
    @Index(name = "idx_deudores_ext_registrado_por", columnList = "registrado_por_empresa_id"),
    @Index(name = "idx_deudores_ext_rif", columnList = "rif_completo")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_deudor_externo_rif", columnNames = {"tipo_rif", "numero_rif"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeudorExterno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ══════════════════════════════════════════════════════════════
    // QUIÉN LO REGISTRÓ
    // ══════════════════════════════════════════════════════════════

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por_empresa_id", nullable = false)
    private Empresa registradoPor;

    // ══════════════════════════════════════════════════════════════
    // IDENTIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rif", length = 1)
    private TipoRif tipoRif;

    @Column(name = "numero_rif", length = 9)
    private String numeroRif;

    @Column(name = "digito_verificador", length = 1)
    private String digitoVerificador;

    @Column(name = "rif_completo", length = 12)
    private String rifCompleto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", length = 15)
    private String numeroDocumento;

    // ══════════════════════════════════════════════════════════════
    // DATOS BÁSICOS
    // ══════════════════════════════════════════════════════════════

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200)
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @Size(max = 200)
    @Column(name = "nombre_comercial", length = 200)
    private String nombreComercial;

    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "sector")
    private SectorEconomico sector;

    // ══════════════════════════════════════════════════════════════
    // SCORING ESTIMADO
    // ══════════════════════════════════════════════════════════════

    @Min(0) @Max(100)
    @Column(name = "score_estimado")
    private Integer scoreEstimado;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo_estimado")
    private NivelRiesgo nivelRiesgoEstimado;

    @Column(name = "notas_evaluacion", columnDefinition = "TEXT")
    private String notasEvaluacion;

    // ══════════════════════════════════════════════════════════════
    // ESTADO
    // ══════════════════════════════════════════════════════════════

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "bloqueado")
    @Builder.Default
    private Boolean bloqueado = false;

    @Column(name = "motivo_bloqueo", columnDefinition = "TEXT")
    private String motivoBloqueo;

    // ══════════════════════════════════════════════════════════════
    // INVITACIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "invitacion_enviada")
    @Builder.Default
    private Boolean invitacionEnviada = false;

    @Column(name = "fecha_invitacion")
    private LocalDateTime fechaInvitacion;

    @Column(name = "token_invitacion", length = 100)
    private String tokenInvitacion;

    // ══════════════════════════════════════════════════════════════
    // RELACIONES
    // ══════════════════════════════════════════════════════════════

    @OneToMany(mappedBy = "deudorExterno")
    @Builder.Default
    private List<Factura> facturas = new ArrayList<>();

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

    @PrePersist
    @PreUpdate
    public void calcularRifCompleto() {
        if (tipoRif != null && numeroRif != null && digitoVerificador != null) {
            this.rifCompleto = tipoRif.name() + "-" + numeroRif + "-" + digitoVerificador;
        }
    }
}
