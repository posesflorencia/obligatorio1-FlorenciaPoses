package Dominio;

import java.time.LocalDate;

public class Socio {

    //Contador estatico para idSocio

    private static int contadorId = 1;

    //Atributos privados

    private final int idSocio;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numDocumento;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String pais;

    //Getters y Setters

    public int getIdSocio() {
        return idSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    //Constructor

    public Socio(String nombre, String apellidoPaterno, String apellidoMaterno, String numDocumento,
                 LocalDate fechaNacimiento, String telefono, String pais) {

        this.idSocio = contadorId++;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        //Maneja que sea opcional el apellido materno
        this.apellidoMaterno = apellidoMaterno != null ? apellidoMaterno : "";
        this.numDocumento = numDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.pais = pais;
    }

    @Override
    public String toString() {
        //Construir el Apellido Completo (Manejando el campo opcional aMaterno)

        String apellidoCompleto = apellidoPaterno;

        //Solo agrega el segundo apellido si existe y no esta vacio

        if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
            apellidoCompleto += " " + apellidoMaterno;
        }

        return "Socio{" +
                "ID=" + idSocio +
                ", Nombre='" + nombre + " " + apellidoCompleto + '\'' +
                ", Documento='" + numDocumento + '\'' +
                ", FechaNacimiento=" + fechaNacimiento +
                ", Teléfono='" + telefono + '\'' +
                ", País='" + pais + '\'' +
                '}';
    }
}
