package model;

public enum NivelPrioridad {
    ROJO(1, "Critico", "Atencion inmediata", "Riesgo vital"),
    NARANJA(2, "Emergencia", "Menos de 15 minutos", "Peligro potencial"),
    AMARILLO(3, "Urgente", "Menos de 1 hora", "Estado serio"),
    VERDE(4, "Poco Urgente", "Menos de 2 horas", "Condicion estable"),
    AZUL(5, "No Urgente", "Atencion programada", "Consulta general");

    private final int codigo;
    private final String descripcion;
    private final String tiempoAtencion;
    private final String riesgo;

    NivelPrioridad(int codigo, String descripcion, String tiempoAtencion, String riesgo) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tiempoAtencion = tiempoAtencion;
        this.riesgo = riesgo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTiempoAtencion() {
        return tiempoAtencion;
    }

    public String getRiesgo() {
        return riesgo;
    }
}