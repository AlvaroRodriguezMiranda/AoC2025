# Dia 5: Cafeteria

En el quinto dia del Advent of Code 2025, el problema trata sobre una cafeteria y una base de datos de ingredientes. El input contiene rangos de IDs que se consideran frescos y, despues de una linea en blanco, una lista de ingredientes disponibles.

La entrada tiene dos secciones:

```text
3-5
10-14
16-20
12-18

1
5
8
11
17
32
```

La primera seccion contiene rangos de IDs frescos. La segunda seccion contiene IDs concretos de ingredientes disponibles.

## Parte 1

En la primera parte hay que contar cuantos ingredientes disponibles son frescos.

Un ingrediente disponible es fresco si su ID cae dentro de cualquiera de los rangos frescos.

Con el ejemplo:

```text
Rangos frescos:
3-5
10-14
16-20
12-18

Disponibles:
1, 5, 8, 11, 17, 32
```

Los ingredientes frescos disponibles son:

```text
5
11
17
```

La respuesta de la parte 1 seria `3`.

## Parte 2

En la segunda parte ya no se mira solo la lista de ingredientes disponibles. Ahora hay que contar cuantos IDs frescos existen en total segun todos los rangos.

El detalle importante es que los rangos pueden solaparse o tocarse.

Por ejemplo:

```text
10-14
12-18
16-20
```

Estos rangos forman una zona continua desde `10` hasta `20`. No se deben contar los IDs repetidos varias veces.

Tambien se fusionan rangos adyacentes:

```text
1-3
4-6
```

Como `4` viene justo despues de `3`, se consideran una zona continua de `1` a `6`.

La respuesta de la parte 2 es el numero total de IDs cubiertos por los rangos frescos despues de fusionarlos.

## Estructura del paquete

```text
aoc.day05
|-- CafeteriaPuzzle.java
|-- input
|   `-- InventoryDatabaseParser.java
|-- inventory
|   |-- FreshIngredientCounter.java
|   |-- IngredientIdRange.java
|   `-- InventoryDatabase.java
`-- solver
    |-- AllFreshIngredientSolver.java
    |-- AvailableFreshIngredientSolver.java
    `-- FreshIngredientSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto en una base de datos del dominio.
* `inventory`: contiene los rangos, la base de datos y la fachada de conteo.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `CafeteriaPuzzle` coordina el dia completo.

## Clase principal `CafeteriaPuzzle`

`CafeteriaPuzzle` une el parser con la fachada que calcula las dos partes.

Tiene dos dependencias:

* `InventoryDatabaseParser`: parsea el input.
* `FreshIngredientCounter`: expone los dos conteos del dia.

Sus metodos principales son:

```java
public long solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `InventoryDatabaseParser`.
2. Obtienen un `InventoryDatabase`.
3. Delegan el calculo en `FreshIngredientCounter`.

El metodo `main` lee el archivo:

```text
src/main/resources/day05/input.txt
```

Despues crea el puzzle con:

```java
new InventoryDatabaseParser()
new FreshIngredientCounter(new AvailableFreshIngredientSolver(), new AllFreshIngredientSolver())
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir las lineas del archivo en una base de datos.

### `InventoryDatabaseParser`

`InventoryDatabaseParser` transforma el input en un `InventoryDatabase`.

Su metodo principal es:

```java
public InventoryDatabase parseLines(List<String> lines)
```

El parser busca primero la linea en blanco que separa las dos secciones.

El flujo es:

1. Busca el indice de la primera linea en blanco.
2. Las lineas anteriores se parsean como rangos frescos.
3. Las lineas posteriores se parsean como IDs disponibles.
4. Con ambas listas se crea un `InventoryDatabase`.

Si no hay linea en blanco, lanza una excepcion porque el input no tiene la estructura esperada.

Tambien tiene:

```java
public IngredientIdRange parseRange(String line)
```

Este metodo parsea un rango con formato `inicio-fin`.

Si el texto no contiene exactamente un guion, lanza una excepcion. Si alguno de los extremos no es numerico, tambien lanza una excepcion.

## Clases del paquete `inventory`

El paquete `inventory` contiene las clases del dominio del inventario.

### `IngredientIdRange`

`IngredientIdRange` es un `record` que representa un rango inclusivo de IDs de ingrediente.

Contiene:

* `firstId`: primer ID del rango.
* `lastId`: ultimo ID del rango.

Valida que el inicio no sea mayor que el final. Por ejemplo, `5-3` no es un rango valido.

Su metodo principal es:

```java
public boolean contains(long ingredientId)
```

Este metodo indica si un ingrediente cae dentro del rango.

Ejemplo:

```java
IngredientIdRange range = new IngredientIdRange(3, 5);

