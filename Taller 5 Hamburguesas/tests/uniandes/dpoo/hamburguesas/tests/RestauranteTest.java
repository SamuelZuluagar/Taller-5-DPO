package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
    }

    // similar a PedidoTest (uso de PrintWriter)
    private File crearArchivo( String nombreArchivo, String... lineas ) throws IOException
    {
        File archivo = new File( nombreArchivo );
        PrintWriter escritor = new PrintWriter( archivo );
        for( String linea : lineas )
        {
            escritor.println( linea );
        }
        escritor.close( );
        return archivo;
    }

    @Test
    void testCargaExitosa( ) throws Exception
    {
        File archivoIngredientes = crearArchivo( "ingredientes_test.txt", "queso;1500", "tomate;500" );
        File archivoMenu = crearArchivo( "menu_test.txt", "hamburguesa;8000", "papas;4000" );
        File archivoCombos = crearArchivo( "combos_test.txt", "combo1;10%;hamburguesa;papas" );

        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );

        assertEquals( 2, restaurante.getIngredientes( ).size( ), "Deberían quedar 2 ingredientes cargados." );
        assertEquals( 2, restaurante.getMenuBase( ).size( ), "Deberían quedar 2 productos cargados." );
        assertEquals( 1, restaurante.getMenuCombos( ).size( ), "Debería quedar 1 combo cargado." );
        assertEquals( "queso", restaurante.getIngredientes( ).get( 0 ).getNombre( ), "El primer ingrediente no es el esperado." );

        archivoIngredientes.delete( );
        archivoMenu.delete( );
        archivoCombos.delete( );
    }

    @Test
    void testCargarIngredienteRepetido( )
    {
        assertThrows( IngredienteRepetidoException.class, ( ) -> {
            File archivoIngredientes = crearArchivo( "ingredientes_rep_test.txt", "queso;1500", "queso;1500" );
            try
            {
                restaurante.cargarInformacionRestaurante( archivoIngredientes, new File( "menu_test.txt" ), new File( "combos_test.txt" ) );
            }
            finally
            {
                archivoIngredientes.delete( );
            }
        }, "Un ingrediente repetido debería lanzar la excepcion (IngredienteRepetidoException)." );
    }

    @Test
    void testCargarProductoRepetido( )
    {
        assertThrows( ProductoRepetidoException.class, ( ) -> {
            File archivoIngredientes = crearArchivo( "ingredientes_test2.txt", "queso;1500" );
            File archivoMenu = crearArchivo( "menu_rep_test.txt", "hamburguesa;8000", "hamburguesa;8000" );
            try
            {
                restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, new File( "combos_test.txt" ) );
            }
            finally
            {
                archivoIngredientes.delete( );
                archivoMenu.delete( );
            }
        }, "Un producto repetido en el menú debería lanzar ProductoRepetidoException." );
    }

    @Test
    void testCargarComboRepetido( )
    {
        assertThrows( ProductoRepetidoException.class, ( ) -> {
            File archivoIngredientes = crearArchivo( "ingredientes_test3.txt", "queso;1500" );
            File archivoMenu = crearArchivo( "menu_test3.txt", "hamburguesa;8000", "papas;4000" );
            File archivoCombos = crearArchivo( "combos_rep_test.txt", "combo1;10%;hamburguesa;papas", "combo1;10%;hamburguesa;papas" );
            try
            {
                restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
            }
            finally
            {
                archivoIngredientes.delete( );
                archivoMenu.delete( );
                archivoCombos.delete( );
            }
        }, "Un combo con nombre repetido debería lanzar la excepcion(ProductoRepetidoException.)" );
    }

    @Test
    void testCargarProductoFaltanteEnCombo( )
    {
        assertThrows( ProductoFaltanteException.class, ( ) -> {
            File archivoIngredientes = crearArchivo( "ingredientes_test4.txt", "queso;1500" );
            File archivoMenu = crearArchivo( "menu_test4.txt", "hamburguesa;8000" );
            File archivoCombos = crearArchivo( "combos_faltante_test.txt", "combo1;10%;hamburguesa;papas" ); // papas no existe
            try
            {
                restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
            }
            finally
            {
                archivoIngredientes.delete( );
                archivoMenu.delete( );
                archivoCombos.delete( );
            }
        }, "Un combo que referencia un producto inexistente debería lanzar la excpcion (ProductoFaltanteException.)" );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Samuel", "Calle 45b # 127-00" );
        assertNotNull( restaurante.getPedidoEnCurso( ), "Debería quedar un pedido en curso." );
        assertEquals( "Samuel", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El cliente del pedido en curso no es el esperado." );
    }

    @Test
    void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Samuel", "Calle 45b # 127-00" );
        assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Otro cliente", "Otra dirección" ), "No debería permitir iniciar un pedido si ya hay uno en curso." );
    }

    // requiere que exista ./facturas/ — ver declaración de uso de IAG
    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        new File( "./facturas/" ).mkdirs( );

        restaurante.iniciarPedido( "Samuel", "Calle 45b # 127-00" );
        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar, no debería quedar pedido en curso." );
        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debería quedar en la lista de pedidos." );
    }

    @Test
    void testCerrarYGuardarPedidoSinPedidoEnCurso( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ), "No debería permitir cerrar un pedido si no hay ninguno en curso." );
    }

    @Test
    void testGettersRestauranteVacio( )
    {
        assertEquals( 0, restaurante.getPedidos( ).size( ), "Un restaurante nuevo no debería tener pedidos." );
        assertEquals( 0, restaurante.getIngredientes( ).size( ), "Un restaurante nuevo no debería tener ingredientes." );
        assertEquals( 0, restaurante.getMenuBase( ).size( ), "Un restaurante nuevo no debería tener productos." );
        assertEquals( 0, restaurante.getMenuCombos( ).size( ), "Un restaurante nuevo no debería tener combos." );
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debería tener pedido en curso." );
    }

}