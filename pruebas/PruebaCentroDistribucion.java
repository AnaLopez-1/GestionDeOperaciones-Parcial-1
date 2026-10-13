package pruebas;

import modelo.ComparadoresPaquete;
import modelo.Paquete;
import servicio.CentroDistribucion;

/** Pruebas de CentroDistribucion usando los ejemplos del enunciado. */
public class PruebaCentroDistribucion { 

    private static int fallos = 0;
    private static void verificar(String descripcion, boolean condicion) { 
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion); 
        if (!condicion) { // Si la prueba falla
            fallos++; // Incrementa el contador de errores
        }
}

    private static String codigos(Iterable<Paquete> paquetes) { 
        StringBuilder sb = new StringBuilder(); 
        for (Paquete p : paquetes) { 
            sb.append(p.getCodigo()).append(' ');
        }
        return sb.toString().trim(); 
}

    private static String texto(Iterable<String> elementos) { 
        StringBuilder sb = new StringBuilder(); 
        for (String e : elementos) { 
            if (sb.length() > 0) {
                sb.append(", "); 
        }
            sb.append(e); 
        }
        return sb.toString();
}
}