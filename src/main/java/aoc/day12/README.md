# Dia 12: Christmas Tree Farm

En el duodecimo dia del Advent of Code 2025, el problema trata sobre una granja de arboles de Navidad. El input define formas de regalos y regiones bajo los arboles donde se quieren colocar esos regalos.

Cada forma se dibuja con una cuadricula:

* `#`: celda ocupada por el regalo.
* `.`: celda vacia.

Un ejemplo de forma es:

```text
0:
###
##.
##.
```

El numero antes de `:` es el indice de la forma.

Despues de definir las formas, aparecen regiones. Cada region indica sus dimensiones y cuantos regalos de cada forma se quieren colocar.

Ejemplo:

```text
4x4: 0 0 0 0 2 0
```

Esto significa una region de ancho `4` y largo `4`, donde se quieren colocar las cantidades indicadas para cada forma.

La clave del dia es comprobar si los regalos caben dentro de cada region, teniendo en cuenta rotaciones, volteos y solapamientos.

## Parte 1

En el codigo de este dia solo hay una parte implementada: `solvePartOne`.

El objetivo es contar cuantas regiones admiten la colocacion de todos los regalos pedidos.

Para que una region sea valida:

1. El area total requerida por los regalos no puede superar el area de la region.
2. Cada regalo debe poder colocarse dentro de los limites.
3. Las celdas ocupadas por regalos distintos no pueden solaparse.
4. Se permiten rotaciones y volteos de las formas.

En mi codigo esta parte se resuelve con `FittingRegionSolver`, que delega en `PresentFitter`.

## Estructura del paquete

```text
aoc.day12
|-- ChristmasTreeFarmPuzzle.java
|-- farm
|   |-- FarmSummary.java
|   |-- GridCell.java
|   |-- PresentFitter.java
|   |-- PresentShape.java
|   |-- ShapeOrientation.java
|   `-- TreeRegion.java
|-- input
|   `-- FarmSummaryParser.java
`-- solver
    |-- ChristmasTreeFarmSolver.java
    `-- FittingRegionSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del input en formas y regiones.
* `farm`: contiene el modelo de formas, regiones y algoritmo de encaje.
* `solver`: contiene el caso de uso que cuenta regiones validas.

La clase `ChristmasTreeFarmPuzzle` coordina el dia completo.

## Clase principal `ChristmasTreeFarmPuzzle`

`ChristmasTreeFarmPuzzle` une el parser con el solver del dia.

Tiene dos dependencias:

* `FarmSummaryParser`: parsea el input.
* `ChristmasTreeFarmSolver`: resuelve el resumen parseado.

Tambien tiene un constructor auxiliar que recibe un `PresentFitter` y crea:

```java
new FittingRegionSolver(presentFitter)
```

Su metodo principal es:

```java
public long solvePartOne(List<String> inputLines)
```

El flujo es:

1. Parsea las lineas con `FarmSummaryParser`.
2. Obtiene un `FarmSummary`.
3. Delega el calculo en `ChristmasTreeFarmSolver`.

El metodo `main` lee el archivo:

```text
src/main/resources/day12/input.txt
```

Despues crea:

```java
new FarmSummaryParser()
new FittingRegionSolver(new PresentFitter())
```

Finalmente imprime el resultado de parte 1.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de interpretar el resumen de la granja.

### `FarmSummaryParser`

`FarmSummaryParser` transforma el input en un `FarmSummary`.

Su metodo principal es:

```java
public FarmSummary parse(List<String> lines)
```

El parser lee primero las formas de regalos y despues las regiones.

### Parseo de formas

Una forma empieza con una cabecera que termina en `:`.

Ejemplo:

```text
2:
.##
###
##.
```

El numero antes de `:` se interpreta como indice de forma.

Despues se leen filas hasta encontrar una linea en blanco.

Cada fila debe tener la misma anchura que las demas filas de esa forma.

Cada caracter se valida:

* `#`: crea una celda ocupada.
* `.`: celda vacia.
* cualquier otro caracter: error.

Las celdas ocupadas se guardan como `GridCell`, usando `x` para columna e `y` para fila.

Al terminar, se crea un `PresentShape`.

### Validacion de indices de forma

Despues de leer las formas, el parser las ordena por indice.

Luego valida que:

* haya al menos una forma,
* los indices empiecen en `0`,
* los indices sean consecutivos.

Esto es importante porque las regiones indican cantidades por posicion. Si hay 6 formas, una region debe tener 6 cantidades.

### Parseo de regiones

Una region tiene este formato:

```text
WIDTHxLENGTH: cantidades
```

Ejemplo:

```text
12x5: 1 0 1 0 2 2
```

El parser:

1. Divide por `:`.
2. Lee las dimensiones separadas por `x`.
3. Valida que ancho y largo sean positivos.
4. Lee las cantidades como enteros no negativos.
5. Comprueba que la cantidad de numeros coincida con el numero de formas.
6. Crea un `TreeRegion`.

