package model;

public class PilaHistorial {
    private NodoCambio tope;

    public PilaHistorial() {
        this.tope = null;
    }

    public boolean apilar(String cambio) {
        NodoCambio nuevoNodo = new NodoCambio(cambio);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        return true;
    }

    public String desapilar() {
        if (tope == null) {
            return null;
        }
        String cambio = tope.getDescripcion();
        tope = tope.getSiguiente();
        return cambio;
    }

    public String verTope() {
        if (tope == null) {
            return null;
        }
        return tope.getDescripcion();
    }

    public boolean estaVacia() {
        if (tope == null) {
            return true;
        }
        return false;
    }

    public int contarElementos() {
        int contador = 0;
        NodoCambio actual = tope;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }
}