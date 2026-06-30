# Dia 4: Printing Department

En el cuarto dia del Advent of Code 2025, el problema trata sobre el departamento de impresion. La entrada representa un mapa con rollos de papel y espacios vacios.

El mapa usa dos caracteres:

* `@`: hay un rollo de papel.
* `.`: hay un hueco vacio.

Por ejemplo:

```text
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
```

La idea del problema es decidir que rollos son accesibles para una carretilla. Un rollo es accesible si tiene menos de cuatro rollos alrededor. En el codigo esto equivale a tener como maximo `3` rollos adyacentes.

La adyacencia se calcula en ocho direcciones:

* arriba
* abajo
* izquierda
* derecha
* cuatro diagonales

## Parte 1

En la primera parte hay que contar cuantos rollos son accesibles en el mapa inicial.

Para cada posicion con `@`, se cuentan sus vecinos en las ocho direcciones. Si el numero de vecinos con rollo es menor que `4`, ese rollo se considera accesible.

La respuesta de la parte 1 es el numero total de rollos accesibles.

En mi codigo esta parte se resuelve con `AccessibleRollSolver`.

## Parte 2

En la segunda parte ya no basta con contar los rollos accesibles del mapa inicial. Ahora se van retirando por rondas.

El flujo es:

1. Se buscan todos los rollos accesibles en el mapa actual.
2. Se retiran todos a la vez.
3. Se genera un nuevo mapa sin esos rollos.
4. Se repite el proceso hasta que no quede ningun rollo accesible.

La respuesta de la parte 2 es el total de rollos retirados sumando todas las rondas.

En mi codigo esta parte se resuelve con `RemovableRollSolver`.

## Estructura del paquete

```text
aoc.day04
|-- PrintingDepartmentPuzzle.java
|-- input
|   `-- PaperRollMapParser.java
|-- paper
|   |-- ForkliftAccessSolver.java
|   |-- GridPosition.java
|   `-- PaperRollGrid.java
`-- solver
    |-- AccessibleRollSolver.java
    |-- PaperRollSolver.java
    `-- RemovableRollSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del mapa en una cuadricula.
* `paper`: contiene el modelo de posiciones, cuadricula y fachada de acceso.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `PrintingDepartmentPuzzle` coordina el dia completo.

## Clase principal `PrintingDepartmentPuzzle`

`PrintingDepartmentPuzzle` une el parser con la fachada que calcula las dos partes.

Tiene dos dependencias:

* `PaperRollMapParser`: parsea el mapa.
* `ForkliftAccessSolver`: expone los calculos de rollos accesibles y removibles.

Sus metodos principales son:

```java
public int solvePartOne(List<String> inputLines)
public int solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo general:

1. Parsean las lineas con `PaperRollMapParser`.
2. Obtienen un `PaperRollGrid`.
3. Delegan el calculo en `ForkliftAccessSolver`.

El metodo `main` lee el archivo:

```text
src/main/resources/day04/input.txt
```

Despues crea el puzzle con:

