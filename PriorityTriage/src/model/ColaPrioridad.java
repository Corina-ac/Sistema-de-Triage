package model;

public class ColaPrioridad {
    private NodoPaciente frente;
    private NodoPaciente fin;

    public ColaPrioridad() {
        this.frente = null;
        this.fin = null;
    }

    public boolean encolar(Paciente paciente) {
        NodoPaciente nuevoNodo = new NodoPaciente(paciente);
        if (frente == null) {
            frente = nuevoNodo;
            fin = nuevoNodo;
            return true;
        }
        if (paciente.getNivelPrioridad().getCodigo() < frente.getDato().getNivelPrioridad().getCodigo()) {
            nuevoNodo.setSiguiente(frente);
            frente = nuevoNodo;
            return true;
        }
        NodoPaciente actual = frente;
        while (actual.getSiguiente() != null && 
               actual.getSiguiente().getDato().getNivelPrioridad().getCodigo() <= 
               paciente.getNivelPrioridad().getCodigo()) {
            actual = actual.getSiguiente();
        }
        nuevoNodo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevoNodo);
        if (nuevoNodo.getSiguiente() == null) {
            fin = nuevoNodo;
        }
        return true;
    }

    public Paciente desencolar() {
        if (frente == null) {
            return null;
        }
        Paciente paciente = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        return paciente;
    }

    public Paciente verFrente() {
        if (frente == null) {
            return null;
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        if (frente == null) {
            return true;
        }
        return false;
    }

    public int contarElementos() {
        int contador = 0;
        NodoPaciente actual = frente;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public Paciente[] convertirAArreglo() {
        int cantidad = contarElementos();
        if (cantidad == 0) {
            return new Paciente[0];
        }
        Paciente[] arreglo = new Paciente[cantidad];
        NodoPaciente actual = frente;
        int indice = 0;
        while (actual != null) {
            arreglo[indice] = actual.getDato();
            indice++;
            actual = actual.getSiguiente();
        }
        return arreglo;
    }

    public NodoPaciente getFrente() {
        return frente;
    }
}
