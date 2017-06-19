package checkhelzio.ccv.servicedeskcucsh;

public class Tecnico {

    private String codigoDelTecnico;
    private String nombreDelTecnico;
    private String apellidosDelTecnico;
    private String unidad;
    private int nivel;  // NIVEL 1 = CONSULTA PERSONAL, NIVEL 2 = CONSULTA GENERAL, NIVEL 3 = CONSULTA GENERAL Y REGISTRO

    public Tecnico(String codigo) {
        this.codigoDelTecnico = codigo;
        switch (codigo) {
            case "207509057": // ADMIN
                this.nombreDelTecnico = "Caryel";
                this.apellidosDelTecnico = "Méndez";
                this.unidad = "UMI";
                this.nivel = 2;
                break;
            case "005107962": // HIRAM PEÑA FRANCO
                this.nombreDelTecnico = "Hiram Alejandro";
                this.apellidosDelTecnico = "Peña Franco";
                this.unidad = "UMI";
                this.nivel = 1;
                break;
            case "207512147": // OSCAR MÉNDEZ HERNÁNDEZ
                this.nombreDelTecnico = "Oscar";
                this.apellidosDelTecnico = "Méndez Hernández";
                this.unidad = "UMI";
                this.nivel = 1;
                break;
            case "01290713": // ZYANYA LÓPEZ DÍAZ
                this.nombreDelTecnico = "Zyanya";
                this.apellidosDelTecnico = "López Díaz";
                this.unidad = "UMI";
                this.nivel = 1;
                break;
            case "210092965": // ARIANA ISABEL PÉREZ ALVAREZ
                this.nombreDelTecnico = "Ariana Isabel";
                this.apellidosDelTecnico = "Pérez Alvarez";
                this.unidad = "Taller";
                this.nivel = 1;
                break;
            case "2806533": // IDANIA GOMEZ COSIO
                this.nombreDelTecnico = "Idania";
                this.apellidosDelTecnico = "Gómez Cosio";
                this.unidad = "UMI";
                this.nivel = 2;
                break;
            case "09537805": // JUAN ALBERTO ESTRADA MANCILLA
                this.nombreDelTecnico = "Juan Alberto";
                this.apellidosDelTecnico = "Estrada Mancilla";
                this.unidad = "Taller";
                this.nivel = 1;
                break;
            case "2528894": // Hector Aceves Shimizu Lopez
                this.nombreDelTecnico = "Héctor";
                this.apellidosDelTecnico = "Aceves Shimizu y López";
                this.unidad = "Coordinador";
                this.nivel = 2;
                break;
            case "2921073": // Eduardo Solano Guzmán
                this.nombreDelTecnico = "Eduardo";
                this.apellidosDelTecnico = "Solano Guzmán";
                this.unidad = "Unidad de Cómputo y Telecomunicaciones";
                this.nivel = 2;
                break;
            case "2906163": // Jonathan Josue De Leon Barajas
                this.nombreDelTecnico = "Jonathan Josue";
                this.apellidosDelTecnico = "De León Barajas";
                this.unidad = "Recepción";
                this.nivel = 3;
                break;
            case "2417626": // Omar Alberto Andrade Muñoz
                this.nombreDelTecnico = "Omar Alberto";
                this.apellidosDelTecnico = "Andrade Muñoz";
                this.unidad = "Desarrollo WEB";
                this.nivel = 1;
                break;
            case "2520761": // Octavio Cortazar Rodríguez
                this.nombreDelTecnico = "Octavio";
                this.apellidosDelTecnico = "Cortazar Rodríguez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case "2915286": // Jesus Enrique Vega Coronel
                this.nombreDelTecnico = "Jesús Enrique";
                this.apellidosDelTecnico = "Vega Coronel";
                this.unidad = "Servidores";
                this.nivel = 1;
                break;
            case "2946467": // Eduardo Antonino García Salazar
                this.nombreDelTecnico = "Eduardo Antonino";
                this.apellidosDelTecnico = "García Salazar";
                this.unidad = "Desarrollo WEB";
                this.nivel = 1;
                break;
            case "2952470": // Emmanuel Gregorio Chacon Vera
                this.nombreDelTecnico = "Emmanuel Gregorio";
                this.apellidosDelTecnico = "Chacon Vera";
                this.unidad = "Espacios Tecnológicos - Auditorios";
                this.nivel = 1;
                break;
            case "2954064": // Roberto Carlos Villaseñor Rodriguez
                this.nombreDelTecnico = "Roberto Carlos";
                this.apellidosDelTecnico = "Villaseñor Rodriguez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case "2957917": // Jose Gerardo Carrillo Manriquez
                this.nombreDelTecnico = "Jose Gerardo";
                this.apellidosDelTecnico = "Carrillo Manriquez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case "9102248": // Jorge Adán Plascencia Perez
                this.nombreDelTecnico = "Jorge Adán";
                this.apellidosDelTecnico = "Plascencia Perez";
                this.unidad = "Soporte Técnico Belenes";
                this.nivel = 1;
                break;
            case "9103031": // Gladys Veronica Tabares Torres
                this.nombreDelTecnico = "Gladys Veronica";
                this.apellidosDelTecnico = "Tabares Torres";
                this.unidad = "Proyectos Especiales";
                this.nivel = 1;
                break;
            case "9319743": // Maria Clara Cuellar Espinosa
                this.nombreDelTecnico = "Maria Clara";
                this.apellidosDelTecnico = "Cuellar Espinosa";
                this.unidad = "Uso Libre Belenes";
                this.nivel = 1;
                break;
            case "9715371": // Ricardo Cortes De la O
                this.nombreDelTecnico = "Ricardo";
                this.apellidosDelTecnico = "Cortes De la O";
                this.unidad = "Soporte técnico La Normal";
                this.nivel = 1;
                break;
            case "9811621": // Teresa De los Santos López
                this.nombreDelTecnico = "Teresa";
                this.apellidosDelTecnico = "De los Santos López";
                this.unidad = "Recepción";
                this.nivel = 1;
                break;
        }
    }

