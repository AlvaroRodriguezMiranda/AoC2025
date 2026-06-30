# Dia 6: Trash Compactor

En el sexto dia del Advent of Code 2025, el problema trata sobre una hoja de operaciones que hay que interpretar para ayudar con un compactador de basura.

El input no viene como una lista normal de operaciones, sino como una especie de hoja escrita en columnas. Varias operaciones aparecen una al lado de otra y se separan por columnas vacias.

Un ejemplo de entrada es:

```text
123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +
```

Cada bloque de columnas representa un problema matematico. El simbolo de la parte inferior indica si los numeros se suman o se multiplican.

La clave del dia es que las dos partes usan la misma hoja, pero la interpretan de forma distinta.

## Parte 1

En la primera parte, cada problema se lee de arriba hacia abajo.

Con el ejemplo anterior, los bloques se interpretan asi:

```text
123      328      51       64
45       64       387      23
6        98       215      314
*        +        *        +
```

Por tanto, se obtienen operaciones como:

```text
123 * 45 * 6
328 + 64 + 98
51 * 387 * 215
64 + 23 + 314
```

La respuesta de la parte 1 es la suma de los resultados de todos los problemas.

En mi codigo esta parte se resuelve con `TopToBottomWorksheetSolver`.

## Parte 2

En la segunda parte, la hoja se interpreta de derecha a izquierda dentro de cada bloque de columnas.

En vez de leer cada numero como una fila completa dentro del bloque, se forman numeros leyendo columnas. Para cada columna, se toman los digitos que aparecen de arriba hacia abajo.

Por ejemplo, con esta entrada:

```text
12
34
*
```

En modo derecha a izquierda se leen primero la columna derecha y luego la izquierda:

```text
columna derecha: 2 y 4 -> 24
columna izquierda: 1 y 3 -> 13
```

La operacion seria:

```text
24 * 13
```

La respuesta de la parte 2 tambien es la suma de los resultados de todos los problemas, pero usando este modo de lectura.

En mi codigo esta parte se resuelve con `RightToLeftWorksheetSolver`.

## Estructura del paquete

```text
aoc.day06
|-- TrashCompactorPuzzle.java
|-- input
|   `-- WorksheetParser.java
|-- solver
|   |-- RightToLeftWorksheetSolver.java
|   |-- TopToBottomWorksheetSolver.java
|   `-- TrashCompactorSolver.java
`-- worksheet
    |-- MathOperation.java
    |-- MathProblem.java
    `-- WorksheetSolver.java
```

El paquete esta separado en tres zonas:

* `input`: interpreta la hoja y la convierte en problemas matematicos.
* `worksheet`: contiene el modelo de operaciones y el solver comun.
* `solver`: contiene las estrategias de lectura de parte 1 y parte 2.

La clase `TrashCompactorPuzzle` coordina el dia completo.

## Clase principal `TrashCompactorPuzzle`

`TrashCompactorPuzzle` une las estrategias de resolucion de las dos partes.

Tiene dos dependencias principales:

* `TrashCompactorSolver partOneSolver`
* `TrashCompactorSolver partTwoSolver`

Tambien tiene un constructor auxiliar que recibe:

```java
WorksheetParser worksheetParser
WorksheetSolver worksheetSolver
```

Con esos objetos crea internamente:

```java
new TopToBottomWorksheetSolver(worksheetParser, worksheetSolver)
new RightToLeftWorksheetSolver(worksheetParser, worksheetSolver)
```

Sus metodos principales son:

```java
public long solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos delegan directamente en el solver correspondiente.

El metodo `main` lee el archivo:

```text
src/main/resources/day06/input.txt
```

Despues crea:

```java
new WorksheetParser()
new WorksheetSolver()
new TopToBottomWorksheetSolver(...)
new RightToLeftWorksheetSolver(...)
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de interpretar visualmente la hoja.

### `WorksheetParser`

`WorksheetParser` convierte las lineas del input en una lista de `MathProblem`.

Tiene dos metodos principales:

```java
public List<MathProblem> parseLines(List<String> lines)
public List<MathProblem> parseRightToLeftLines(List<String> lines)
```

`parseLines(...)` se usa para la parte 1 y lee los problemas de arriba hacia abajo.

`parseRightToLeftLines(...)` se usa para la parte 2 y lee los problemas por columnas de derecha a izquierda.

### Preparacion de lineas

