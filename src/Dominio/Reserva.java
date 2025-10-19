package Dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reserva {

    //Contador estatico para idReserva

    private static int contadorIdReserva = 1;

    //Atributos privados

    private final int idReserva;
    private Socio socio;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private double duracionHoras;
    private boolean abonoCompleto;
    private LocalDateTime fechaRegistro;
    private String observacion;

    //Getters y Setters

    public int getIdReserva() {
        return idReserva;
    }

    public Socio getSocio() {
        return socio;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public double getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(double duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public boolean isAbonoCompleto() {
        return abonoCompleto;
    }

    public void setAbonoCompleto(boolean abonoCompleto) {
        this.abonoCompleto = abonoCompleto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    //Constructor

    public Reserva(Socio socio, Cancha cancha, LocalDate fecha, LocalTime horaInicio,
                   double duracionHoras, String observacion) {

        this.idReserva = contadorIdReserva++;
        this.socio = socio;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionHoras = duracionHoras;
        this.abonoCompleto = false; // Estado inicial por defecto
        this.fechaRegistro = LocalDateTime.now();
        this.observacion = observacion;
    }

    //Metodo para calcular la hora de finalizacion de la reserva

    public LocalTime getHoraFin() {
        // Extrae las horas enteras
        long horas = (long) duracionHoras;

        // Extrae la parte decimal (minutos)
        long minutos = (long) ((duracionHoras % 1) * 60);

        // Se suman horas y minutos
        return horaInicio.plusHours(horas).plusMinutes(minutos);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "ID=" + idReserva +
                // Muestra datos concisos de las entidades relacionadas
                ", Socio ID=" + socio.getIdSocio() + " (" + socio.getNombre() + " " + socio.getApellidoPaterno() + ")" +
                ", Cancha ID=" + cancha.getIdCancha() + " (" + cancha.getNombre() + ")" +
                ", Fecha=" + fecha +
                ", Horario=" + horaInicio + " a " + getHoraFin() +
                ", Duración=" + duracionHoras + " hrs" +
                ", Abono=" + (abonoCompleto ? "SÍ" : "NO") +
                ", Registrada=" + fechaRegistro.toLocalDate() +
                '}';
    }

}
