package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
class ProductoAjustadoTest {
	private ProductoMenu C_base;
	private ProductoAjustado C_ajustado;
	private Ingrediente lechuga;
	private Ingrediente bacon;
	
	@BeforeEach
    void setUp( ) throws Exception
    {
        C_base = new ProductoMenu( "Hamburguesa sencilla", 8000 );
        C_ajustado = new ProductoAjustado( C_base );
        lechuga = new Ingrediente( "lechuga", 800 );
        bacon = new Ingrediente( "bacon", 2000 );
    }



    @Test
    void testGetNombre( )
    {
        assertEquals( "Hamburguesa sencilla", C_ajustado.getNombre( ), "El nombre del producto ajustado debe ser el mismo que del producto base" );
    }

    @Test
    void testGetPrecioSinModificaciones( )
    {
        assertEquals( 8000, C_ajustado.getPrecio( ), "Sin agregados ni eliminados el precio debe ser igual al del producto base" );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        C_ajustado.agregarIngrediente( lechuga );
        C_ajustado.agregarIngrediente( bacon );
        assertEquals( 10800, C_ajustado.getPrecio( ), "El precio debe ser el costo base más el costo de los ingredientes agregados." ); 
    }

    @Test
    void testGetPrecioConEliminados( )
    {
        C_ajustado.eliminarIngrediente( lechuga );
        assertEquals( 8000, C_ajustado.getPrecio( ), "Eliminar ingredientes no debe cambiar el precio." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        C_ajustado.agregarIngrediente( bacon );
        C_ajustado.eliminarIngrediente( lechuga );
        String texto = C_ajustado.generarTextoFactura( );

        assertTrue( texto.contains( "bacon" ), "La factura debe mostrar el ingrediente agregado." );
        assertTrue( texto.contains( "2000" ), "La factura debe mostrar el costo del ingrediente agregado." );
        assertTrue( texto.contains( "lechuga" ), "La factura debe mostrar el ingrediente eliminado." );
        assertTrue( texto.contains( String.valueOf( C_ajustado.getPrecio( ) ) ), "La factura debe mostrar el precio total." );
    }

}