Antes de parsear, el parser llama a `padLines(...)`.

Este metodo:

1. Comprueba que la hoja no este vacia.
2. Calcula la anchura maxima de todas las lineas.
3. Rellena las lineas mas cortas con espacios a la derecha.

Esto permite trabajar con indices de columna sin que una linea mas corta provoque errores.

### Separacion de problemas por columnas

El metodo `findProblemColumns(...)` localiza bloques de columnas no vacias.

Una columna se considera vacia si todas las filas tienen un espacio en esa posicion.

Cada bloque no vacio se guarda como un `ColumnRange`, que contiene:

* `startColumn`: columna inicial incluida.
* `endColumn`: columna final excluida.

Asi, el parser puede separar una hoja con varios problemas escritos en paralelo.

### Parseo de arriba hacia abajo

El metodo interno `parseProblemTopToBottom(...)` interpreta un bloque de columnas de la forma usada en la parte 1.

Para cada linea:

1. Extrae el texto dentro del rango de columnas.
2. Limpia espacios con `trim`.
3. Si el texto es `+` o `*`, lo convierte en `MathOperation`.
4. Si no esta vacio, lo parsea como numero.

Al final crea un `MathProblem` con la lista de numeros y la operacion.

Si no encuentra operacion, lanza una excepcion.

### Parseo de derecha a izquierda

El metodo interno `parseProblemRightToLeft(...)` interpreta un bloque de columnas de la forma usada en la parte 2.

El flujo es:

1. Recorre las columnas desde la derecha hacia la izquierda.
2. En cada columna, baja por todas las filas.
3. Si encuentra digitos, los concatena para formar un numero.
4. Si encuentra `+` o `*`, guarda la operacion.
5. Si encuentra otro caracter distinto de espacio, lanza una excepcion.

Si una columna produce digitos, ese numero se anade a la lista de numeros.

Este metodo permite que una misma hoja se interprete de forma distinta sin duplicar el modelo matematico.

## Clases del paquete `worksheet`

El paquete `worksheet` contiene el modelo matematico comun.

### `MathOperation`

`MathOperation` es un enum con las operaciones posibles:

* `ADDITION`
* `MULTIPLICATION`

Cada valor implementa:

```java
public long apply(List<Long> numbers)
```

`ADDITION` suma todos los numeros.

`MULTIPLICATION` multiplica todos los numeros.

Tambien tiene:

```java
public static MathOperation fromSymbol(char symbol)
```

Este metodo convierte:

* `+` en `ADDITION`
* `*` en `MULTIPLICATION`

Si aparece otro simbolo, lanza una excepcion.

### `MathProblem`

`MathProblem` representa un problema matematico ya parseado.

Contiene:

* `numbers`: lista de numeros.
* `operation`: operacion que se aplica a esos numeros.

En el constructor valida que la lista de numeros no este vacia. Tambien copia la lista con `List.copyOf(...)` para evitar modificaciones externas.

Su metodo principal es:

```java
public long solve()
```

Este metodo delega en:

```java
operation.apply(numbers)
```

Tambien expone:

```java
public List<Long> numbers()
public MathOperation operation()
```

Estos metodos se usan principalmente para tests y para inspeccionar el parseo.

### `WorksheetSolver`

`WorksheetSolver` resuelve una lista de problemas ya parseados.

Su metodo principal es:

```java
public long grandTotal(List<MathProblem> problems)
```

El flujo es:

1. Recorre todos los `MathProblem`.
2. Llama a `solve()` en cada uno.
3. Suma todos los resultados.
4. Devuelve el total.

Esta clase no sabe si los problemas vienen de lectura vertical o de lectura derecha a izquierda. Solo trabaja con problemas matematicos.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias que conectan el parser con el solver comun.

### `TrashCompactorSolver`

`TrashCompactorSolver` es la interfaz comun de las dos partes.

Define este contrato:

```java
long solve(List<String> inputLines);
```

Cada solver recibe las lineas originales del input y devuelve el resultado total.

### `TopToBottomWorksheetSolver`

`TopToBottomWorksheetSolver` resuelve la parte 1.

Tiene dos dependencias:

* `WorksheetParser`
* `WorksheetSolver`

El flujo es:

1. Llama a `worksheetParser.parseLines(inputLines)`.
2. Obtiene una lista de `MathProblem`.
3. Llama a `worksheetSolver.grandTotal(problems)`.
4. Devuelve el total.

