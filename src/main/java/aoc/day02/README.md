# Dia 2: Gift Shop

En el segundo dia del Advent of Code 2025, el problema trata sobre una tienda de regalos con IDs de producto. El input contiene varios rangos de IDs y hay que detectar cuales son invalidos segun un patron repetido.

La entrada viene como una sola linea con rangos separados por comas:

```text
11-22,95-115,998-1012
```

Cada rango indica un intervalo cerrado. Por ejemplo, `95-115` incluye todos los IDs desde `95` hasta `115`, ambos incluidos.

La clave del problema es que los rangos pueden ser grandes. Por eso el codigo no recorre todos los numeros uno a uno, sino que genera directamente los candidatos que tienen estructura de ID invalido y comprueba si caen dentro de cada rango.

## Parte 1

En la primera parte, un ID de producto es invalido si esta formado por dos mitades iguales.

Ejemplos:

```text
55       -> 5 + 5
6464     -> 64 + 64
123123   -> 123 + 123
```

En cambio, estos no son invalidos para la parte 1:

```text
101
123124
12345
```

La respuesta de la parte 1 es la suma de todos los IDs invalidos que aparecen dentro de los rangos del input.

En mi codigo esta parte se resuelve con `DoublePatternInvalidProductIdRule`, usada por `ProductIdSumCalculator`.

## Parte 2

En la segunda parte se amplia la regla. Ahora un ID es invalido si esta formado por una secuencia repetida dos o mas veces.

Ejemplos:

```text
12341234     -> 1234 + 1234
123123123    -> 123 + 123 + 123
1212121212   -> 12 + 12 + 12 + 12 + 12
1111111      -> 1 repetido varias veces
```

Esta parte incluye los casos de la parte 1, pero tambien acepta repeticiones de tres, cuatro o mas bloques.

Un detalle importante es que un mismo ID podria generarse de varias formas. Por ejemplo, un numero formado solo por unos puede verse como una secuencia corta repetida muchas veces o como una secuencia mas larga repetida menos veces. Para evitar sumarlo dos veces, las reglas devuelven un `Set<Long>`.

En mi codigo esta parte se resuelve con `RepeatedPatternInvalidProductIdRule`, usada tambien por `ProductIdSumCalculator`.

## Estructura del paquete

```text
aoc.day02
|-- GiftShopPuzzle.java
|-- input
|   `-- ProductRangeParser.java
|-- product
|   |-- ProductIdRange.java
|   `-- RepeatedPatternProductId.java
`-- solver
    |-- DoublePatternInvalidProductIdRule.java
    |-- InvalidProductIdRule.java
    |-- ProductIdSumCalculator.java
    `-- RepeatedPatternInvalidProductIdRule.java
```

El paquete esta separado en tres zonas:

* `input`: convierte la linea de entrada en rangos del dominio.
* `product`: contiene los conceptos propios del problema, como rangos e IDs repetidos.
* `solver`: contiene las reglas de invalidez y el calculo de la suma.

La clase `GiftShopPuzzle` coordina el dia completo.

## Clase principal `GiftShopPuzzle`

`GiftShopPuzzle` es la clase que une el parser con los calculadores de cada parte.

Tiene tres dependencias:

* `ProductRangeParser`: parsea la linea del input.
* `ProductIdSumCalculator` para parte 1.
* `ProductIdSumCalculator` para parte 2.

Sus metodos principales son:

```java
public long solvePartOne(String inputLine)
public long solvePartTwo(String inputLine)
```

Ambos metodos hacen el mismo flujo general:

1. Parsean la linea con `ProductRangeParser`.
2. Obtienen una lista de `ProductIdRange`.
3. Delegan el calculo en el `ProductIdSumCalculator` correspondiente.

El metodo `main` lee el archivo:

```text
src/main/resources/day02/input.txt
```

Despues crea el puzzle con:

