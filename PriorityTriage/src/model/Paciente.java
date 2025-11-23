package model;

public class Paciente extends Persona {
    private String historiaClinica;
    private String sexo;
    private String grupoSanguineo;
    private String[] alergias;
    private int numeroAlergias;
    private NivelPrioridad nivelPrioridad;
    private String sintomas;
    private int tiempoEspera;
    private String especialidadRequerida;
    private String horaLlegada;

    public Paciente(String dni, String nombre, String apellido, String fechaNacimiento,
                    String direccion, String ciudad, String historiaClinica, String sexo,
                    String grupoSanguineo, String sintomas) {
        super(dni, nombre, apellido, fechaNacimiento, direccion, ciudad);
        this.historiaClinica = historiaClinica;
        this.sexo = sexo;
        this.grupoSanguineo = grupoSanguineo;
        this.alergias = new String[10];
        this.numeroAlergias = 0;
        this.sintomas = sintomas;
        this.tiempoEspera = 0;
        this.nivelPrioridad = NivelPrioridad.AZUL;
        this.horaLlegada = obtenerHoraActual();
    }

    private String obtenerHoraActual() {
        long millis = System.currentTimeMillis();
        int horas = (int) ((millis / 3600000) % 24);
        int minutos = (int) ((millis / 60000) % 60);
        int segundos = (int) ((millis / 1000) % 60);
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    public boolean agregarAlergia(String alergia) {
        if (numeroAlergias < alergias.length) {
            alergias[numeroAlergias] = alergia;
            numeroAlergias++;
            return true;
        }
        return false;
    }

    public int contarAlergias() {
        return numeroAlergias;
    }

    public String getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(String historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String[] getAlergias() {
        return alergias;
    }

    public NivelPrioridad getNivelPrioridad() {
        return nivelPrioridad;
    }

    public void setNivelPrioridad(NivelPrioridad nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public String getEspecialidadRequerida() {
        return especialidadRequerida;
    }

    public void setEspecialidadRequerida(String especialidadRequerida) {
        this.especialidadRequerida = especialidadRequerida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }
}
