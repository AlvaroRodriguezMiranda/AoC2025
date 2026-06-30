# Dia 1: Secret Entrance

En el primer dia del Advent of Code 2025, el problema plantea una entrada secreta protegida por una caja fuerte. Para abrirla hay que calcular una contrasena a partir de una lista de giros sobre un dial circular.

El dial contiene los numeros del `0` al `99` y empieza apuntando al numero `50`. El archivo de entrada contiene una rotacion por linea. Cada rotacion empieza con una letra:

* `L`: girar hacia la izquierda, es decir, hacia numeros menores.
* `R`: girar hacia la derecha, es decir, hacia numeros mayores.

Despues de la letra aparece un numero que indica cuantos clics debe moverse el dial. Como el dial es circular, al girar a la izquierda desde `0` se vuelve a `99`, y al girar a la derecha desde `99` se vuelve a `0`.

Por ejemplo:

```text
L68
R48
L5
```

Cada linea se transforma en una rotacion con una direccion y una distancia.

## Parte 1

En la primera parte hay que seguir todas las rotaciones y contar cuantas veces el dial termina apuntando al numero `0` justo despues de completar una instruccion.

Es importante que en esta parte solo cuenta la posicion final de cada rotacion. Si durante el movimiento el dial pasa por `0`, pero termina en otro numero, esa aparicion no se suma.

La contrasena de la parte 1 es el numero total de rotaciones que dejan el dial en `0`.

En mi codigo esta parte la resuelve `FinalPositionPasswordSolver`.

## Parte 2

En la segunda parte cambia la forma de contar la contrasena. Ahora se cuenta cada clic individual que hace que el dial apunte a `0`, aunque ocurra a mitad de una rotacion.

Esto significa que una rotacion larga puede sumar varias veces. Por ejemplo, una rotacion `R1000` desde la posicion `50` pasa por el `0` diez veces.

La contrasena de la parte 2 es el numero total de veces que el dial apunta a `0` durante todos los clics realizados.

En mi codigo esta parte la resuelve `ClickPasswordSolver`.

## Estructura del paquete

```text
aoc.day01
|-- SecretEntrancePuzzle.java
|-- dial
|   |-- DialRotation.java
|   |-- RotationDirection.java
|   |-- RotationProgram.java
|   `-- SafeDial.java
|-- input
|   `-- RotationParser.java
`-- password
    |-- ClickPasswordSolver.java
    |-- FinalPositionPasswordSolver.java
    `-- PasswordSolver.java
```

El paquete esta separado en tres zonas:

* `input`: convierte texto del input en objetos del dominio.
* `dial`: contiene el modelo principal del problema.
* `password`: contiene las estrategias que calculan la contrasena de cada parte.

La clase `SecretEntrancePuzzle` queda como coordinadora del dia completo.

## Clases del paquete `dial`

El paquete `dial` contiene las clases que representan el dominio principal del problema: el dial, las rotaciones, la direccion y el programa completo de instrucciones.

### `SafeDial`

`SafeDial` representa el dial circular de la caja fuerte. Es la clase que guarda la posicion actual y contiene la logica para moverse dentro de un rango circular de `100` posiciones.

Sus responsabilidades principales son:

* Guardar la posicion actual del dial.
* Validar que la posicion inicial este entre `0` y `99`.
* Aplicar una rotacion al dial.
* Calcular cuantas veces una rotacion aterriza en una posicion objetivo.

El metodo `rotate(DialRotation rotation)` cambia la posicion del dial aplicando la direccion y la distancia de la rotacion.

El metodo `countClicksLandingOn(int targetPosition, DialRotation rotation)` se usa para la parte 2. No mueve directamente el dial, sino que calcula cuantas veces esa rotacion pasaria por una posicion concreta, normalmente el `0`.

Esta separacion es importante porque en la parte 2 primero se cuentan los cruces por `0` y despues se aplica la rotacion para dejar el dial preparado para la siguiente instruccion.

### `RotationDirection`

`RotationDirection` es un enum con los dos sentidos posibles:

* `LEFT`
* `RIGHT`

Cada direccion sabe aplicar su propio movimiento:

* `LEFT` resta distancia a la posicion.
* `RIGHT` suma distancia a la posicion.

Tambien sabe calcular cuantos clics hacen falta para llegar desde una posicion actual hasta una posicion objetivo.

Para que el dial sea circular, el codigo usa `Math.floorMod(...)`. Esto evita problemas con resultados negativos cuando se gira hacia la izquierda.

