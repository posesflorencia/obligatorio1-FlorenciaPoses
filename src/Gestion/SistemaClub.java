package Gestion;

import Dominio.Socio;
import Dominio.Cancha;
import Dominio.Reserva;
import Dominio.Tarifa;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class SistemaClub {

    //Almacenamiento en listas:
    private final ArrayList<Socio> listaSocios;
    private final ArrayList<Cancha> listaCanchas;
    private final ArrayList<Reserva> listaReservas;
    private final ArrayList<Tarifa> listaTarifas;

    //Constructor
    public SistemaClub() {
        this.listaSocios = new ArrayList<>();
        this.listaCanchas = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
        this.listaTarifas = new ArrayList<>();
        this.cargarDatosIniciales();
    }

    //Getters

    public ArrayList<Socio> getListaSocios() { return listaSocios; }
    public ArrayList<Cancha> getListaCanchas() { return listaCanchas; }
    public ArrayList<Reserva> getListaReservas() { return listaReservas; }
    public ArrayList<Tarifa> getListaTarifas() { return listaTarifas; }

    //Logica de negocio:

    //Metodos de CRUD y Busqueda Basica

    //Socios:

    public boolean registrarSocio(Socio nuevoSocio) {
        for (Socio s : listaSocios) {
            if (s.getNumDocumento().equals(nuevoSocio.getNumDocumento())) {
                return false;
            }
        }
        listaSocios.add(nuevoSocio);
        return true;
    }

    public Socio buscarSocioPorId(int id) {
        for (Socio s : listaSocios) {
            if (s.getIdSocio() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean modificarSocio(int idSocio, String telefono, String pais, String apellidoMaterno) {
        Socio socioAModificar = buscarSocioPorId(idSocio);

        if (socioAModificar != null) {
            socioAModificar.setTelefono(telefono);
            socioAModificar.setPais(pais);
            socioAModificar.setApellidoMaterno(apellidoMaterno);
            return true;
        }
        return false;
    }

    public boolean eliminarSocio(int idSocio) {
        Socio socioAEliminar = buscarSocioPorId(idSocio);

        if (socioAEliminar == null) {
            return false;
        }

        //Verificar si tiene reservas:

        for (Reserva r : listaReservas) {
            if (r.getSocio().getIdSocio() == idSocio && !r.getFecha().isBefore(LocalDate.now())) {
                return false;
            }
        }

        return listaSocios.remove(socioAEliminar);
    }

    //Cancha:

    public void registrarCancha(Cancha nuevaCancha) {
        listaCanchas.add(nuevaCancha);
    }

    public Cancha buscarCanchaPorId(int id) {
        for (Cancha c : listaCanchas) {
            if (c.getIdCancha() == id) {
                return c;
            }
        }
        return null;
    }

    public boolean modificarCancha(int idCancha, String nombre, String deporte,
                                   boolean cubierta, int capacidad, String caracteristicas) {
        Cancha canchaAModificar = buscarCanchaPorId(idCancha);

        if (canchaAModificar == null) {
            return false;
        }

        //Verificar si hay reservas futuras pendientes:

        boolean tieneReservasFuturas = false;

        for (Reserva r : listaReservas) {
            if (r.getCancha().getIdCancha() == idCancha && !r.getFecha().isBefore(LocalDate.now())) {
                tieneReservasFuturas = true;
                break;
            }
        }

        //Si hay reservas futuras, solo permitir modificaciones no criticas para la tarifa:

        if (tieneReservasFuturas) {

            //Si intenta cambiar deporte o tipo (cubierta), se rechaza:

            if (!canchaAModificar.getDeporte().equalsIgnoreCase(deporte) ||
                    canchaAModificar.isCubierta() != cubierta) {

                System.out.println("ADVERTENCIA (SistemaClub): Error de integridad. No se pueden cambiar el Deporte o la Condición (Cubierta/Descubierta) de la cancha ID " + idCancha + " porque tiene reservas futuras pendientes.");
                return false;
            }
        }

        //Aplicar las modificaciones:
        //Si hay reservas futuras, solo se modificarán el nombre, capacidad y características.

        canchaAModificar.setNombre(nombre);
        canchaAModificar.setDeporte(deporte);
        canchaAModificar.setCubierta(cubierta);
        canchaAModificar.setCapacidad(capacidad);
        canchaAModificar.setCaracteristicas(caracteristicas);
        return true;
    }

    public boolean eliminarCancha(int idCancha) {
        Cancha canchaAEliminar = buscarCanchaPorId(idCancha);

        if (canchaAEliminar == null) {
            return false;
        }

        //Verificar si tiene reservas:

        for (Reserva r : listaReservas) {
            if (r.getCancha().getIdCancha() == idCancha && !r.getFecha().isBefore(LocalDate.now())) {
                return false;
            }
        }

        return listaCanchas.remove(canchaAEliminar);
    }

    //Reserva:

    public boolean validarSuperposicion(Cancha cancha, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        //Verificar estado de la cancha:

//        if (!cancha.getEstado().equalsIgnoreCase("Disponible")) {
//            return true;
//        }

        //Recorrer las reservas para verificar si hay solapamiento:

        for (Reserva r : listaReservas) {

            if (r.getCancha().getIdCancha() == cancha.getIdCancha() && r.getFecha().isEqual(fecha)) {

                LocalTime r_horaInicio = r.getHoraInicio();
                LocalTime r_horaFin = r.getHoraFin();

                if (horaInicio.isBefore(r_horaFin) && horaFin.isAfter(r_horaInicio)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean crearReserva(Socio socio, Cancha cancha, LocalDate fecha,
                                LocalTime horaInicio, double duracionHoras, String observacion) {

        //Validar duracion:

        if (duracionHoras <= 0) return false;

        //Calcular hora de fin:

        Reserva tempReserva = new Reserva(socio, cancha, fecha, horaInicio, duracionHoras, observacion);
        LocalTime horaFin = tempReserva.getHoraFin();

        //Validar Superposicion:

        if (validarSuperposicion(cancha, fecha, horaInicio, horaFin)) {
            return false;
        }

        //Registrar la reserva
        listaReservas.add(tempReserva);
        return true;
    }

    public boolean cancelarReserva(int idReserva) {

        //Buscar la reserva por su ID:

        Reserva reservaAEncontrar = null;
        for (Reserva r : listaReservas) {
            if (r.getIdReserva() == idReserva) {
                reservaAEncontrar = r;
                break;
            }
        }

        if (reservaAEncontrar != null) {

            //Antes de eliminar, actualizar el estado de la cancha a "Disponible":

            reservaAEncontrar.getCancha().setEstado("Disponible");

            //Eliminar la reserva de la lista
            return listaReservas.remove(reservaAEncontrar);
        }

        return false;
    }

    //Tarifa:

    public void registrarTarifa(Tarifa nuevaTarifa) {
        this.listaTarifas.add(nuevaTarifa);
    }

    //Metodo para encontrar la tarifa mas reciente:

    public Tarifa obtenerTarifaVigente(Cancha cancha, LocalDate fechaReserva) {
        Tarifa tarifaVigente = null;

        //Normalizar el deporte de la Cancha para la busqueda:

        String deporteCanchaNormalizado = normalizarTexto(cancha.getDeporte());

        for (Tarifa t : listaTarifas) {

            //Normalizar el deporte de la Tarifa:

            String deporteTarifaNormalizado = normalizarTexto(t.getDeporte());
            if (deporteTarifaNormalizado.equals(deporteCanchaNormalizado) && t.isCubierta() == cancha.isCubierta()) {
                if (!t.getFechaVigencia().isAfter(fechaReserva)) {
                    if (tarifaVigente == null || t.getFechaVigencia().isAfter(tarifaVigente.getFechaVigencia())) {
                        tarifaVigente = t;
                    }
                }
            }
        }
        return tarifaVigente;
    }

    public double calcularCostoTurno(Reserva reserva) {
        Cancha c = reserva.getCancha();
        Tarifa t = obtenerTarifaVigente(c, reserva.getFecha());
        if (t != null) {
            return t.getPrecioPorHora() * reserva.getDuracionHoras();
        }
        return 0.0;
    }

    public double calcularSaldoFinal(Reserva reserva, double costoExtras) {
        double costoBase = calcularCostoTurno(reserva);
        if (reserva.isAbonoCompleto()) {
            costoBase = 0.0;
        }
        return costoBase + costoExtras;
    }

    //Metodos de consulta:

    //Canchas por deporte:

    public ArrayList<Cancha> consultarCanchasPorDeporte(String deporte) {
        ArrayList<Cancha> resultado = new ArrayList<>();

        //Normalizar la entrada del usuario

        String deporteNormalizado = normalizarTexto(deporte);

        for (Cancha c : listaCanchas) {

            //Normalizar el deporte de la cancha

            String canchaDeporteNormalizado = normalizarTexto(c.getDeporte());
            if (canchaDeporteNormalizado.equals(deporteNormalizado)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    //Canchas por nombre:

    public ArrayList<Cancha> consultarCanchasPorNombre(String nombre) {
        ArrayList<Cancha> resultado = new ArrayList<>();

        //Normalizar la entrada del usuario

        String nombreNormalizado = normalizarTexto(nombre);

        for (Cancha c : listaCanchas) {

            //Normalizar el nombre de la cancha

            if (normalizarTexto(c.getNombre()).contains(nombreNormalizado)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    //Canchas por condicion (cubierta o abierta):

    public ArrayList<Cancha> consultarCanchasPorCondicion(boolean esCubierta) {
        ArrayList<Cancha> resultado = new ArrayList<>();
        for (Cancha c : listaCanchas) {
            if (c.isCubierta() == esCubierta) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    //Metodo que se utilizara en el main para consulta canchas por fecha y hora:

    public boolean estaLibre(Cancha cancha, LocalDate fecha, LocalTime horaInicio, double duracionHoras) {

        //Crear una Reserva temporal solo para calcular la hora de fin
        //El socio es null porque solo nos importa la cancha y el tiempo para la validacion.

        Reserva tempReserva = new Reserva(null, cancha, fecha, horaInicio, duracionHoras, "");
        LocalTime horaFin = tempReserva.getHoraFin();

        //validarSuperposicion devuelve TRUE si HAY solapamiento (NO esta disponible).
        //Si esta libre, devolvemos la negacion del resultado.

        return !validarSuperposicion(cancha, fecha, horaInicio, horaFin);
    }

    //Canchas disponibles en una fecha (devuelve las que no tienen reservas para esa fecha):

    public ArrayList<Cancha> filtrarCanchasSinReservaPrevia(LocalDate fecha) {
        ArrayList<Cancha> canchasSinReserva = new ArrayList<>();

        for (Cancha cancha : listaCanchas) {
            boolean tieneReserva = false;
            for (Reserva r : listaReservas) {

                //Si encuentra alguna reserva para esta cancha en la fecha:

                if (r.getCancha().getIdCancha() == cancha.getIdCancha() && r.getFecha().isEqual(fecha)) {
                    tieneReserva = true;
                    break;
                }
            }
            if (!tieneReserva) {
                canchasSinReserva.add(cancha);
            }
        }
        return canchasSinReserva;
    }

    //Canchas con reserva previa en una fecha dada:

    public ArrayList<Cancha> filtrarCanchasConReservaPrevia(LocalDate fecha) {
        ArrayList<Cancha> canchasConReserva = new ArrayList<>();

        for (Cancha cancha : listaCanchas) {
            for (Reserva r : listaReservas) {

                //Si encuentra alguna reserva para la cancha la agrega a la lista:

                if (r.getCancha().getIdCancha() == cancha.getIdCancha() && r.getFecha().isEqual(fecha)) {
                    if (!canchasConReserva.contains(cancha)) {
                        canchasConReserva.add(cancha);
                    }
                    break;
                }
            }
        }
        return canchasConReserva;
    }

    //Carga Inicial de Datos:

    private void cargarDatosIniciales() {

        //TARIFAS:

        // Fútbol Cubierta: Tarifa Antigua
        listaTarifas.add(new Tarifa("Fútbol", true, 900.0, LocalDate.of(2023, 1, 1)));
        // Fútbol Descubierta:
        listaTarifas.add(new Tarifa("Fútbol", false, 700.0, LocalDate.of(2023, 1, 1)));
        // Fútbol Cubierta: Tarifa Vigente (Desde una fecha posterior)
        listaTarifas.add(new Tarifa("Fútbol", true, 1050.0, LocalDate.of(2024, 6, 15)));
        // Tenis Descubierta:
        listaTarifas.add(new Tarifa("Tenis", false, 650.0, LocalDate.of(2023, 1, 1)));
        // Padel descubierta:
        listaTarifas.add(new Tarifa("Pádel", false, 800.0, LocalDate.of(2024, 7, 1)));


        //CANCHAS:

        Cancha c1 = new Cancha("Cancha Principal F1", "Fútbol", true, 10, "Césped Sintético Nuevo");
        Cancha c2 = new Cancha("Cancha Auxiliar F2", "Fútbol", false, 8, "Césped Natural");
        Cancha c3 = new Cancha("Cancha Tenis 1", "Tenis", false, 4, "Arcilla");
        Cancha c4 = new Cancha("Cancha Padel A1", "Pádel", false, 4, "Sintético");
        Cancha c5 = new Cancha("Cancha Voley Cub", "Voley", true, 12, "Parquet");

        listaCanchas.add(c1);
        listaCanchas.add(c2);
        listaCanchas.add(c3);
        listaCanchas.add(c4);
        listaCanchas.add(c5);

        //SOCIOS:

        Socio s1 = new Socio("Ana", "García", "Pérez", "45678901", LocalDate.of(1990, 5, 10), "099111222", "Uruguay");
        listaSocios.add(s1);

        Socio s2 = new Socio("Juan", "Díaz", "", "12345678", LocalDate.of(1985, 8, 20), "098777666", "Argentina");
        listaSocios.add(s2);

        //RESERVAS:

        LocalDate manana = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        // Reserva ID 1: s1 en c1, Mañana, 10:00 a 11:30
        listaReservas.add(new Reserva(s1, c1, manana, horaInicio, 1.5, "Reserva de prueba para solapamiento"));
        c1.setEstado("Reservada"); // Actualizamos el estado para reflejar que está ocupada

        // Reserva ID 2: s2 en c2, Mañana, 12:00 a 14:00
        listaReservas.add(new Reserva(s2, c2, manana, LocalTime.of(12, 0), 2.0, "Reserva que se puede cancelar"));
        c2.setEstado("Reservada");

        // Reserva ID 3: s1 en c1, Mañana, 15:00 a 16:00
        listaReservas.add(new Reserva(s1, c1, manana, LocalTime.of(15, 0), 1.0, "Reserva de tarde en F1"));
    }

    //Metodo auxiliar para manejar acentos en las entradas de texto:

    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String textoSinAcentos = texto.toLowerCase();

        textoSinAcentos = textoSinAcentos.replace('á', 'a');
        textoSinAcentos = textoSinAcentos.replace('é', 'e');
        textoSinAcentos = textoSinAcentos.replace('í', 'i');
        textoSinAcentos = textoSinAcentos.replace('ó', 'o');
        textoSinAcentos = textoSinAcentos.replace('ú', 'u');

        return textoSinAcentos;
    }
}