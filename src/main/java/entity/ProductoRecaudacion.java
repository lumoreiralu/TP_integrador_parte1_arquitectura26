package entity;
import lombok.*;
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor


//La responsabilidad es representar los datos de la recaudación de un producto.

public class ProductoRecaudacion {
    private int idProducto;
    private String nombre;
    private float valor;
    private float recaudacion;
}
