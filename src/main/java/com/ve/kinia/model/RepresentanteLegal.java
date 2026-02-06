package com.ve.kinia.model;

import com.ve.kinia.model.enums.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa a una persona física que actúa como representante legal
 * de una empresa registrada en Kinia.
 * 
 * Propósito:
 *   - Cumplimiento KYC (Know Your Customer)
 *   - Verificación de identidad
 *   - Autorización para firmar contratos
 * 
 * Una empresa puede tener múltiples representantes legales.
 */
@Entity
@Table(name = "representantes_legales", indexes = {
    @Index(name = "idx_representantes_empresa", columnList = "empresa_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_representante_empresa", 
        columnNames = {"empresa_id", "tipo_documento", "numero_documento"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RepresentanteLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // ══════════════════════════════════════════════════════════════
    // IDENTIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    @Builder.Default
    private TipoDocumento tipoDocumento = TipoDocumento.CEDULA_V;

    @NotBlank
    @Size(max = 15)
    @Column(name = "numero_documento", nullable = false, length = 15)
    private String numeroDocumento;

    // ══════════════════════════════════════════════════════════════
    // DATOS PERSONALES
    // ══════════════════════════════════════════════════════════════

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Past
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Size(max = 50)
    @Column(name = "nacionalidad", length = 50)
    @Builder.Default
    private String nacionalidad = "Venezolana";

    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    // ══════════════════════════════════════════════════════════════
    // CARGO
    // ══════════════════════════════════════════════════════════════

    @NotBlank
    @Size(max = 100)
    @Column(name = "cargo", nullable = false, length = 100)
    private String cargo;

    @Column(name = "es_firmante_autorizado")
    @Builder.Default
    private Boolean esFirmanteAutorizado = true;

    // ══════════════════════════════════════════════════════════════
    // VERIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "documento_verificado")
    @Builder.Default
    private Boolean documentoVerificado = false;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    // ══════════════════════════════════════════════════════════════
    // RELACIONES
    // ══════════════════════════════════════════════════════════════

    @OneToMany(mappedBy = "representante")
    @Builder.Default
    private List<Documento> documentos = new ArrayList<>();

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
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Transient
    public String getDocumentoCompleto() {
        return tipoDocumento.getPrefijo() + "-" + numeroDocumento;
    }
}
