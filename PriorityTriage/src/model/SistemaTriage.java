package model;

public class SistemaTriage {
    private ColaPrioridad colaPrincipal;
    private ListaEnlazadaPacientes listaPacientes;
    private PilaHistorial historialCambios;
    private ColaPrioridad[] colasPorNivel;

    public SistemaTriage() {
        this.colaPrincipal = new ColaPrioridad();
        this.listaPacientes = new ListaEnlazadaPacientes();
        this.historialCambios = new PilaHistorial();
        this.colasPorNivel = new ColaPrioridad[5];
        for (int i = 0; i < 5; i++) {
            colasPorNivel[i] = new ColaPrioridad();
        }
    }

    public boolean registrarPaciente(Paciente paciente) {
        if (paciente == null) {
            return false;
        }
        if (listaPacientes.buscarPorDni(paciente.getDni()) != null) {
            return false;
        }
        listaPacientes.agregar(paciente);
        return true;
    }

    public NivelPrioridad asignarPrioridad(String sintomas) {
        if (sintomas == null || sintomas.trim().isEmpty()) {
            return NivelPrioridad.AZUL;
        }
        String sintomasLower = sintomas.toLowerCase();
        if (contieneSimboloCritico(sintomasLower)) {
            return NivelPrioridad.ROJO;
        }
        if (contieneSimboloEmergencia(sintomasLower)) {
            return NivelPrioridad.NARANJA;
        }
        if (contieneSimboloUrgente(sintomasLower)) {
            return NivelPrioridad.AMARILLO;
        }
        if (contieneSimboloPocoUrgente(sintomasLower)) {
            return NivelPrioridad.VERDE;
        }
        return NivelPrioridad.AZUL;
    }

    private boolean contieneSimboloCritico(String sintomasLower) {
        String[] criticosLower = {"dolor toracico", "infarto", "paro", "hemorragia severa", "inconsciencia", "respiracion"};
        for (int i = 0; i < criticosLower.length; i++) {
            if (sintomasLower.contains(criticosLower[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneSimboloEmergencia(String sintomasLower) {
        String[] emergenciaLower = {"fractura", "quemadura", "trauma", "convulsion", "sangrado"};
        for (int i = 0; i < emergenciaLower.length; i++) {
            if (sintomasLower.contains(emergenciaLower[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneSimboloUrgente(String sintomasLower) {
        String[] urgenteLower = {"dolor abdominal", "fiebre alta", "vomito", "diarrea severa"};
        for (int i = 0; i < urgenteLower.length; i++) {
            if (sintomasLower.contains(urgenteLower[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneSimboloPocoUrgente(String sintomasLower) {
        String[] pocoUrgenteLower = {"fiebre leve", "tos", "dolor leve", "resfriado"};
        for (int i = 0; i < pocoUrgenteLower.length; i++) {
            if (sintomasLower.contains(pocoUrgenteLower[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarAColaTriage(Paciente paciente) {
        if (paciente == null) {
            return false;
        }
        NivelPrioridad prioridad = asignarPrioridad(paciente.getSintomas());
        paciente.setNivelPrioridad(prioridad);
        colaPrincipal.encolar(paciente);
        int indice = prioridad.getCodigo() - 1;
        colasPorNivel[indice].encolar(paciente);
        String cambio = "Paciente " + paciente.getNombre() + " agregado con prioridad " + prioridad.name();
        historialCambios.apilar(cambio);
        return true;
    }

    public Paciente atenderSiguiente() {
        Paciente paciente = colaPrincipal.desencolar();
        if (paciente != null) {
            String cambio = "Paciente " + paciente.getNombre() + " atendido";
            historialCambios.apilar(cambio);
        }
        return paciente;
    }

    public boolean cambiarPrioridad(String dni, NivelPrioridad nuevaPrioridad) {
        Paciente paciente = listaPacientes.buscarPorDni(dni);
        if (paciente == null) {
            return false;
        }
        NivelPrioridad prioridadAnterior = paciente.getNivelPrioridad();
        paciente.setNivelPrioridad(nuevaPrioridad);
        String cambio = "Paciente " + paciente.getNombre() + " cambio de " + 
                       prioridadAnterior.name() + " a " + nuevaPrioridad.name();
        historialCambios.apilar(cambio);
        return true;
    }
    
    public void ordenarPorPrioridadIntercambio(Paciente[] arreglo) {
        int n = arreglo.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arreglo[j].getNivelPrioridad().getCodigo() > arreglo[j + 1].getNivelPrioridad().getCodigo()) {
                    Paciente temp = arreglo[j];
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = temp;
                }
            }
        }
    }


    public Paciente buscarPacienteCriticoRecursivo(NodoPaciente nodo) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getDato().getNivelPrioridad() == NivelPrioridad.ROJO) {
            return nodo.getDato();
        }
        return buscarPacienteCriticoRecursivo(nodo.getSiguiente());
    }

    public Paciente buscarPorDniRecursivo(String dni, NodoPaciente nodo) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getDato().getDni().equals(dni)) {
            return nodo.getDato();
        }
        return buscarPorDniRecursivo(dni, nodo.getSiguiente());
    }

    public int contarPacientesPorPrioridad(NivelPrioridad prioridad) {
        int indice = prioridad.getCodigo() - 1;
        return colasPorNivel[indice].contarElementos();
    }

    public ColaPrioridad getColaPrincipal() {
        return colaPrincipal;
    }

    public ListaEnlazadaPacientes getListaPacientes() {
        return listaPacientes;
    }

    public PilaHistorial getHistorialCambios() {
        return historialCambios;
    }

    public ColaPrioridad getColaPorNivel(int indice) {
        if (indice >= 0 && indice < 5) {
            return colasPorNivel[indice];
        }
        return null;
    }
}