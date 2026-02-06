package com.ve.kinia.model;

import com.ve.kinia.model.enums.TipoDocumentoArchivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa un archivo subido al sistema.
 * 
 * Tipos: RIF, cédulas, estados financieros, facturas PDF, comprobantes de pago.
 * 
 * Seguridad:
 *   - Los archivos se almacenan FUERA del webroot
 *   - Se calcula hash SHA-256 para verificar integridad
 *   - Acceso solo vía servicio autenticado
 */
@Entity
@Table(name = "documentos", indexes = {
    @Index(name = "idx_documentos_empresa", columnList = "empresa_id"),
    @Index(name = "idx_documentos_tipo", columnList = "tipo")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representante_id")
    private RepresentanteLegal representante;

    // ══════════════════════════════════════════════════════════════
    // CLASIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoDocumentoArchivo tipo;

    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;

    // ══════════════════════════════════════════════════════════════
    // ARCHIVO
    // ══════════════════════════════════════════════════════════════

    @NotBlank
    @Size(max = 255)
    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nombre_almacenado", nullable = false, length = 255)
    private String nombreAlmacenado;

    @NotBlank
    @Column(name = "ruta_almacenamiento", nullable = false, columnDefinition = "TEXT")
    private String rutaAlmacenamiento;

    @NotBlank
    @Size(max = 10)
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Size(max = 100)
    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Min(0)
    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Size(max = 64)
    @Column(name = "hash_sha256", length = 64)
    private String hashSha256;

    // ══════════════════════════════════════════════════════════════
    // VERIFICACIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "verificado")
    @Builder.Default
    private Boolean verificado = false;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    @Column(name = "verificado_por")
    private UUID verificadoPor;

    @Column(name = "notas_verificacion", columnDefinition = "TEXT")
    private String notasVerificacion;

    // ══════════════════════════════════════════════════════════════
    // VIGENCIA
    // ══════════════════════════════════════════════════════════════

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

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
    public boolean isVencido() {
        return fechaVencimiento != null && LocalDate.now().isAfter(fechaVencimiento);
    }

    @Transient
    public String getTamanoFormateado() {
        if (tamanoBytes == null) return "N/A";
        if (tamanoBytes < 1024) return tamanoBytes + " B";
        if (tamanoBytes < 1024 * 1024) return String.format("%.1f KB", tamanoBytes / 1024.0);
        return String.format("%.1f MB", tamanoBytes / (1024.0 * 1024.0));
    }

    @Transient
    public String getRutaCompleta() {
        return rutaAlmacenamiento + "/" + nombreAlmacenado;
    }
}
