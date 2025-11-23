package controller;

import model.Paciente;
import model.SistemaTriage;
import model.NivelPrioridad;
import view.TriageView;

public class TriageController {
    private SistemaTriage sistema;
    private TriageView vista;

    public TriageController() {
        this.sistema = new SistemaTriage();
    }

    public void setVista(TriageView vista) {
        this.vista = vista;
    }

    private boolean esLetra(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        if (c == ' ') {
            return true;
        }
        if (c == 'ñ' || c == 'Ñ' ||
            c == 'á' || c == 'Á' || c == 'é' || c == 'É' ||
            c == 'í' || c == 'Í' || c == 'ó' || c == 'Ó' ||
            c == 'ú' || c == 'Ú' || c == 'ü' || c == 'Ü') {
            return true;
        }
        return false;
    }

    private boolean esNumero(String texto) {
        if (texto == null || texto.length() == 0) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private int convertirAEntero(String texto) {
        if (!esNumero(texto)) {
            return -1;
        }
        int resultado = 0;
        int potencia = 1;
        for (int i = texto.length() - 1; i >= 0; i--) {
            char digitoChar = texto.charAt(i);
            int digito = digitoChar - '0';
            resultado = resultado + digito * potencia;
            potencia = potencia * 10;
        }
        return resultado;
    }

    private String validarDNI(String dni) {
        if (dni == null || dni.length() != 10) {
            return "El DNI debe tener exactamente 10 dígitos.";
        }
        if (!esNumero(dni)) {
            return "El DNI solo puede contener números (0-9).";
        }
        return null;
    }
    
    private String validarSoloLetras(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            return "El " + nombreCampo + " es obligatorio.";
        }
        for (int i = 0; i < texto.length(); i++) {
            if (!esLetra(texto.charAt(i))) {
                return "El " + nombreCampo + " solo puede contener letras y espacios.";
            }
        }
        return null;
    }

