package Dominio;

import java.time.LocalDate;

public class Tarifa {

    //Atributos privados

    private String deporte;
    private boolean cubierta;
    private double precioPorHora;
    private LocalDate fechaVigencia;

    //Getters

    public String getDeporte() {
        return deporte;
    }

    public boolean isCubierta() {
        return cubierta;
    }

    public double getPrecioPorHora() {
        return precioPorHora;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    //Constructor

    public Tarifa(String deporte, boolean cubierta, double precioPorHora, LocalDate fechaVigencia) {
        this.deporte = deporte;
        this.cubierta = cubierta;
        this.precioPorHora = precioPorHora;
        this.fechaVigencia = fechaVigencia;
    }

    @Override
    public String toString() {
        String tipo = cubierta ? "Cubierta" : "Abierta";
        return "Tarifa{" +
                "Deporte=" + deporte +
                ", Tipo=" + tipo +
                ", Precio/hr=" + precioPorHora +
                ", Vigencia desde=" + fechaVigencia +
                '}';
    }
}
