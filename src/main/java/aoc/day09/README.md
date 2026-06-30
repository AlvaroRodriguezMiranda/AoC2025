# Dia 9: Movie Theater

En el noveno dia del Advent of Code 2025, el problema trata sobre un cine representado mediante baldosas rojas. El input contiene coordenadas `x,y`, una por linea.

Un ejemplo de entrada es:

```text
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
```

Cada coordenada representa una baldosa roja. Con esas baldosas se buscan rectangulos y se calcula su area.

La formula de area es inclusiva. Si dos baldosas estan en `(2,5)` y `(9,7)`, el ancho es:

```text
abs(2 - 9) + 1 = 8
```

Y el alto es:

```text
abs(5 - 7) + 1 = 3
```

Por tanto, el area es:

```text
8 * 3 = 24
```

## Parte 1

En la primera parte hay que buscar el rectangulo de mayor area que se puede formar usando cualquier par de baldosas rojas.

No hay restricciones adicionales. Se comparan todos los pares posibles y se queda el area maxima.

En mi codigo esta parte se resuelve con `UnrestrictedRectangleAreaSolver`.

## Parte 2

En la segunda parte ya no vale cualquier rectangulo. El rectangulo debe estar dentro de la zona roja o verde delimitada por el camino formado por las baldosas rojas.

La lista de baldosas rojas representa un contorno cerrado. El codigo construye la zona permitida a partir de ese contorno y despues comprueba si cada rectangulo candidato cabe dentro.

El problema puede tener coordenadas grandes, asi que no conviene crear una matriz con todas las posiciones reales. Para evitarlo, el codigo usa compresion de coordenadas:

1. Toma las coordenadas relevantes del contorno.
2. Crea rangos comprimidos en los ejes `x` e `y`.
3. Marca el contorno.
4. Hace flood fill desde fuera para saber que bloques son exteriores.
5. Todo lo que no es exterior dentro de los limites se considera zona permitida.

En mi codigo esta parte se resuelve con `RedGreenRectangleAreaSolver`.

## Estructura del paquete

```text
aoc.day09
|-- MovieTheaterPuzzle.java
|-- input
|   `-- RedTileParser.java
|-- solver
|   |-- RedGreenRectangleAreaSolver.java
|   |-- TheaterAreaSolver.java
|   `-- UnrestrictedRectangleAreaSolver.java
`-- theater
    |-- LargestRectangleFinder.java
    |-- RedGreenTileArea.java
    `-- RedTile.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del input en baldosas rojas.
* `theater`: contiene el modelo de baldosa, la zona permitida y la fachada de busqueda.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `MovieTheaterPuzzle` coordina el dia completo.

## Clase principal `MovieTheaterPuzzle`

`MovieTheaterPuzzle` une el parser con los solvers de cada parte.

Tiene tres dependencias:

* `RedTileParser`: parsea las coordenadas.
* `TheaterAreaSolver partOneSolver`: resuelve parte 1.
* `TheaterAreaSolver partTwoSolver`: resuelve parte 2.

Sus metodos principales son:

```java
public long solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `RedTileParser`.
2. Obtienen una lista de `RedTile`.
3. Delegan el calculo en el solver correspondiente.

Tambien tiene un constructor auxiliar que recibe un `LargestRectangleFinder`, aunque internamente crea los solvers concretos:

```java
new UnrestrictedRectangleAreaSolver()
new RedGreenRectangleAreaSolver()
```

El metodo `main` lee el archivo:

```text
src/main/resources/day09/input.txt
```

Despues crea:

```java
new RedTileParser()
new UnrestrictedRectangleAreaSolver()
new RedGreenRectangleAreaSolver()
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir el texto del archivo en baldosas rojas.

### `RedTileParser`

`RedTileParser` transforma el input en una lista de `RedTile`.

Su metodo principal es:

```java
public List<RedTile> parse(List<String> lines)
```

El parser:

1. Ignora lineas en blanco.
2. Convierte cada linea no vacia en una baldosa.
3. Valida que al final exista al menos una baldosa.

Cada linea debe tener exactamente dos coordenadas separadas por coma:

```text
x,y
```

Si una linea no tiene dos partes, lanza una excepcion.

Si alguna coordenada no es numerica, tambien lanza una excepcion.

Ejemplos invalidos:

```text
1,2,3
x,3
```

## Clases del paquete `theater`

El paquete `theater` contiene el modelo principal del cine y la fachada de busqueda.

### `RedTile`

`RedTile` es un `record` que representa una baldosa roja.

Contiene:

* `x`: coordenada horizontal.
* `y`: coordenada vertical.

Su metodo principal es:

```java
public long rectangleAreaWith(RedTile other)
```

Este metodo calcula el area inclusiva del rectangulo formado entre dos baldosas.

La formula es:

```text
(abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
```

La suma de `1` es importante porque las baldosas de los extremos tambien cuentan.

### `LargestRectangleFinder`

`LargestRectangleFinder` funciona como fachada para buscar areas maximas.

Tiene dos solvers:

* `TheaterAreaSolver partOneSolver`
* `TheaterAreaSolver partTwoSolver`

Su constructor por defecto crea:

```java
new UnrestrictedRectangleAreaSolver()
new RedGreenRectangleAreaSolver()
```

Expone dos metodos:

```java
public long findLargestArea(List<RedTile> redTiles)
public long findLargestAreaInsideRedGreenArea(List<RedTile> redTiles)
```

El primero usa la regla sin restricciones de parte 1.

El segundo usa la regla restringida de parte 2.

### `RedGreenTileArea`

`RedGreenTileArea` representa la zona permitida de la parte 2.

No guarda una matriz gigante de coordenadas reales. En su lugar, construye una representacion comprimida por bandas verticales.

Se crea con:

```java
public static RedGreenTileArea from(List<RedTile> redTiles)
```

El flujo de construccion es:

1. Calcula los limites del contorno con `Bounds`.
2. Comprime el eje `x` con `AxisCompression`.
3. Comprime el eje `y` con `AxisCompression`.
4. Marca las lineas del contorno con `buildBoundary(...)`.
5. Hace flood fill desde el exterior con `findOutsideTiles(...)`.
6. Construye bandas permitidas con `buildAllowedBands(...)`.

El metodo principal para los solvers es:

```java
public boolean containsRectangleBetween(RedTile first, RedTile second)
```

Este metodo:

1. Calcula el minimo y maximo de `x`.
2. Calcula el minimo y maximo de `y`.
3. Revisa todas las bandas verticales que se solapan con ese rango de `y`.
4. Comprueba que en cada banda exista un rango horizontal que contenga todo el rectangulo.

Si todas las bandas necesarias contienen el rango horizontal, el rectangulo cabe dentro de la zona permitida.

### Compresion de coordenadas

La clase interna `AxisCompression` crea rangos comprimidos.

Para cada coordenada relevante, anade:

* `coordinate - 1`
* `coordinate`
* `coordinate + 1`

Tambien anade un borde exterior:

* minimo `- 1`
* maximo `+ 1`

Esto permite distinguir bien entre contorno, interior y exterior sin recorrer todas las coordenadas reales.

### Flood fill exterior

`findOutsideTiles(...)` empieza desde el bloque `(0,0)`, que queda fuera del contorno por el borde extra.

Usa una cola:

```java
Queue<BlockPosition>
```

Y recorre vecinos en cuatro direcciones:

* derecha
* izquierda
* abajo
* arriba

Si un bloque no esta marcado como contorno y no se ha visitado, se marca como exterior.

Al terminar, los bloques no exteriores dentro de los limites reales forman la zona roja o verde permitida.

### Bandas permitidas

`buildAllowedBands(...)` transforma la matriz comprimida en una lista de `VerticalBand`.

Cada banda vertical contiene una lista de rangos horizontales permitidos.

Esto hace que comprobar un rectangulo sea mas directo: no hay que recorrer todas sus celdas, solo comprobar que cada banda vertical relevante contiene el rango horizontal completo.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `TheaterAreaSolver`

`TheaterAreaSolver` es la interfaz comun de las dos partes.

Define este contrato:

```java
long solve(List<RedTile> redTiles);
```

Cada solver recibe la misma lista de baldosas rojas y devuelve el area maxima encontrada.

### `UnrestrictedRectangleAreaSolver`

`UnrestrictedRectangleAreaSolver` resuelve la parte 1.

El flujo es:

1. Valida que haya al menos dos baldosas.
2. Recorre todos los pares de baldosas.
3. Calcula el area con `rectangleAreaWith(...)`.
4. Guarda el maximo.
5. Devuelve el area mayor.

Los pares se recorren con dos bucles, empezando el segundo en `firstIndex + 1`, para evitar comparar dos veces el mismo par.

### `RedGreenRectangleAreaSolver`

`RedGreenRectangleAreaSolver` resuelve la parte 2.

El flujo es:

1. Valida que haya al menos dos baldosas.
2. Construye la zona permitida con `RedGreenTileArea.from(redTiles)`.
3. Recorre todos los pares de baldosas.
4. Calcula el area del rectangulo.
5. Si el area supera la mejor encontrada, comprueba si el rectangulo cabe en la zona rojo-verde.
6. Si cabe, actualiza el maximo.
7. Devuelve el area mayor valida.

La comprobacion de zona solo se hace si el area puede mejorar el resultado actual. Eso evita validar rectangulos que de todas formas no cambiarian la respuesta.

## Diferencia entre parte 1 y parte 2 en el codigo

Las dos partes comparan pares de `RedTile` y calculan areas inclusivas.

La diferencia esta en la regla de validez.

En parte 1:

```java
new UnrestrictedRectangleAreaSolver()
```

Se acepta cualquier rectangulo formado por dos baldosas.

En parte 2:

```java
new RedGreenRectangleAreaSolver()
```

El rectangulo solo se acepta si `RedGreenTileArea` confirma que esta dentro de la zona permitida.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, modelo, zona y solvers estan separados.
* Bajo acoplamiento: los solvers trabajan con `RedTile`, no con strings del input.
* Modularidad: la construccion de zona rojo-verde esta aislada en `RedGreenTileArea`.
* Encapsulacion: los detalles de compresion y flood fill no salen de `RedGreenTileArea`.
* Abstraccion: `TheaterAreaSolver` permite tratar parte 1 y parte 2 con el mismo contrato.
* Codigo expresivo: los nombres distinguen entre busqueda sin restricciones y busqueda dentro de zona rojo-verde.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `RedTileParser` parsea coordenadas.
* `RedTile` representa una baldosa y calcula areas.
* `RedGreenTileArea` construye y consulta la zona permitida.
* `LargestRectangleFinder` actua como fachada.
* `UnrestrictedRectangleAreaSolver` resuelve parte 1.
* `RedGreenRectangleAreaSolver` resuelve parte 2.
* `MovieTheaterPuzzle` coordina.

### DRY

La formula de area esta centralizada en `RedTile.rectangleAreaWith(...)`.

La validacion de rectangulos dentro de la zona esta centralizada en `RedGreenTileArea.containsRectangleBetween(...)`.

### OCP: Abierto/Cerrado

Si apareciera otra regla para aceptar rectangulos, se podria crear otra implementacion de `TheaterAreaSolver` sin modificar las existentes.

### DIP: Inversion de Dependencias

`MovieTheaterPuzzle` y `LargestRectangleFinder` dependen de la interfaz `TheaterAreaSolver`, no de una implementacion concreta obligatoria.

### KISS

La parte 1 usa comparacion directa entre pares. La parte 2 usa compresion de coordenadas para evitar una matriz enorme sin complicar el solver principal.

### YAGNI

No se crea una representacion completa del cine si solo hace falta responder si un rectangulo cabe. Las bandas permitidas son suficientes para esa consulta.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `TheaterAreaSolver`.

Las dos estrategias son:

* `UnrestrictedRectangleAreaSolver`
* `RedGreenRectangleAreaSolver`

Ambas reciben una lista de `RedTile`, pero aplican reglas distintas para aceptar rectangulos.

### Facade

`LargestRectangleFinder` funciona como fachada.

Expone metodos de alto nivel:

* `findLargestArea(...)`
* `findLargestAreaInsideRedGreenArea(...)`

Por debajo delega en los solvers.

### Value Object

`RedTile` funciona como value object al estar modelado como `record`.

Representa una coordenada del dominio y se compara por contenido.

### Flood Fill

`RedGreenTileArea` usa flood fill para distinguir exterior e interior.

No es un patron GoF, pero si es el algoritmo clave para saber que parte de la zona queda fuera del contorno.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a baldosas o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque las comparaciones de rectangulos no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 9 estan en:

```text
src/test/java/aoc/day09
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de coordenadas `x,y`.
* Ignorar lineas vacias.
* Rechazar input vacio.
* Rechazar posiciones sin dos coordenadas.
* Rechazar coordenadas no numericas.
* Calculo de area inclusiva.
* Busqueda del mayor rectangulo entre varias baldosas.
* Rectangulos horizontales finos.
* Rechazo de menos de dos baldosas para parte 1.
* Busqueda del mayor rectangulo dentro de la zona rojo-verde.
* Rechazo de menos de dos baldosas para parte 2.

Un caso importante es el area entre `(2,5)` y `(9,7)`. El resultado es `24`, no `14`, porque el rectangulo cuenta las baldosas de los bordes usando `+ 1` en ancho y alto.
