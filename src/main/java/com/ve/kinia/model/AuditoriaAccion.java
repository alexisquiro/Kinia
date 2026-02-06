package com.ve.kinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Log de auditoría para acciones importantes del sistema.
 */
@Entity
@Table(name = "auditoria_acciones", indexes = {
    @Index(name = "idx_auditoria_empresa", columnList = "empresa_id"),
    @Index(name = "idx_auditoria_fecha", columnList = "created_at"),
    @Index(name = "idx_auditoria_accion", columnList = "accion")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuditoriaAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "empresa_id")
    private UUID empresaId;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Size(max = 50)
    @Column(name = "entidad", length = 50)
    private String entidad;

    @Column(name = "entidad_id")
    private UUID entidadId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_antes", columnDefinition = "jsonb")
    private Map<String, Object> datosAntes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_despues", columnDefinition = "jsonb")
    private Map<String, Object> datosDespues;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Size(max = 500)
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
