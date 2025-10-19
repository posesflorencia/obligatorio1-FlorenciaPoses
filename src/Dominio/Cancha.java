package Dominio;

public class Cancha {

    //Contador estatico para idCancha

    private static int contadorIdCancha = 1;

    //Atributos privados

    private final int idCancha;
    private String nombre;
    private String deporte;
    private boolean cubierta;
    private int capacidad;
    private String estado;
    private String caracteristicas;

    //Getters y Setters

    public int getIdCancha() {
        return idCancha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public boolean isCubierta() {
        return cubierta;
    }

    public void setCubierta(boolean cubierta) {
        this.cubierta = cubierta;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    //Constructor

    public Cancha(String nombre, String deporte, boolean cubierta,
                  int capacidad, String caracteristicas) {

        this.idCancha = contadorIdCancha++;
        this.nombre = nombre;
        this.deporte = deporte;
        this.cubierta = cubierta;
        this.capacidad = capacidad;
        this.estado = "Disponible";
        this.caracteristicas = caracteristicas;
    }

    @Override
    public String toString() {

        String tipo = cubierta ? "Cubierta" : "Abierta";

        return "Cancha{" +
                "idCancha=" + idCancha +
                ", nombre='" + nombre + '\'' +
                ", deporte='" + deporte + '\'' +
                ", Tipo=" + tipo +
                ", capacidad=" + capacidad +
                ", estado='" + estado + '\'' +
                ", caracteristicas='" + caracteristicas + '\'' +
                '}';
    }
}
