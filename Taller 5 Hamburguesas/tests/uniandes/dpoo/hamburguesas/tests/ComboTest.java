package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
class ComboTest {
	private ProductoMenu hamburguesa;
    private ProductoMenu gaseosa;
    private ArrayList<ProductoMenu> items;

    @BeforeEach
    void setUp( ) throws Exception
    {
        hamburguesa = new ProductoMenu( "Hamburguesa sencilla", 8000 );
        gaseosa = new ProductoMenu( "Gaseosa", 4000 );
        items = new ArrayList<ProductoMenu>( );
        items.add( hamburguesa );
        items.add( gaseosa );
    }

 
    @Test
    void testGetNombre( )
    {
        Combo combo = new Combo( "Combo especial", 0.07, items );
        assertEquals( "Combo especial", combo.getNombre( ), "El nombre del combo no es el correcto" );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo combo = new Combo( "Combo sin descuento", 0, items );
        assertEquals( 12000, combo.getPrecio( ), "Sin descuento, el precio debe ser la suma de ambos productos pedidos" ); 
    }

    @Test
    void testGetPrecioConDescuento( )
    {
        Combo combo = new Combo( "Combo especial", 0.07, items );
        assertEquals( 11160, combo.getPrecio( ), "El precio debe aplicar el descuento sobre el precio sumado de ambos productos" ); 
    }

    @Test
    void testGenerarTextoFactura( )
    {
        Combo combo = new Combo( "Combo especial", 0.07, items );
        String texto = combo.generarTextoFactura( );
        assertTrue( texto.contains( "Combo especial" ), "La factura debe contener el nombre del combo." );
        assertTrue( texto.contains( String.valueOf( combo.getPrecio( ) ) ), "La factura debe contener el precio del combo." );
    }

}


