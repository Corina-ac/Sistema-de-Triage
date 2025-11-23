package model;

public class NodoCambio {
    private String descripcion;
    private NodoCambio siguiente;

    public NodoCambio(String descripcion) {
        this.descripcion = descripcion;
        this.siguiente = null;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NodoCambio getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCambio siguiente) {
        this.siguiente = siguiente;
    }
}
