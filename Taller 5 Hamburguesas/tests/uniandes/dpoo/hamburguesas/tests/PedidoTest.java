package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader; //Se uso iag para la factura, el enunciado pedia que se hiciera el caso completo para la factura
//y no encontre una forma de hacerlo distinta a crear un File real, llamar a guardarFactura con él, y volver a leerlo con BufferedReader.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


import org.junit.jupiter.api.BeforeEach;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class PedidoTest {
	//numeroPedidos es estático, por eso testGetIdPedido compara dos pedidos entre sí en vez de un valor fijo.
    private Pedido pedido;
    private ProductoMenu hamburguesa;
    private ProductoMenu gaseosa;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Samuel Zuluaga", "Calle 45b # 127-00" );
        hamburguesa = new ProductoMenu( "Hamburguesa sencilla", 8000 );
        gaseosa = new ProductoMenu( "Gaseosa", 4000 );
        pedido.agregarProducto( hamburguesa );
        pedido.agregarProducto( gaseosa );
    }

    @Test
    void testGetIdPedido( )
    {
        Pedido otroPedido = new Pedido( "Otro cliente", "Otra dirección" );
        assertNotEquals( pedido.getIdPedido( ), otroPedido.getIdPedido( ), "Dos pedidos distintos no deberían (en este contexto) tener el mismo id" );
        assertTrue( otroPedido.getIdPedido( ) > pedido.getIdPedido( ), "El id debe ser incremental." );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Samuel Zuluaga", pedido.getNombreCliente( ), "El nombre del cliente no es el correcto." );
    }

    @Test
    void testGetPrecioTotalPedido( )
    {
        assertEquals( 14280, pedido.getPrecioTotalPedido( ), "El precio total del pedido no es el correcto." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String texto = pedido.generarTextoFactura( );
        assertTrue( texto.contains( "Samuel Zuluaga" ), "La factura debe contener el nombre del cliente." );
        assertTrue( texto.contains( "Calle 45b # 127-00" ), "La factura debe contener la dirección del cliente." );
        assertTrue( texto.contains( "Hamburguesa sencilla" ), "La factura debe contener los productos del pedido." );
        assertTrue( texto.contains( "Gaseosa" ), "La factura debe contener los productos del pedido." );
        assertTrue( texto.contains( "12000" ), "La factura debe contener el precio neto." );
        assertTrue( texto.contains( "2280" ), "La factura debe contener el IVA." ); //por el 0.19 del private static final de los atributos de la clase
        assertTrue( texto.contains( "14280" ), "La factura debe contener el precio total." );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        File archivo = new File( "factura_prueba.txt" );
        pedido.guardarFactura( archivo );

        BufferedReader lector = new BufferedReader( new FileReader( archivo ) );
        StringBuilder contenido = new StringBuilder( );
        String linea = lector.readLine( );
        while( linea != null )
        {
            contenido.append( linea + "\n" );
            linea = lector.readLine( );
        }
        lector.close( );

        assertEquals( pedido.generarTextoFactura( ), contenido.toString( ), "El contenido del archivo debe ser igual al texto de la factur" );

        archivo.delete( );
    }

    @Test
    void testGuardarFacturaRutaInvalida( )
    {
        File archivoInvalido = new File( "/ruta/que/no/existe/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( archivoInvalido ), "Debería lanzar FileNotFoundException con una ruta inválida." );
    }

}
