# Dia 8: Playground

En el octavo dia del Advent of Code 2025, el problema trata sobre cajas de conexiones colocadas en un espacio tridimensional. Cada linea del input representa una caja con sus coordenadas `x,y,z`.

Un ejemplo de entrada es:

```text
162,817,812
57,618,57
906,360,560
592,479,940
```

Las cajas se van conectando por parejas. Las conexiones se procesan de menor a mayor distancia, es decir, primero se conectan las cajas mas cercanas.

La idea principal del codigo es:

1. Parsear las cajas.
2. Generar todas las conexiones posibles entre pares.
3. Ordenarlas por distancia.
4. Usar Union-Find para mantener los circuitos conectados.

## Parte 1

En la primera parte se aplican las primeras `1000` conexiones mas cortas.

Despues de aplicar esas conexiones, hay que mirar los tamanos de los circuitos resultantes y multiplicar los tamanos de los tres circuitos mas grandes.

Por ejemplo, si los tres circuitos mas grandes tienen tamanos:

```text
5, 4, 2
```

La respuesta seria:

```text
5 * 4 * 2 = 40
```

En mi codigo esta parte se resuelve con `ThreeLargestCircuitSizeSolver`.

En los tests del ejemplo oficial se usa una variante con `10` conexiones mediante `CircuitSizeCalculator`, porque el ejemplo pequeno del enunciado no necesita llegar a `1000`.

## Parte 2

En la segunda parte se siguen procesando conexiones hasta que todas las cajas quedan dentro de un unico circuito.

Cuando una conexion consigue que la red pase a ser un unico circuito, se toma esa ultima conexion y se multiplican las coordenadas `x` de las dos cajas conectadas.

Por ejemplo, si la ultima conexion necesaria une cajas con:

```text
x = 2
x = 15
```

La respuesta seria:

```text
2 * 15 = 30
```

En mi codigo esta parte se resuelve con `LastConnectionXCoordinateSolver`.

## Estructura del paquete

```text
aoc.day08
|-- PlaygroundPuzzle.java
|-- circuit
|   |-- CircuitNetwork.java
|   |-- CircuitSizeCalculator.java
|   |-- JunctionBox.java
|   |-- JunctionConnection.java
|   `-- JunctionConnectionPlanner.java
|-- input
|   `-- JunctionBoxParser.java
`-- solver
    |-- CircuitSolver.java
    |-- LastConnectionXCoordinateSolver.java
    `-- ThreeLargestCircuitSizeSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del input en cajas 3D.
* `circuit`: contiene el modelo de cajas, conexiones, red y fachada de calculo.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `PlaygroundPuzzle` coordina el dia completo.

## Clase principal `PlaygroundPuzzle`

`PlaygroundPuzzle` une el parser con los solvers de cada parte.

Tiene tres dependencias:

* `JunctionBoxParser`: parsea las coordenadas.
* `CircuitSolver partOneSolver`: resuelve parte 1.
* `CircuitSolver partTwoSolver`: resuelve parte 2.

Sus metodos principales son:

```java
public long solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `JunctionBoxParser`.
2. Obtienen una lista de `JunctionBox`.
3. Delegan el calculo en el solver correspondiente.

El metodo `main` crea un `JunctionConnectionPlanner` compartido y despues crea:

```java
new JunctionBoxParser()
new ThreeLargestCircuitSizeSolver(connectionPlanner)
new LastConnectionXCoordinateSolver(connectionPlanner)
```

El input se lee desde:

```text
src/main/resources/day08/input.txt
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir las lineas del archivo en cajas de conexiones.

### `JunctionBoxParser`

`JunctionBoxParser` transforma el input en una lista de `JunctionBox`.

Su metodo principal es:

```java
public List<JunctionBox> parse(List<String> lines)
```

El parser:

1. Ignora lineas en blanco.
2. Convierte cada linea no vacia en una caja.
3. Valida que al final exista al menos una caja.

Cada linea debe tener exactamente tres coordenadas separadas por comas:

```text
x,y,z
```

Si una linea no tiene tres partes, lanza una excepcion.

Si alguna coordenada no es numerica, tambien lanza una excepcion.

Ejemplos invalidos:

```text
1,2
1,x,3
```

## Clases del paquete `circuit`

El paquete `circuit` contiene las clases del dominio de conexiones y circuitos.

### `JunctionBox`

`JunctionBox` es un `record` que representa una caja de conexiones en 3D.

Contiene:

* `x`
* `y`
* `z`

Su metodo principal es:

```java
public long squaredDistanceTo(JunctionBox other)
```

Calcula la distancia al cuadrado entre dos cajas:

```text
(x1 - x2)^2 + (y1 - y2)^2 + (z1 - z2)^2
```

Se usa distancia al cuadrado porque para ordenar distancias no hace falta calcular la raiz cuadrada. Esto evita operaciones innecesarias y mantiene todos los valores como enteros.

### `JunctionConnection`

`JunctionConnection` es un `record` que representa una conexion candidata entre dos cajas.

Contiene:

* `firstIndex`: indice de la primera caja.
* `secondIndex`: indice de la segunda caja.
* `squaredDistance`: distancia al cuadrado entre ambas.

Guarda indices en vez de referencias directas para que `CircuitNetwork` pueda unir componentes usando posiciones de la lista.

### `JunctionConnectionPlanner`

`JunctionConnectionPlanner` genera y ordena conexiones posibles.

Su metodo principal es:

```java
public List<JunctionConnection> connectionsByDistance(List<JunctionBox> junctionBoxes)
```

El flujo es:

1. Recorre todos los pares de cajas.
2. Para cada par calcula la distancia al cuadrado.
3. Crea un `JunctionConnection`.
4. Ordena todas las conexiones por `squaredDistance`.
5. Devuelve la lista ordenada.

Los pares se generan con dos bucles:

```java
for firstIndex
    for secondIndex = firstIndex + 1