## Clases del paquete `farm`

El paquete `farm` contiene el modelo principal y el algoritmo de encaje.

### `GridCell`

`GridCell` es un `record` que representa una celda ocupada.

Contiene:

* `x`: columna.
* `y`: fila.

Se usa tanto para las celdas de una forma como para calcular orientaciones.

### `FarmSummary`

`FarmSummary` agrupa los datos parseados.

Contiene:

* `shapes`: lista de `PresentShape`.
* `regions`: lista de `TreeRegion`.

Valida que exista al menos una forma.

Tambien copia las listas con `List.copyOf(...)`, para evitar modificaciones externas.

### `PresentShape`

`PresentShape` representa una forma base de regalo.

Guarda:

* `index`: indice de la forma.
* `cells`: celdas ocupadas de la forma.

Valida que la forma tenga al menos una celda `#`.

Sus metodos principales son:

```java
public int index()
public int area()
public List<ShapeOrientation> orientations()
```

`area()` devuelve el numero de celdas ocupadas.

`orientations()` genera todas las orientaciones unicas de la forma.

### Transformaciones de formas

`PresentShape` tiene un enum interno `Transform` con ocho transformaciones:

* identidad,
* rotacion 90,
* rotacion 180,
* rotacion 270,
* volteo,
* volteo + rotacion 90,
* volteo + rotacion 180,
* volteo + rotacion 270.

Cada transformacion sabe transformar una celda.

Despues de transformar, la forma se normaliza:

1. Se calcula el minimo `x`.
2. Se calcula el minimo `y`.
3. Todas las celdas se desplazan para que la forma empiece en `(0,0)`.
4. Las celdas se ordenan por fila y columna.
5. Se crea un `ShapeOrientation`.

Para evitar orientaciones duplicadas, se usa una firma textual:

```java
x,y;x,y;...
```

Si dos transformaciones producen la misma firma, solo se conserva una.

### `ShapeOrientation`

`ShapeOrientation` representa una orientacion concreta de una forma.

Guarda:

* `cells`: celdas ocupadas normalizadas.
* `width`: ancho de la orientacion.
* `height`: alto de la orientacion.

El ancho se calcula como:

```java
maxX + 1
```

El alto se calcula como:

```java
maxY + 1
```

Tambien tiene un metodo interno `signature()` usado para detectar orientaciones repetidas.

### `TreeRegion`

`TreeRegion` representa una region bajo un arbol.

Contiene:

* `width`: ancho.
* `length`: largo.
* `presentCounts`: cantidad de regalos de cada forma.

Valida que las dimensiones sean positivas.

Tambien copia la lista de cantidades con `List.copyOf(...)`.

Su metodo principal es:

```java
public int area()
```

Devuelve:

```text
width * length
```

### `PresentFitter`

`PresentFitter` contiene el algoritmo de encaje de regalos.

Su metodo principal para el puzzle es:

```java
public long countFittingRegions(FarmSummary farmSummary)
```

Este metodo cuenta cuantas regiones cumplen:

```java
canFit(farmSummary.shapes(), region)
```

### Comprobacion inicial de area

`canFit(...)` empieza calculando el area requerida:

```java
area de cada forma * cantidad pedida
```

Si el area requerida es mayor que el area de la region, devuelve `false` directamente.

Esta poda evita intentar colocar regalos cuando matematicamente no caben.

### Optimizacion para regiones grandes

El codigo tiene esta constante:

```java
private static final int DIRECT_PACKING_MINIMUM_SIDE = 20;
```

Si el lado menor de la region es al menos `20`, y el area requerida no supera el area disponible, `canFit(...)` devuelve `true`.

Es una optimizacion del codigo para evitar una busqueda combinatoria costosa en regiones grandes.

### Generacion de colocaciones

Para regiones pequenas, `PresentFitter` genera las colocaciones posibles de cada forma.

El metodo:

```java
placementsFor(PresentShape shape, TreeRegion region)
```

hace lo siguiente:

1. Recorre todas las orientaciones de la forma.
2. Para cada orientacion, prueba todos los desplazamientos `(x,y)` que caben dentro de la region.
3. Convierte cada colocacion en un `BitSet`.

Una celda `(x,y)` dentro de la region se convierte en indice lineal con:

```java
y * region.width() + x
```

### Uso de `BitSet`

Cada colocacion se representa como un `BitSet`.

Si una celda esta ocupada por la colocacion, su bit esta activo.

Esto permite comprobar solapamientos de forma sencilla:

```java
placement.intersects(occupiedCells)
```

Si dos colocaciones se solapan, sus `BitSet` tienen algun bit en comun.

### Backtracking con poda

El metodo interno `canFitRemaining(...)` intenta colocar todos los regalos restantes.

Su estado incluye:

* colocaciones posibles por forma,
* cantidades restantes de cada forma,
* celdas ocupadas,
* area de la region,
* area pendiente de colocar.

