package com.ve.kinia.model.enums;

/**
 * Clasificación de riesgo crediticio.
 * 
 * Rangos de score:
 *   MUY_BAJO:  80-100 puntos
 *   BAJO:      65-79 puntos
 *   MEDIO:     50-64 puntos
 *   ALTO:      35-49 puntos
 *   MUY_ALTO:  0-34 puntos
 */
public enum NivelRiesgo {
    MUY_BAJO("Muy bajo riesgo", 80, 100),
    BAJO("Bajo riesgo", 65, 79),
    MEDIO("Riesgo medio", 50, 64),
    ALTO("Alto riesgo", 35, 49),
    MUY_ALTO("Muy alto riesgo", 0, 34);

    private final String descripcion;
    private final int scoreMinimo;
    private final int scoreMaximo;

    NivelRiesgo(String descripcion, int scoreMinimo, int scoreMaximo) {
        this.descripcion = descripcion;
        this.scoreMinimo = scoreMinimo;
        this.scoreMaximo = scoreMaximo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getScoreMinimo() {
        return scoreMinimo;
    }

    public int getScoreMaximo() {
        return scoreMaximo;
    }

    public static NivelRiesgo fromScore(int score) {
        for (NivelRiesgo nivel : values()) {
            if (score >= nivel.scoreMinimo && score <= nivel.scoreMaximo) {
                return nivel;
            }
        }
        return MUY_ALTO;
    }
}