    private String validarSintomas(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "Los síntomas son obligatorios.";
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (!(esLetra(c) || c == ',' || c == '.')) { 
                return "Los síntomas solo pueden contener letras, espacios, comas (,) y puntos (.).";
            }
        }
        return null;
    }

    private String validarFechaNacimiento(String fecha) {
        if (fecha == null || fecha.length() != 10 || fecha.charAt(4) != '-' || fecha.charAt(7) != '-') {
            return "La Fecha de Nacimiento debe tener el formato estricto: YYYY-MM-DD.";
        }

        String sAnio = fecha.substring(0, 4);
        String sMes = fecha.substring(5, 7);
        String sDia = fecha.substring(8, 10);

        if (!esNumero(sAnio) || !esNumero(sMes) || !esNumero(sDia)) {
            return "El año, mes y día deben ser valores numéricos.";
        }

        int anio = convertirAEntero(sAnio);
        int mes = convertirAEntero(sMes);
        int dia = convertirAEntero(sDia);

        if (anio < 1900 || anio > 2024 || mes < 1 || mes > 12 || dia < 1 || dia > 31) {
            return "Fecha fuera de rango (1900-2024, mes 1-12, día 1-31).";
        }

        int diasEnMes;
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            diasEnMes = 30;
        } else if (mes == 2) {
            boolean esBisiesto = (anio % 4 == 0) && (anio % 100 != 0 || anio % 400 == 0);
            diasEnMes = esBisiesto ? 29 : 28;
        } else {
            diasEnMes = 31;
        }

        if (dia > diasEnMes) {
            return "Fecha inválida. El mes " + mes + " de " + anio + " solo tiene " + diasEnMes + " días.";
        }

        return null;
    }


    public void manejarRegistroPacienteGUI(String dni, String nombre, String apellido, 
                                           String fechaNacimiento, String sintomas) {
        
        String d = dni != null ? dni.trim() : null;
        String n = nombre != null ? nombre.trim() : null;
        String a = apellido != null ? apellido.trim() : null;
        String f = fechaNacimiento != null ? fechaNacimiento.trim() : null;
        String s = sintomas != null ? sintomas.trim() : null;
        
        String errorDni = validarDNI(d);
        if (errorDni != null) { vista.mostrarError(errorDni); return; }
        String errorNombre = validarSoloLetras(n, "nombre");
        if (errorNombre != null) { vista.mostrarError(errorNombre); return; }
        String errorApellido = validarSoloLetras(a, "apellido");
        if (errorApellido != null) { vista.mostrarError(errorApellido); return; }
        String errorFecha = validarFechaNacimiento(f);
        if (errorFecha != null) { vista.mostrarError(errorFecha); return; }
        String errorSintomas = validarSintomas(s);
        if (errorSintomas != null) { vista.mostrarError(errorSintomas); return; }

        Paciente paciente = new Paciente(d, n, a, f, 
                                        "", "", "HC-" + d, "M", "O+", s);
        boolean registrado = sistema.registrarPaciente(paciente);
        
        if (!registrado) {
            vista.mostrarError("Paciente ya existe con ese DNI: " + d);
            return;
        }
        
        boolean encolado = sistema.agregarAColaTriage(paciente);
        if (encolado) {
            vista.mostrarExito("Paciente registrado con prioridad: " + paciente.getNivelPrioridad().getDescripcion());
            vista.limpiarCamposRegistro();
            actualizarColaEnVista();
        } else {
            vista.mostrarError("Error al agregar a cola de triage");
        }
    }

    public void manejarAtenderSiguiente() {
        Paciente paciente = sistema.atenderSiguiente();
        if (paciente == null) {
            vista.mostrarError("No hay pacientes en cola");
            return;
        }
        vista.mostrarDetallesPaciente(paciente, "Paciente Atendido");
        actualizarColaEnVista();
    }

    public void manejarCambioPrioridad(String dni, NivelPrioridad nuevaPrioridad) {
        if (dni == null || dni.trim().isEmpty()) {
            vista.mostrarError("DNI invalido");
            return;
        }
        boolean cambiado = sistema.cambiarPrioridad(dni.trim(), nuevaPrioridad);
        if (cambiado) {
            vista.mostrarExito("Prioridad cambiada exitosamente a " + nuevaPrioridad.getDescripcion());
            actualizarColaEnVista();
        } else {
            vista.mostrarError("Paciente no encontrado");
        }
    }
   
    public void manejarOrdenamientoIntercambio() {
        if (sistema.getListaPacientes().estaVacia()) {
            vista.mostrarError("La lista de pacientes está vacía. No hay nada que ordenar.");
            return;
        }

        Paciente[] pacientesOriginales = sistema.getListaPacientes().convertirAArreglo();

        vista.mostrarListaPacientes(pacientesOriginales, "7. Antes del Ordenamiento por Intercambio - Estado Original");
        
        Paciente[] pacientesOrdenados = new Paciente[pacientesOriginales.length];
        System.arraycopy(pacientesOriginales, 0, pacientesOrdenados, 0, pacientesOriginales.length);

        sistema.ordenarPorPrioridadIntercambio(pacientesOrdenados);

        vista.mostrarListaPacientes(pacientesOrdenados, "7. Después del Ordenamiento por Intercambio - Prioridad Ascendente");
        
        vista.mostrarExito("La demostración de ordenamiento por Intercambio (Antes/Después) se ha completado.");
    }


    public void manejarBusquedaRecursiva(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            vista.mostrarError("DNI invalido");
            return;
        }
        Paciente encontrado = sistema.buscarPorDniRecursivo(dni.trim(), sistema.getListaPacientes().getCabeza());
        if (encontrado != null) {
            vista.mostrarDetallesPaciente(encontrado, "Busqueda Recursiva - Paciente Encontrado");
        } else {
            vista.mostrarError("Paciente no encontrado mediante busqueda recursiva");
        }
    }

    public void manejarGestionMemoriaEstatica() {
        Paciente[] arreglo = sistema.getListaPacientes().convertirAArreglo();
        vista.mostrarListaPacientes(arreglo, "4. Gestion Memoria Estatica - Arreglo de Pacientes");
    }

    private void actualizarColaEnVista() {
        Paciente[] pacientesEnCola = sistema.getColaPrincipal().convertirAArreglo();
        vista.actualizarColaEspera(pacientesEnCola);
    }

    public SistemaTriage getSistema() {
        return sistema;
    }
}