Ademas, `RotationDirection.fromSymbol(char symbol)` convierte el caracter del input (`L` o `R`) en el enum correspondiente. Si aparece otro simbolo, lanza una excepcion.

### `DialRotation`

`DialRotation` es un `record` que representa una instruccion ya interpretada.

Contiene dos datos:

* `direction`: la direccion del giro.
* `distance`: la cantidad de clics.

Al estar definido como `record`, es inmutable: una vez creada una rotacion, su direccion y distancia no cambian.

Tambien valida sus datos:

* La direccion no puede ser `null`.
* La distancia no puede ser negativa.

La clase incluye el metodo `fromInstruction(String instruction)`, que permite crear una rotacion directamente desde una linea como `L68` o `R48`.

### `RotationProgram`

`RotationProgram` representa el conjunto completo de rotaciones del input.

Internamente guarda una lista de `DialRotation`, pero la copia con `List.copyOf(...)` para evitar modificaciones externas.

Implementa `Iterable<DialRotation>`, por lo que los solvers pueden recorrer el programa con un `for`:

```java
for (DialRotation rotation : rotationProgram) {
    ...
}
```

Esto hace que los solvers no dependan de como esta guardada internamente la lista de rotaciones.

## Clase del paquete `input`

El paquete `input` contiene la clase encargada de convertir el texto del archivo en objetos del dominio.

### `RotationParser`

`RotationParser` transforma las lineas del input en un `RotationProgram`.

Su metodo principal es:

```java
public RotationProgram parseProgram(List<String> lines)
```

Este metodo hace tres cosas:

1. Limpia espacios con `trim`.
2. Ignora lineas vacias.
3. Convierte cada linea en un `DialRotation`.

Tambien tiene metodos auxiliares:

* `parseLines(List<String> lines)`: devuelve directamente la lista de rotaciones.
* `parseLine(String line)`: parsea una sola linea.

Gracias a esta clase, el resto del codigo no trabaja con strings del input, sino con objetos ya validados.

## Clases del paquete `password`

El paquete `password` contiene la logica especifica para calcular la contrasena.

### `PasswordSolver`

`PasswordSolver` es la interfaz comun para las dos partes del problema.

Define este contrato:

```java
int solve(RotationProgram rotationProgram);
```

Esto significa que cualquier solver de contrasena recibe un programa de rotaciones y devuelve un numero entero con el resultado.

Esta interfaz permite que la clase principal trate las dos partes de forma uniforme.

### `FinalPositionPasswordSolver`

`FinalPositionPasswordSolver` resuelve la parte 1.

Su responsabilidad es contar cuantas rotaciones terminan exactamente en la posicion `0`.

El flujo es:

1. Crea un `SafeDial` empezando en `50`.
2. Recorre cada `DialRotation` del `RotationProgram`.
3. Aplica la rotacion con `safeDial.rotate(rotation)`.
4. Comprueba si `safeDial.position()` es `0`.
5. Si lo es, incrementa el contador.

Esta clase no cuenta cruces intermedios. Solo mira el estado final despues de cada instruccion.

### `ClickPasswordSolver`

`ClickPasswordSolver` resuelve la parte 2.

Su responsabilidad es contar todas las veces que un clic deja el dial apuntando a `0`.

El flujo es:

1. Crea un `SafeDial` empezando en `50`.
2. Recorre cada `DialRotation`.
3. Antes de mover el dial, llama a `safeDial.countClicksLandingOn(0, rotation)`.
4. Suma al total los cruces por `0` de esa rotacion.
5. Despues aplica la rotacion con `safeDial.rotate(rotation)`.

El orden es importante. Primero se cuenta usando la posicion actual y despues se actualiza el dial para la siguiente instruccion.

## Clase principal `SecretEntrancePuzzle`

`SecretEntrancePuzzle` es la clase que coordina el dia completo.

No contiene la logica concreta del dial ni de las contrasenas. Su trabajo es unir las piezas:

* Recibe un `RotationParser`.
* Recibe un solver para la parte 1.
* Recibe un solver para la parte 2.
* Parsea el input.
* Llama al solver correspondiente.

Los metodos principales son:

```java
public int solvePartOne(List<String> inputLines)
public int solvePartTwo(List<String> inputLines)
```

Ambos metodos convierten las lineas en un `RotationProgram` y delegan el calculo en su solver.

El metodo `main` lee el archivo:

