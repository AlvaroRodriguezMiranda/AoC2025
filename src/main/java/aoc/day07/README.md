# Dia 7: Laboratories

En el septimo dia del Advent of Code 2025, el problema trata sobre unos laboratorios con un manifold de taquiones. El input es un mapa rectangular donde una particula empieza en una posicion marcada con `S` y avanza hacia abajo por la cuadricula.

El mapa usa estos caracteres:

* `S`: posicion inicial.
* `^`: splitter de taquiones.
* `.`: espacio vacio.

Un ejemplo de mapa es:

```text
.......S.......
...............
.......^.......
...............
......^.^......
```

La particula baja fila a fila. Si llega a una celda vacia, sigue bajando por la misma columna. Si llega a un splitter `^`, la trayectoria se divide hacia las columnas izquierda y derecha.

La clave del dia es que parte 1 y parte 2 parecen parecidas, pero no necesitan guardar la misma informacion.

## Parte 1

En la primera parte hay que contar cuantos splitters se activan.

Para esto basta con saber en que columnas hay haces activos en cada fila. Si dos haces llegan a la misma columna, se pueden fusionar para el conteo de parte 1, porque a partir de ahi se comportan igual.

Por eso el codigo usa un `Set<Integer>` de columnas activas.

Cuando un haz llega a un splitter:

1. Se incrementa el contador de splitters.
2. Se intenta crear un nuevo haz en la columna izquierda.
3. Se intenta crear un nuevo haz en la columna derecha.
4. Solo se conservan haces que quedan dentro del ancho del mapa.

En mi codigo esta parte se resuelve con `TachyonSplitterCounter`, adaptado por `SplitCountingSolver`.

## Parte 2

En la segunda parte hay que contar todas las lineas temporales posibles.

Aqui ya no basta con saber si una columna esta activa o no. Si varias lineas temporales llegan a la misma columna, hay que conservar cuantas son.

Por eso el codigo usa:

```java
Map<Integer, BigInteger>
```

La clave del mapa es la columna y el valor es la cantidad de lineas temporales que llegan a esa columna.

Cuando una linea temporal llega a un splitter:

1. Se divide hacia izquierda y derecha.
2. La cantidad de lineas se propaga a cada salida.
3. Si varias lineas llegan a la misma columna, sus cantidades se suman.
4. Si una salida queda fuera del mapa, esa linea temporal se considera completada.

El resultado puede crecer mucho, asi que se usa `BigInteger`.

En mi codigo esta parte se resuelve con `QuantumTimelineCounter`, adaptado por `QuantumTimelineSolver`.

## Estructura del paquete

```text
aoc.day07
|-- LaboratoriesPuzzle.java
|-- input
|   `-- TachyonManifoldParser.java
|-- manifold
|   |-- GridPosition.java
|   |-- QuantumTimelineCounter.java
|   |-- TachyonManifold.java
|   `-- TachyonSplitterCounter.java
`-- solver
    |-- QuantumTimelineSolver.java
    |-- SplitCountingSolver.java
    `-- TachyonSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del mapa en un manifold valido.
* `manifold`: contiene el modelo de la cuadricula y los contadores principales.
* `solver`: adapta los contadores a una interfaz comun de resolucion.

La clase `LaboratoriesPuzzle` coordina el dia completo.

## Clase principal `LaboratoriesPuzzle`

`LaboratoriesPuzzle` une el parser con las estrategias de las dos partes.

Tiene tres dependencias en su constructor principal:

* `TachyonManifoldParser`: parsea el mapa.
* `TachyonSolver<Integer>`: solver de parte 1.
* `TachyonSolver<BigInteger>`: solver de parte 2.

Tambien tiene un constructor auxiliar que recibe directamente:

```java
TachyonSplitterCounter splitterCounter
QuantumTimelineCounter timelineCounter
```

Con esos contadores crea internamente:

```java
new SplitCountingSolver(splitterCounter)
new QuantumTimelineSolver(timelineCounter)
```

Sus metodos principales son:

```java
public int solvePartOne(List<String> inputLines)
public BigInteger solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `TachyonManifoldParser`.
2. Obtienen un `TachyonManifold`.
3. Delegan el calculo en el solver correspondiente.

El metodo `main` lee el archivo:

```text
src/main/resources/day07/input.txt
```

Despues crea:

```java
new TachyonManifoldParser()
new TachyonSplitterCounter()
new QuantumTimelineCounter()
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir el texto del mapa en un objeto del dominio.

### `TachyonManifoldParser`

`TachyonManifoldParser` transforma las lineas del input en un `TachyonManifold`.

Su metodo principal es:

```java
public TachyonManifold parse(List<String> lines)
```

El parser hace lo siguiente:

1. Ignora lineas en blanco.
2. Comprueba que el mapa no este vacio.
3. Comprueba que todas las filas tengan la misma anchura.
4. Valida que cada celda sea `.`, `^` o `S`.
5. Localiza la posicion inicial `S`.
6. Rechaza mapas sin inicio.
7. Rechaza mapas con mas de un inicio.

Si el mapa es valido, crea una lista de listas de caracteres y la pasa a `TachyonManifold` junto con la posicion inicial.

Ejemplos de entradas invalidas:

```text
...
```

No tiene `S`.

```text
S.S
```

Tiene mas de un inicio.

```text
Sx.
```

Contiene una celda desconocida.

## Clases del paquete `manifold`

El paquete `manifold` contiene el modelo de la cuadricula y los algoritmos principales.

### `GridPosition`

`GridPosition` es un `record` que representa una posicion del mapa.

Contiene:

* `row`: fila.
* `column`: columna.

Al ser un `record`, es inmutable y se compara por valor.

### `TachyonManifold`

`TachyonManifold` representa el mapa del laboratorio.

Guarda:

* `cells`: las celdas del mapa.
* `startPosition`: posicion inicial de la particula.

En el constructor copia cada fila con `List.copyOf(...)`, para evitar modificaciones externas.

Sus metodos principales son:

```java
public int height()
public int width()
```

Devuelven dimensiones del mapa.

```java
public GridPosition startPosition()
```

Devuelve la posicion donde esta `S`.

```java
public boolean contains(int row, int column)
```

Indica si una coordenada esta dentro de la cuadricula.

```java
public boolean hasSplitterAt(int row, int column)
```

Indica si hay un splitter `^` en una posicion. Si la posicion esta fuera del mapa, devuelve `false`.

Esta clase no contiene la logica de conteo. Solo representa el mapa y ofrece consultas seguras.

### `TachyonSplitterCounter`

`TachyonSplitterCounter` contiene el algoritmo de parte 1.

Su metodo principal es:

```java
public int countSplits(TachyonManifold manifold)
```

El algoritmo mantiene un conjunto de columnas activas:

```java
Set<Integer> beamColumns
```

Al principio solo esta activa la columna de `S`.

Despues recorre las filas desde la fila siguiente al inicio hasta el final del mapa.

En cada fila:

1. Crea un nuevo set para las columnas de la siguiente fila.
2. Recorre las columnas activas actuales.
3. Si la celda contiene un splitter, incrementa el contador.
4. Si hay splitter, intenta anadir `column - 1` y `column + 1`.
5. Si no hay splitter, mantiene la misma columna.
6. Pasa al siguiente conjunto de columnas.

El uso de `Set<Integer>` hace que si dos haces llegan a la misma columna, se fusionen automaticamente para la parte 1.

### `QuantumTimelineCounter`

`QuantumTimelineCounter` contiene el algoritmo de parte 2.

Su metodo principal es:

```java
public BigInteger countTimelines(TachyonManifold manifold)
```

El algoritmo mantiene un mapa de lineas temporales activas:

```java
Map<Integer, BigInteger> activeTimelines
```

La clave es la columna y el valor es cuantas lineas temporales llegan a esa columna.

Al principio hay una sola linea temporal en la columna de `S`:

```java
Map.of(manifold.startPosition().column(), BigInteger.ONE)
```

Tambien mantiene:

```java
BigInteger completedTimelines
```

Este valor suma las lineas temporales que salen fuera del mapa al dividirse.

En cada fila:

1. Crea un nuevo mapa para la siguiente fila.
2. Recorre cada columna activa y su cantidad.
3. Si hay splitter, propaga esa cantidad a izquierda y derecha.
4. Si una salida queda fuera del mapa, suma esa cantidad a `completedTimelines`.
5. Si varias lineas llegan a la misma columna, las suma con `merge`.
6. Si no hay splitter, mantiene la misma columna con la misma cantidad.

Al final devuelve la suma de:

* lineas temporales completadas al salir del mapa.
* lineas temporales que siguen activas al terminar las filas.

El uso de `BigInteger` evita desbordamientos cuando el numero de lineas temporales crece mucho.

## Clases del paquete `solver`

El paquete `solver` contiene adaptadores finos sobre los contadores.

### `TachyonSolver<T>`

`TachyonSolver<T>` es la interfaz comun de los solvers del dia.

Define este contrato:

```java
T solve(TachyonManifold manifold);
```

Es generica porque las dos partes devuelven tipos distintos:

* Parte 1 devuelve `Integer`.
* Parte 2 devuelve `BigInteger`.

### `SplitCountingSolver`

`SplitCountingSolver` adapta `TachyonSplitterCounter` a la interfaz `TachyonSolver<Integer>`.

Tiene una dependencia:

```java
private final TachyonSplitterCounter splitterCounter;
```

Su metodo `solve(...)` solo delega:

```java
return splitterCounter.countSplits(manifold);
```

### `QuantumTimelineSolver`

`QuantumTimelineSolver` adapta `QuantumTimelineCounter` a la interfaz `TachyonSolver<BigInteger>`.

Tiene una dependencia:

```java
private final QuantumTimelineCounter timelineCounter;
```

Su metodo `solve(...)` delega:

```java
return timelineCounter.countTimelines(manifold);
```

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia principal esta en la informacion que se conserva durante el recorrido.

En parte 1:

```java
Set<Integer> beamColumns
```

Solo importa si una columna tiene algun haz activo. Si llegan dos haces a la misma columna, se tratan como uno.

En parte 2:

```java
Map<Integer, BigInteger> activeTimelines
```

Importa cuantas lineas temporales llegan a cada columna. Si llegan varias a la misma columna, se suman.

Por eso parte 1 puede contar splitters con un set, pero parte 2 necesita un mapa con cantidades.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, manifold, contadores y solvers estan separados.
* Bajo acoplamiento: los contadores trabajan con `TachyonManifold`, no con strings.
* Modularidad: parte 1 y parte 2 tienen algoritmos separados.
* Encapsulacion: `TachyonManifold` copia sus celdas y ofrece consultas controladas.
* Abstraccion: `TachyonSolver<T>` permite tratar los solvers con un contrato comun.
* Codigo expresivo: los nombres distinguen conteo de splitters y conteo de lineas temporales.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `TachyonManifoldParser` parsea y valida el mapa.
* `GridPosition` representa coordenadas.
* `TachyonManifold` representa la cuadricula.
* `TachyonSplitterCounter` resuelve parte 1.
* `QuantumTimelineCounter` resuelve parte 2.
* `SplitCountingSolver` adapta el contador de parte 1.
* `QuantumTimelineSolver` adapta el contador de parte 2.
* `LaboratoriesPuzzle` coordina.

### DRY

Las consultas sobre dimensiones, limites y splitters estan centralizadas en `TachyonManifold`.

El parseo de celdas validas esta centralizado en `TachyonManifoldParser`.

### OCP: Abierto/Cerrado

Si apareciera otra forma de analizar el manifold, se podria crear otra implementacion de `TachyonSolver<T>` sin modificar las existentes.

### DIP: Inversion de Dependencias

`LaboratoriesPuzzle` depende de la interfaz `TachyonSolver<T>`, no de un contador concreto obligatorio.

### KISS

Cada parte usa la estructura de datos mas simple que necesita:

* `Set` para columnas activas en parte 1.
* `Map` con cantidades en parte 2.

### YAGNI

No se modelan objetos de haz o linea temporal si con columnas y cantidades basta para resolver el problema.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `TachyonSolver<T>`.

Las dos estrategias son:

* `SplitCountingSolver`
* `QuantumTimelineSolver`

Ambas reciben un `TachyonManifold`, pero devuelven resultados distintos y usan contadores distintos.

### Adapter

`SplitCountingSolver` y `QuantumTimelineSolver` funcionan como adaptadores simples.

Adaptan clases con metodos concretos:

* `countSplits(...)`
* `countTimelines(...)`

a una interfaz comun:

```java
solve(...)
```

### Value Object

`GridPosition` funciona como value object. Representa una coordenada, es inmutable y se compara por contenido.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a contadores o solvers.

No se usa `Command`, porque los movimientos no necesitan historial, deshacer ni ejecucion diferida.

No se usa `Factory Method` complejo, porque la creacion de objetos es directa y no hay familias de objetos que construir.

## Tests

Los tests del dia 7 estan en:

```text
src/test/java/aoc/day07
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de posicion inicial y splitters.
* Rechazo de diagramas sin `S`.
* Rechazo de diagramas con mas de un `S`.
* Rechazo de mapas no rectangulares.
* Rechazo de celdas desconocidas.
* Conteo de cero splits cuando el haz no toca splitters.
* Conteo de un split cuando el haz toca un splitter.
* Fusion de haces que llegan a la misma columna en parte 1.
* Mantener una linea temporal cuando no hay splitters.
* Crear dos lineas temporales al tocar un splitter.
* Sumar lineas temporales distintas que llegan a la misma columna.

Un caso importante es el mapa donde dos caminos llegan a la misma columna. En parte 1 se fusionan porque solo importa la columna activa, pero en parte 2 se suman porque cada linea temporal sigue contando como posibilidad distinta.
