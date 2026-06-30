# Dia 10: Factory

En el decimo dia del Advent of Code 2025, el problema trata sobre inicializar maquinas de una fabrica. Cada linea del input describe una maquina con tres tipos de informacion:

* Un diagrama de luces objetivo.
* Una lista de botones.
* Una lista de requisitos de voltaje.

Un ejemplo de linea es:

```text
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
```

El diagrama `[...]` indica que luces deben quedar encendidas (`#`) o apagadas (`.`).

Cada boton aparece entre parentesis. Por ejemplo:

```text
(1,3)
```

Ese boton afecta a las posiciones `1` y `3`.

Los requisitos de voltaje aparecen entre llaves:

```text
{3,5,4,7}
```

La clave del dia es que las dos partes usan las mismas maquinas y botones, pero interpretan el efecto de los botones de forma distinta.

## Parte 1

En la primera parte, los botones alternan luces.

Cada luz tiene dos estados:

* apagada
* encendida

Cuando se pulsa un boton, cambia el estado de las luces que tiene conectadas. Si una luz estaba apagada, se enciende. Si estaba encendida, se apaga.

El objetivo es encontrar el minimo numero de pulsaciones necesario para llegar al patron objetivo del diagrama.

En mi codigo esta parte se resuelve con `LightInitializationSolver`, que delega en `MachineInitializer.minimumPressesForAll(...)`.

## Parte 2

En la segunda parte, los botones ya no alternan luces. Ahora los botones suman voltaje a los contadores que tienen conectados.

Cada posicion tiene un requisito de voltaje. Hay que encontrar cuantas veces pulsar cada boton para alcanzar exactamente esos requisitos usando el menor numero total de pulsaciones.

Por ejemplo, si un boton afecta a los contadores `0` y `2`, cada pulsacion suma `1` a esos dos contadores.

El problema se puede ver como un sistema de ecuaciones:

```text
botonA + botonC = requisito0
botonB          = requisito1
botonA + botonB = requisito2
```

Las soluciones validas deben ser enteras y no negativas, porque no se puede pulsar un boton una cantidad negativa ni media vez.

En mi codigo esta parte se resuelve con `JoltageInitializationSolver`, que delega en `MachineInitializer.minimumJoltagePressesForAll(...)`.

## Estructura del paquete

```text
aoc.day10
|-- FactoryPuzzle.java
|-- factory
|   |-- ButtonWiring.java
|   |-- FactoryMachine.java
|   `-- MachineInitializer.java
|-- input
|   `-- FactoryManualParser.java
`-- solver
    |-- FactorySolver.java
    |-- JoltageInitializationSolver.java
    `-- LightInitializationSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte cada linea del manual en una maquina.
* `factory`: contiene el modelo de maquina, botones y algoritmos de inicializacion.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `FactoryPuzzle` coordina el dia completo.

## Clase principal `FactoryPuzzle`

`FactoryPuzzle` une el parser con los solvers de cada parte.

Tiene tres dependencias:

* `FactoryManualParser`: parsea las lineas del manual.
* `FactorySolver<Integer>`: solver de parte 1.
* `FactorySolver<Long>`: solver de parte 2.

Tambien tiene un constructor auxiliar que recibe un `MachineInitializer` y crea:

```java
new LightInitializationSolver(initializer)
new JoltageInitializationSolver(initializer)
```

Sus metodos principales son:

```java
public int solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `FactoryManualParser`.
2. Obtienen una lista de `FactoryMachine`.
3. Delegan el calculo en el solver correspondiente.

El metodo `main` lee el archivo:

```text
src/main/resources/day10/input.txt
```

Despues crea:

```java
new MachineInitializer()
new FactoryManualParser()
new LightInitializationSolver(initializer)
new JoltageInitializationSolver(initializer)
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de interpretar las lineas del manual.

### `FactoryManualParser`

`FactoryManualParser` convierte el input en una lista de `FactoryMachine`.

Su metodo principal es:

```java
public List<FactoryMachine> parse(List<String> lines)
```

Este metodo:

1. Ignora lineas en blanco.
2. Parsea cada linea no vacia.
3. Valida que exista al menos una maquina.

Para parsear una linea usa expresiones regulares:

```java
private static final Pattern DIAGRAM_PATTERN = Pattern.compile("\\[([.#]+)]");
private static final Pattern BUTTON_PATTERN = Pattern.compile("\\(([^)]*)\\)");
private static final Pattern JOLTAG_PATTERN = Pattern.compile("\\{([^}]*)}");
```

### Diagrama de luces

El diagrama se encuentra entre corchetes.

Ejemplo:

```text
[.##.]
```

Cada posicion se convierte en un bit dentro de `targetMask`.

* `#`: el bit se activa.
* `.`: el bit queda apagado.

En el ejemplo `[.##.]`, las posiciones `1` y `2` estan encendidas, asi que la mascara es:

```text
0b0110
```

### Botones

Cada boton se encuentra entre parentesis.

Ejemplo:

```text
(0,2)
```

El parser convierte esos indices en una mascara de bits. Si el boton afecta a las posiciones `0` y `2`, su mascara es:

```text
0b0101
```

Los indices deben estar dentro del tamano del diagrama. Si aparece un indice fuera, se lanza una excepcion.

### Requisitos de voltaje

Los requisitos se encuentran entre llaves.

Ejemplo:

```text
{3,5,4,7}
```

Se parsean como enteros no negativos.

La cantidad de requisitos debe coincidir con el numero de luces del diagrama. Si el diagrama tiene 4 posiciones, deben existir 4 requisitos.

## Clases del paquete `factory`

El paquete `factory` contiene el modelo de la maquina y los algoritmos principales.

### `ButtonWiring`

`ButtonWiring` es un `record` que representa que posiciones afecta un boton.

Contiene:

```java
long toggleMask
```

En parte 1 esa mascara indica que luces se alternan.

En parte 2 esa misma mascara indica que contadores de voltaje reciben `+1` cuando se pulsa el boton.

### `FactoryMachine`

`FactoryMachine` representa una maquina de la fabrica.

Guarda:

* `lightCount`: numero de luces o contadores.
* `targetMask`: patron objetivo de luces para parte 1.
* `buttons`: lista de botones.
* `joltageRequirements`: requisitos de voltaje para parte 2.

Valida varias reglas:

* La maquina debe tener entre `1` y `62` luces.
* Debe tener al menos un boton.
* Si hay requisitos de voltaje, su cantidad debe coincidir con `lightCount`.

El limite de `62` luces evita problemas con desplazamientos de bits en un `long`.

Tambien copia las listas con `List.copyOf(...)` para evitar modificaciones externas.

### `MachineInitializer`

`MachineInitializer` contiene los algoritmos principales de inicializacion.

Tiene metodos para resolver todas las maquinas:

```java
public int minimumPressesForAll(List<FactoryMachine> machines)
public long minimumJoltagePressesForAll(List<FactoryMachine> machines)
```

Ambos recorren la lista de maquinas y suman el resultado individual.

Tambien tiene metodos para una sola maquina:

```java
public int minimumPressesFor(FactoryMachine machine)
public long minimumJoltagePressesFor(FactoryMachine machine)
```

### BFS para luces

`minimumPressesFor(...)` resuelve la parte 1.

La maquina empieza con todas las luces apagadas, representadas por la mascara `0`.

Si `targetMask` ya es `0`, devuelve `0` porque no hace falta pulsar nada.

Si no, usa BFS:

```java
Queue<MachineState> pendingStates
Set<Long> visitedMasks
```

Cada `MachineState` guarda:

* `lightMask`: estado actual de luces.
* `pressCount`: cantidad de pulsaciones usadas para llegar a ese estado.

Para generar el siguiente estado se usa XOR:

```java
nextMask = state.lightMask() ^ button.toggleMask()
```

XOR encaja con el comportamiento de alternar luces:

* `0 ^ 1 = 1`
* `1 ^ 1 = 0`

BFS garantiza que la primera vez que se alcanza `targetMask` se ha encontrado el minimo numero de pulsaciones.

### Busqueda de voltaje

`minimumJoltagePressesFor(...)` resuelve la parte 2.

Primero valida que la maquina tenga requisitos de voltaje.

Despues crea un objeto interno `JoltageSearch`, que transforma el problema en un sistema lineal:

* Cada fila representa un contador.
* Cada columna representa un boton.
* El valor es `1` si el boton afecta a ese contador y `0` si no.
* La ultima columna contiene el requisito de voltaje.

El sistema se reduce con una eliminacion similar a Gauss-Jordan usando la clase interna `Rational`, para evitar errores con divisiones.

Despues se identifican:

* columnas pivote
* columnas libres
* rango del sistema

Las variables libres se enumeran dentro de limites razonables y cada solucion se evalua.

Una solucion solo es valida si todas las variables resultantes son:

* enteras
* no negativas

El resultado es el minimo total de pulsaciones.

### `Rational`

`Rational` es un record interno usado por la reduccion del sistema.

Guarda:

* `numerator`
* `denominator`

Normaliza el signo y reduce la fraccion con maximo comun divisor.

Tiene operaciones:

* `add`
* `subtract`
* `multiply`
* `divide`

Tambien permite comprobar:

```java
isZero()
isNonNegativeInteger()
```

Esto es importante porque parte 2 no acepta soluciones decimales.

### `JoltageButton`

`JoltageButton` es un record interno que transforma una mascara de boton en una lista de contadores afectados.

Se crea desde un `ButtonWiring` y el numero de contadores:

```java
JoltageButton.from(button, counterCount)
```

Tambien ofrece:

* `affects(int counter)`: indica si afecta a un contador.
* `width()`: numero de contadores afectados.

Los botones se ordenan por anchura descendente en la busqueda de voltaje.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias de resolucion.

### `FactorySolver<T>`

`FactorySolver<T>` es la interfaz comun de los solvers del dia.

Define este contrato:

```java
T solve(List<FactoryMachine> machines);
```

Es generica porque parte 1 devuelve `Integer` y parte 2 devuelve `Long`.

### `LightInitializationSolver`

`LightInitializationSolver` resuelve la parte 1.

Tiene una dependencia:

```java
private final MachineInitializer initializer;
```

Su metodo `solve(...)` delega en:

```java
initializer.minimumPressesForAll(machines)
```

### `JoltageInitializationSolver`

`JoltageInitializationSolver` resuelve la parte 2.

Tambien depende de `MachineInitializer`.

Su metodo `solve(...)` delega en:

```java
initializer.minimumJoltagePressesForAll(machines)
```

## Diferencia entre parte 1 y parte 2 en el codigo

Las dos partes usan:

* el mismo parser,
* la misma clase `FactoryMachine`,
* la misma lista de `ButtonWiring`.

La diferencia esta en como se interpreta cada boton.

En parte 1:

```java
new LightInitializationSolver(initializer)
```

El boton alterna bits de una mascara de luces y se busca el minimo con BFS.

En parte 2:

```java
new JoltageInitializationSolver(initializer)
```

El boton suma a contadores y se busca una combinacion de pulsaciones que cumpla un sistema lineal con soluciones enteras no negativas.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, modelo, inicializador y solvers estan separados.
* Bajo acoplamiento: los solvers trabajan con `FactoryMachine`, no con lineas de texto.
* Modularidad: BFS y busqueda de voltaje estan dentro de `MachineInitializer`.
* Encapsulacion: `FactoryMachine` copia sus listas internas.
* Abstraccion: `FactorySolver<T>` permite tratar ambas partes con un contrato comun.
* Codigo expresivo: los nombres distinguen inicializacion de luces e inicializacion de voltaje.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `FactoryManualParser` parsea el manual.
* `ButtonWiring` representa la mascara de un boton.
* `FactoryMachine` representa una maquina.
* `MachineInitializer` contiene los algoritmos de inicializacion.
* `LightInitializationSolver` resuelve parte 1.
* `JoltageInitializationSolver` resuelve parte 2.
* `FactoryPuzzle` coordina.

### DRY

El parseo de maquinas se centraliza en `FactoryManualParser`.

Los solvers no duplican algoritmos: ambos delegan en `MachineInitializer`.

### OCP: Abierto/Cerrado

Si apareciera otro modo de inicializar maquinas, se podria crear otra implementacion de `FactorySolver<T>` sin modificar las existentes.

### DIP: Inversion de Dependencias

`FactoryPuzzle` depende de la interfaz `FactorySolver<T>`, no de implementaciones concretas obligatorias.

### KISS

La parte 1 usa BFS porque el estado es binario y finito. La parte 2 usa un sistema lineal porque los voltajes son acumulaciones numericas.

### YAGNI

No se fuerza un unico algoritmo para ambas partes. Cada parte usa la herramienta que encaja con su modelo.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `FactorySolver<T>`.

Las dos estrategias son:

* `LightInitializationSolver`
* `JoltageInitializationSolver`

Ambas reciben una lista de `FactoryMachine`, pero devuelven tipos distintos y usan algoritmos distintos.

### Value Object

`ButtonWiring` funciona como value object al estar modelado como `record`.

Representa la mascara de un boton y se compara por contenido.

### Polimorfismo mediante genericos

`FactorySolver<T>` permite que parte 1 y parte 2 compartan una abstraccion aunque devuelvan tipos distintos:

* `Integer` para luces.
* `Long` para voltajes.

### BFS

`MachineInitializer.minimumPressesFor(...)` usa busqueda en anchura.

No es un patron GoF, pero es el algoritmo clave para garantizar el minimo numero de pulsaciones en parte 1.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a maquinas o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque las pulsaciones no necesitan historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 10 estan en:

```text
src/test/java/aoc/day10
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de diagrama de luces y botones.
* Parseo de requisitos de voltaje.
* Ignorar lineas vacias.
* Rechazo de input vacio.
* Rechazo de lineas sin diagrama.
* Rechazo de indices de boton fuera del diagrama.
* Rechazo de requisitos de voltaje con cantidad distinta al diagrama.
* Devolver `0` cuando el estado objetivo ya coincide con el estado inicial.
* Encontrar el menor numero de pulsaciones para luces.
* Rechazar maquinas sin botones.
* Encontrar el menor numero de pulsaciones para requisitos de voltaje.
* Devolver `0` cuando todos los requisitos de voltaje ya son cero.

Un caso importante es el uso de XOR en parte 1. Pulsar un boton dos veces devuelve esas luces al estado anterior, por eso BFS necesita guardar estados visitados para no repetir mascaras indefinidamente.