```text
src/main/resources/day01/input.txt
```

Despues crea el puzzle con:

```java
new RotationParser()
new FinalPositionPasswordSolver()
new ClickPasswordSolver()
```

Finalmente imprime los resultados de parte 1 y parte 2.

## Diferencia entre parte 1 y parte 2 en el codigo

La diferencia principal esta en que se considera una aparicion valida del numero `0`.

En parte 1:

* Se aplica la rotacion.
* Se mira la posicion final.
* Solo cuenta si termina en `0`.

En parte 2:

* Se analiza la rotacion desde la posicion actual.
* Se cuentan todos los clics que aterrizan en `0`.
* Despues se aplica la rotacion.

Por eso hay dos implementaciones de `PasswordSolver`:

* `FinalPositionPasswordSolver`: cuenta finales.
* `ClickPasswordSolver`: cuenta clics intermedios y finales.

## Fundamentos de diseno utilizados

En esta solucion se aplican varios fundamentos de diseno:

* Alta cohesion: cada paquete agrupa clases relacionadas con una misma responsabilidad.
* Bajo acoplamiento: los solvers no parsean texto y el parser no calcula contrasenas.
* Modularidad: el problema esta separado en input, modelo y resolucion.
* Encapsulacion: `SafeDial` guarda la posicion y controla como cambia.
* Abstraccion: `PasswordSolver` permite tratar las dos partes con un contrato comun.
* Codigo expresivo: nombres como `FinalPositionPasswordSolver` y `ClickPasswordSolver` explican que cuenta cada clase.

## Principios aplicados

### SRP: Principio de Responsabilidad Unica

Cada clase tiene una responsabilidad concreta:

* `RotationParser` parsea.
* `SafeDial` modela el dial.
* `FinalPositionPasswordSolver` resuelve parte 1.
* `ClickPasswordSolver` resuelve parte 2.
* `SecretEntrancePuzzle` coordina.

### DRY

La logica circular del dial esta en `SafeDial` y `RotationDirection`. No se repite en los dos solvers.

### OCP: Abierto/Cerrado

Si apareciera otra forma de calcular una contrasena, se podria crear otra implementacion de `PasswordSolver` sin cambiar los solvers existentes.

### DIP: Inversion de Dependencias

`SecretEntrancePuzzle` depende de la interfaz `PasswordSolver`, no de una unica implementacion obligatoria.

### YAGNI

No se anaden patrones innecesarios. El diseno solo separa las partes que realmente cambian: parseo, dial y criterio de conteo.

## Patrones de diseno aplicados

### Strategy

El patron Strategy aparece con `PasswordSolver`.

Las dos estrategias son:

* `FinalPositionPasswordSolver`
* `ClickPasswordSolver`

Ambas reciben el mismo tipo de entrada (`RotationProgram`), pero aplican criterios diferentes para calcular la contrasena.

### Iterator

`RotationProgram` implementa `Iterable<DialRotation>`.

Esto permite recorrer las rotaciones sin exponer una lista modificable y sin que los solvers dependan de la estructura interna.

### Static Factory Method

Hay metodos de creacion desde datos externos:

* `DialRotation.fromInstruction(...)`
* `RotationDirection.fromSymbol(...)`

Estos metodos centralizan la conversion desde texto y evitan que el parseo de simbolos quede repartido por el codigo.

## Patrones no aplicados

No se usa `Singleton`, porque no hay ningun objeto global que deba tener una unica instancia.

No se usa `Observer`, porque ninguna clase necesita notificar cambios a varios suscriptores.

No se usa `Decorator`, porque no se estan anadiendo responsabilidades dinamicamente a objetos existentes.

No se usa `Adapter`, porque no se integra ninguna API externa incompatible.

No se usa `Command`, porque las rotaciones no necesitan historial, deshacer ni ejecucion diferida como comandos independientes.

## Tests

Los tests del dia 1 estan en:

```text
src/test/java/aoc/day01
```

Cubren:

* El ejemplo oficial de parte 1.
* El ejemplo oficial de parte 2.
* Que la posicion inicial `50` no se cuenta sola.
* Rotaciones largas como `R1000`.
* Parseo de instrucciones.
* Validaciones del dial.
* Comportamiento de los dos solvers.

Un caso importante es `R1000`, porque demuestra que la parte 2 no puede limitarse a mirar la posicion final. Aunque el dial termina otra vez en `50`, durante el giro pasa por `0` diez veces.