    public Tecnico(int random) {
        switch (random) {
            case 0: // HIRAM PEÑA FRANCO
                this.codigoDelTecnico = "005107962";
                this.nombreDelTecnico = "Hiram Alejandro";
                this.apellidosDelTecnico = "Peña Franco";
                this.unidad = "UMI";
                this.nivel = 1;
                break;
            case 1: // OSCAR MÉNDEZ HERNÁNDEZ
                this.codigoDelTecnico = "207512147";
                this.nombreDelTecnico = "Oscar";
                this.apellidosDelTecnico = "Méndez Hernández";
                this.unidad = "UMI";
                this.nivel = 3;
                break;
            case 2: // ZYANYA LÓPEZ DÍAZ
                this.codigoDelTecnico = "01290713";
                this.nombreDelTecnico = "Zyanya";
                this.apellidosDelTecnico = "López Díaz";
                this.unidad = "UMI";
                this.nivel = 1;
                break;
            case 3: // ARIANA Meraz Nieblas
                this.codigoDelTecnico = "1234";
                this.nombreDelTecnico = "Ariana";
                this.apellidosDelTecnico = "Meraz Nieblas";
                this.unidad = "Taller";
                this.nivel = 1;
                break;
            case 4: // JUAN ALBERTO ESTRADA MANCILLA
                this.codigoDelTecnico = "09537805";
                this.nombreDelTecnico = "Juan Alberto";
                this.apellidosDelTecnico = "Estrada Mancilla";
                this.unidad = "Taller";
                this.nivel = 1;
                break;
            case 5: // Jonathan Josue De Leon Barajas
                this.codigoDelTecnico = "2906163";
                this.nombreDelTecnico = "Jonathan Josue";
                this.apellidosDelTecnico = "De León Barajas";
                this.unidad = "Recepción";
                this.nivel = 3;
                break;
            case 6: // Omar Alberto Andrade Muñoz
                this.codigoDelTecnico = "2417626";
                this.nombreDelTecnico = "Omar Alberto";
                this.apellidosDelTecnico = "Andrade Muñoz";
                this.unidad = "Desarrollo WEB";
                this.nivel = 1;
                break;
            case 7: // Octavio Cortazar Rodríguez
                this.codigoDelTecnico = "2520761";
                this.nombreDelTecnico = "Octavio";
                this.apellidosDelTecnico = "Cortazar Rodríguez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case 8: // Jesus Enrique Vega Coronel
                this.codigoDelTecnico = "2915286";
                this.nombreDelTecnico = "Jesús Enrique";
                this.apellidosDelTecnico = "Vega Coronel";
                this.unidad = "Servidores";
                this.nivel = 1;
                break;
            case 9: // Eduardo Antonino García Salazar
                this.codigoDelTecnico = "2946467";
                this.nombreDelTecnico = "Eduardo Antonino";
                this.apellidosDelTecnico = "García Salazar";
                this.unidad = "Desarrollo WEB";
                this.nivel = 1;
                break;
            case 10: // Emanuel Gregorio Chacon Vera
                this.codigoDelTecnico = "2952470";
                this.nombreDelTecnico = "Emanuel Gregorio";
                this.apellidosDelTecnico = "Chacon Vera";
                this.unidad = "Espacios Tecnológicos - Auditorios";
                this.nivel = 1;
                break;
            case 11: // Roberto Carlos Villaseñor Rodriguez
                this.codigoDelTecnico = "2954064";
                this.nombreDelTecnico = "Roberto Carlos";
                this.apellidosDelTecnico = "Villaseñor Rodriguez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case 12: // Jose Gerardo Carrillo Manriquez
                this.codigoDelTecnico = "2957917";
                this.nombreDelTecnico = "Jose Gerardo";
                this.apellidosDelTecnico = "Carrillo Manriquez";
                this.unidad = "Redes y Telefonía";
                this.nivel = 1;
                break;
            case 13: // Jorge Adán Plascencia Perez
                this.codigoDelTecnico = "9102248";
                this.nombreDelTecnico = "Jorge Adán";
                this.apellidosDelTecnico = "Plascencia Perez";
                this.unidad = "Soporte Técnico Belenes";
                this.nivel = 1;
                break;
            case 14: // Gladys Veronica Tabares Torres
                this.codigoDelTecnico = "9103031";
                this.nombreDelTecnico = "Gladys Veronica";
                this.apellidosDelTecnico = "Tabares Torres";
                this.unidad = "Proyectos Especiales";
                this.nivel = 1;
                break;
            case 15: // Maria Clara Cuellar Espinosa
                this.codigoDelTecnico = "9319743";
                this.nombreDelTecnico = "Maria Clara";
                this.apellidosDelTecnico = "Cuellar Espinosa";
                this.unidad = "Uso Libre Belenes";
                this.nivel = 1;
                break;
            case 16: // Ricardo Cortes De la O
                this.codigoDelTecnico = "9715371";
                this.nombreDelTecnico = "Ricardo";
                this.apellidosDelTecnico = "Cortes De la O";
                this.unidad = "Soporte técnico La Normal";
                this.nivel = 1;
                break;
            case 17: // Teresa De los Santos López
                this.codigoDelTecnico = "9811621";
                this.nombreDelTecnico = "Teresa";
                this.apellidosDelTecnico = "De los Santos López";
                this.unidad = "Recepción";
                this.nivel = 1;
                break;
        }
    }

    public Tecnico(String codigoDelTecnico, String nombreDelTecnico, String apellidosDelTecnico, String unidad, int nivel) {
        this.codigoDelTecnico = codigoDelTecnico;
        this.nombreDelTecnico = nombreDelTecnico;
        this.apellidosDelTecnico = apellidosDelTecnico;
        this.unidad = unidad;
        this.nivel = nivel;
    }

    public String getNombreDelTecnico() {
        return nombreDelTecnico;
    }

    public String getApellidosDelTecnico() {
        return apellidosDelTecnico;
    }

    public String getNombreCompletoDelTecnico() {
        return nombreDelTecnico + " " + apellidosDelTecnico;
    }

    public String getUnidad() {
        return unidad;
    }

    public int getNivel() {
        return nivel;
    }

    public String getCodigoDelTecnico() {
        return codigoDelTecnico;
    }

    @Override
    public String toString() {
        return "Tecnico{" +
                "codigoDelTecnico='" + codigoDelTecnico + '\'' +
                ", nombreDelTecnico='" + nombreDelTecnico + '\'' +
                ", apellidosDelTecnico='" + apellidosDelTecnico + '\'' +
                ", unidad='" + unidad + '\'' +
                ", nivel=" + nivel +
                '}';
    }
}