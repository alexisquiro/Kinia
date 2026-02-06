package com.ve.kinia.model;

import com.ve.kinia.model.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad principal del sistema.
 * 
 * Representa a los comercios/PYMES registrados en Kinia.
 * Una empresa puede actuar como:
 *   - PROVEEDOR: Emite facturas, solicita factoring, recibe anticipos
 *   - CLIENTE: Aparece como deudor en facturas de otras empresas
 * 
 * NOTA: En el MVP, Empresa = Usuario (autenticación incluida aquí).
 * A futuro se puede separar en tabla usuarios si se necesitan múltiples
 * usuarios por empresa.
 */
@Entity
@Table(name = "empresas", indexes = {
    @Index(name = "idx_empresas_email", columnList = "email"),
    @Index(name = "idx_empresas_rif", columnList = "rif_completo"),
    @Index(name = "idx_empresas_estado", columnList = "estado"),
    @Index(name = "idx_empresas_kyc", columnList = "estado_kyc")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_rif", columnNames = {"tipo_rif", "numero_rif"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ══════════════════════════════════════════════════════════════
    // DATOS DEL RIF
    // ══════════════════════════════════════════════════════════════

    @NotNull(message = "El tipo de RIF es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rif", nullable = false, length = 1)
    private TipoRif tipoRif;

    @NotBlank(message = "El número de RIF es obligatorio")
    @Pattern(regexp = "^\\d{8,9}$", message = "El número de RIF debe tener 8 o 9 dígitos")
    @Column(name = "numero_rif", nullable = false, length = 9)
    private String numeroRif;

    @NotBlank(message = "El dígito verificador es obligatorio")
    @Pattern(regexp = "^\\d$", message = "El dígito verificador debe ser un número")
    @Column(name = "digito_verificador", nullable = false, length = 1)
    private String digitoVerificador;

    @Column(name = "rif_completo", length = 12)
    private String rifCompleto;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sector", nullable = false)
    @Builder.Default
    private SectorEconomico sector = SectorEconomico.OTRO;

    @Size(max = 300)
    @Column(name = "actividad_economica", length = 300)
    private String actividadEconomica;

    // ══════════════════════════════════════════════════════════════
    // CONTACTO
    // ══════════════════════════════════════════════════════════════

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Size(max = 20)
    @Column(name = "telefono_principal", length = 20)
    private String telefonoPrincipal;

    @Size(max = 20)
    @Column(name = "telefono_secundario", length = 20)
    private String telefonoSecundario;

    // ══════════════════════════════════════════════════════════════
    // DIRECCIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "direccion_fiscal", columnDefinition = "TEXT")
    private String direccionFiscal;

    @Size(max = 50)
    @Column(name = "estado_ubicacion", length = 50)
    private String estadoUbicacion;

    @Size(max = 100)
    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Size(max = 10)
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    // ══════════════════════════════════════════════════════════════
    // DATOS DE CONSTITUCIÓN
    // ══════════════════════════════════════════════════════════════

    @Column(name = "fecha_constitucion")
    private LocalDate fechaConstitucion;

    @DecimalMin(value = "0.0")
    @Column(name = "capital_social", precision = 18, scale = 2)
    private BigDecimal capitalSocial;

    @Min(0)
    @Column(name = "numero_empleados")
    private Integer numeroEmpleados;

    // ══════════════════════════════════════════════════════════════
    // DATOS BANCARIOS
    // ══════════════════════════════════════════════════════════════

    @Size(max = 100)
    @Column(name = "banco_principal", length = 100)
    private String bancoPrincipal;

    @Size(max = 30)
    @Column(name = "numero_cuenta", length = 30)
    private String numeroCuenta;

    @Size(max = 20)
    @Column(name = "tipo_cuenta", length = 20)
    private String tipoCuenta;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(name = "porcentaje_ingresos_usd", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal porcentajeIngresosUsd = BigDecimal.ZERO;

    // ══════════════════════════════════════════════════════════════
    // ESTADO Y KYC
    // ══════════════════════════════════════════════════════════════

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoEmpresa estado = EstadoEmpresa.ACTIVA;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_kyc", nullable = false)
    @Builder.Default
    private EstadoKyc estadoKyc = EstadoKyc.PENDIENTE;

    @Column(name = "fecha_aprobacion_kyc")
    private LocalDateTime fechaAprobacionKyc;

    @Column(name = "notas_kyc", columnDefinition = "TEXT")
    private String notasKyc;

    // ══════════════════════════════════════════════════════════════
    // ROLES EN EL ECOSISTEMA
    // ══════════════════════════════════════════════════════════════

    @Column(name = "es_proveedor")
    @Builder.Default
    private Boolean esProveedor = true;

    @Column(name = "es_cliente")
    @Builder.Default
    private Boolean esCliente = true;

    // ══════════════════════════════════════════════════════════════
    // AUTENTICACIÓN (Usuario)
    // ══════════════════════════════════════════════════════════════

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "email_verificado")
    @Builder.Default
    private Boolean emailVerificado = false;

    @Column(name = "token_verificacion", length = 100)
    private String tokenVerificacion;

    @Column(name = "fecha_expiracion_token")
    private LocalDateTime fechaExpiracionToken;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "intentos_fallidos")
    @Builder.Default
    private Integer intentosFallidos = 0;

    @Column(name = "cuenta_bloqueada_hasta")
    private LocalDateTime cuentaBloqueadaHasta;

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
    // RELACIONES
    // ══════════════════════════════════════════════════════════════

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RepresentanteLegal> representantes = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Documento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DatosFinancieros> datosFinancieros = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Score> scores = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Factura> facturasEmitidas = new ArrayList<>();

    @OneToMany(mappedBy = "deudorEmpresa")
    @Builder.Default
    private List<Factura> facturasComoDeudor = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SolicitudFactoring> solicitudesFactoring = new ArrayList<>();

    @OneToMany(mappedBy = "registradoPor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeudorExterno> deudoresExternosRegistrados = new ArrayList<>();

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

    @Transient
    public Score getScoreVigente() {
        return scores.stream()
                .filter(Score::getEsVigente)
                .findFirst()
                .orElse(null);
    }

    @Transient
    public boolean isCuentaBloqueada() {
        return cuentaBloqueadaHasta != null && 
               LocalDateTime.now().isBefore(cuentaBloqueadaHasta);
    }

    @Transient
    public Integer getAntiguedadAnios() {
        if (fechaConstitucion == null) return null;
        return Period.between(fechaConstitucion, LocalDate.now()).getYears();
    }

    @Transient
    public boolean puedeOperar() {
        return estado == EstadoEmpresa.ACTIVA && 
               estadoKyc == EstadoKyc.APROBADO;
    }

    public void addRepresentante(RepresentanteLegal representante) {
        representantes.add(representante);
        representante.setEmpresa(this);
    }

    public void addDocumento(Documento documento) {
        documentos.add(documento);
        documento.setEmpresa(this);
    }
}