```

Asi se evita crear dos veces la misma conexion en sentido contrario.

### `CircuitNetwork`

`CircuitNetwork` representa los circuitos conectados usando Union-Find, tambien conocido como Disjoint Set.

Internamente usa:

* `parents`: array que indica el padre de cada componente.
* `sizes`: array con el tamano de cada componente raiz.
* `circuitCount`: cantidad actual de circuitos.

Al inicio, cada caja es su propio circuito:

```text
parents[i] = i
sizes[i] = 1
```

Su metodo principal es:

```java
public boolean connect(int firstCircuit, int secondCircuit)
```

Este metodo:

1. Busca la raiz de cada circuito con `find`.
2. Si ya tienen la misma raiz, devuelve `false`.
3. Si son distintos, une el circuito pequeno al grande.
4. Actualiza el tamano.
5. Reduce `circuitCount`.
6. Devuelve `true`.

El metodo `find` usa compresion de caminos:

```java
parents[circuit] = find(parents[circuit])
```

Esto hace que futuras busquedas sean mas rapidas.

Otros metodos importantes:

```java
public boolean isSingleCircuit()
```

Indica si todas las cajas ya forman un unico circuito.

```java
public List<Integer> circuitSizesDescending()
```

Devuelve los tamanos de los circuitos actuales ordenados de mayor a menor.

### `CircuitSizeCalculator`

`CircuitSizeCalculator` es una fachada usada para exponer calculos de forma sencilla.

Tiene un `JunctionConnectionPlanner` y ofrece:

```java
public long multiplyThreeLargestCircuitSizes(List<JunctionBox> junctionBoxes, int connectionCount)
public long multiplyLastConnectionXCoordinates(List<JunctionBox> junctionBoxes)
```

El primer metodo permite indicar cuantas conexiones aplicar. Esto es util para tests, porque el ejemplo oficial pequeno usa `10` conexiones en vez de las `1000` de la entrada real.

Internamente delega en:

* `ThreeLargestCircuitSizeSolver`
* `LastConnectionXCoordinateSolver`

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `CircuitSolver`

`CircuitSolver` es la interfaz comun de las dos partes.

Define este contrato:

```java
long solve(List<JunctionBox> junctionBoxes);
```

Cada solver recibe la misma lista de cajas, pero tiene un criterio de parada y un resultado distinto.

### `ThreeLargestCircuitSizeSolver`

`ThreeLargestCircuitSizeSolver` resuelve la parte 1.

Tiene:

```java
private static final int DEFAULT_CONNECTION_COUNT = 1000;
```

Tambien permite recibir otro `connectionCount` por constructor, lo que se usa en tests.

El flujo es:

1. Valida que haya al menos tres cajas.
2. Crea un `CircuitNetwork` con una componente por caja.
3. Pide las conexiones ordenadas a `JunctionConnectionPlanner`.
4. Toma solo las primeras `connectionCount`.
5. Aplica cada conexion con `network.connect(...)`.
6. Obtiene los tamanos de circuitos en orden descendente.
7. Multiplica los tres primeros.

Si una conexion une dos cajas que ya estan en el mismo circuito, `CircuitNetwork.connect(...)` devuelve `false` y no cambia los tamanos.

### `LastConnectionXCoordinateSolver`

`LastConnectionXCoordinateSolver` resuelve la parte 2.

El flujo es:

1. Valida que haya al menos dos cajas.
2. Crea un `CircuitNetwork`.
3. Genera todas las conexiones ordenadas por distancia.
4. Recorre las conexiones en orden.
5. Aplica cada conexion.
6. Si la conexion realmente une dos circuitos y la red queda en un unico circuito, se detiene.
7. Obtiene las dos cajas de esa conexion.
8. Devuelve el producto de sus coordenadas `x`.

Si no se pudiera formar un unico circuito, lanza una excepcion.

## Diferencia entre parte 1 y parte 2 en el codigo

Las dos partes usan la misma base:

* `JunctionConnectionPlanner` para ordenar conexiones.
* `CircuitNetwork` para unir componentes.

La diferencia esta en cuando se detienen y que devuelven.

En parte 1:

```java
new ThreeLargestCircuitSizeSolver(connectionPlanner)
```

Se aplican las primeras `1000` conexiones y se multiplican los tres tamanos mas grandes.

En parte 2:

```java
new LastConnectionXCoordinateSolver(connectionPlanner)
```

Se sigue conectando hasta que queda un unico circuito, y se devuelve el producto de las `x` de la ultima conexion necesaria.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, cajas, conexiones, red y solvers estan separados.
* Bajo acoplamiento: los solvers trabajan con `JunctionBox`, no con strings del input.
* Modularidad: la generacion de conexiones y la union de circuitos son piezas independientes.
* Encapsulacion: `CircuitNetwork` oculta los arrays de Union-Find.
* Abstraccion: `CircuitSolver` permite tratar parte 1 y parte 2 con el mismo contrato.
* Codigo expresivo: los nombres describen si se calculan tamanos de circuitos o la ultima conexion.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `JunctionBoxParser` parsea coordenadas.
* `JunctionBox` representa una caja y calcula distancias.
* `JunctionConnection` representa una conexion candidata.
* `JunctionConnectionPlanner` genera y ordena conexiones.
* `CircuitNetwork` gestiona componentes conectadas.
* `CircuitSizeCalculator` actua como fachada.
* `ThreeLargestCircuitSizeSolver` resuelve parte 1.
* `LastConnectionXCoordinateSolver` resuelve parte 2.
* `PlaygroundPuzzle` coordina.

### DRY

La generacion y ordenacion de conexiones esta centralizada en `JunctionConnectionPlanner`.

La logica de unir circuitos esta centralizada en `CircuitNetwork`.

### OCP: Abierto/Cerrado

Si apareciera otro calculo basado en conexiones, se podria crear otra implementacion de `CircuitSolver` reutilizando `JunctionConnectionPlanner` y `CircuitNetwork`.

### DIP: Inversion de Dependencias

`PlaygroundPuzzle` depende de la interfaz `CircuitSolver`, no de una implementacion concreta obligatoria.

### KISS

Se usa Union-Find porque el problema consiste en unir componentes. Es una estructura directa para esta necesidad y evita recalcular grupos manualmente.

### YAGNI

No se guardan listas complejas de conexiones activas ni grafos completos despues de unir. Para responder al problema basta con saber a que componente pertenece cada caja y el tamano de cada componente.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `CircuitSolver`.

Las dos estrategias son:

* `ThreeLargestCircuitSizeSolver`
* `LastConnectionXCoordinateSolver`

Ambas reciben una lista de `JunctionBox`, pero tienen objetivos distintos.

### Facade

`CircuitSizeCalculator` funciona como fachada.

Expone metodos de alto nivel:

* `multiplyThreeLargestCircuitSizes(...)`
* `multiplyLastConnectionXCoordinates(...)`

Por debajo delega en los solvers.

### Value Object

`JunctionBox` y `JunctionConnection` funcionan como value objects al estar modelados como `record`.

Representan valores del dominio y se comparan por contenido.

### Union-Find

`CircuitNetwork` implementa Union-Find.

No es un patron GoF, pero si es la estructura de datos clave del dia. Permite unir circuitos, consultar si ya hay un unico circuito y obtener tamanos de componentes de forma eficiente.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a cajas, redes o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque las conexiones no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 8 estan en:

```text
src/test/java/aoc/day08
```

Cubren:

* El ejemplo oficial de parte 1 usando 10 conexiones.
* El ejemplo oficial de parte 2.
* Parseo de coordenadas `x,y,z`.
* Ignorar lineas vacias.
* Rechazar input vacio.
* Rechazar posiciones sin tres coordenadas.
* Rechazar coordenadas no numericas.
* Calculo de distancia al cuadrado.
* Conexion de parejas cercanas.
* Multiplicacion de los tres circuitos mas grandes.
* Que conexiones repetidas dentro del mismo circuito no cambien tamanos.
* Rechazo de menos de tres cajas para parte 1.
* Producto de coordenadas `x` de la conexion que crea un unico circuito.
* Rechazo de menos de dos cajas para parte 2.

Un caso importante es el de conexiones repetidas dentro del mismo circuito. Aunque una conexion aparezca entre cajas que ya estan conectadas indirectamente, `CircuitNetwork.connect(...)` devuelve `false` y no altera los tamanos ni el numero de circuitos.