```java
new ProductRangeParser()
new ProductIdSumCalculator(new DoublePatternInvalidProductIdRule())
new ProductIdSumCalculator(new RepeatedPatternInvalidProductIdRule())
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase que transforma el texto del problema en rangos de producto.

### `ProductRangeParser`

`ProductRangeParser` convierte una linea como:

```text
11-22, 95-115,
```

en una lista de objetos `ProductIdRange`.

Su metodo principal es:

```java
public List<ProductIdRange> parseRanges(String inputLine)
```

Este metodo:

1. Valida que la linea no sea `null`, vacia o solo espacios.
2. Elimina espacios con `replaceAll("\\s+", "")`.
3. Separa los rangos por comas.
4. Ignora tokens vacios, por ejemplo si hay una coma final.
5. Convierte cada token en un `ProductIdRange`.

Tambien tiene:

```java
public ProductIdRange parseRange(String text)
```

Este metodo parsea un unico rango con formato `inicio-fin`.

Si el rango no tiene exactamente un guion, lanza una excepcion. Si alguno de los extremos no es numerico, tambien lanza una excepcion.

Una particularidad del parser es que elimina espacios incluso si aparecen dentro de un numero. Por ejemplo, un texto como:

```text
580816-6161 31
```

se interpreta como:

```text
580816-616131
```

Esto esta cubierto por los tests.

## Clases del paquete `product`

El paquete `product` contiene las clases del dominio del problema.

### `ProductIdRange`

`ProductIdRange` es un `record` que representa un rango cerrado de IDs.

Contiene:

* `firstId`: primer ID del rango.
* `lastId`: ultimo ID del rango.

Valida dos cosas al crearse:

* Los IDs deben ser positivos.
* El inicio no puede ser mayor que el final.

Su metodo principal es:

```java
public boolean contains(long productId)
```

Este metodo indica si un ID concreto cae dentro del rango.

Al ser un `record`, tambien aporta igualdad por valor. Esto ayuda en los tests, porque se pueden comparar rangos esperados directamente con rangos parseados.

### `RepeatedPatternProductId`

`RepeatedPatternProductId` es una clase de utilidad del dominio. No tiene estado y por eso su constructor es privado.

Agrupa operaciones relacionadas con IDs formados por patrones repetidos.

Sus metodos principales son:

```java
public static boolean isInvalid(long productId)
```

Comprueba la regla de parte 1: el ID debe tener longitud par y sus dos mitades deben ser iguales.

```java
public static boolean isInvalidWithRepeatedSequence(long productId)
```

Comprueba la regla de parte 2: el ID debe estar formado por una secuencia repetida dos o mas veces.

```java
public static long fromRepeatedHalf(long half, int halfLength)
```

Construye un ID repitiendo una mitad dos veces. Por ejemplo, con mitad `123` y longitud `3`, genera `123123`.

```java
public static long fromRepeatedSequence(long sequence, int sequenceLength, int repetitions)
```

Construye un ID repitiendo una secuencia un numero determinado de veces.

```java
public static long powerOfTen(int exponent)
```

Calcula potencias de diez. Se usa para construir rangos de mitades o secuencias segun su cantidad de digitos.

Esta clase permite que las reglas no tengan que repetir la logica de construir numeros con patrones.

## Clases del paquete `solver`

El paquete `solver` contiene la parte que decide que IDs son invalidos y como sumarlos.

### `InvalidProductIdRule`

`InvalidProductIdRule` es la interfaz comun para las reglas de invalidez.

Define este contrato:

```java
Set<Long> findInvalidProductIds(ProductIdRange range);
```

Cada regla recibe un rango y devuelve los IDs invalidos encontrados dentro de ese rango.

Se devuelve un `Set<Long>` para evitar duplicados. Esto es especialmente importante en la parte 2.

### `DoublePatternInvalidProductIdRule`

`DoublePatternInvalidProductIdRule` implementa la regla de parte 1.

En vez de recorrer todos los IDs del rango, genera mitades posibles y construye IDs duplicando esa mitad.

El flujo es:

1. Calcula el numero maximo de digitos necesario segun `range.lastId()`.
2. Recorre posibles longitudes de mitad.
3. Genera todas las mitades con esa longitud.
4. Construye el ID con `RepeatedPatternProductId.fromRepeatedHalf(...)`.
5. Si el ID cae dentro del rango, lo anade al set de invalidos.

Por ejemplo, para mitades de longitud `2`, puede generar:

```text
1010
1111
1212
...
9999
```

Luego solo se quedan los que estan dentro del rango.

### `RepeatedPatternInvalidProductIdRule`

`RepeatedPatternInvalidProductIdRule` implementa la regla de parte 2.

Genera candidatos formados por una secuencia repetida dos o mas veces.

El flujo es:

1. Calcula el maximo de digitos segun `range.lastId()`.
2. Recorre longitudes totales posibles.
3. Recorre longitudes de secuencia.
4. Solo acepta longitudes donde la longitud total sea divisible por la longitud de la secuencia.
5. Calcula el numero de repeticiones.
6. Genera candidatos con `fromRepeatedSequence(...)`.
7. Comprueba si caen dentro del rango.

Por ejemplo, para longitud total `6` y secuencia de longitud `2`, puede generar numeros como:

```text
121212
343434
999999
```

El resultado tambien se guarda en un `Set<Long>` para no repetir IDs.

### `ProductIdSumCalculator`

`ProductIdSumCalculator` es la clase que suma los IDs invalidos.

Recibe una regla en el constructor:

```java
public ProductIdSumCalculator(InvalidProductIdRule invalidProductIdRule)
```

Su metodo principal es:

```java
public long sumInvalidProductIds(List<ProductIdRange> ranges)
```

El flujo es:

1. Recorre cada rango.
2. Pide a la regla los IDs invalidos de ese rango.
3. Suma cada ID devuelto.
4. Devuelve el total.

Esta clase no sabe si esta usando la regla de parte 1 o parte 2. Solo depende de la interfaz `InvalidProductIdRule`.

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia esta en la regla que se inyecta dentro de `ProductIdSumCalculator`.

En parte 1:

```java
new ProductIdSumCalculator(new DoublePatternInvalidProductIdRule())
```

Solo se generan IDs con dos mitades iguales.

En parte 2:

```java
new ProductIdSumCalculator(new RepeatedPatternInvalidProductIdRule())
```

Se generan IDs formados por una secuencia repetida dos o mas veces.

El calculador de suma no cambia. Cambia la estrategia que encuentra IDs invalidos.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: las clases de parseo, dominio y solucion estan separadas por paquetes.
* Bajo acoplamiento: `ProductIdSumCalculator` depende de una interfaz, no de una regla concreta.
* Modularidad: se puede entender cada parte del problema por separado.
* Encapsulacion: `ProductIdRange` controla sus invariantes y la pertenencia al rango.
* Abstraccion: `InvalidProductIdRule` representa cualquier criterio de invalidez.
* Codigo expresivo: los nombres indican claramente si se trabaja con doble patron o secuencia repetida.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `ProductRangeParser` parsea rangos.
* `ProductIdRange` representa un intervalo valido.
* `RepeatedPatternProductId` construye y comprueba patrones.
* `DoublePatternInvalidProductIdRule` aplica la regla de parte 1.
* `RepeatedPatternInvalidProductIdRule` aplica la regla de parte 2.
* `ProductIdSumCalculator` suma resultados.
* `GiftShopPuzzle` coordina.

### DRY

La construccion de IDs repetidos esta centralizada en `RepeatedPatternProductId`. Las reglas reutilizan esa logica en lugar de montar numeros manualmente cada una por su lado.

### OCP: Abierto/Cerrado

Si apareciera otra regla de invalidez, se podria crear otra implementacion de `InvalidProductIdRule` sin cambiar `ProductIdSumCalculator`.

### DIP: Inversion de Dependencias

`ProductIdSumCalculator` depende de la interfaz `InvalidProductIdRule`, no de una clase concreta.

### KISS

La solucion evita recorrer rangos completos y se centra en generar candidatos que pueden ser invalidos. Esto mantiene el algoritmo mas directo para rangos grandes.

### YAGNI

No se anaden capas extra ni jerarquias innecesarias. Solo hay una interfaz porque realmente hay dos reglas que cambian entre partes.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `InvalidProductIdRule`.

Las dos estrategias son:

* `DoublePatternInvalidProductIdRule`
* `RepeatedPatternInvalidProductIdRule`

Ambas tienen el mismo contrato, pero generan IDs invalidos con criterios distintos.

### Static Factory / metodos de construccion

`RepeatedPatternProductId` contiene metodos estaticos para construir IDs desde patrones:

* `fromRepeatedHalf(...)`
* `fromRepeatedSequence(...)`

No son fabricas de objetos complejos, pero si centralizan la creacion de numeros a partir de reglas del dominio.

### Value Object

`ProductIdRange` funciona como value object: representa un valor del dominio, valida sus invariantes y se compara por contenido gracias a ser un `record`.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun servicio global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a una regla existente.

No se usa `Adapter`, porque no se integra una API externa incompatible.

No se usa `Command`, porque las reglas no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 2 estan en:

```text
src/test/java/aoc/day02
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de un rango individual.
* Parseo de rangos separados por comas.
* Comas finales.
* Espacios insertados dentro de una linea larga.
* Formatos de rango invalidos.
* Input vacio.
* Deteccion de IDs con dos mitades iguales.
* Deteccion de IDs con secuencias repetidas.
* Rangos sin IDs invalidos.
* Evitar contar dos veces el mismo ID repetido, como `111111`.

Un caso importante es `111111`, porque puede generarse desde distintas secuencias repetidas. El uso de `Set<Long>` evita que se sume mas de una vez dentro de la misma regla.
