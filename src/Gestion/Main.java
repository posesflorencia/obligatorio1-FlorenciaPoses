package Gestion;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import Dominio.Socio;
import Dominio.Cancha;
import Dominio.Reserva;
import Dominio.Tarifa;

import java.util.ArrayList;

public class Main {

    private static SistemaClub sistema = new SistemaClub();
    private static Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {

        System.out.println("--- INICIANDO SISTEMA DE GESTIÓN DE CLUB ---");
        int opcion;

        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    menuGestionSocios();
                    break;
                case 2:
                    menuGestionCanchas();
                    break;
                case 3:
                    menuGestionReservas();
                    break;
                case 4: // <-- NUEVO
                    menuGestionTarifas();
                    break;
                case 5: // <-- Antes era 4
                    menuConsultas();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    //Menu principal:

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=============================");
        System.out.println("       MENÚ PRINCIPAL");
        System.out.println("=============================");
        System.out.println("1. Gestión de Socios");
        System.out.println("2. Gestión de Canchas");
        System.out.println("3. Gestión de Reservas");
        System.out.println("4. Gestión de Tarifas");
        System.out.println("5. Consultas y Reportes");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Ingrese un número.");
            scanner.next();
            System.out.print("Seleccione una opción: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    private static LocalDate leerFecha(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (dd/MM/yyyy): ");
                String fechaStr = scanner.nextLine();
                return LocalDate.parse(fechaStr, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha incorrecto. Use dd/MM/yyyy.");
            }
        }
    }

    private static LocalTime leerHora(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (HH:mm): ");
                String horaStr = scanner.nextLine();
                return LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("H:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora incorrecto. Use HH:mm.");
            }
        }
    }

    //Gestion de socios:

    private static void menuGestionSocios() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE SOCIOS ---");
            System.out.println("1. Registrar nuevo socio");
            System.out.println("2. Listar todos los socios");
            System.out.println("3. Modificar datos de socio"); // <-- Nuevo
            System.out.println("4. Dar de baja socio");       // <-- Nuevo
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    registrarSocio();
                    break;
                case 2:
                    System.out.println("\n--- LISTADO DE SOCIOS ---");
                    if (sistema.getListaSocios().isEmpty()) {
                        System.out.println("No hay socios registrados.");
                    } else {
                        for (Socio s : sistema.getListaSocios()) {
                            System.out.println(s);
                        }
                    }
                    break;
                case 3:
                    modificarSocio();
                    break;
                case 4:
                    darDeBajaSocio();
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarSocio() {
        System.out.println("\n--- REGISTRO DE SOCIO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido Paterno: ");
        String aPaterno = scanner.nextLine();
        System.out.print("Apellido Materno (Opcional, Enter para omitir): ");
        String aMaterno = scanner.nextLine();
        System.out.print("Número de Documento: ");
        String numDoc = scanner.nextLine();
        LocalDate fechaNac = leerFecha("Fecha de Nacimiento");
        System.out.print("Teléfono: ");
        String tel = scanner.nextLine();
        System.out.print("País: ");
        String pais = scanner.nextLine();

        Socio nuevoSocio = new Socio(nombre, aPaterno, aMaterno, numDoc, fechaNac, tel, pais);

        if (sistema.registrarSocio(nuevoSocio)) {
            System.out.println("Socio registrado exitosamente. ID: " + nuevoSocio.getIdSocio());
        } else {
            System.out.println("ERROR: Ya existe un socio registrado con el número de documento " + numDoc);
        }
    }

    private static void modificarSocio() {
        System.out.println("\n--- MODIFICAR DATOS DE SOCIO ---");
        System.out.print("Ingrese ID del Socio a modificar: ");
        int idSocio = leerOpcion();

        Socio socioActual = sistema.buscarSocioPorId(idSocio);
        if (socioActual == null) {
            System.out.println("Socio no encontrado.");
            return;
        }

        System.out.println("Socio: " + socioActual.getNombre() + " " + socioActual.getApellidoPaterno());
        System.out.print("Nuevo Teléfono (actual: " + socioActual.getTelefono() + " - Enter para mantener): ");
        String nuevoTel = scanner.nextLine();
        String telFinal = nuevoTel.trim().isEmpty() ? socioActual.getTelefono() : nuevoTel;
        System.out.print("Nuevo País (actual: " + socioActual.getPais() + " - Enter para mantener): ");
        String nuevoPais = scanner.nextLine();
        String paisFinal = nuevoPais.trim().isEmpty() ? socioActual.getPais() : nuevoPais;
        System.out.print("Nuevo Apellido Materno (actual: " + socioActual.getApellidoMaterno() + " - Enter para mantener): ");
        String nuevoAMaterno = scanner.nextLine();
        String aMaternoFinal = nuevoAMaterno.trim().isEmpty() ? socioActual.getApellidoMaterno() : nuevoAMaterno;

        if (sistema.modificarSocio(idSocio, telFinal, paisFinal, aMaternoFinal)) {
            System.out.println("Socio ID " + idSocio + " modificado exitosamente.");
        } else {
            System.out.println("Error: No se pudo modificar el socio.");
        }
    }

    private static void darDeBajaSocio() {
        System.out.print("Ingrese ID del Socio a dar de baja: ");
        int id = leerOpcion();

        if (sistema.eliminarSocio(id)) {
            System.out.println("Socio ID " + id + " dado de baja exitosamente.");
        } else {
            Socio s = sistema.buscarSocioPorId(id);
            if (s == null) {
                System.out.println("Error: Socio ID " + id + " no encontrado.");
            } else {
                System.out.println("Error: No se puede dar de baja. El Socio ID " + id + " tiene reservas futuras pendientes.");
            }
        }
    }

    //Gestion de canchas:

    private static void menuGestionCanchas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CANCHAS ---");
            System.out.println("1. Listar todas las canchas");
            System.out.println("2. Registrar nueva cancha");
            System.out.println("3. Modificar cancha");
            System.out.println("4. Dar de baja cancha");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- LISTADO DE CANCHAS ---");
                    for (Cancha c : sistema.getListaCanchas()) {
                        System.out.println(c);
                    }
                    break;
                case 2:
                    registrarCancha();
                    break;
                case 3:
                    modificarCancha();
                    break;
                case 4:
                    darDeBajaCancha();
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarCancha() {
        System.out.println("\n--- REGISTRO DE CANCHA ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Deporte (Ej: Fútbol, Tenis): ");
        String deporte = scanner.nextLine();
        System.out.print("¿Es cubierta (S/N)? ");
        boolean cubierta = scanner.nextLine().equalsIgnoreCase("S");
        System.out.print("Capacidad (Nro de personas): ");
        int capacidad = leerOpcion();
        System.out.print("Características adicionales: ");
        String caracteristicas = scanner.nextLine();

        Cancha nuevaCancha = new Cancha(nombre, deporte, cubierta, capacidad, caracteristicas);
        sistema.registrarCancha(nuevaCancha);
        System.out.println("Cancha registrada exitosamente. ID: " + nuevaCancha.getIdCancha());
    }

    private static void modificarCancha() {
        System.out.println("\n--- MODIFICAR CANCHA ---");
        System.out.print("Ingrese ID de la Cancha a modificar: ");
        int idCancha = leerOpcion();

        Cancha canchaActual = sistema.buscarCanchaPorId(idCancha);
        if (canchaActual == null) {
            System.out.println("Cancha no encontrada.");
            return;
        }

        System.out.println("\n--- DATOS ACTUALES DE CANCHA ID " + idCancha + " ---");
        System.out.println(canchaActual);

        System.out.print("Nuevo Nombre (actual: " + canchaActual.getNombre() + " - Enter para mantener): ");
        String nuevoNombre = scanner.nextLine();
        String nombreFinal = nuevoNombre.trim().isEmpty() ? canchaActual.getNombre() : nuevoNombre;

        System.out.print("Nuevo Deporte (actual: " + canchaActual.getDeporte() + " - Enter para mantener): ");
        String nuevoDeporte = scanner.nextLine();
        String deporteFinal = nuevoDeporte.trim().isEmpty() ? canchaActual.getDeporte() : nuevoDeporte;

        boolean cubiertaFinal = canchaActual.isCubierta();
        System.out.print("¿Es Cubierta (S/N)? (actual: " + (canchaActual.isCubierta() ? "S" : "N") + " - Enter para mantener): ");
        String nuevaCubiertaStr = scanner.nextLine();
        if (!nuevaCubiertaStr.trim().isEmpty()) {
            cubiertaFinal = nuevaCubiertaStr.equalsIgnoreCase("S");
        }

        System.out.print("Nueva Capacidad (Nro de personas) [OBLIGATORIO, ingrese el valor actual si no desea cambiarlo]: ");
        int capacidadFinal = leerOpcion();

        System.out.print("Nuevas Características (actual: " + canchaActual.getCaracteristicas() + " - Enter para mantener): ");
        String nuevasCaracteristicas = scanner.nextLine();
        String caracteristicasFinal = nuevasCaracteristicas.trim().isEmpty() ? canchaActual.getCaracteristicas() : nuevasCaracteristicas;

        if (sistema.modificarCancha(idCancha, nombreFinal, deporteFinal, cubiertaFinal, capacidadFinal, caracteristicasFinal)) {
            System.out.println("Cancha ID " + idCancha + " modificada exitosamente.");
        } else {
            System.out.println("Error: No se pudo modificar la cancha.");
        }
    }

    private static void darDeBajaCancha() {
        System.out.print("Ingrese ID de la Cancha a dar de baja: ");
        int id = leerOpcion();

        if (sistema.eliminarCancha(id)) {
            System.out.println("Cancha ID " + id + " dada de baja exitosamente.");
        } else {
            Cancha c = sistema.buscarCanchaPorId(id);
            if (c == null) {
                System.out.println("Error: Cancha ID " + id + " no encontrada.");
            } else {
                System.out.println("Error: No se puede dar de baja. La Cancha ID " + id + " tiene reservas futuras pendientes.");
            }
        }
    }

    //Gestion de reservas:

    private static void menuGestionReservas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Registrar nueva reserva");
            System.out.println("2. Registrar Pago/Prepago (Congela tarifa)");
            System.out.println("3. Finalizar Turno y Cobro (con Extras)");
            System.out.println("4. Listar todas las reservas");
            System.out.println("5. Cancelar Reserva");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    registrarReserva();
                    break;
                case 2:
                    registrarPrepago();
                    break;
                case 3:
                    finalizarTurnoYCobro();
                    break;
                case 4:
                    System.out.println("\n--- LISTADO DE RESERVAS ---");
                    for (Reserva r : sistema.getListaReservas()) {
                        System.out.println(r);
                    }
                    break;
                case 5:
                    cancelarReserva();
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarReserva() {
        System.out.println("\n--- REGISTRO DE RESERVA ---");
        System.out.print("Ingrese ID del Socio: ");
        int idSocio = leerOpcion();
        Socio socio = sistema.buscarSocioPorId(idSocio);

        if (socio == null) {
            System.out.println("Socio no encontrado.");
            return;
        }

        System.out.print("Ingrese ID de la Cancha: ");
        int idCancha = leerOpcion();
        Cancha cancha = sistema.buscarCanchaPorId(idCancha);

        if (cancha == null) {
            System.out.println("Cancha no encontrada.");
            return;
        }

        LocalDate fecha = leerFecha("Fecha de la Reserva");
        LocalTime horaInicio = leerHora("Hora de Inicio");
        System.out.print("Duración en horas (Ej: 1.5, 2.0): ");
        double duracionHoras;
        try {
            duracionHoras = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Duración inválida. Usando 0.0.");
            duracionHoras = 0.0;
        }

        System.out.print("Observaciones (Enter para omitir): ");
        String observacion = scanner.nextLine();

        if (sistema.crearReserva(socio, cancha, fecha, horaInicio, duracionHoras, observacion)) {
            Reserva nuevaReserva = sistema.getListaReservas().get(sistema.getListaReservas().size() - 1);
            double costo = sistema.calcularCostoTurno(nuevaReserva);

            System.out.println("Reserva creada exitosamente. ID: " + nuevaReserva.getIdReserva());
            System.out.println("Costo estimado del turno (Tarifa vigente): $" + String.format("%.2f", costo));

            cancha.setEstado("Reservada");

        } else {
            System.out.println("ERROR: La cancha no está disponible en esa fecha y horario (Superposición).");
        }
    }

    private static void registrarPrepago() {
        System.out.print("Ingrese ID de la Reserva para prepago: ");
        int idReserva = leerOpcion();

        Reserva reserva = null;
        for (Reserva r : sistema.getListaReservas()) {
            if (r.getIdReserva() == idReserva) {
                reserva = r;
                break;
            }
        }

        if (reserva != null) {
            reserva.setAbonoCompleto(true);
            double costo = sistema.calcularCostoTurno(reserva);
            System.out.println("Prepago total registrado para Reserva ID " + idReserva);
            System.out.println("Tarifa del turno ($" + String.format("%.2f", costo) + ") ha sido CONGELADA.");
        } else {
            System.out.println("Reserva no encontrada.");
        }
    }

    private static void finalizarTurnoYCobro() {
        System.out.print("Ingrese ID de la Reserva para finalizar turno y cobrar: ");
        int idReserva = leerOpcion();

        Reserva reserva = null;
        for (Reserva r : sistema.getListaReservas()) {
            if (r.getIdReserva() == idReserva) {
                reserva = r;
                break;
            }
        }

        if (reserva != null) {
            System.out.println("\n--- COBRO Y FIN DE TURNO (Reserva ID: " + idReserva + ") ---");

            //Mostrar estado de pago
            System.out.println("Estado de Prepago: " + (reserva.isAbonoCompleto() ? "COMPLETO (Tarifa congelada)" : "PENDIENTE"));

            //Pedir Extras
            System.out.print("Ingrese el monto total por EXTRAS (iluminación, elementos, etc.): $");
            double costoExtras;
            try {
                costoExtras = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Monto de extras inválido. Se asume $0.");
                costoExtras = 0.0;
            }

            //Calcular saldo final
            double saldoFinal = sistema.calcularSaldoFinal(reserva, costoExtras);

            System.out.println("\n==================================");
            System.out.println("     DETALLE DEL COBRO");
            System.out.println("==================================");
            System.out.println("Costo Base del Turno: $" + String.format("%.2f", sistema.calcularCostoTurno(reserva)));
            System.out.println("Monto por Extras:     $" + String.format("%.2f", costoExtras));
            System.out.println("----------------------------------");
            System.out.println("SALDO PENDIENTE A COBRAR: $" + String.format("%.2f", saldoFinal));
            System.out.println("==================================");

            //Actualizar estado de la cancha (Vuelve a Disponible)
            reserva.getCancha().setEstado("Disponible");
            System.out.println("Cancha devuelta al estado 'Disponible'.");

        } else {
            System.out.println("Reserva no encontrada.");
        }
    }

    private static void cancelarReserva() {
        System.out.print("Ingrese ID de la Reserva a cancelar: ");
        int idReserva = leerOpcion();

        if (sistema.cancelarReserva(idReserva)) {
            System.out.println("Reserva ID " + idReserva + " cancelada exitosamente. Cancha liberada.");
        } else {
            System.out.println("Error: Reserva ID " + idReserva + " no encontrada.");
        }
    }

    //Gestion de tarifas:

    private static void menuGestionTarifas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE TARIFAS ---");
            System.out.println("1. Registrar nueva tarifa");
            System.out.println("2. Listar tarifas históricas");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    registrarTarifa();
                    break;
                case 2:
                    System.out.println("\n--- LISTADO DE TARIFAS HISTÓRICAS ---");
                    if (sistema.getListaTarifas().isEmpty()) {
                        System.out.println("No hay tarifas registradas.");
                    } else {
                        for (Tarifa t : sistema.getListaTarifas()) {
                            System.out.println(t);
                        }
                    }
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarTarifa() {
        System.out.println("\n--- REGISTRO DE NUEVA TARIFA ---");

        System.out.print("Deporte al que aplica (Ej: Fútbol, Tenis): ");
        String deporte = scanner.nextLine();

        System.out.print("¿Aplica a canchas Cubiertas (S/N)? ");
        boolean cubierta = scanner.nextLine().equalsIgnoreCase("S");

        System.out.print("Precio por Hora: $");
        double precio;
        try {
            precio = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Precio inválido. Se asume $0.0.");
            precio = 0.0;
        }

        LocalDate fechaVigencia = leerFecha("Fecha de Vigencia (desde cuándo aplica)");

        Tarifa nuevaTarifa = new Tarifa(deporte, cubierta, precio, fechaVigencia);

        sistema.registrarTarifa(nuevaTarifa);
        System.out.println("Tarifa registrada exitosamente.");
    }

    //Consultas y reportes:

    private static void menuConsultas() {
        int opcion;
        do {
            System.out.println("\n--- CONSULTAS ---");
            System.out.println("1. Canchas por Deporte");
            System.out.println("2. Canchas por Nombre");
            System.out.println("3. Canchas por Condición (Cubierta/Descubierta)");
            System.out.println("4. Canchas por Fecha y Hora (Disponibilidad)");
            System.out.println("5. Filtrar Canchas por Reserva Previa (Con/Sin)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    consultarPorDeporte();
                    break;
                case 2:
                    consultarPorNombre();
                    break;
                case 3:
                    consultarPorCondicion();
                    break;
                case 4:
                    consultarDisponibilidadPorFranja();
                    break;
                case 5:
                    filtrarCanchasPorReserva();
                    break;
            }
        } while (opcion != 0);
    }

    private static void mostrarResultadosCancha(ArrayList<Cancha> resultados) {
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron canchas que cumplan el criterio.");
        } else {
            System.out.println("\n--- RESULTADOS ---");
            for (Cancha c : resultados) {
                System.out.println(c);
            }
        }
    }

    private static void consultarPorDeporte() {
        System.out.print("Ingrese Deporte (Ej: Fútbol, Tenis): ");
        String deporte = scanner.nextLine();
        ArrayList<Cancha> resultados = sistema.consultarCanchasPorDeporte(deporte);
        mostrarResultadosCancha(resultados);
    }

    private static void consultarPorNombre() {
        System.out.print("Ingrese parte del Nombre de la cancha: ");
        String nombre = scanner.nextLine();
        ArrayList<Cancha> resultados = sistema.consultarCanchasPorNombre(nombre);
        mostrarResultadosCancha(resultados);
    }

    private static void consultarPorCondicion() {
        System.out.print("¿Desea ver Canchas Cubiertas (S/N)? ");
        String condicion = scanner.nextLine();
        boolean esCubierta = condicion.equalsIgnoreCase("S");
        ArrayList<Cancha> resultados = sistema.consultarCanchasPorCondicion(esCubierta);
        mostrarResultadosCancha(resultados);
    }

    private static void consultarDisponibilidadPorFranja() {
        System.out.println("\n--- CONSULTA DE DISPONIBILIDAD POR FRANJA HORARIA ---");
        LocalDate fecha = leerFecha("Ingrese la Fecha");
        LocalTime horaInicio = leerHora("Hora de Inicio");

        System.out.print("Duración en horas (Ej: 1.0, 1.5): ");
        double duracionHoras;
        try {
            duracionHoras = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Duración inválida. Operación cancelada.");
            return;
        }

        System.out.println("\n--- REPORTE PARA EL " + fecha.format(FORMATO_FECHA) + " de " + horaInicio + " por " + duracionHoras + "hrs ---");

        boolean alMenosUnaDisponible = false;

        //Recorrer TODAS las canchas:
        for (Cancha cancha : sistema.getListaCanchas()) {

            String estado;

            if (sistema.estaLibre(cancha, fecha, horaInicio, duracionHoras)) {
                estado = "DISPONIBLE";
                alMenosUnaDisponible = true;
            } else {
                estado = "NO DISPONIBLE (Reservada)";
            }

            //Imprimir el resultado
            System.out.printf("%-30s | %s%n", cancha.getNombre(), estado);
        }

        if (!alMenosUnaDisponible) {
            System.out.println("\nLamentablemente, no hay canchas disponibles en ese horario.");
        }
    }

    private static void filtrarCanchasPorReserva() {
        LocalDate fecha = leerFecha("Ingrese la Fecha a consultar");

        System.out.println("\n--- Filtrar Canchas para la Fecha " + fecha.format(FORMATO_FECHA) + " ---");
        System.out.println("1. Canchas CON reserva previa");
        System.out.println("2. Canchas SIN reserva previa (Libres)");
        System.out.print("Seleccione una opción: ");
        int opcion = leerOpcion();

        ArrayList<Cancha> resultados;
        if (opcion == 1) {
            resultados = sistema.filtrarCanchasConReservaPrevia(fecha);
            System.out.println("\n--- CANCHAS CON RESERVA ---");
        } else if (opcion == 2) {
            resultados = sistema.filtrarCanchasSinReservaPrevia(fecha);
            System.out.println("\n--- CANCHAS SIN RESERVA (LIBRES) ---");
        } else {
            System.out.println("Opción inválida.");
            return;
        }

        mostrarResultadosCancha(resultados);
    }
}