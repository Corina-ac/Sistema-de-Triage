package model;

public class ListaEnlazadaPacientes {
    private NodoPaciente cabeza;

    public ListaEnlazadaPacientes() {
        this.cabeza = null;
    }

    public boolean agregar(Paciente paciente) {
        NodoPaciente nuevoNodo = new NodoPaciente(paciente);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            return true;
        }
        NodoPaciente actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        actual.setSiguiente(nuevoNodo);
        return true;
    }

    public boolean estaVacia() {
        if (cabeza == null) {
            return true;
        }
        return false;
    }

    public int obtenerCantidad() {
        int contador = 0;
        NodoPaciente actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public Paciente obtenerPorIndice(int indice) {
        if (indice < 0 || cabeza == null) {
            return null;
        }
        NodoPaciente actual = cabeza;
        int posicion = 0;
        while (actual != null) {
            if (posicion == indice) {
                return actual.getDato();
            }
            posicion++;
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Paciente buscarPorDni(String dni) {
        NodoPaciente actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getDni().equals(dni)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean eliminarPorDni(String dni) {
        if (cabeza == null) {
            return false;
        }
        if (cabeza.getDato().getDni().equals(dni)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }
        NodoPaciente actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getDni().equals(dni)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Paciente[] convertirAArreglo() {
        int cantidad = obtenerCantidad();
        if (cantidad == 0) {
            return new Paciente[0];
        }
        Paciente[] arreglo = new Paciente[cantidad];
        NodoPaciente actual = cabeza;
        int indice = 0;
        while (actual != null) {
            arreglo[indice] = actual.getDato();
            indice++;
            actual = actual.getSiguiente();
        }
        return arreglo;
    }

    public NodoPaciente getCabeza() {
        return cabeza;
    }
}