range.contains(3); // true
range.contains(5); // true
range.contains(2); // false
range.contains(6); // false
```

Al ser un `record`, representa un valor del dominio y se compara por contenido.

### `InventoryDatabase`

`InventoryDatabase` agrupa los datos parseados del input.

Contiene dos listas:

* `freshRanges`: rangos de ingredientes frescos.
* `availableIngredientIds`: IDs concretos disponibles.

En el constructor copia ambas listas con `List.copyOf(...)`, para evitar modificaciones externas despues de crear la base de datos.

Expone sus datos con:

```java
public List<IngredientIdRange> freshRanges()
public List<Long> availableIngredientIds()
```

Esta clase no calcula resultados. Solo representa el estado del inventario ya parseado.

### `FreshIngredientCounter`

`FreshIngredientCounter` es una fachada que expone los dos conteos del dia:

```java
public long countFreshAvailableIngredients(InventoryDatabase inventoryDatabase)
public long countAllFreshIngredientIds(InventoryDatabase inventoryDatabase)
```

Internamente tiene dos solvers:

* `FreshIngredientSolver partOneSolver`
* `FreshIngredientSolver partTwoSolver`

Tambien tiene un constructor por defecto que crea:

```java
new AvailableFreshIngredientSolver()
new AllFreshIngredientSolver()
```

La ventaja de esta clase es que `CafeteriaPuzzle` no necesita conocer los detalles de cada estrategia. Solo pide el conteo que corresponde.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `FreshIngredientSolver`

`FreshIngredientSolver` es la interfaz comun para las dos partes.

Define este contrato:

```java
long solve(InventoryDatabase inventoryDatabase);
```

Cada solver recibe la misma base de datos, pero responde una pregunta distinta.

### `AvailableFreshIngredientSolver`

`AvailableFreshIngredientSolver` resuelve la parte 1.

Su objetivo es contar cuantos IDs de la lista `availableIngredientIds` caen dentro de algun rango fresco.

El flujo es:

1. Recorre los ingredientes disponibles.
2. Para cada ID, comprueba si algun rango de `freshRanges` lo contiene.
3. Cuenta los que cumplen la condicion.

Internamente usa:

```java
inventoryDatabase.availableIngredientIds().stream()
        .filter(ingredientId -> isFresh(ingredientId, inventoryDatabase))
        .count()
```

La comprobacion de frescura se hace con `anyMatch`, porque basta con que un ingrediente este en un rango.

Si un ingrediente cae en dos rangos solapados, sigue contando una sola vez, porque se esta recorriendo la lista de ingredientes disponibles, no los rangos.

### `AllFreshIngredientSolver`

`AllFreshIngredientSolver` resuelve la parte 2.

Su objetivo es contar todos los IDs cubiertos por los rangos frescos, sin duplicar IDs cuando los rangos se solapan.

El flujo es:

1. Ordena los rangos por `firstId`.
2. Si no hay rangos, devuelve `0`.
3. Mantiene un rango actual con `currentFirstId` y `currentLastId`.
4. Recorre los rangos ordenados.
5. Si el siguiente rango se solapa o es adyacente, lo fusiona ampliando `currentLastId`.
6. Si esta separado, suma el tamano del rango actual y empieza uno nuevo.
7. Al final suma el ultimo rango acumulado.

La condicion de fusion es:

```java
range.firstId() <= currentLastId + 1
```

```java
currentLastId - currentFirstId + 1
```

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia entre las dos partes no esta en el parser ni en el modelo. Ambas usan el mismo `InventoryDatabase`.

En parte 1:

```java
new AvailableFreshIngredientSolver()
```

Se cuenta cuantos ingredientes disponibles caen en algun rango.

En parte 2:

```java
new AllFreshIngredientSolver()
```

Se calcula cuantos IDs existen en total dentro de la union de todos los rangos.

Por eso parte 1 trabaja sobre `availableIngredientIds`, mientras que parte 2 trabaja sobre `freshRanges`.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, inventario y solvers estan separados por responsabilidad.
* Bajo acoplamiento: los solvers reciben un `InventoryDatabase`, no lineas de texto.
* Modularidad: parte 1 y parte 2 se expresan como estrategias distintas.
* Encapsulacion: `InventoryDatabase` copia sus listas internas.
* Abstraccion: `FreshIngredientSolver` permite tratar ambos calculos con el mismo contrato.
* Codigo expresivo: los nombres indican si se cuentan disponibles frescos o todos los IDs frescos.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `InventoryDatabaseParser` parsea el input.
* `IngredientIdRange` representa un rango y comprueba pertenencia.
* `InventoryDatabase` agrupa los datos parseados.
* `FreshIngredientCounter` actua como fachada.
* `AvailableFreshIngredientSolver` resuelve parte 1.
* `AllFreshIngredientSolver` resuelve parte 2.
* `CafeteriaPuzzle` coordina.

### DRY

La comprobacion de pertenencia a un rango esta en `IngredientIdRange.contains(...)`.

El parseo de IDs numericos esta centralizado dentro de `InventoryDatabaseParser`.

### OCP: Abierto/Cerrado

Si apareciera otro calculo sobre el inventario, se podria crear otra implementacion de `FreshIngredientSolver` sin modificar los solvers existentes.

### DIP: Inversion de Dependencias

`FreshIngredientCounter` depende de la interfaz `FreshIngredientSolver`, no de implementaciones concretas obligatorias.

### KISS

La parte 2 se resuelve ordenando y fusionando rangos. Es una solucion directa y evita enumerar todos los IDs cuando los rangos son grandes.

### YAGNI

No se anaden estructuras mas complejas de intervalos. Una lista ordenada y un rango acumulado bastan para resolver el problema.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `FreshIngredientSolver`.

Las dos estrategias son:

* `AvailableFreshIngredientSolver`
* `AllFreshIngredientSolver`

Ambas reciben un `InventoryDatabase`, pero una cuenta ingredientes disponibles y la otra cuenta la union completa de rangos.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a rangos o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque los calculos no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 5 estan en:

```text
src/test/java/aoc/day05
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de un rango individual.
* Parseo de las dos secciones separadas por linea en blanco.
* Rechazo de una base de datos sin separador.
* Rechazo de formato de rango invalido.
* Comprobacion de pertenencia dentro de `IngredientIdRange`.
* Rechazo de rangos con inicio mayor que final.
* Conteo de ingredientes disponibles dentro de algun rango fresco.
* Conteo unico aunque rangos solapados contengan el mismo ingrediente disponible.
* Fusion de rangos solapados.
* Conteo de rangos separados sin fusionarlos.
* Fusion de rangos adyacentes.

Un caso importante es la union de `10-14`, `16-20` y `12-18`. Aunque son tres rangos, juntos cubren una zona continua de `10` a `20`, y los IDs solapados no se cuentan dos veces.