Esta clase no implementa la suma ni la multiplicacion. Solo decide el modo de parseo de la parte 1.

### `RightToLeftWorksheetSolver`

`RightToLeftWorksheetSolver` resuelve la parte 2.

Tiene las mismas dependencias que el solver de parte 1:

* `WorksheetParser`
* `WorksheetSolver`

El flujo es:

1. Llama a `worksheetParser.parseRightToLeftLines(inputLines)`.
2. Obtiene una lista de `MathProblem`.
3. Llama a `worksheetSolver.grandTotal(problems)`.
4. Devuelve el total.

La diferencia con parte 1 esta solo en el metodo de parseo usado.

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia principal esta en como se interpreta la hoja.

En parte 1:

```java
worksheetParser.parseLines(inputLines)
```

Los problemas se leen de arriba hacia abajo dentro de cada bloque de columnas.

En parte 2:

```java
worksheetParser.parseRightToLeftLines(inputLines)
```

Los problemas se leen columna a columna, empezando por la derecha.

Despues de parsear, ambas partes reutilizan exactamente el mismo modelo:

* `MathProblem`
* `MathOperation`
* `WorksheetSolver`

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parseo, operaciones y estrategias estan separados.
* Bajo acoplamiento: `WorksheetSolver` no conoce el formato visual del input.
* Modularidad: cada modo de lectura esta aislado en su solver.
* Encapsulacion: `MathProblem` copia su lista de numeros.
* Abstraccion: `TrashCompactorSolver` permite tratar parte 1 y parte 2 con el mismo contrato.
* Codigo expresivo: los nombres indican si la hoja se lee de arriba abajo o de derecha a izquierda.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `WorksheetParser` interpreta visualmente la hoja.
* `MathOperation` representa y aplica operaciones.
* `MathProblem` representa un problema matematico.
* `WorksheetSolver` suma resultados de problemas.
* `TopToBottomWorksheetSolver` resuelve parte 1.
* `RightToLeftWorksheetSolver` resuelve parte 2.
* `TrashCompactorPuzzle` coordina.

### DRY

La resolucion de operaciones esta centralizada en `MathProblem`, `MathOperation` y `WorksheetSolver`.

Las dos partes no duplican la suma final ni la aplicacion de operaciones. Solo cambian el modo de parseo.

### OCP: Abierto/Cerrado

Si apareciera otro modo de lectura de la hoja, se podria crear otra implementacion de `TrashCompactorSolver` sin modificar las existentes.

### DIP: Inversion de Dependencias

`TrashCompactorPuzzle` depende de la interfaz `TrashCompactorSolver`, no de un unico solver concreto.

### KISS

La solucion mantiene el parser como responsable de la interpretacion visual y deja el calculo matematico en clases simples.

### YAGNI

No se crea un motor de expresiones complejo. Solo se modelan las operaciones que existen en el problema: suma y multiplicacion.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `TrashCompactorSolver`.

Las dos estrategias son:

* `TopToBottomWorksheetSolver`
* `RightToLeftWorksheetSolver`

Ambas reciben el mismo input, pero lo interpretan de forma distinta antes de resolverlo.

### Static Factory Method

`MathOperation.fromSymbol(...)` funciona como metodo de fabrica estatico.

Convierte el simbolo del input en el enum correspondiente y centraliza la validacion de operaciones.

### Polimorfismo

`MathOperation` usa polimorfismo en el enum.

Cada operacion implementa su propio `apply(...)`, por lo que el resto del codigo no necesita hacer `if` o `switch` para decidir si suma o multiplica.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a problemas o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque las operaciones no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 6 estan en:

```text
src/test/java/aoc/day06
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Separacion de problemas por columnas vacias.
* Resultados de los problemas del ejemplo oficial en modo arriba-abajo.
* Resultados de los problemas del ejemplo oficial en modo derecha-izquierda.
* Lectura de numeros desde columnas en modo derecha-izquierda.
* Rechazo de hojas sin operacion.
* Resolucion de sumas.
* Resolucion de multiplicaciones.
* Rechazo de problemas sin numeros.
* Suma total de resultados con `WorksheetSolver`.

Un caso importante es:

```text
12
34
*
```

En modo derecha-izquierda se convierte en los numeros `24` y `13`, no en `12` y `34`. Ese test demuestra que la parte 2 cambia la interpretacion visual de la hoja, no la forma de resolver las operaciones.