```java
new PaperRollMapParser()
new ForkliftAccessSolver(new AccessibleRollSolver(), new RemovableRollSolver())
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de transformar las lineas del input en un mapa valido.

### `PaperRollMapParser`

`PaperRollMapParser` convierte una lista de strings en un `PaperRollGrid`.

Su metodo principal es:

```java
public PaperRollGrid parseLines(List<String> lines)
```

Este metodo:

1. Limpia espacios con `trim`.
2. Ignora lineas vacias.
3. Valida que cada linea solo contenga `@` y `.`.
4. Crea un `PaperRollGrid` con las filas resultantes.

Si aparece cualquier otro caracter, lanza una excepcion.

Por ejemplo, esta entrada es invalida:

```text
.#
@.
```

El parser no comprueba directamente si el mapa es rectangular. Esa validacion queda en `PaperRollGrid`, que es quien conoce las reglas de una cuadricula valida.

## Clases del paquete `paper`

El paquete `paper` contiene las clases del dominio del mapa de rollos.

### `GridPosition`

`GridPosition` es un `record` que representa una posicion dentro de la cuadricula.

Contiene:

* `row`: fila.
* `column`: columna.

Al ser un `record`, es inmutable y se compara por valor. Esto lo hace comodo para guardar posiciones, pasarlas entre clases y usarlas en tests.

### `PaperRollGrid`

`PaperRollGrid` representa el mapa de rollos de papel.

Internamente guarda las filas como una lista de strings. En el constructor copia la lista con `List.copyOf(...)`, para evitar cambios externos despues de crear la cuadricula.

Valida tres cosas:

* La lista de filas no puede estar vacia.
* Las filas no pueden tener longitud cero.
* Todas las filas deben tener la misma anchura.

Sus metodos principales son:

```java
public int height()
public int width()
```

Devuelven las dimensiones de la cuadricula.

```java
public boolean hasPaperRollAt(GridPosition position)
```

Indica si hay un rollo en una posicion. Si la posicion esta fuera del mapa, devuelve `false`.

```java
public int adjacentPaperRolls(GridPosition position)
```

Cuenta cuantos rollos hay alrededor de una posicion en las ocho direcciones.

```java
public List<GridPosition> paperRollPositions()
```

Devuelve todas las posiciones donde hay un `@`.

```java
public PaperRollGrid withoutPaperRolls(List<GridPosition> positionsToRemove)
```

Crea una nueva cuadricula sustituyendo por `.` las posiciones indicadas.

Este metodo es importante para la parte 2. No modifica el mapa original; crea un nuevo `PaperRollGrid`. Asi se evita que una ronda afecte parcialmente al calculo de esa misma ronda.

### `ForkliftAccessSolver`

`ForkliftAccessSolver` es una fachada que expone los dos calculos del dia:

```java
public int countAccessibleRolls(PaperRollGrid paperRollGrid)
public int countRemovableRolls(PaperRollGrid paperRollGrid)
```

Internamente tiene dos solvers:

* `PaperRollSolver partOneSolver`
* `PaperRollSolver partTwoSolver`

Tambien tiene un constructor por defecto que crea:

```java
new AccessibleRollSolver()
new RemovableRollSolver()
```

La ventaja de esta clase es que el codigo que usa el paquete `paper` no necesita conocer las clases concretas del paquete `solver`.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `PaperRollSolver`

`PaperRollSolver` es la interfaz comun para las dos partes.

Define este contrato:

```java
int solve(PaperRollGrid paperRollGrid);
```

Cada solver recibe una cuadricula y devuelve un entero con el resultado.

### `AccessibleRollSolver`

`AccessibleRollSolver` resuelve la parte 1.

Tiene esta constante:

```java
private static final int MAXIMUM_ACCESSIBLE_ADJACENT_ROLLS = 3;
```

Esto representa la regla del enunciado: un rollo es accesible si tiene menos de cuatro rollos alrededor.

El flujo es:

1. Recorre todas las posiciones con rollo usando `paperRollGrid.paperRollPositions()`.
2. Para cada posicion, calcula sus vecinos con `paperRollGrid.adjacentPaperRolls(position)`.
3. Si tiene `3` o menos vecinos, lo cuenta como accesible.
4. Devuelve el total.

El metodo:

```java
static boolean isAccessible(PaperRollGrid paperRollGrid, GridPosition position)
```

es estatico a nivel de paquete para que `RemovableRollSolver` pueda reutilizar exactamente la misma regla en la parte 2.

### `RemovableRollSolver`

`RemovableRollSolver` resuelve la parte 2.

Su trabajo es aplicar la regla de accesibilidad repetidamente por rondas.

El flujo es:

1. Empieza con la cuadricula inicial.
2. Calcula las posiciones accesibles en esa cuadricula.
3. Mientras haya posiciones accesibles:
   * Suma cuantas se van a retirar.
   * Crea una nueva cuadricula sin esas posiciones.
   * Vuelve a calcular accesibles sobre la nueva cuadricula.
4. Devuelve el total de rollos retirados.

Es importante que dentro de una ronda se retiren todos los accesibles a la vez. Por eso primero se guarda la lista de posiciones accesibles y despues se llama a:

```java
currentGrid.withoutPaperRolls(accessiblePositions)
```

Asi el calculo de una ronda no se mezcla con la siguiente.

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia no esta en la regla de accesibilidad. Esa regla es la misma:

```text
un rollo es accesible si tiene como maximo 3 vecinos con rollo
```

La diferencia esta en como se aplica:

En parte 1:

```java
new AccessibleRollSolver()
```

La regla se aplica una sola vez sobre el mapa inicial.

En parte 2:

```java
new RemovableRollSolver()
```

La regla se aplica por rondas, retirando rollos y recalculando sobre el nuevo mapa.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, cuadricula, posiciones y solvers estan separados por responsabilidad.
* Bajo acoplamiento: los solvers trabajan con `PaperRollGrid`, no con strings del input.
* Modularidad: la regla de accesibilidad y la retirada por rondas estan en clases distintas.
* Encapsulacion: `PaperRollGrid` controla la representacion interna del mapa.
* Abstraccion: `PaperRollSolver` permite tratar parte 1 y parte 2 con un contrato comun.
* Codigo expresivo: nombres como `AccessibleRollSolver` y `RemovableRollSolver` indican que calcula cada clase.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `PaperRollMapParser` parsea y valida caracteres.
* `GridPosition` representa coordenadas.
* `PaperRollGrid` representa la cuadricula y cuenta vecinos.
* `AccessibleRollSolver` resuelve parte 1.
* `RemovableRollSolver` resuelve parte 2.
* `ForkliftAccessSolver` actua como fachada.
* `PrintingDepartmentPuzzle` coordina.

### DRY

La regla de accesibilidad esta en `AccessibleRollSolver.isAccessible(...)` y se reutiliza en parte 2.

La logica de contar vecinos esta una sola vez en `PaperRollGrid.adjacentPaperRolls(...)`.

### OCP: Abierto/Cerrado

Si apareciera otra forma de resolver el mapa, se podria crear otra implementacion de `PaperRollSolver` sin cambiar la fachada ni el puzzle principal.

### DIP: Inversion de Dependencias

`ForkliftAccessSolver` depende de la interfaz `PaperRollSolver`, no de clases concretas obligatorias. Aunque el constructor por defecto crea las implementaciones habituales, tambien se pueden inyectar otras.

### KISS

La solucion usa una cuadricula simple y recorre posiciones. Para el problema, esto es mas claro que introducir estructuras mas complejas.

### YAGNI

No se anaden capas extra. Solo hay una interfaz porque hay dos formas reales de resolver el mismo mapa.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `PaperRollSolver`.

Las dos estrategias son:

* `AccessibleRollSolver`
* `RemovableRollSolver`

Ambas reciben un `PaperRollGrid`, pero una cuenta accesibles una vez y la otra repite el proceso por rondas.

### Facade

`ForkliftAccessSolver` funciona como fachada.

Expone metodos simples:

* `countAccessibleRolls(...)`
* `countRemovableRolls(...)`

Por debajo delega en las estrategias concretas.

### Value Object

`GridPosition` funciona como value object. Representa una coordenada, es inmutable y se compara por contenido.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a una cuadricula o solver.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque retirar rollos no necesita historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 4 estan en:

```text
src/test/java/aoc/day04
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parsear mapas ignorando lineas vacias.
* Rechazar caracteres distintos de `@` y `.`.
* Rechazar mapas no rectangulares.
* Detectar posiciones con rollo.
* Contar vecinos en ocho direcciones.
* Contar vecinos en bordes del mapa.
* Devolver todas las posiciones con rollo.
* Crear una nueva cuadricula sin rollos seleccionados.
* Contar rollos accesibles.
* No contar como accesible un rollo con cuatro vecinos.
* Contar rollos retirados a lo largo de varias rondas.

Un caso importante es una cuadricula completa de `3x3` con todos los caracteres `@`. En la parte 2 se terminan retirando los 9 rollos, pero no todos necesariamente por la misma razon inicial: despues de cada ronda, cambia el mapa y hay que recalcular la accesibilidad.