El flujo es:

1. Si todas las cantidades son cero, devuelve `true`.
2. Si el area libre restante es menor que el area pendiente, devuelve `false`.
3. Selecciona la forma pendiente con menos colocaciones compatibles.
4. Prueba sus colocaciones compatibles.
5. Marca la colocacion como ocupada.
6. Reduce la cantidad pendiente de esa forma.
7. Llama recursivamente.
8. Si una rama funciona, devuelve `true`.
9. Si ninguna funciona, devuelve `false`.

Seleccionar la forma con menos colocaciones compatibles ayuda a reducir la busqueda.

## Clases del paquete `solver`

El paquete `solver` contiene el caso de uso de alto nivel.

### `ChristmasTreeFarmSolver`

`ChristmasTreeFarmSolver` es la interfaz comun del solver del dia.

Define este contrato:

```java
long solve(FarmSummary farmSummary);
```

Recibe el resumen parseado y devuelve el numero de regiones validas.

### `FittingRegionSolver`

`FittingRegionSolver` implementa `ChristmasTreeFarmSolver`.

Tiene una dependencia:

```java
private final PresentFitter presentFitter;
```

Su metodo `solve(...)` delega en:

```java
presentFitter.countFittingRegions(farmSummary)
```

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, formas, regiones, encaje y solver estan separados.
* Bajo acoplamiento: el solver trabaja con `FarmSummary`, no con lineas de texto.
* Modularidad: orientaciones, regiones y colocaciones estan separadas.
* Encapsulacion: las listas internas se copian para evitar modificaciones externas.
* Abstraccion: `ChristmasTreeFarmSolver` define el contrato del caso de uso.
* Codigo expresivo: los nombres distinguen formas, orientaciones, regiones y encaje.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `FarmSummaryParser` parsea el input.
* `GridCell` representa una celda.
* `FarmSummary` agrupa formas y regiones.
* `PresentShape` representa una forma y genera orientaciones.
* `ShapeOrientation` representa una orientacion concreta.
* `TreeRegion` representa una region.
* `PresentFitter` decide si los regalos caben.
* `FittingRegionSolver` resuelve el caso de uso.
* `ChristmasTreeFarmPuzzle` coordina.

### DRY

La generacion de orientaciones esta centralizada en `PresentShape.orientations()`.

La comprobacion de solapamientos esta centralizada en el uso de `BitSet` dentro de `PresentFitter`.

### OCP: Abierto/Cerrado

Si apareciera otra forma de evaluar regiones, se podria crear otra implementacion de `ChristmasTreeFarmSolver` sin modificar `ChristmasTreeFarmPuzzle`.

### DIP: Inversion de Dependencias

`ChristmasTreeFarmPuzzle` depende de la interfaz `ChristmasTreeFarmSolver`, no de una implementacion concreta obligatoria.

### KISS

Las formas se modelan como listas de celdas ocupadas. Esto hace que rotar, voltear y convertir a `BitSet` sea directo.

### YAGNI

No se inventa una segunda parte ni una jerarquia adicional de estrategias. El codigo tiene un unico caso de calculo y lo expresa con un solver concreto.

## Patrones de diseno aplicados

### Strategy

Hay una interfaz `ChristmasTreeFarmSolver`, aunque actualmente solo existe una implementacion:

* `FittingRegionSolver`

No se usa para separar parte 1 y parte 2, sino para separar el caso de uso de la clase principal.

### Value Object

`GridCell`, `FarmSummary` y `TreeRegion` se comportan como objetos de valor dentro del dominio.

`GridCell`, `FarmSummary` y `TreeRegion` estan definidos como `record`, lo que facilita comparacion por contenido e inmutabilidad estructural.

### Polimorfismo en enum

El enum interno `Transform` de `PresentShape` usa polimorfismo.

Cada transformacion implementa como convertir una celda.

### Backtracking

`PresentFitter` usa backtracking para probar colocaciones cuando la region es pequena.

No es un patron GoF, pero es el algoritmo clave para encontrar una distribucion sin solapamientos.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a formas o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque colocar regalos no necesita historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 12 estan en:

```text
src/test/java/aoc/day12
```

Cubren:

* El ejemplo oficial de parte 1.
* Parseo de formas y regiones.
* Calculo de area de una forma.
* Rechazo de celdas desconocidas en formas.
* Rechazo de regiones con cantidad de regalos distinta al numero de formas.
* Generacion de orientaciones unicas con rotaciones y volteos.
* Encaje de dos regalos rotados en una region `4x4`.
* Rechazo cuando el area requerida es demasiado grande.
* Rechazo cuando una forma no puede colocarse dentro de la region.

Un caso importante es el encaje de dos regalos rotados en una region `4x4`. Ese test demuestra que no basta con mirar la forma original: hay que generar orientaciones y comprobar colocaciones sin solapamiento.
