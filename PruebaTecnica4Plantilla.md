##### @RestController
##### @RequestMapping("/agency")
Usuario corriente:
- Hoteles:
> **GET** Path: /hotels
> Obtener un listado de todos los hoteles registrados.

> **GET** Path: /hotels?dateFrom=[dd/mm/yyy]&dateTo=[dd/mm/yyy]&destination=["nombre_destino"]
> Obtener un listado de hoteles disponibles en un rango de fechas y destino.

> **POST** Path: /hotel-booking/new
> Realizar una reserva de un hotel, indicando cantidad de personas, fecha de entrada, fecha de salida y tipo de habitación. Obtener como respuesta el monto total de la reserva realizada.
>Ejemplo:
```json
{
    "dateFrom" : "05/04/2024",
    "dateTo" : "09/04/2024",
    "nights" : 4
    "place" : "Barcelona",
    "hotelCode" : "MT-0003",
    "peopleQ" : 2,
    "roomType" : "Double",
    "hosts" : [
        {datos persona 1},
        {datos persona 2}
    ]
}
```

- Vuelos:
> **GET**
> **Path:** /flights
> Obtener un listado de todos los vuelos registrados.

> **GET**
> **Path:** /flights?dateFrom=[dd/mm/yyy]&dateTo=[dd/mm/yyy]&origin=["ciudad_origen"]&destination=["ciudad_destino"]
> Obtener un listado de vuelos disponibles en un rango de fechas y según origen y destino elegidos.

> **POST**
> **Path:** /flight-booking/new
> Realizar una reserva de un vuelo, indicando fecha de salida, origen, destino, cantidad de personas y datos de los pasajeros. Obtener como respuesta el monto total de la reserva realizada.


Autorizados:
- Hoteles:
> **POST**
> **Path:** /hotels/new
> Registrar un nuevo hotel.

> **PUT**
> **Path:** /hotels/edit/{id}

> **DELETE**
> **Path:** /hotels/delete/{id}

> **GET**
> **Path:** /hotels/{id}
> Obtener un hotel en particular.

> **GET**
> **Path:** /hotels
> Obtener un listado de todos los hoteles registrados.

- Vuelos:
> **POST**
> **Path:** /flights/new
> Registrar un nuevo vuelo.

> **PUT**
> **Path:** /flights/edit/{id}

> **DELETE**
> **Path:** /flights/delete/{id}

> **GET**
> **Path:** /flights/{id}
> Obtener un vuelo en particular.

> **GET**
> **Path:** /flights
> Obtener un listado de todos los vuelos registrados.












Preguntas:
1.  ¿A qué nivel guardamos las excepciones propias?
    ¿En servicio, controlador o un paquete propio de excepciones?

2.  ¿Qué criterio aplicarías para considerar que dos reservas son idénticas?
    > Para altas, validar que no exista anteriormente una reserva con idénticas características.

3.  ¿DTO dentro de DTO? ¿Puedo usar Builder?

4. Manejo de excepciones en el controlador?
    - Con try catch?
    - Le vale el ExceptionHandler?
    - Y con ControllerAdvice?

5. ¿Valora comentarios en inglés?