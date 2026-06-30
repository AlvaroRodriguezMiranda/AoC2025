# Dia 11: Reactor

En el undecimo dia del Advent of Code 2025, el problema trata sobre una red de dispositivos de un reactor. Cada dispositivo puede enviar la senal a uno o varios dispositivos de salida.

El input describe un grafo dirigido. Cada linea tiene este formato:

```text
dispositivo: salida1 salida2 salida3
```

Por ejemplo:

```text
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
eee: out
fff: out
```

La parte izquierda antes de `:` es el dispositivo origen. La parte derecha contiene sus salidas.

La clave del dia es contar caminos dentro de ese grafo.

## Parte 1

En la primera parte hay que contar cuantos caminos distintos existen desde:

```text
you
```

hasta:

```text
out
```

Un camino es una secuencia de dispositivos siguiendo las salidas definidas por el grafo.

Si `you` puede ir a `a` y `b`, y ambos llegan a `out`, entonces hay dos caminos.

En mi codigo esta parte se resuelve con `DirectPathSolver`, que delega en `PathCounter.countPaths(...)`.

## Parte 2

En la segunda parte cambia el inicio y tambien aparece una condicion extra.

Hay que contar caminos desde:

```text
svr
```

hasta:

```text
out
```

pero solo son validos los caminos que pasan por estos dos dispositivos:

```text
dac
fft
```

No importa el orden. Un camino puede pasar primero por `dac` y luego por `fft`, o primero por `fft` y luego por `dac`.

Si llega a `out` sin haber pasado por los dos, no cuenta.

En mi codigo esta parte se resuelve con `RequiredDevicePathSolver`, que delega en `PathCounter.countPathsVisitingBoth(...)`.

## Estructura del paquete

```text
aoc.day11
|-- ReactorPuzzle.java
|-- input
|   `-- DeviceNetworkParser.java
|-- reactor
|   |-- DeviceNetwork.java
|   `-- PathCounter.java
`-- solver
    |-- DirectPathSolver.java
    |-- ReactorSolver.java
    `-- RequiredDevicePathSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte el texto del input en una red de dispositivos.
* `reactor`: contiene el grafo y el contador de caminos.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `ReactorPuzzle` coordina el dia completo.

## Clase principal `ReactorPuzzle`

`ReactorPuzzle` une el parser con los solvers de cada parte.

Tiene tres dependencias:

* `DeviceNetworkParser`: parsea el input.
* `ReactorSolver partOneSolver`: resuelve parte 1.
* `ReactorSolver partTwoSolver`: resuelve parte 2.

Tambien tiene un constructor auxiliar que recibe un `PathCounter` y crea:

```java
new DirectPathSolver(pathCounter)
new RequiredDevicePathSolver(pathCounter)
```

Sus metodos principales son:

```java
public long solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `DeviceNetworkParser`.
2. Obtienen un `DeviceNetwork`.
3. Delegan el conteo en el solver correspondiente.

El metodo `main` lee el archivo:

```text
src/main/resources/day11/input.txt
```

Despues crea:

```java
new PathCounter()
new DeviceNetworkParser()
new DirectPathSolver(pathCounter)
new RequiredDevicePathSolver(pathCounter)
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir el texto del archivo en un grafo.

### `DeviceNetworkParser`

`DeviceNetworkParser` transforma el input en un `DeviceNetwork`.

Su metodo principal es:

```java
public DeviceNetwork parse(List<String> lines)
```

El parser:

1. Ignora lineas en blanco.
2. Parsea cada linea no vacia.
3. Valida que exista al menos un dispositivo definido.
4. Crea un mapa de dispositivo a lista de salidas.

Cada linea debe contener exactamente un `:`.

Ejemplo valido:

```text
you: bbb ccc
```

Ejemplo invalido:

```text
you bbb ccc
```

Tambien valida que:

* El nombre del dispositivo no este vacio.
* Un dispositivo no se defina dos veces.
* Cada dispositivo tenga al menos una salida.

Para conservar el orden de lectura, el parser usa un `LinkedHashMap` durante el parseo.

## Clases del paquete `reactor`

El paquete `reactor` contiene el modelo de red y el algoritmo de conteo.

### `DeviceNetwork`

`DeviceNetwork` representa el grafo dirigido de dispositivos.

Internamente guarda:

```java
Map<String, List<String>> outputsByDevice
```

La clave es el nombre del dispositivo y el valor es la lista de dispositivos a los que puede enviar la senal.

En el constructor convierte el mapa en un mapa no modificable y copia tambien las listas de salidas.

Sus metodos principales son:

```java
public List<String> outputsFrom(String device)
```

Devuelve las salidas de un dispositivo. Si el dispositivo no esta definido, devuelve una lista vacia.

```java
public boolean contains(String device)
```

Indica si un dispositivo esta definido como origen en el grafo.

Esta clase no cuenta caminos. Solo representa la red y ofrece consultas.

### `PathCounter`

`PathCounter` contiene la logica para contar caminos.

Tiene dos metodos publicos:

```java
public long countPaths(DeviceNetwork network, String startDevice, String targetDevice)
```

Cuenta todos los caminos desde un dispositivo inicial hasta un destino.

```java
public long countPathsVisitingBoth(
        DeviceNetwork network,
        String startDevice,
        String targetDevice,
        String firstRequiredDevice,
        String secondRequiredDevice
)
```

Cuenta solo los caminos que llegan al destino habiendo visitado los dos dispositivos obligatorios.

Ambos metodos validan que el dispositivo inicial este definido en la red. Si no lo esta, lanzan una excepcion.

## Conteo de caminos de parte 1

La parte 1 usa el metodo interno:

```java
countPathsFrom(...)
```

El algoritmo es un DFS con memoizacion.

El flujo es:

1. Si el dispositivo actual es el destino, devuelve `1`.
2. Si ya se conoce el numero de caminos desde ese dispositivo, devuelve el valor cacheado.
3. Anade el dispositivo al set `currentPath` para detectar ciclos.
4. Recorre todas sus salidas.
5. Suma los caminos desde cada salida hasta el destino.
6. Elimina el dispositivo del camino actual.
7. Guarda el resultado en `pathsByDevice`.
8. Devuelve el total.

La memoizacion se guarda en:

```java
Map<String, Long> pathsByDevice
```

Esto evita recalcular muchas veces los caminos desde el mismo dispositivo.

Si durante la recursion se intenta visitar un dispositivo que ya esta en `currentPath`, el codigo lanza una excepcion porque ha detectado un ciclo.

## Conteo de caminos de parte 2

La parte 2 usa el metodo interno:

```java
countRequiredPathsFrom(...)
```

Tambien es DFS con memoizacion, pero el estado es mas grande.

En parte 1 bastaba con memorizar por dispositivo. En parte 2 no basta, porque llegar al mismo dispositivo no significa lo mismo si ya se ha visitado `dac`, `fft`, ambos o ninguno.

Por eso se usan dos records internos:

```java
RequiredVisitState
RequiredPathKey
```

### `RequiredVisitState`

`RequiredVisitState` guarda:

* `visitedFirstRequiredDevice`
* `visitedSecondRequiredDevice`

Tiene un metodo:

```java
hasVisitedBoth()
```

que indica si el camino ya ha pasado por los dos dispositivos obligatorios.

Tambien tiene:

```java
afterVisiting(...)
```

que devuelve un nuevo estado actualizado al visitar un dispositivo.

### `RequiredPathKey`

`RequiredPathKey` combina:

* dispositivo actual.
* estado de visitas obligatorias.

Se usa como clave de memoizacion:

```java
Map<RequiredPathKey, Long> pathsByState
```

Esto permite cachear correctamente resultados distintos para el mismo dispositivo segun lo que se haya visitado antes.

### Caso base de parte 2

Cuando el DFS llega a `out`, no devuelve siempre `1`.

Devuelve:

* `1` si ya se han visitado los dos dispositivos obligatorios.
* `0` si falta alguno.

Por eso los caminos que llegan a `out` demasiado pronto se descartan.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `ReactorSolver`

`ReactorSolver` es la interfaz comun de las dos partes.

Define este contrato:

```java
long solve(DeviceNetwork network);
```

Cada solver recibe la misma red, pero cuenta caminos con reglas distintas.

### `DirectPathSolver`

`DirectPathSolver` resuelve la parte 1.

Tiene estas constantes:

```java
private static final String START_DEVICE = "you";
private static final String TARGET_DEVICE = "out";
```

Tiene una dependencia:

```java
private final PathCounter pathCounter;
```

Su metodo `solve(...)` delega en:

```java
pathCounter.countPaths(network, START_DEVICE, TARGET_DEVICE)
```

### `RequiredDevicePathSolver`

`RequiredDevicePathSolver` resuelve la parte 2.

Tiene estas constantes:

```java
private static final String START_DEVICE = "svr";
private static final String TARGET_DEVICE = "out";
private static final String FIRST_REQUIRED_DEVICE = "dac";
private static final String SECOND_REQUIRED_DEVICE = "fft";
```

Su metodo `solve(...)` delega en:

```java
pathCounter.countPathsVisitingBoth(
        network,
        START_DEVICE,
        TARGET_DEVICE,
        FIRST_REQUIRED_DEVICE,
        SECOND_REQUIRED_DEVICE
)
```

## Diferencia entre parte 1 y parte 2 en el codigo

Las dos partes usan:

* `DeviceNetworkParser`
* `DeviceNetwork`
* `PathCounter`

La diferencia esta en el estado necesario para contar.

En parte 1:

```java
new DirectPathSolver(pathCounter)
```

Se cuentan caminos desde `you` hasta `out` y la memoizacion usa solo el dispositivo actual.

En parte 2:

```java
new RequiredDevicePathSolver(pathCounter)
```

Se cuentan caminos desde `svr` hasta `out`, pero la memoizacion necesita incluir si ya se han visitado `dac` y `fft`.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, red, contador y solvers estan separados.
* Bajo acoplamiento: los solvers trabajan con `DeviceNetwork`, no con lineas de texto.
* Modularidad: el conteo de caminos esta concentrado en `PathCounter`.
* Encapsulacion: `DeviceNetwork` copia sus listas internas y expone consultas controladas.
* Abstraccion: `ReactorSolver` permite tratar ambas partes con el mismo contrato.
* Codigo expresivo: los nombres distinguen caminos directos y caminos con dispositivos obligatorios.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `DeviceNetworkParser` parsea el grafo.
* `DeviceNetwork` representa la red.
* `PathCounter` cuenta caminos.
* `DirectPathSolver` resuelve parte 1.
* `RequiredDevicePathSolver` resuelve parte 2.
* `ReactorPuzzle` coordina.

### DRY

La logica de DFS y memoizacion esta centralizada en `PathCounter`.

Los solvers solo fijan los nombres de dispositivos que corresponden a cada parte.

### OCP: Abierto/Cerrado

Si apareciera otra regla para contar caminos, se podria crear otra implementacion de `ReactorSolver` reutilizando `DeviceNetwork` y `PathCounter`.

### DIP: Inversion de Dependencias

`ReactorPuzzle` depende de la interfaz `ReactorSolver`, no de implementaciones concretas obligatorias.

### KISS

La red se modela como un mapa de dispositivo a salidas. Es suficiente para recorrer caminos sin crear una estructura de grafo mas pesada.

### YAGNI

No se modelan nodos y aristas como clases separadas porque el problema solo necesita nombres de dispositivos y listas de salidas.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `ReactorSolver`.

Las dos estrategias son:

* `DirectPathSolver`
* `RequiredDevicePathSolver`

Ambas reciben un `DeviceNetwork`, pero cuentan caminos con reglas distintas.

### Memoization

`PathCounter` usa memoizacion para evitar recalcular caminos desde el mismo estado.

En parte 1 la clave es el dispositivo.

En parte 2 la clave es `RequiredPathKey`, que combina dispositivo y estado de visitas obligatorias.

### Value Object

Los records internos `RequiredVisitState` y `RequiredPathKey` funcionan como value objects.

Representan estado de busqueda, son inmutables y se comparan por contenido.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a la red o a los solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque recorrer caminos no necesita historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 11 estan en:

```text
src/test/java/aoc/day11
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de salidas de dispositivos.
* Ignorar lineas vacias.
* Rechazo de input vacio.
* Rechazo de lineas sin un unico `:`.
* Rechazo de dispositivos definidos dos veces.
* Conteo de un unico camino hasta `out`.
* Conteo de caminos con bifurcaciones.
* Rechazo de dispositivo inicial inexistente.
* Deteccion de ciclos.
* Conteo de caminos que visitan `dac` y `fft`.
* Aceptar `dac` y `fft` en cualquier orden.
* Ignorar caminos que llegan a `out` sin pasar por los dos obligatorios.

Un caso importante es parte 2: llegar al mismo dispositivo no es siempre el mismo estado. No es igual llegar a `ccc` habiendo pasado ya por `dac` que llegar a `ccc` sin haber pasado por `dac`. Por eso la clave de memoizacion incluye tambien `RequiredVisitState`.
