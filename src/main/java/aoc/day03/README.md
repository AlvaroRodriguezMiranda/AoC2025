# Dia 3: Lobby

En el tercer dia del Advent of Code 2025, el problema trata sobre bancos de baterias colocados en el vestibulo. Cada linea del input representa un banco de baterias mediante una secuencia de digitos.

Cada digito representa el joltage de una bateria. El objetivo es elegir algunas baterias de cada banco para formar el mayor numero posible.

Por ejemplo:

```text
987654321111111
811111111111119
234234234234278
818181911112111
```

La restriccion importante es que no se pueden reordenar las baterias libremente. Hay que mantener el orden original de los digitos. Por tanto, el problema consiste en elegir la mejor subsecuencia, no en ordenar los digitos de mayor a menor.

## Parte 1

En la primera parte hay que elegir 2 baterias de cada banco.

Con esas dos baterias se forma el mayor numero posible manteniendo el orden original.

Ejemplo:

```text
9876 -> 98
8119 -> 89
1234 -> 34
```

En `8119`, aunque el `9` es el mayor digito, no se puede formar `98` porque el `9` aparece despues y ya no habria un `8` a su derecha. La mejor eleccion valida es `89`.

La respuesta de la parte 1 es la suma del mejor numero de 2 digitos de cada banco.

En mi codigo esta parte se resuelve con `TwoBatteryJoltageSolver`.

## Parte 2

En la segunda parte hay que elegir 12 baterias de cada banco.

La idea es la misma que en la parte 1, pero el numero resultante tiene 12 digitos. Por eso el resultado se maneja como `long`.

El orden original sigue siendo obligatorio. No se busca el mayor conjunto de digitos, sino la mayor subsecuencia posible de longitud 12.

En mi codigo esta parte se resuelve con `TwelveBatteryJoltageSolver`.

## Estructura del paquete

```text
aoc.day03
|-- LobbyPuzzle.java
|-- battery
|   `-- BatteryBank.java
|-- input
|   `-- BatteryBankParser.java
`-- solver
    |-- JoltageSolver.java
    |-- TwelveBatteryJoltageSolver.java
    `-- TwoBatteryJoltageSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte las lineas del input en bancos de baterias.
* `battery`: contiene el modelo principal y el algoritmo de seleccion.
* `solver`: contiene las estrategias de parte 1 y parte 2.

La clase `LobbyPuzzle` coordina el dia completo.

## Clase principal `LobbyPuzzle`

`LobbyPuzzle` une el parser con los solvers de cada parte.

Tiene tres dependencias:

* `BatteryBankParser`: parsea las lineas del input.
* `JoltageSolver` para parte 1.
* `JoltageSolver` para parte 2.

Sus metodos principales son:

```java
public int solvePartOne(List<String> inputLines)
public long solvePartTwo(List<String> inputLines)
```

Ambos metodos siguen el mismo flujo:

1. Parsean las lineas con `BatteryBankParser`.
2. Obtienen una lista de `BatteryBank`.
3. Delegan el calculo en el solver correspondiente.

La parte 1 devuelve `int` porque el resultado oficial de esa parte es mas pequeno. Internamente, el solver devuelve `long` y `LobbyPuzzle` usa `Math.toIntExact(...)` para convertirlo.

El metodo `main` lee el archivo:

```text
src/main/resources/day03/input.txt
```

Despues crea el puzzle con:

```java
new BatteryBankParser()
new TwoBatteryJoltageSolver()
new TwelveBatteryJoltageSolver()
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Clase del paquete `input`

El paquete `input` contiene la clase que transforma el texto del problema en objetos del dominio.

### `BatteryBankParser`

`BatteryBankParser` convierte cada linea del input en un `BatteryBank`.

Su metodo principal para el input completo es:

```java
public List<BatteryBank> parseLines(List<String> lines)
```

Este metodo:

1. Limpia espacios con `trim`.
2. Ignora lineas vacias.
3. Convierte cada linea valida en un `BatteryBank`.

Tambien tiene:

```java
public BatteryBank parseLine(String line)
```

Este metodo parsea una sola linea. Valida que la linea no sea `null`, vacia o solo espacios.

Despues recorre cada caracter y exige que sea un digito entre `1` y `9`. El `0` se rechaza porque el problema solo trabaja con baterias con valores del `1` al `9`.

Ejemplos de entradas invalidas:

```text
120
12A
```

Si la linea es valida, se transforma en una lista de enteros y se crea un `BatteryBank`.

## Clase del paquete `battery`

El paquete `battery` contiene el modelo principal del dia.

### `BatteryBank`

`BatteryBank` representa un banco de baterias.

Internamente guarda una lista de enteros llamada `joltageRatings`. La lista se copia con `List.copyOf(...)` para evitar que se modifique desde fuera despues de crear el banco.

Al construirse, valida que haya al menos 2 baterias:

```java
public BatteryBank(List<Integer> joltageRatings)
```

Sus metodos principales son:

```java
public int maximumTwoBatteryJoltage()
```

Calcula el mejor numero usando exactamente 2 baterias. Internamente delega en el metodo general con `batteryCount = 2`.

```java
public long maximumJoltageUsingBatteries(int batteryCount)
```

Calcula el mayor numero posible usando una cantidad concreta de baterias.

Este metodo valida que `batteryCount` este entre `1` y el tamano del banco. Si se pide usar mas baterias de las que existen, lanza una excepcion.

### Algoritmo de seleccion

El algoritmo de `maximumJoltageUsingBatteries(...)` es voraz.

Para cada posicion del resultado:

1. Calcula cuantas baterias faltaran despues de elegir la actual.
2. Determina hasta que indice puede mirar sin quedarse sin suficientes baterias para completar el resultado.
3. Busca el digito mas grande dentro de esa ventana.
4. Lo anade al numero resultado.
5. Continua buscando a partir del indice siguiente al elegido.

La clave esta en no elegir simplemente el mayor digito de toda la lista, porque podria estar demasiado tarde y no dejar suficientes baterias para completar el numero.

Ejemplo simplificado:

```text
Banco: 8 1 8 1 8 1 9 1 1 1 1 2 1 1 1
Elegir: 12 baterias
Resultado esperado: 888911112111
```

El algoritmo escoge el mejor digito posible en cada paso, pero siempre respetando el orden y dejando hueco para los digitos restantes.

## Clases del paquete `solver`

El paquete `solver` contiene las estrategias que calculan el total de joltages para cada parte.

### `JoltageSolver`

`JoltageSolver` es la interfaz comun de los solvers del dia.

Define este contrato:

```java
long solve(List<BatteryBank> batteryBanks);
```

Cada solver recibe una lista de bancos de baterias y devuelve la suma total del mejor joltage de cada banco.

### `TwoBatteryJoltageSolver`

`TwoBatteryJoltageSolver` resuelve la parte 1.

Tiene una constante:

```java
private static final int BATTERY_COUNT = 2;
```

El flujo es:

1. Recorre todos los `BatteryBank`.
2. Para cada banco llama a `maximumJoltageUsingBatteries(2)`.
3. Suma todos los resultados.

Usa streams:

```java
batteryBanks.stream()
        .mapToLong(...)
        .sum()
```

### `TwelveBatteryJoltageSolver`

`TwelveBatteryJoltageSolver` resuelve la parte 2.

Tiene una constante:

```java
private static final int BATTERY_COUNT = 12;
```

El flujo es el mismo que en parte 1, pero llama a:

```java
maximumJoltageUsingBatteries(12)
```

Devuelve `long` porque cada numero individual puede tener 12 digitos.

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia entre las dos partes esta en cuantas baterias se seleccionan de cada banco.

En parte 1:

```java
new TwoBatteryJoltageSolver()
```

Cada banco se reduce a su mejor numero de 2 digitos.

En parte 2:

```java
new TwelveBatteryJoltageSolver()
```

Cada banco se reduce a su mejor numero de 12 digitos.

El algoritmo no se duplica. La seleccion general esta en `BatteryBank.maximumJoltageUsingBatteries(...)` y cada solver solo fija el valor de `BATTERY_COUNT`.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: parser, modelo y solvers estan separados por responsabilidad.
* Bajo acoplamiento: los solvers no conocen el formato del input.
* Modularidad: el algoritmo principal esta aislado en `BatteryBank`.
* Encapsulacion: la lista de joltages se copia y no se expone para modificacion.
* Abstraccion: `JoltageSolver` permite tratar parte 1 y parte 2 con el mismo contrato.
* Codigo expresivo: los nombres indican claramente si se usan 2 o 12 baterias.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `BatteryBankParser` parsea lineas.
* `BatteryBank` representa el banco y calcula la mejor subsecuencia.
* `TwoBatteryJoltageSolver` resuelve parte 1.
* `TwelveBatteryJoltageSolver` resuelve parte 2.
* `LobbyPuzzle` coordina.

### DRY

El algoritmo para elegir baterias esta una sola vez en `BatteryBank.maximumJoltageUsingBatteries(...)`.

Los dos solvers reutilizan ese metodo y solo cambian la cantidad de baterias.

### OCP: Abierto/Cerrado

Si se necesitara una tercera parte con otra cantidad de baterias, se podria crear otro `JoltageSolver` sin modificar los solvers existentes.

### DIP: Inversion de Dependencias

`LobbyPuzzle` depende de la interfaz `JoltageSolver`, no de una implementacion concreta obligatoria.

### KISS

El algoritmo voraz es directo y suficiente para el problema. No se usa backtracking ni programacion dinamica porque no hace falta explorar todas las combinaciones.

### YAGNI

No se anaden estructuras extra ni jerarquias complejas. La unica abstraccion de solver existe porque realmente hay dos variantes del calculo.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `JoltageSolver`.

Las dos estrategias son:

* `TwoBatteryJoltageSolver`
* `TwelveBatteryJoltageSolver`

Ambas reciben la misma lista de `BatteryBank`, pero cada una usa un numero distinto de baterias.

### Value Object / modelo de dominio

`BatteryBank` funciona como modelo del dominio. No es un simple contenedor de lista: valida el banco y contiene el comportamiento principal para calcular el joltage maximo.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque no hay notificaciones de cambios de estado.

No se usa `Decorator`, porque no se anaden responsabilidades dinamicas a bancos o solvers.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque elegir baterias no necesita historial, deshacer ni ejecucion diferida.

## Tests

Los tests del dia 3 estan en:

```text
src/test/java/aoc/day03
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Parseo de una linea de baterias.
* Ignorar lineas vacias.
* Rechazar el digito `0`.
* Rechazar caracteres que no son digitos.
* Elegir el mejor joltage con 2 baterias.
* Mantener el orden original al elegir baterias.
* Elegir el mejor joltage con 12 baterias.
* Rechazar bancos con menos de 2 baterias.
* Rechazar una cantidad de baterias mayor que el tamano del banco.
* Sumar el resultado de varios bancos en el solver.

Un caso importante es `8119`, porque demuestra que no se pueden ordenar los digitos. Aunque `9` es el mayor valor, el mejor numero de dos baterias respetando el orden es `89`.
