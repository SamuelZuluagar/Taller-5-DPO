package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoMenuTest {
    private ProductoMenu producto1;

    @BeforeEach
    void setUp( ) throws Exception
    {
        producto1= new ProductoMenu ("Hamburguesa sencilla", 8000);
    }

    @Test
    void testGetNombre( )
    {
        assertEquals ("Hamburguesa sencilla", producto1.getNombre(), "El nombre del producto no es el que corresponde");
        
    }

    @Test
    void testGetPrecio( )
    {
    	assertEquals (8000, producto1.getPrecio(), "El precio del producto no es el que corresponde");
    }

    @Test
    void testGenerarTextoFactura( )
    {
      String text = producto1.generarTextoFactura ();
      assertTrue (text.contains ("Hamburguesa sencilla"), "La factura debe incluir el nombre del producto.");
      assertTrue (text.contains ("8000"), "La factura debe incluir el precio del producto correspondiente.");
      
    }

}