# Advent of Code

Este proyecto es mi práctica de Java basada en Advent of Code. Mi objetivo no es solo sacar el número correcto de cada puzzle, sino construir una solución que pueda defender en clase: código legible, nombres claros, clases con responsabilidades separadas, patrones usados con sentido y tests que demuestren que la solución funciona.

La idea que sigo en cada día es separar el problema en partes pequeñas. Primero modelo el dominio del puzzle, después separo el parseo de la entrada y finalmente dejo la lógica de solución en clases específicas. Así evito tener todo mezclado en una sola clase grande.

## Día 1: Entrada secreta

### Qué pide el problema

El Día 1 trata de una caja fuerte con un dial circular numerado del 0 al 99. El dial empieza apuntando a 50 y la entrada contiene una lista de rotaciones, una por línea, con este formato:

```text
L68
R48
L5
```

La letra indica la dirección:

- `L`: girar a la izquierda, hacia números menores.
- `R`: girar a la derecha, hacia números mayores.

El número indica cuántos clics se mueve el dial. Como el dial es circular, si se pasa del 99 vuelve al 0, y si se pasa del 0 vuelve al 99.

### Parte 1

En la parte 1 la contraseña es la cantidad de veces que el dial termina apuntando a 0 después de completar una rotación.

Esto significa que solo miro la posición final de cada instrucción. Si durante una rotación el dial pasa por 0 pero no termina ahí, eso no cuenta para la parte 1.

Con el ejemplo oficial:

```text
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
```

El resultado de la parte 1 es:

```text
3
```

### Parte 2

En la parte 2 aparece el método `0x434C49434B`, que significa `CLICK`. Esto no se añade al archivo de entrada. Es una regla nueva del enunciado.

La entrada sigue siendo la misma lista de rotaciones, pero ahora la contraseña se calcula contando cada clic individual que hace que el dial apunte a 0, incluso si ocurre en mitad de una rotación.

Con el mismo ejemplo oficial, el resultado de la parte 2 es:

```text
6
```

También se comprueba el caso importante `R1000`: si el dial empieza en 50, esa rotación hace que el dial apunte a 0 diez veces antes de volver a 50.

## Estructura del Día 1

El código del Día 1 está en `aoc.day01` y lo he dividido así:

```text
aoc.day01
├── SecretEntrancePuzzle.java
├── dial
├── input
└── password
```

### Paquete `dial`

Este paquete contiene el modelo principal del problema.

`SafeDial` representa el dial circular. Guarda la posición actual y tiene la lógica para aplicar rotaciones. También contiene la lógica común que permite saber cuántos clics caen en una posición concreta, que en este puzzle es la posición 0.

`DialRotation` representa una instrucción ya parseada. Tiene una dirección y una distancia. Esto evita trabajar todo el rato con textos como `L68` dentro de la lógica del puzzle.

`RotationDirection` representa las dos direcciones posibles: izquierda y derecha. Cada dirección sabe cómo aplicar su movimiento.

### Paquete `input`

`RotationParser` se encarga de transformar líneas de texto en objetos `DialRotation`.

Separé esta parte porque parsear texto no es la misma responsabilidad que resolver el puzzle. Así, si una línea está mal escrita, el error queda localizado en el parser y no en el solver.

### Paquete `password`

`PasswordSolver` contiene la lógica para calcular la contraseña.

Ahora mismo tiene dos métodos principales:

- `countRotationsEndingAtZero`: resuelve la parte 1.
- `countClicksLandingOnZero`: resuelve la parte 2.

La diferencia está en qué se cuenta. La parte 1 cuenta finales de rotación. La parte 2 cuenta clics que caen en 0.

### `SecretEntrancePuzzle`

Esta clase coordina el caso de uso. Recibe las líneas de entrada, usa el parser y llama al método correspondiente del solver.

Tiene métodos separados para cada parte:

- `solvePartOne`
- `solvePartTwo`

Así queda claro que las dos partes pertenecen al mismo día, usan la misma entrada y comparten el mismo modelo, pero no calculan exactamente lo mismo.

## Patrones usados en Día 1

### Factory Method

Uso un Factory Method sencillo en `RotationDirection.fromSymbol`.

Lo considero un Factory Method porque centraliza la creación de una dirección a partir del símbolo de entrada. El resto del código no necesita saber que `L` significa izquierda o que `R` significa derecha.

Esto ayuda a que el parser sea más limpio y a que la traducción de símbolos esté en un único sitio.

### Polimorfismo con `enum`

En `RotationDirection`, cada valor del enum implementa su propia forma de aplicar el movimiento.

La dirección `LEFT` calcula la nueva posición restando distancia, y `RIGHT` lo hace sumando distancia. Esto evita tener condicionales repetidos por el código preguntando si la dirección es izquierda o derecha.

No lo presentaría como un patrón de diseño principal, pero sí como una aplicación de polimorfismo y código expresivo.

### Patrones no forzados

No he usado Decorator, Observer, Singleton ni Adapter en Día 1 porque no aportaban nada real a este puzzle con la estructura actual.

Tampoco he usado Iterator como patrón explícito porque todavía no hay una clase propia tipo `RotationProgram` que oculte una colección interna. Si más adelante lo necesitamos para organizar mejor varios días, se puede añadir con sentido, pero ahora habría sido añadir complejidad sin necesidad.

No he usado Strategy como patrón formal porque el diseño actual mantiene un solver simple. La separación entre `solvePartOne` y `solvePartTwo` ya deja claras las dos operaciones. Si el proyecto crece y cada parte necesita clases separadas, tendría sentido extraer una interfaz común para los solvers.

## Principios aplicados

### SRP

Cada clase tiene una responsabilidad principal:

- `SafeDial` modela el dial.
- `DialRotation` representa una rotación.
- `RotationParser` parsea la entrada.
- `PasswordSolver` calcula la contraseña.
- `SecretEntrancePuzzle` coordina la ejecución.

Esto hace que el código sea más fácil de explicar y de probar.

### DRY

No dupliqué la lógica del dial para la parte 2. La parte 1 y la parte 2 reutilizan las mismas clases de dominio: `SafeDial`, `DialRotation` y `RotationDirection`.

La diferencia está solo en la forma de contar la contraseña.

### KISS

He intentado mantener la solución simple. El puzzle no necesita una arquitectura grande ni muchos patrones. Por eso usé paquetes pequeños y clases directas.

### YAGNI

No añadí funcionalidades que no se pidieran. La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no tenía sentido inventar su comportamiento.

### Encapsulación

La posición del dial está encapsulada dentro de `SafeDial`. Desde fuera no se modifica directamente, sino mediante `rotate`.

Esto protege la lógica del dial y evita que otras clases cambien su estado de cualquier forma.

### Bajo acoplamiento

`PasswordSolver` no trabaja con líneas de texto, sino con objetos `DialRotation`. Eso significa que la lógica de contraseña no depende del formato exacto del archivo.

### Alta cohesión

Cada paquete agrupa clases relacionadas:

- `dial` agrupa el modelo del dial.
- `input` agrupa el parseo.
- `password` agrupa el cálculo de la contraseña.

## Tests del Día 1

He creado tests para cubrir tanto el comportamiento normal como los casos importantes.

### `SafeDialTest`

Comprueba:

- Giro a la derecha dentro del rango.
- Giro a la izquierda dentro del rango.
- Paso de 0 a 99 al girar a la izquierda.
- Paso de 99 a 0 al girar a la derecha.
- Rechazo de una posición inicial inválida.
- Conteo de clics que caen en 0 durante una rotación.
- Caso largo `R1000`, que debe contar 10.
- Que estar ya en 0 al inicio no cuenta como clic si todavía no se ha movido.

### `RotationParserTest`

Comprueba:

- Parseo de una línea como `L68`.
- Ignorar líneas vacías.
- Rechazar direcciones desconocidas.
- Rechazar distancias que no son números.

### `PasswordSolverTest`

Comprueba:

- Ejemplo oficial de parte 1, con resultado 3.
- Que la posición inicial no se cuenta por sí sola.
- Rotaciones que terminan varias veces en 0.
- Ejemplo oficial de parte 2, con resultado 6.
- Caso `R1000`, con resultado 10.

### `SecretEntrancePuzzleTest`

Comprueba que la clase coordinadora llama correctamente al parser y al solver para resolver parte 1 y parte 2 desde líneas de entrada.

## Cómo ejecuté los tests

Los tests se ejecutan con Maven:

```powershell
mvn test
```

En mi caso, como estoy usando Java 26, también configuré Maven para usar el almacén de certificados de Windows:

```powershell
$env:MAVEN_OPTS = '-Djavax.net.ssl.trustStoreType=Windows-ROOT'
```

El resultado actual de los tests fue:

```text
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Cómo defendería el Día 1

Yo explicaría que empecé identificando los conceptos del problema: hay un dial, hay rotaciones y hay una contraseña que se calcula siguiendo unas reglas.

Después separé esos conceptos en clases. El dial no sabe leer archivos ni parsear texto. El parser no sabe calcular contraseñas. Y el solver no trabaja con strings, sino con rotaciones ya convertidas al modelo del dominio.

Para la parte 2 no dupliqué el día ni el parser. Usé la misma entrada y el mismo modelo, porque el problema sigue siendo el mismo. Lo único que cambia es el criterio de conteo: parte 1 cuenta posiciones finales y parte 2 cuenta clics que caen en 0.

También destacaría que no he metido patrones por obligación. El Factory Method sí encaja porque crear una dirección desde un símbolo de texto es una responsabilidad concreta. Otros patrones no los he usado porque ahora mismo harían el código más difícil de defender.

## Día 2: Gift Shop

### Qué pide el problema

El Día 2 trata de una tienda de regalos donde se han metido IDs de producto inválidos en la base de datos. La entrada tiene varios rangos de IDs separados por comas, por ejemplo:

```text
11-22,95-115,998-1012
```

Cada rango tiene un primer ID y un último ID separados por un guion. La parte 1 pide encontrar todos los IDs inválidos dentro de esos rangos y sumarlos.

Un ID es inválido si está formado por una secuencia de dígitos repetida dos veces. Por ejemplo:

- `55` es inválido porque es `5` repetido dos veces.
- `6464` es inválido porque es `64` repetido dos veces.
- `123123` es inválido porque es `123` repetido dos veces.

En cambio, `101` no cuenta porque no tiene dos mitades iguales. Además, el enunciado dice que los IDs no tienen ceros a la izquierda.

Con el ejemplo oficial, el resultado de la parte 1 es:

```text
1227775554
```

### Parte 2

En la parte 2 la regla se amplía. Ahora un ID es inválido si está formado por una secuencia de dígitos repetida al menos dos veces, no solo exactamente dos veces.

Por ejemplo:

- `12341234` es inválido porque `1234` se repite dos veces.
- `123123123` es inválido porque `123` se repite tres veces.
- `1212121212` es inválido porque `12` se repite cinco veces.
- `1111111` es inválido porque `1` se repite siete veces.

La entrada no cambia. Se usan los mismos rangos que en la parte 1, pero cambia la regla para decidir si un ID es inválido.

Con el ejemplo oficial, el resultado de la parte 2 es:

```text
4174379265
```

### Estructura del Día 2

El código del Día 2 está en `aoc.day02` y lo he dividido así:

```text
aoc.day02
├── GiftShopPuzzle.java
├── input
└── product
```

### Paquete `product`

Este paquete contiene el dominio del problema.

`ProductIdRange` representa un rango de IDs. Guarda el primer ID y el último ID, valida que el rango tenga sentido y ofrece el método `contains` para saber si un ID está dentro.

`RepeatedPatternProductId` contiene la regla que decide si un ID es inválido. La regla está separada porque no es responsabilidad del rango saber cómo se detecta el patrón repetido.

`ProductIdSumCalculator` calcula la suma de los IDs inválidos dentro de una lista de rangos. Para la parte 1 usa la regla de dos mitades iguales. Para la parte 2 usa la regla más general de una secuencia repetida dos o más veces.

### Paquete `input`

`ProductRangeParser` convierte la línea de entrada en objetos `ProductIdRange`.

La entrada viene como una sola línea larga separada por comas, así que el parser se encarga de dividirla y validar cada rango. De esta forma, el solver no depende del formato exacto del texto.

### `GiftShopPuzzle`

`GiftShopPuzzle` coordina el caso de uso del Día 2. Recibe la línea de entrada, usa el parser y después llama al calculador de suma.

También tiene un `main` para poder ejecutar el día usando:

```text
src/main/resources/day02/input.txt
```

### Decisión de algoritmo

Para Día 2 no recorro todos los números de cada rango uno por uno. Eso podría ser muy lento si un rango es grande.

En la parte 1 genero candidatos que ya tienen forma de patrón repetido dos veces. Por ejemplo, si la mitad es `123`, construyo `123123`. Luego solo compruebo si ese número cae dentro del rango.

En la parte 2 hago lo mismo pero con repeticiones de dos o más veces. Por ejemplo, puedo generar `123123123` usando la secuencia `123` repetida tres veces.

También tengo en cuenta que un mismo ID puede tener más de una interpretación. Por ejemplo, `111111` puede verse como `1` repetido seis veces, `11` repetido tres veces o `111` repetido dos veces. Para no sumarlo varias veces, guardo los IDs inválidos encontrados en un `Set`.

Esta decisión mantiene el código entendible, pero evita hacer trabajo innecesario.

### Patrones usados en Día 2

En Día 2 no he forzado patrones de diseño.

El parser separa la creación de objetos `ProductIdRange` desde texto, pero no lo presentaría como un Factory Method formal porque no hay una jerarquía ni una fábrica clara como tal. Es simplemente una clase de parsing con una responsabilidad concreta.

Tampoco he usado Iterator, Decorator, Adapter, Observer o Singleton porque no aportan nada real a este puzzle en su estado actual. En la defensa diría que preferí aplicar bien SRP, DRY y KISS antes que meter patrones artificiales.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `ProductIdRange` representa rangos.
- `RepeatedPatternProductId` detecta el patrón inválido.
- `ProductRangeParser` parsea la entrada.
- `ProductIdSumCalculator` suma los IDs inválidos.
- `GiftShopPuzzle` coordina el caso de uso.

La parte 1 y la parte 2 están separadas en métodos distintos dentro del mismo flujo del Día 2. Siguen usando el mismo parser y el mismo modelo de rangos.

### DRY

La lógica para detectar IDs repetidos está en `RepeatedPatternProductId`, no repetida en tests, parser o solver.

### KISS

La estructura es pequeña y directa. No he creado interfaces ni abstracciones extra porque todavía no hacen falta.

### YAGNI

Solo está implementada la parte 1. No he añadido parte 2 porque todavía no tengo su enunciado.

### Encapsulación

`ProductIdRange` valida su propio estado y ofrece `contains`, así que el resto del código no necesita repetir cómo se comprueba si un ID pertenece a un rango.

### Bajo acoplamiento

`ProductIdSumCalculator` trabaja con objetos `ProductIdRange`, no con la línea original de texto. Así la lógica del cálculo queda separada del formato de entrada.

### Alta cohesión

El paquete `product` solo trata conceptos de producto e IDs. El paquete `input` solo trata el parseo.

## Tests del Día 2

### `GiftShopPuzzleTest`

Comprueba:

- El ejemplo oficial completo de la parte 1, con resultado `1227775554`.
- El ejemplo oficial completo de la parte 2, con resultado `4174379265`.

### `ProductRangeParserTest`

Comprueba:

- Parseo de un rango simple.
- Parseo de varios rangos separados por comas.
- Que se ignore una coma final si aparece.
- Rechazo de formatos inválidos.
- Rechazo de entrada vacía.

### `ProductIdRangeTest`

Comprueba:

- Que `contains` funciona para IDs dentro y fuera del rango.
- Que no se permite un rango donde el inicio sea mayor que el final.

### `RepeatedPatternProductIdTest`

Comprueba:

- IDs inválidos como `55`, `6464` y `123123`.
- IDs válidos que deben ignorarse, como `101`, `123124` y números con longitud impar.
- IDs inválidos de parte 2 como `12341234`, `123123123`, `1212121212` y `1111111`.

### `ProductIdSumCalculatorTest`

Comprueba:

- La suma de IDs inválidos en rangos pequeños del ejemplo.
- Un rango que no contiene ningún ID inválido.
- La suma con la regla ampliada de parte 2.
- Que un mismo ID como `111111` no se sume dos veces aunque pueda generarse con distintas secuencias.

## Cómo defendería el Día 2

Yo explicaría que primero separé el formato de entrada del cálculo. El parser convierte el texto en rangos y después el calculador trabaja con objetos del dominio.

También explicaría que el criterio de invalidez está aislado en una clase propia. Cuando apareció la parte 2, pude ampliar esa regla sin tocar el parser ni el rango.

La parte más importante del algoritmo es que no reviso todos los IDs del rango. Genero directamente IDs con forma repetida y compruebo si están dentro. Esto respeta KISS porque sigue siendo fácil de entender, pero también evita un recorrido innecesario.

Para la parte 2 destacaría que reutilizo el mismo input, el mismo parser y el mismo modelo. Solo cambia la regla de detección de IDs inválidos. Eso respeta DRY y SRP.

Por último, diría que no he usado patrones solo por decorar el proyecto. En Día 2 la mejora real está en la separación de responsabilidades, el bajo acoplamiento y los tests.

## Día 3: Vestíbulo

### Qué pide el problema

El Día 3 trata de unos bancos de baterías. Cada línea de la entrada representa un banco y cada dígito es la potencia de una batería, siempre entre 1 y 9.

En la parte 1 hay que encender exactamente dos baterías de cada banco. El número de descarga se forma con los dos dígitos elegidos, respetando el orden en el que aparecen.

Por ejemplo, en un banco `12345`, si elijo las baterías `2` y `4`, la descarga es `24`. No puedo cambiar el orden de las baterías.

Con el ejemplo oficial:

```text
987654321111111
811111111111119
234234234234278
818181911112111
```

Las mejores descargas son `98`, `89`, `78` y `92`, así que el total es:

```text
357
```

### Parte 2

En la parte 2 la idea es la misma, pero ahora hay que encender exactamente doce baterías de cada banco.

La descarga ya no tiene dos dígitos, sino doce. Por eso en el código uso `long` para esta parte, porque los resultados son mucho más grandes que en la parte 1.

Con el mismo ejemplo oficial, las mejores descargas son:

```text
987654321111
811111111119
434234234278
888911112111
```

La suma total es:

```text
3121910778619
```

### Estructura del Día 3

El código del Día 3 está en `aoc.day03` y lo he dividido así:

```text
aoc.day03
├── LobbyPuzzle.java
├── battery
└── input
```

### Paquete `battery`

`BatteryBank` representa un banco de baterías. Guarda las potencias y calcula la descarga máxima posible eligiendo una cantidad concreta de baterías en orden. Para la parte 1 se usan dos baterías y para la parte 2 se usan doce.

`JoltageCalculator` suma la mejor descarga de todos los bancos. Tiene un método para parte 1 y otro método que recibe cuántas baterías se deben elegir.

### Paquete `input`

`BatteryBankParser` convierte cada línea de texto en un `BatteryBank`. También valida que cada carácter sea un dígito entre 1 y 9.

### `LobbyPuzzle`

`LobbyPuzzle` coordina el Día 3. Recibe las líneas de entrada, usa el parser y llama al calculador.

También tiene un `main` que lee:

```text
src/main/resources/day03/input.txt
```

### Patrones usados en Día 3

No he usado un patrón de diseño formal en Día 3 porque la regla sigue siendo bastante directa. Lo importante aquí era separar bien responsabilidades y no complicar el código.

No he usado Factory Method porque la creación del banco desde texto está en un parser simple. Tampoco he usado Iterator, Decorator, Adapter o Singleton porque no aportan claridad a este caso.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `BatteryBank` modela un banco y calcula su mejor descarga.
- `BatteryBankParser` convierte texto en bancos.
- `JoltageCalculator` suma resultados.
- `LobbyPuzzle` coordina el flujo del día.

### DRY

La regla para calcular la mejor descarga de un banco está solo en `BatteryBank`. La parte 1 y la parte 2 reutilizan el mismo método general cambiando solo la cantidad de baterías que se eligen.

### KISS

La solución elige la mejor secuencia respetando el orden. Para cada posición del resultado busca el mejor dígito posible dejando suficientes baterías para completar el resto. Es una forma simple de resolver tanto parte 1 como parte 2 sin duplicar algoritmos.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no había añadido comportamiento inventado.

### Encapsulación

`BatteryBank` guarda internamente la lista de potencias usando una copia. Desde fuera no se puede modificar esa lista.

### Bajo acoplamiento

`JoltageCalculator` trabaja con objetos `BatteryBank`, no con strings de entrada. El parseo queda separado de la lógica del cálculo.

### Alta cohesión

El paquete `battery` trata solo conceptos de baterías y descargas. El paquete `input` trata solo el parseo.

## Tests del Día 3

### `LobbyPuzzleTest`

Comprueba:

- El ejemplo oficial de parte 1, con resultado `357`.
- El ejemplo oficial de parte 2, con resultado `3121910778619`.

### `BatteryBankTest`

Comprueba:

- Que se encuentra la mejor descarga en un banco descendente como `9876`.
- Que se respeta el orden original de las baterías.
- Que se encuentra la mejor descarga usando doce baterías.
- Que el algoritmo de doce baterías respeta el orden.
- Que no se permite un banco con menos de dos baterías.
- Que no se permite pedir más baterías de las que tiene el banco.

### `JoltageCalculatorTest`

Comprueba que se suman correctamente las mejores descargas de varios bancos, tanto con la regla de parte 1 como con una cantidad configurable de baterías.

### `BatteryBankParserTest`

Comprueba:

- Parseo de una línea válida.
- Ignorar líneas vacías.
- Rechazo de `0`, porque el enunciado solo permite valores de 1 a 9.
- Rechazo de caracteres que no sean dígitos.

## Cómo defendería el Día 3

Yo explicaría que separé el problema en dos ideas: un banco sabe calcular su mejor descarga, y otro objeto suma las descargas de todos los bancos.

También diría que el parser está separado porque leer caracteres no es la misma responsabilidad que calcular la descarga máxima. Así puedo probar el modelo sin depender de la entrada.

Para la parte 2 no dupliqué el algoritmo. Generalicé el cálculo para elegir una cantidad concreta de baterías. Parte 1 llama a ese cálculo con 2 y parte 2 con 12. Esto respeta DRY y mantiene el diseño fácil de defender.

En este día no he metido patrones artificiales. La parte importante es que el diseño sigue siendo simple, cohesivo y fácil de probar.

## Día 4: Printing Department

### Qué pide el problema

El Día 4 trata de un mapa donde aparecen rollos de papel marcados con `@` y espacios vacíos marcados con `.`.

Los montacargas solo pueden acceder a un rollo de papel si hay menos de cuatro rollos de papel en las ocho posiciones adyacentes. Esas ocho posiciones incluyen horizontal, vertical y diagonal.

Con el ejemplo oficial:

```text
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
```

El resultado de la parte 1 es:

```text
13
```

### Parte 2

En la parte 2 los rollos accesibles se pueden retirar. Después de retirarlos, el mapa cambia, porque algunos rollos que antes tenían demasiados vecinos pueden quedar ahora con menos de cuatro.

La regla se repite por rondas:

1. Buscar todos los rollos accesibles en el estado actual.
2. Quitarlos del mapa.
3. Volver a calcular accesibilidad.
4. Parar cuando ya no haya rollos accesibles.

Con el ejemplo oficial, el total de rollos que se pueden retirar es:

```text
43
```

### Estructura del Día 4

El código del Día 4 está en `aoc.day04` y lo he dividido así:

```text
aoc.day04
├── PrintingDepartmentPuzzle.java
├── input
└── paper
```

### Paquete `paper`

`GridPosition` representa una posición dentro del mapa usando fila y columna.

`PaperRollGrid` representa la cuadrícula. Sabe si hay un rollo de papel en una posición, cuenta los rollos adyacentes y devuelve todas las posiciones donde hay rollos.

`ForkliftAccessSolver` aplica la regla del problema: un rollo es accesible si tiene como máximo tres rollos adyacentes, porque el enunciado dice "fewer than four". Para la parte 2 repite el proceso de retirada hasta que no queden más rollos accesibles.

### Paquete `input`

`PaperRollMapParser` convierte las líneas de texto en un `PaperRollGrid`. También valida que el mapa solo tenga `@` y `.`, y que la cuadrícula sea rectangular.

### `PrintingDepartmentPuzzle`

`PrintingDepartmentPuzzle` coordina el Día 4. Recibe las líneas de entrada, usa el parser y llama al solver.

También tiene un `main` que lee:

```text
src/main/resources/day04/input.txt
```

### Patrones usados en Día 4

No he usado un patrón de diseño formal en Día 4 porque la parte 1 se resuelve mejor con un modelo de cuadrícula claro y un solver pequeño.

No he usado Factory Method porque no hay creación compleja de objetos. Tampoco he usado Iterator, Decorator, Adapter o Singleton porque no aportan claridad a esta solución.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `GridPosition` representa coordenadas.
- `PaperRollGrid` modela la cuadrícula.
- `PaperRollMapParser` parsea la entrada.
- `ForkliftAccessSolver` cuenta los rollos accesibles.
- `PrintingDepartmentPuzzle` coordina el flujo del día.

### DRY

La lógica para contar vecinos está solo en `PaperRollGrid`. El solver reutiliza ese método tanto para parte 1 como para parte 2 y no repite cómo se recorren las ocho direcciones.

### KISS

La solución es directa: recorro los rollos de papel, cuento sus vecinos y aplico la regla de menos de cuatro.

### YAGNI

La parte 2 se implementó cuando tuve el enunciado. Antes de eso no añadí comportamiento inventado.

### Encapsulación

`PaperRollGrid` oculta cómo se guarda internamente el mapa. Desde fuera se pregunta por posiciones y vecinos, pero no se manipula directamente la lista de filas.

### Bajo acoplamiento

`ForkliftAccessSolver` no depende del texto original del input. Trabaja con `PaperRollGrid`, que ya es parte del dominio.

### Alta cohesión

El paquete `paper` contiene solo conceptos del mapa y los rollos. El paquete `input` solo se encarga de parsear.

## Tests del Día 4

### `PrintingDepartmentPuzzleTest`

Comprueba:

- El ejemplo oficial de parte 1, con resultado `13`.
- El ejemplo oficial de parte 2, con resultado `43`.

### `PaperRollMapParserTest`

Comprueba:

- Parseo de un mapa válido.
- Ignorar líneas vacías.
- Rechazo de caracteres que no sean `@` o `.`.
- Rechazo de mapas no rectangulares.

### `PaperRollGridTest`

Comprueba:

- Detección de rollos en posiciones concretas.
- Conteo de vecinos en las ocho direcciones.
- Conteo correcto en bordes del mapa.
- Devolver todas las posiciones con rollos.
- Crear una nueva cuadrícula sin algunos rollos, sin modificar la original.

### `ForkliftAccessSolverTest`

Comprueba:

- Que los rollos con menos de cuatro vecinos se cuentan como accesibles.
- Que un rollo con cuatro vecinos no se cuenta como accesible.
- Que las retiradas se acumulan correctamente durante varias rondas.

## Cómo defendería el Día 4

Yo explicaría que separé el mapa de la regla de acceso. La cuadrícula sabe contar vecinos, pero no decide por sí sola qué significa ser accesible. Esa regla está en `ForkliftAccessSolver`.

También diría que el parser está separado porque validar caracteres y construir la cuadrícula no debe mezclarse con la lógica del puzzle.

Para la parte 2 reutilicé la misma regla de accesibilidad, pero aplicada varias veces. En vez de mezclar la mutación directamente con el mapa original, `PaperRollGrid` puede crear una nueva cuadrícula sin los rollos retirados. Así el comportamiento es más fácil de probar.

En este día no he usado patrones artificiales. Lo más importante es que la solución tiene responsabilidades claras, bajo acoplamiento y tests que cubren tanto el ejemplo oficial como los casos de borde.

## Día 5: Cafeteria

### Qué pide el problema

El Día 5 trata de una base de datos de ingredientes. El archivo tiene dos partes separadas por una línea en blanco.

Primero aparecen rangos de IDs frescos:

```text
3-5
10-14
16-20
12-18
```

Después aparecen IDs de ingredientes disponibles:

```text
1
5
8
11
17
32
```

Los rangos son inclusivos. Por ejemplo, `3-5` contiene `3`, `4` y `5`. Además, los rangos pueden solaparse, pero un ingrediente cuenta una sola vez porque lo que se pregunta es si ese ingrediente disponible es fresco o no.

Con el ejemplo oficial, hay 3 ingredientes frescos disponibles.

### Parte 2

En la parte 2 ya no importan los ingredientes disponibles de la segunda sección. Lo que se pide es contar cuántos IDs distintos son considerados frescos por todos los rangos.

Con el ejemplo:

```text
3-5
10-14
16-20
12-18
```

Los IDs frescos son `3`, `4`, `5` y todos los IDs del `10` al `20`. En total son:

```text
14
```

Para resolverlo no enumero todos los IDs uno por uno. Ordeno los rangos por inicio, fusiono los que se solapan o quedan unidos, y después sumo el tamaño de los rangos resultantes.

### Estructura del Día 5

El código del Día 5 está en `aoc.day05` y lo he dividido así:

```text
aoc.day05
├── CafeteriaPuzzle.java
├── input
└── inventory
```

### Paquete `inventory`

`IngredientIdRange` representa un rango inclusivo de IDs frescos. Tiene el método `contains` para saber si un ID está dentro.

`InventoryDatabase` agrupa los rangos frescos y los IDs disponibles. Así el solver recibe un objeto del dominio en vez de trabajar directamente con líneas de texto.

`FreshIngredientCounter` cuenta cuántos IDs disponibles caen dentro de al menos un rango fresco para la parte 1. Para la parte 2 cuenta cuántos IDs distintos cubren los rangos frescos, fusionando solapamientos.

### Paquete `input`

`InventoryDatabaseParser` lee las dos secciones del archivo. Primero parsea los rangos, luego detecta la línea en blanco y después parsea los IDs disponibles.

### `CafeteriaPuzzle`

`CafeteriaPuzzle` coordina el Día 5. Recibe las líneas de entrada, usa el parser y llama al contador.

También tiene un `main` que lee:

```text
src/main/resources/day05/input.txt
```

### Patrones usados en Día 5

No he usado un patrón de diseño formal en Día 5 porque la parte 1 se entiende mejor con clases pequeñas y directas.

El parser crea objetos del dominio desde texto, pero no lo presentaría como Factory Method formal porque no hay una familia de objetos ni una fábrica clara. Es una responsabilidad de parsing.

Tampoco he usado Iterator, Decorator, Adapter o Singleton porque no aportan nada real al problema actual.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `IngredientIdRange` representa un rango.
- `InventoryDatabase` agrupa los datos del archivo ya parseados.
- `InventoryDatabaseParser` parsea la entrada.
- `FreshIngredientCounter` cuenta ingredientes frescos.
- `CafeteriaPuzzle` coordina el flujo del día.

### DRY

La lógica para comprobar si un ID está dentro de un rango está solo en `IngredientIdRange`.

### KISS

La solución de parte 1 es directa: para cada ingrediente disponible, compruebo si aparece en algún rango fresco. En parte 2 ordeno y fusiono rangos para contar la unión sin recorrer cada ID.

### YAGNI

La parte 2 se implementó cuando tuve el enunciado. Antes de eso no añadí comportamiento inventado.

### Encapsulación

`InventoryDatabase` guarda copias de las listas que recibe, así no se modifica su contenido desde fuera sin control.

### Bajo acoplamiento

`FreshIngredientCounter` no sabe nada del formato del archivo. Trabaja con `InventoryDatabase`, que ya es un objeto del dominio.

### Alta cohesión

El paquete `inventory` trata solo conceptos del inventario. El paquete `input` solo se encarga del parseo.

## Tests del Día 5

### `CafeteriaPuzzleTest`

Comprueba:

- El ejemplo oficial de parte 1, con resultado `3`.
- El ejemplo oficial de parte 2, con resultado `14`.

### `InventoryDatabaseParserTest`

Comprueba:

- Parseo de un rango.
- Parseo de las dos secciones separadas por una línea en blanco.
- Rechazo de una base de datos sin separador.
- Rechazo de rangos con formato inválido.

### `IngredientIdRangeTest`

Comprueba:

- IDs dentro y fuera de un rango.
- Rechazo de rangos donde el inicio es mayor que el final.

### `FreshIngredientCounterTest`

Comprueba:

- Conteo de ingredientes disponibles que están en algún rango fresco.
- Que un ingrediente se cuenta una sola vez aunque caiga en rangos solapados.
- Conteo total de IDs frescos fusionando rangos solapados.
- Conteo de rangos separados.
- Conteo de rangos adyacentes como una zona continua.

## Cómo defendería el Día 5

Yo explicaría que separé la estructura del archivo de la regla del problema. El parser convierte el texto en una base de datos del dominio, y después el contador trabaja con esa base de datos.

También destacaría que los rangos solapados no duplican ingredientes. En parte 1 el contador pregunta si un ID está en algún rango, no cuántos rangos lo contienen. En parte 2 se fusionan rangos para contar cada ID fresco una sola vez.

No he usado patrones artificiales. En este día lo más importante es SRP, bajo acoplamiento y tests claros sobre el ejemplo oficial y los casos de solapamiento.

## Día 6: Trash Compactor

### Qué pide el problema

El Día 6 trata de una hoja de ejercicios de matemáticas escrita de una forma rara. Los problemas están colocados uno al lado de otro, pero cada problema se lee en vertical.

Cada bloque tiene varios números y al final aparece el operador:

```text
123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +
```

En ese ejemplo hay cuatro problemas:

- `123 * 45 * 6`
- `328 + 64 + 98`
- `51 * 387 * 215`
- `64 + 23 + 314`

La respuesta que se pide es la suma de todos los resultados. En el ejemplo oficial, el total es:

```text
4277556
```

### Parte 2

En la parte 2 el problema cambia porque la matemática de los cefalópodos se lee de derecha a izquierda por columnas.

La entrada es exactamente la misma, pero ahora cada columna forma un número. Dentro de cada columna, el dígito más importante está arriba y el menos importante está abajo. Los problemas siguen separados por columnas vacías y el operador sigue estando en la parte inferior del bloque.

Con el ejemplo oficial, los problemas ya no son los mismos que en la parte 1. Ahora se leen así:

- `4 + 431 + 623`
- `175 * 581 * 32`
- `8 + 248 + 369`
- `356 * 24 * 1`

El resultado del ejemplo oficial para la parte 2 es:

```text
3263827
```

### Estructura del Día 6

El código del Día 6 está en `aoc.day06` y lo he dividido así:

```text
aoc.day06
├── TrashCompactorPuzzle.java
├── input
└── worksheet
```

### Paquete `worksheet`

`MathOperation` representa las operaciones posibles: suma y multiplicación. Cada operación sabe aplicarse sobre una lista de números.

`MathProblem` representa un problema concreto, con sus números y su operación.

`WorksheetSolver` suma los resultados de todos los problemas.

### Paquete `input`

`WorksheetParser` reconstruye los problemas desde las líneas horizontales de la hoja.

La parte importante del parser es detectar columnas vacías. Una columna completamente vacía separa un problema del siguiente. Después, cada bloque se lee de arriba abajo: los números se parsean y el símbolo final indica la operación.

Para la parte 2 reutilizo la misma separación por columnas vacías, pero cambio la forma de leer cada bloque. En vez de leer filas completas, leo las columnas de derecha a izquierda y formo los números con los dígitos que aparecen de arriba abajo.

### `TrashCompactorPuzzle`

`TrashCompactorPuzzle` coordina el Día 6. Recibe las líneas de entrada, usa el parser y llama al solver.

Tiene dos métodos separados:

- `solvePartOne`, que usa la lectura vertical normal de la parte 1.
- `solvePartTwo`, que usa la lectura por columnas de derecha a izquierda.

También tiene un `main` que lee:

```text
src/main/resources/day06/input.txt
```

### Patrones usados en Día 6

No he usado patrones complejos. Sí hay polimorfismo en `MathOperation`, porque cada operación implementa su forma de calcular el resultado.

`MathOperation.fromSymbol` funciona como un Factory Method sencillo, porque centraliza cómo se convierte `+` o `*` en una operación del dominio.

No he usado Iterator, Decorator, Adapter o Singleton porque no aportaban claridad a este puzzle.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `WorksheetParser` reconstruye problemas desde texto.
- `MathProblem` representa un problema.
- `MathOperation` representa la operación.
- `WorksheetSolver` suma resultados.
- `TrashCompactorPuzzle` coordina el flujo del día.

### DRY

La lógica de suma y multiplicación está en `MathOperation`, no repetida en el parser ni en el solver.

La parte 1 y la parte 2 también reutilizan `MathProblem`, `MathOperation` y `WorksheetSolver`. Lo que cambia es solo la forma de reconstruir los problemas desde la hoja.

### KISS

La solución separa problemas detectando columnas vacías, que es exactamente la regla del enunciado.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no había inventado una lectura alternativa.

### Encapsulación

`MathProblem` guarda una copia de la lista de números, así no se modifica desde fuera.

### Bajo acoplamiento

`WorksheetSolver` no sabe nada del formato visual de la hoja. Trabaja con objetos `MathProblem`.

### Alta cohesión

El paquete `worksheet` trata solo conceptos de la hoja de matemáticas. El paquete `input` se centra solo en parsear.

## Tests del Día 6

### `TrashCompactorPuzzleTest`

Comprueba:

- El ejemplo oficial completo de parte 1, con resultado `4277556`.
- El ejemplo oficial completo de parte 2, con resultado `3263827`.

### `WorksheetParserTest`

Comprueba:

- Separar problemas usando columnas vacías.
- Parsear los resultados del ejemplo oficial.
- Parsear los resultados del ejemplo oficial usando la lectura de derecha a izquierda.
- Formar números desde columnas en la parte 2.
- Rechazar un problema sin operación.

### `MathProblemTest`

Comprueba:

- Resolver sumas.
- Resolver multiplicaciones.
- Rechazar problemas sin números.

### `WorksheetSolverTest`

Comprueba que se suman correctamente los resultados de varios problemas.

## Cómo defendería el Día 6

Yo explicaría que la dificultad principal no es la operación matemática, sino leer bien la hoja. Por eso separé el parser del solver.

El parser entiende el formato visual y convierte la entrada en problemas del dominio. Después el solver ya no trabaja con columnas ni espacios, solo con `MathProblem`.

Para la parte 2 explicaría que no cambié el solver matemático, porque sumar y multiplicar problemas ya estaba resuelto. Lo que cambió fue la interpretación del texto. Por eso añadí otro método de parseo en `WorksheetParser`.

También destacaría el uso de `MathOperation`: evita condicionales repartidos por el código y deja cada operación en un sitio claro.

## Día 7: Laboratories

### Qué pide el problema

El Día 7 trata de un laboratorio con un manifold de taquiones. La entrada es un diagrama con tres tipos de celdas:

- `S`: punto por donde entra el haz.
- `.`: espacio vacío.
- `^`: splitter, que divide un haz en dos.

El haz empieza en `S` y siempre baja. Si pasa por espacios vacíos, sigue bajando en la misma columna. Si encuentra un splitter `^`, ese haz se detiene, se cuenta una división y nacen dos haces nuevos: uno a la izquierda y otro a la derecha del splitter.

Con el ejemplo oficial, el haz se divide:

```text
21
```

### Parte 1

En la parte 1 hay que contar cuántas veces se divide el haz antes de que todos los haces salgan del mapa o queden procesados.

Una parte importante del problema es que dos haces pueden acabar en la misma posición. En ese caso no tiene sentido duplicarlos, porque desde esa posición se comportan igual. Por eso uso un `Set` de columnas activas en cada fila.

### Parte 2

En la parte 2 el manifold es cuántico. Ya no se pregunta cuántos splitters se activan, sino cuántas líneas temporales existen al final.

La idea es distinta: si una partícula llega a un splitter, el tiempo se divide en dos. En una línea temporal la partícula va a la izquierda y en otra va a la derecha.

Con el ejemplo oficial, el resultado de la parte 2 es:

```text
40
```

Para esta parte no puedo usar solo un `Set`, porque perdería información. Si dos caminos llegan a la misma columna, en parte 1 eso se puede juntar, pero en parte 2 siguen siendo líneas temporales distintas. Por eso uso un mapa donde la clave es la columna y el valor es cuántas líneas temporales llegan ahí.

También uso `BigInteger` para el resultado de parte 2. Lo hago porque las líneas temporales pueden crecer muy rápido cuando hay muchos splitters, y así evito depender del límite de `long`.

### Estructura del Día 7

El código del Día 7 está en `aoc.day07` y lo he dividido así:

```text
aoc.day07
├── LaboratoriesPuzzle.java
├── input
└── manifold
```

### Paquete `manifold`

`GridPosition` representa una posición del diagrama con fila y columna.

`TachyonManifold` representa el mapa del manifold. Guarda las celdas, la posición inicial y ofrece métodos para saber si una posición está dentro del mapa o si contiene un splitter.

`TachyonSplitterCounter` contiene la regla principal del puzzle. Recorre el mapa por filas, mantiene las columnas donde hay haces activos y cuenta cada vez que un haz encuentra un splitter.

`QuantumTimelineCounter` contiene la regla de la parte 2. Recorre el mismo mapa, pero en vez de guardar solo columnas activas, guarda cuántas líneas temporales llegan a cada columna.

### Paquete `input`

`TachyonManifoldParser` convierte las líneas del archivo en un `TachyonManifold`. También valida que el mapa no esté vacío, que sea rectangular, que solo tenga caracteres permitidos y que exista exactamente un `S`.

### `LaboratoriesPuzzle`

`LaboratoriesPuzzle` coordina el Día 7. Recibe las líneas de entrada, usa el parser y después llama al contador correspondiente.

Tiene dos métodos separados:

- `solvePartOne`, que cuenta divisiones clásicas del haz.
- `solvePartTwo`, que cuenta líneas temporales cuánticas.

También tiene un `main` que lee:

```text
src/main/resources/day07/input.txt
```

### Patrones usados en Día 7

No he usado un patrón de diseño formal en este día porque el problema se entiende mejor con un modelo de mapa y un solver directo.

No he usado Factory Method porque la creación del manifold desde texto está en un parser simple. Tampoco he usado Decorator, Adapter, Iterator o Singleton porque no aportaban claridad para esta parte.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `TachyonManifoldParser` parsea y valida la entrada.
- `TachyonManifold` representa el mapa.
- `GridPosition` representa coordenadas.
- `TachyonSplitterCounter` aplica la regla de propagación del haz.
- `QuantumTimelineCounter` aplica la regla de líneas temporales de la parte 2.
- `LaboratoriesPuzzle` coordina el flujo del día.

### DRY

La comprobación de si hay splitter está en `TachyonManifold`, no repetida en los contadores. Los dos solvers preguntan al dominio y no acceden directamente a la estructura interna del mapa.

Parte 1 y parte 2 reutilizan el mismo parser y el mismo modelo del manifold. Lo único que cambia es la regla de conteo.

### KISS

La solución recorre el mapa de arriba abajo, igual que se mueve el haz. En cada fila solo mantiene las columnas activas, así que el algoritmo es fácil de explicar.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no añadí comportamiento futuro inventado.

### Encapsulación

`TachyonManifold` guarda una copia de las celdas. Desde fuera no se modifica directamente la lista interna.

### Bajo acoplamiento

`TachyonSplitterCounter` y `QuantumTimelineCounter` trabajan con un `TachyonManifold`, no con líneas de texto. Así la lógica del haz no depende del formato exacto del archivo.

### Alta cohesión

El paquete `manifold` contiene solo conceptos del mapa y del haz. El paquete `input` se centra solo en convertir texto en dominio.

## Tests del Día 7

### `LaboratoriesPuzzleTest`

Comprueba:

- El ejemplo oficial completo de parte 1, con resultado `21`.
- El ejemplo oficial completo de parte 2, con resultado `40`.

### `TachyonManifoldParserTest`

Comprueba:

- Parseo de la posición inicial y los splitters.
- Rechazo de un diagrama sin `S`.
- Rechazo de más de un `S`.
- Rechazo de un mapa no rectangular.
- Rechazo de caracteres no permitidos.

### `TachyonSplitterCounterTest`

Comprueba:

- Un mapa sin splitters alcanzados.
- Un mapa donde el haz se divide una vez.
- Un caso donde varios haces se combinan en la misma columna.

### `QuantumTimelineCounterTest`

Comprueba:

- Que sin splitters sigue existiendo una sola línea temporal.
- Que un splitter crea dos líneas temporales.
- Que varias líneas temporales que llegan a la misma columna se suman, no se eliminan.

## Cómo defendería el Día 7

Yo explicaría que separé el dibujo del manifold de la regla de propagación. El parser solo entiende texto y valida el formato. El manifold representa el mapa. El contador ya trabaja con ese modelo y no necesita saber cómo venía escrito el archivo.

También destacaría que uso un `Set` para las columnas activas porque, si dos haces llegan al mismo sitio, desde ahí tienen el mismo comportamiento. Eso evita duplicar trabajo y refleja bien lo que dice el enunciado cuando varios haces acaban en una misma zona.

Para la parte 2 explicaría que cambié el `Set` por un `Map`, porque ahora no solo importa dónde hay partícula, sino cuántas líneas temporales llegan a ese sitio. Esa diferencia justifica tener dos clases distintas: `TachyonSplitterCounter` para parte 1 y `QuantumTimelineCounter` para parte 2.

No he metido patrones artificiales. En este día lo más importante es SRP, bajo acoplamiento y un algoritmo sencillo que se puede explicar paso a paso.

## Día 8: Playground

### Qué pide el problema

El Día 8 trata de cajas de conexión suspendidas en un espacio 3D. Cada línea del input contiene una posición con coordenadas `X,Y,Z`, por ejemplo:

```text
162,817,812
57,618,57
906,360,560
```

La idea es conectar las parejas de cajas que estén más cerca entre sí. Cuando dos cajas se conectan, pasan a formar parte del mismo circuito, y la electricidad puede llegar a todas las cajas de ese circuito.

En la parte 1 hay que tomar las 1000 parejas de cajas más cercanas. Después de hacer esas conexiones, se buscan los tamaños de los tres circuitos más grandes y se multiplican.

Con el ejemplo oficial, usando las 10 conexiones más cortas, los tres circuitos más grandes tienen tamaños `5`, `4` y `2`, y el resultado es:

```text
40
```

### Parte 2

En la parte 2 hay que seguir conectando parejas por orden de cercanía hasta que todas las cajas formen un único circuito.

La respuesta no es el tamaño del circuito final, porque al final contiene todas las cajas. Lo que pide el enunciado es mirar cuál fue la última conexión necesaria para unirlo todo y multiplicar las coordenadas `X` de esas dos cajas.

Con el ejemplo oficial, la conexión final necesaria es entre:

```text
216,146,977
117,168,530
```

El resultado del ejemplo es:

```text
25272
```

### Estructura del Día 8

El código del Día 8 está en `aoc.day08` y lo he dividido así:

```text
aoc.day08
├── PlaygroundPuzzle.java
├── circuit
└── input
```

### Paquete `circuit`

`JunctionBox` representa una caja de conexión con coordenadas `x`, `y` y `z`. También calcula la distancia al cuadrado con otra caja.

Uso distancia al cuadrado porque para ordenar distancias no hace falta calcular la raíz cuadrada. Si una distancia al cuadrado es menor que otra, la distancia real también lo es. Así mantengo el cálculo más simple.

`JunctionConnection` representa una posible conexión entre dos cajas. Guarda los índices de las cajas y la distancia al cuadrado entre ellas.

`CircuitNetwork` representa los circuitos que se van formando. Internamente usa una estructura de unión de conjuntos para juntar circuitos y consultar sus tamaños.

`CircuitSizeCalculator` genera todas las parejas posibles, las ordena por distancia y aplica las conexiones más cortas. En parte 1 obtiene los tres circuitos más grandes y multiplica sus tamaños. En parte 2 sigue conectando hasta que solo queda un circuito y devuelve el producto de las coordenadas `X` de la última conexión útil.

### Paquete `input`

`JunctionBoxParser` convierte las líneas del archivo en objetos `JunctionBox`. También valida que cada línea tenga tres coordenadas y que sean números.

### `PlaygroundPuzzle`

`PlaygroundPuzzle` coordina el Día 8. Recibe las líneas de entrada, usa el parser y llama al calculador.

Tiene dos métodos separados:

- `solvePartOne`, que aplica las 1000 conexiones más cortas.
- `solvePartTwo`, que conecta hasta formar un único circuito.

También tiene un `main` que lee:

```text
src/main/resources/day08/input.txt
```

### Patrones usados en Día 8

No he usado un patrón formal porque la solución se entiende mejor con un modelo de dominio y una estructura de unión de conjuntos.

No he usado Factory Method, Decorator, Adapter, Iterator o Singleton porque no aportaban claridad a este puzzle. En este caso era más importante mantener el algoritmo claro.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `JunctionBox` representa una posición 3D.
- `JunctionBoxParser` parsea la entrada.
- `CircuitNetwork` gestiona la unión de circuitos.
- `CircuitSizeCalculator` decide qué conexiones aplicar y calcula el resultado.
- `PlaygroundPuzzle` coordina el flujo del día.

### DRY

La distancia entre cajas se calcula solo en `JunctionBox`. La lógica de unión de circuitos está solo en `CircuitNetwork`.

Parte 1 y parte 2 reutilizan el mismo parser, el mismo modelo de caja y la misma red de circuitos. Lo que cambia es cuándo se detiene el proceso y qué valor se devuelve.

### KISS

La solución sigue directamente el enunciado: generar parejas, ordenarlas por distancia y aplicar conexiones. En parte 1 se detiene tras 1000 parejas; en parte 2 se detiene cuando todo queda en un único circuito.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no añadí comportamiento futuro inventado.

### Encapsulación

`CircuitNetwork` oculta cómo se representan internamente los circuitos. Desde fuera solo se le pide conectar dos cajas y devolver los tamaños finales.

### Bajo acoplamiento

`CircuitSizeCalculator` trabaja con `JunctionBox`, no con líneas de texto. El parser queda separado del cálculo.

### Alta cohesión

El paquete `circuit` contiene solo conceptos de cajas, conexiones y circuitos. El paquete `input` se centra solo en parsear.

## Tests del Día 8

### `PlaygroundPuzzleTest`

Comprueba:

- El ejemplo oficial de la parte 1 usando 10 conexiones, con resultado `40`.
- El ejemplo oficial de la parte 2, con resultado `25272`.

### `JunctionBoxParserTest`

Comprueba:

- Parseo de coordenadas.
- Ignorar líneas vacías.
- Rechazo de input vacío.
- Rechazo de líneas sin tres coordenadas.
- Rechazo de coordenadas no numéricas.

### `JunctionBoxTest`

Comprueba el cálculo de distancia al cuadrado entre dos cajas.

### `CircuitSizeCalculatorTest`

Comprueba:

- Que se conectan las parejas más cercanas.
- Que una conexión dentro del mismo circuito no cambia los tamaños.
- Que se rechaza calcular el producto si hay menos de tres cajas.
- Que se devuelve el producto de las coordenadas `X` de la conexión que deja un solo circuito.
- Que parte 2 rechaza una lista con menos de dos cajas.

## Cómo defendería el Día 8

Yo explicaría que el problema se parece a ir formando componentes conectados. Por eso separé las cajas, las conexiones y la red de circuitos.

La parte clave es `CircuitNetwork`: cuando conecto dos cajas, no necesito guardar todas las conexiones explícitamente, sino saber qué cajas pertenecen al mismo circuito. Para eso uso unión de conjuntos, que es una forma sencilla y eficiente de mantener grupos.

También explicaría que uso distancia al cuadrado para evitar cálculos innecesarios con raíces cuadradas. El orden de cercanía se mantiene igual y el código queda más directo.

Para la parte 2 diría que reutilicé la misma lista ordenada de conexiones. La diferencia es que ahora necesito saber si una conexión realmente une dos circuitos distintos. Por eso `CircuitNetwork.connect` devuelve si la unión cambió la red. Cuando esa unión deja un solo circuito, guardo esa conexión y multiplico sus coordenadas `X`.

No he forzado patrones de diseño. En este día la defensa se centra más en SRP, encapsulación, bajo acoplamiento y en elegir una estructura de datos adecuada.

## Día 9: Movie Theater

### Qué pide el problema

El Día 9 trata de un suelo de cine con baldosas rojas. La entrada contiene las posiciones de esas baldosas en una cuadrícula, una por línea:

```text
7,1
11,1
11,7
```

Hay que elegir dos baldosas rojas como esquinas opuestas de un rectángulo y encontrar el área más grande posible.

Un detalle importante es que el área se cuenta de forma inclusiva. Por ejemplo, entre `2,5` y `9,7` la anchura es `8`, porque se cuentan las columnas `2` hasta `9`, y la altura es `3`, porque se cuentan las filas `5` hasta `7`. Por eso el área es:

```text
24
```

Con el ejemplo oficial, el área máxima es:

```text
50
```

### Parte 2

En la parte 2 ya no sirve cualquier rectángulo. Los elfos solo pueden cambiar baldosas rojas o verdes.

Las baldosas rojas de la entrada forman un camino cerrado. Cada baldosa roja está conectada con la anterior y la siguiente mediante una línea recta de baldosas verdes. Además, todo lo que queda dentro de ese bucle también se considera verde.

El rectángulo sigue necesitando dos baldosas rojas como esquinas opuestas, pero ahora todas las baldosas que ocupa el rectángulo deben ser rojas o verdes. En el ejemplo oficial, el área máxima baja de `50` a:

```text
24
```

### Estructura del Día 9

El código del Día 9 está en `aoc.day09` y lo he dividido así:

```text
aoc.day09
├── MovieTheaterPuzzle.java
├── input
└── theater
```

### Paquete `theater`

`RedTile` representa una baldosa roja con coordenadas `x` e `y`. También calcula el área del rectángulo que forma con otra baldosa roja.

`LargestRectangleFinder` compara pares de baldosas rojas. En parte 1 se queda con el área más grande sin más restricciones. En parte 2 solo acepta el rectángulo si está completamente dentro de la zona roja o verde.

`RedGreenTileArea` representa la zona permitida de la parte 2. Construye el borde del camino cerrado y después hace un relleno desde fuera para saber qué baldosas quedan dentro del bucle. Para que funcione con coordenadas grandes, no rellena cada baldosa una por una, sino que usa coordenadas comprimidas y trabaja con bloques.

### Paquete `input`

`RedTileParser` convierte las líneas del archivo en objetos `RedTile`. También valida que cada línea tenga dos coordenadas y que ambas sean números.

### `MovieTheaterPuzzle`

`MovieTheaterPuzzle` coordina el Día 9. Recibe las líneas de entrada, usa el parser y llama al buscador del rectángulo máximo.

Tiene dos métodos separados:

- `solvePartOne`, que busca el mayor rectángulo usando cualquier par de baldosas rojas.
- `solvePartTwo`, que busca el mayor rectángulo que solo usa baldosas rojas o verdes.

También tiene un `main` que lee:

```text
src/main/resources/day09/input.txt
```

### Patrones usados en Día 9

No he usado patrones de diseño formales en este día. El problema es directo y se entiende mejor con un modelo pequeño y un solver claro.

No he usado Factory Method, Decorator, Adapter, Iterator o Singleton porque no aportaban claridad. En este caso preferí aplicar bien SRP, KISS y nombres claros.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `RedTile` representa una baldosa roja y calcula áreas con otra baldosa.
- `RedGreenTileArea` representa la zona roja y verde disponible.
- `RedTileParser` parsea la entrada.
- `LargestRectangleFinder` busca el área máxima.
- `MovieTheaterPuzzle` coordina el flujo del día.

### DRY

La fórmula del área inclusiva está solo en `RedTile`. Así no se repite en el solver ni en los tests.

La parte 1 y la parte 2 reutilizan el mismo parser y el mismo modelo `RedTile`. Lo que cambia es la validación extra de la zona roja/verde.

### KISS

La solución compara todos los pares de baldosas rojas. Para parte 1 basta con calcular áreas. Para parte 2 añado una comprobación clara: el rectángulo debe estar contenido dentro de la zona roja/verde.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no añadí comportamiento inventado.

### Encapsulación

`RedTile` encapsula cómo se calcula el área con otra baldosa. El solver no necesita conocer los detalles de anchura, altura y suma inclusiva.

`RedGreenTileArea` encapsula cómo se decide si una baldosa pertenece a la zona válida. El solver no necesita saber los detalles del relleno exterior.

### Bajo acoplamiento

`LargestRectangleFinder` trabaja con objetos `RedTile`, no con líneas de texto. El parseo queda separado del cálculo.

### Alta cohesión

El paquete `theater` contiene solo conceptos del cine y sus baldosas. El paquete `input` solo se encarga de convertir texto en dominio.

## Tests del Día 9

### `MovieTheaterPuzzleTest`

Comprueba:

- El ejemplo oficial completo de parte 1, con resultado `50`.
- El ejemplo oficial completo de parte 2, con resultado `24`.

### `RedTileParserTest`

Comprueba:

- Parseo de coordenadas.
- Ignorar líneas vacías.
- Rechazo de input vacío.
- Rechazo de líneas sin dos coordenadas.
- Rechazo de coordenadas no numéricas.

### `RedTileTest`

Comprueba que el área del rectángulo se calcula de forma inclusiva.

### `LargestRectangleFinderTest`

Comprueba:

- Que se encuentra el área más grande entre varios pares.
- Que funcionan rectángulos finos de una sola fila.
- Que se encuentra el área más grande dentro de la zona roja/verde.
- Que se rechaza una lista con menos de dos baldosas rojas.

## Cómo defendería el Día 9

Yo explicaría que primero separé la lectura de la entrada del cálculo. El parser convierte texto en baldosas rojas y el solver ya trabaja con objetos del dominio.

También destacaría que la regla importante está en `RedTile`: el área se calcula con `+1` en anchura y altura porque las esquinas también forman parte del rectángulo. Eso evita errores típicos de contar solo la diferencia entre coordenadas.

Para la parte 2 explicaría que construyo la zona válida a partir del camino cerrado. Primero marco el borde que une las baldosas rojas y luego relleno desde fuera; lo que no es exterior queda dentro del bucle y por tanto es rojo o verde. Como el input real tiene coordenadas grandes, uso compresión de coordenadas para no recorrer una cuadrícula enorme.

No he añadido patrones innecesarios. En este día la solución se defiende por su claridad: modelo pequeño, parser separado, fórmula encapsulada y tests sobre el ejemplo oficial y casos de borde.

## Día 10: Factory

### Qué pide el problema

El Día 10 trata de máquinas de una fábrica. Cada línea del input describe una máquina con tres partes:

```text
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
```

La parte entre corchetes es el estado objetivo de las luces. `.` significa apagada y `#` significa encendida.

Las partes entre paréntesis son botones. Cada botón indica qué luces cambia cuando se pulsa. Por ejemplo, `(0,2)` cambia la primera y la tercera luz.

La parte entre llaves son requisitos de voltaje, pero en la parte 1 se ignoran porque el enunciado dice que todavía no importan.

La pregunta es cuántas pulsaciones mínimas hacen falta en total para configurar todas las máquinas.

Con el ejemplo oficial, las máquinas necesitan `2`, `3` y `2` pulsaciones, así que el resultado es:

```text
7
```

### Parte 2

En la parte 2 ya no importan las luces. Ahora los botones aumentan contadores de voltaje.

Los valores entre llaves sí se usan. Por ejemplo, `{3,5,4,7}` significa que la máquina tiene cuatro contadores y que hay que dejarlos exactamente en `3`, `5`, `4` y `7`.

Un botón como `(1,3)` aumenta en `1` el segundo y el cuarto contador cada vez que se pulsa. La pregunta vuelve a ser el mínimo total de pulsaciones, pero ahora para cumplir los requisitos de voltaje.

Con el ejemplo oficial, las tres máquinas necesitan `10`, `12` y `11` pulsaciones, así que el resultado de la parte 2 es:

```text
33
```

### Estructura del Día 10

El código del Día 10 está en `aoc.day10` y lo he dividido así:

```text
aoc.day10
├── FactoryPuzzle.java
├── factory
└── input
```

### Paquete `factory`

`ButtonWiring` representa un botón mediante una máscara de bits. Cada bit indica una luz que ese botón cambia.

`FactoryMachine` representa una máquina. Guarda cuántas luces tiene, cuál es la máscara objetivo, qué botones tiene disponibles y cuáles son los requisitos de voltaje.

`MachineInitializer` calcula el mínimo de pulsaciones. Para parte 1 usa BFS sobre estados de luces. Para parte 2 resuelve el sistema de contadores como un sistema lineal con restricciones enteras no negativas.

### Paquete `input`

`FactoryManualParser` convierte cada línea del manual en un `FactoryMachine`. Lee el diagrama de luces, extrae los botones, guarda los requisitos de voltaje y valida que los índices existan dentro del tamaño de la máquina.

### `FactoryPuzzle`

`FactoryPuzzle` coordina el Día 10. Recibe las líneas de entrada, usa el parser y suma el mínimo de pulsaciones de todas las máquinas.

Tiene dos métodos separados:

- `solvePartOne`, que configura las luces.
- `solvePartTwo`, que configura los contadores de voltaje.

También tiene un `main` que lee:

```text
src/main/resources/day10/input.txt
```

### Decisión de algoritmo

Para la parte 1, pulsar un botón dos veces no ayuda porque deja las luces como estaban y solo suma dos pulsaciones. Por eso cada botón se puede tratar como si se usara una vez o ninguna.

Uso máscaras de bits porque las luces solo tienen dos estados: encendida o apagada. Si un botón cambia varias luces, su efecto se aplica con XOR. Esto encaja bien con el problema y evita manejar listas de booleanos por todo el código.

Para la parte 2 uso otra idea. Cada botón suma `1` a algunos contadores, así que el problema se puede ver como un sistema de ecuaciones: cuántas veces pulso cada botón para llegar exactamente a los requisitos. Reduzco el sistema lineal, enumero las pocas variables libres que quedan y compruebo soluciones enteras no negativas.

### Patrones usados en Día 10

No he usado patrones de diseño formales. El problema se resuelve mejor con un modelo pequeño y una búsqueda clara.

No he usado Factory Method, Adapter, Decorator, Iterator o Singleton porque no aportaban claridad a esta parte.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `FactoryManualParser` parsea el texto del manual.
- `FactoryMachine` representa una máquina.
- `ButtonWiring` representa el efecto de un botón.
- `MachineInitializer` calcula el mínimo de pulsaciones.
- `FactoryPuzzle` coordina el flujo del día.

### DRY

La conversión de luces, botones y voltajes está en el parser. La búsqueda del mínimo está solo en `MachineInitializer`.

Parte 1 y parte 2 reutilizan `FactoryMachine` y `ButtonWiring`. Lo que cambia es la interpretación de los botones: en parte 1 alternan luces y en parte 2 suman voltaje.

### KISS

La solución de parte 1 mantiene una idea directa: partir del estado con todas las luces apagadas y buscar el camino más corto hasta la máscara objetivo. En parte 2 uso álgebra lineal porque probar pulsaciones una por una sería demasiado lento con requisitos grandes.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso los requisitos de voltaje se ignoraban porque el propio enunciado de parte 1 decía que no importaban.

### Encapsulación

`FactoryMachine` guarda sus botones como una copia de la lista recibida. El resto del código no modifica su estado interno.

### Bajo acoplamiento

`MachineInitializer` trabaja con `FactoryMachine`, no con líneas de texto. El formato del manual queda aislado en el parser.

### Alta cohesión

El paquete `factory` contiene solo conceptos de máquinas, botones y pulsaciones. El paquete `input` solo se encarga de parsear.

## Tests del Día 10

### `FactoryPuzzleTest`

Comprueba:

- El ejemplo oficial completo de parte 1, con resultado `7`.
- El ejemplo oficial completo de parte 2, con resultado `33`.

### `FactoryManualParserTest`

Comprueba:

- Parseo del diagrama de luces y los botones.
- Ignorar líneas vacías.
- Rechazo de input vacío.
- Rechazo de líneas sin diagrama.
- Rechazo de botones que apuntan a luces fuera del diagrama.
- Rechazo de requisitos de voltaje con una cantidad distinta al diagrama.

### `MachineInitializerTest`

Comprueba:

- Que si el objetivo ya es todo apagado, el mínimo es `0`.
- Que se encuentra el mínimo de pulsaciones en una máquina concreta.
- Que no se permite una máquina sin botones.
- Que se encuentra el mínimo de pulsaciones de voltaje.
- Que si los requisitos de voltaje ya son cero, el mínimo es `0`.

## Cómo defendería el Día 10

Yo explicaría que el problema tiene luces con dos estados, así que modelarlo con bits es natural. Una luz encendida es un bit a `1` y una apagada es un bit a `0`.

También diría que pulsar un botón equivale a hacer XOR con su máscara. Eso expresa directamente la idea de alternar luces: si una luz estaba apagada se enciende, y si estaba encendida se apaga.

Para la parte 2 explicaría que no reutilizo BFS porque los voltajes pueden ser grandes. En su lugar, trato cada botón como una variable: si pulso el botón `x` veces, suma `x` a los contadores que afecta. Así puedo reducir el sistema y buscar solo soluciones enteras válidas.

Separé el parser del cálculo porque leer líneas como `[.##.] (3) (0,2)` no es la misma responsabilidad que buscar el mínimo de pulsaciones. Así puedo probar el algoritmo con objetos ya construidos y probar el parser por separado.

## Día 11: Reactor

### Qué pide el problema

El Día 11 trata de una red de dispositivos conectados por salidas. Cada línea indica un dispositivo y a qué otros dispositivos puede enviar datos:

```text
you: bbb ccc
bbb: ddd eee
eee: out
```

Los datos solo avanzan siguiendo las salidas. La pregunta de la parte 1 es cuántos caminos distintos llevan desde `you` hasta `out`.

Con el ejemplo oficial, hay:

```text
5
```

### Parte 2

En la parte 2 cambia el punto de inicio y aparece una condición extra. Ahora hay que contar los caminos desde `svr` hasta `out`, pero solo cuentan los caminos que pasan por `dac` y por `fft`.

El orden no importa: puede aparecer primero `dac` y luego `fft`, o al revés.

Con el ejemplo oficial de la parte 2, el resultado es:

```text
2
```

### Estructura del Día 11

El código del Día 11 está en `aoc.day11` y lo he dividido así:

```text
aoc.day11
├── ReactorPuzzle.java
├── input
└── reactor
```

### Paquete `reactor`

`DeviceNetwork` representa la red dirigida de dispositivos. Guarda para cada dispositivo la lista de salidas a las que puede enviar datos.

`PathCounter` cuenta los caminos desde un dispositivo inicial hasta uno final. Usa DFS con memoización para no recalcular los caminos de un mismo dispositivo varias veces.

Para la parte 2, `PathCounter` también guarda en el estado si ya se visitó `dac` y si ya se visitó `fft`. Así puede contar solo los caminos válidos sin tener que guardar la ruta completa.

### Paquete `input`

`DeviceNetworkParser` convierte líneas como `bbb: ddd eee` en un `DeviceNetwork`. También valida entradas vacías, líneas sin formato correcto y dispositivos duplicados.

### `ReactorPuzzle`

`ReactorPuzzle` coordina el Día 11. Recibe las líneas de entrada, usa el parser y llama al contador de caminos.

Tiene dos métodos separados:

- `solvePartOne`, que cuenta caminos desde `you` hasta `out`.
- `solvePartTwo`, que cuenta caminos desde `svr` hasta `out` pasando por `dac` y `fft`.

También tiene un `main` que lee:

```text
src/main/resources/day11/input.txt
```

### Patrones usados en Día 11

No he usado patrones de diseño formales. El problema encaja mejor con una estructura de grafo sencilla y un contador separado.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `DeviceNetworkParser` parsea la entrada.
- `DeviceNetwork` representa el grafo dirigido.
- `PathCounter` cuenta caminos simples y caminos con dispositivos obligatorios.
- `ReactorPuzzle` coordina el flujo del día.

### DRY

La lógica de recorrer caminos está solo en `PathCounter`. El parser no sabe contar caminos y el puzzle no sabe cómo se recorren internamente.

Parte 1 y parte 2 reutilizan el mismo parser y el mismo `DeviceNetwork`. Lo que cambia es el estado que usa el contador.

### KISS

La solución usa DFS con memoización, que es una forma directa de contar caminos en una red dirigida.

### YAGNI

La parte 2 se implementó cuando ya tenía el enunciado. Antes de eso no añadí comportamiento inventado.

### Encapsulación

`DeviceNetwork` oculta el mapa interno de salidas. Desde fuera solo se pregunta por las salidas de un dispositivo.

### Bajo acoplamiento

`PathCounter` trabaja con `DeviceNetwork`, no con líneas de texto. El formato del archivo queda aislado en el parser.

### Alta cohesión

El paquete `reactor` contiene solo conceptos de la red del reactor. El paquete `input` solo parsea texto.

## Tests del Día 11

### `ReactorPuzzleTest`

Comprueba:

- El ejemplo oficial completo de parte 1, con resultado `5`.
- El ejemplo oficial completo de parte 2, con resultado `2`.

### `DeviceNetworkParserTest`

Comprueba:

- Parseo de salidas de dispositivos.
- Ignorar líneas vacías.
- Rechazo de input vacío.
- Rechazo de líneas sin un único separador `:`.
- Rechazo de dispositivos duplicados.

### `PathCounterTest`

Comprueba:

- Conteo de un único camino.
- Conteo de caminos con bifurcaciones.
- Conteo de caminos que visitan los dos dispositivos obligatorios.
- Conteo cuando los dispositivos obligatorios aparecen en el otro orden.
- Ignorar caminos a los que les falta uno de los dispositivos obligatorios.
- Rechazo de dispositivo inicial inexistente.
- Detección de ciclos para evitar recursión infinita.

## Cómo defendería el Día 11

Yo explicaría que el problema es un grafo dirigido: cada dispositivo es un nodo y cada salida es una arista.

Separé el parser del contador porque leer el formato `aaa: bbb ccc` no es lo mismo que recorrer la red. El contador usa memoización porque si varios caminos llegan al mismo dispositivo, la cantidad de caminos desde ahí hasta `out` es siempre la misma y no hace falta recalcularla.

Para la parte 2 explicaría que no enumero las rutas completas. Solo guardo tres cosas: en qué dispositivo estoy, si ya pasé por `dac` y si ya pasé por `fft`. Con eso se puede contar de forma eficiente y sigue siendo fácil de defender.

## Día 12: Christmas Tree Farm

### Qué pide el problema

El Día 12 trata de encajar regalos debajo de árboles de Navidad. La entrada tiene dos secciones.

Primero aparecen las formas de los regalos. Cada forma tiene un índice y un dibujo con `#` y `.`:

```text
4:
###
#..
###
```

Los `#` son las partes reales del regalo. Los `.` solo son huecos del dibujo y no bloquean a otros regalos.

Después aparecen las regiones debajo de los árboles. Cada línea indica el tamaño de la región y cuántos regalos de cada forma hay que colocar:

```text
4x4: 0 0 0 0 2 0
```

Esto significa que la región mide 4 de ancho y 4 de largo, y que hay que colocar dos regalos de la forma con índice 4.

Los regalos se pueden rotar y girar, pero siempre tienen que estar alineados con la cuadrícula. No pueden solaparse en celdas `#`, aunque los huecos `.` de una forma no ocupan espacio real.

La parte 1 pide contar cuántas regiones pueden encajar todos los regalos que tienen asignados. Con el ejemplo oficial, el resultado es:

```text
2
```

### Parte 2

La parte 2 de este día no añade un cálculo nuevo. El texto que aparece después de la parte 1 es narrativo y no pide procesar el input otra vez ni obtener otra respuesta numérica.

Por eso no he creado un solver separado para parte 2. En este caso, el Día 12 queda completo con la solución de la parte 1.

### Estructura del Día 12

El código del Día 12 está en `aoc.day12` y lo he dividido así:

```text
aoc.day12
├── ChristmasTreeFarmPuzzle.java
├── farm
└── input
```

### Paquete `farm`

`GridCell` representa una celda ocupada dentro de una forma.

`PresentShape` representa una forma de regalo. Guarda sus celdas ocupadas y genera sus orientaciones posibles usando rotaciones y volteos. También elimina orientaciones repetidas, porque algunas formas pueden verse igual después de girarlas.

`ShapeOrientation` representa una orientación concreta de una forma. Tiene sus celdas, anchura y altura ya normalizadas.

`TreeRegion` representa una zona debajo de un árbol. Guarda el ancho, el largo y cuántos regalos de cada forma hay que colocar.

`FarmSummary` agrupa las formas y las regiones parseadas desde el input.

`PresentFitter` contiene el algoritmo que decide si una región puede encajar sus regalos. Genera todas las posiciones posibles de cada orientación dentro de la región y luego usa backtracking para intentar colocarlas sin solapamientos.

### Paquete `input`

`FarmSummaryParser` convierte las líneas del archivo en un `FarmSummary`.

Este parser se encarga de leer las formas, validar caracteres, leer las dimensiones de cada región y comprobar que cada región tenga una cantidad para cada forma. Así el solver no depende del formato textual del archivo.

### `ChristmasTreeFarmPuzzle`

`ChristmasTreeFarmPuzzle` coordina el Día 12. Recibe las líneas de entrada, usa `FarmSummaryParser` y después llama a `PresentFitter` para contar las regiones que sí pueden encajar sus regalos.

Tiene `solvePartOne` porque es la única parte con cálculo real en este día.

### Decisión de algoritmo

Para cada región, primero calculo el área total que ocuparían los regalos. Si esa área es mayor que el área de la región, ya sé que no pueden caber y no sigo probando.

En las regiones grandes del input real, después de esa comprobación de área ya no hago un backtracking enorme, porque sería demasiado lento y no aporta nada práctico para el tamaño de esas regiones. Para los casos pequeños, como el ejemplo oficial, sí genero las colocaciones posibles de cada forma en la región. Cada colocación se guarda con un `BitSet`, porque así comprobar si dos regalos se solapan es rápido: basta con ver si sus bits se cruzan.

Después uso backtracking. En cada paso elijo una forma que todavía queda por colocar y pruebo sus posiciones compatibles con lo ya ocupado. Si una rama se bloquea, vuelvo atrás y pruebo otra colocación.

No simulo los puntos `.` como si ocuparan espacio, porque el enunciado dice que esos huecos no bloquean a otros regalos.

### Patrones usados en Día 12

No he usado un patrón de diseño formal en este día. El problema se entiende mejor como un modelo de dominio pequeño más un algoritmo de búsqueda.

No he forzado Factory Method, Decorator, Adapter, Iterator o Singleton porque no aportaban claridad aquí. En la defensa diría que la decisión importante fue separar responsabilidades y usar una representación eficiente para las celdas ocupadas.

### Principios aplicados

### SRP

Cada clase tiene una responsabilidad clara:

- `FarmSummaryParser` parsea la entrada.
- `PresentShape` representa una forma y calcula sus orientaciones.
- `TreeRegion` representa una región.
- `PresentFitter` decide si los regalos caben.
- `ChristmasTreeFarmPuzzle` coordina el flujo del día.

### DRY

La generación de orientaciones está solo en `PresentShape`. El backtracking no repite cómo se rota o se voltea una forma.

El parser está separado, así que no se repite el tratamiento del formato de entrada en el solver ni en los tests.

### KISS

Aunque el problema es más difícil que otros días, la solución sigue una idea defendible: generar colocaciones válidas y probar combinaciones sin solapamiento.

No he metido una arquitectura grande ni clases innecesarias.

### YAGNI

Solo está implementada la parte 1 porque la parte 2 no pide una regla nueva. No he preparado una parte 2 inventada porque sería añadir comportamiento que el problema no necesita.

### Encapsulación

Las formas exponen orientaciones ya normalizadas, pero no obligan al resto del código a conocer los detalles internos de rotación y volteo.

`TreeRegion` y `FarmSummary` copian sus listas para que no se modifique su contenido desde fuera.

### Bajo acoplamiento

`PresentFitter` trabaja con objetos del dominio, no con texto del archivo. El formato del input queda aislado en `FarmSummaryParser`.

### Alta cohesión

El paquete `farm` contiene solo conceptos del problema de los regalos y regiones. El paquete `input` solo se encarga de convertir texto en esos objetos.

## Tests del Día 12

### `ChristmasTreeFarmPuzzleTest`

Comprueba el ejemplo oficial completo de la parte 1, con resultado `2`.

### `FarmSummaryParserTest`

Comprueba:

- Parseo de formas y regiones.
- Rechazo de caracteres desconocidos en una forma.
- Rechazo de regiones con una cantidad de regalos distinta al número de formas.

### `PresentShapeTest`

Comprueba:

- Que se generan orientaciones únicas usando rotaciones y volteos.
- Que el área de una forma se calcula contando solo sus celdas `#`.

### `PresentFitterTest`

Comprueba:

- Que dos regalos de la forma del ejemplo pueden caber en una región `4x4`.
- Que se rechaza una región si el área total necesaria es demasiado grande.
- Que se rechaza una forma que no puede colocarse dentro de una región por sus dimensiones.

## Cómo defendería el Día 12

Yo explicaría que primero separé el problema en tres ideas: formas de regalos, regiones debajo de los árboles y una clase que comprueba si el conjunto puede colocarse.

También destacaría que las rotaciones y volteos están dentro de `PresentShape`, porque pertenecen al concepto de forma. El solver no debería saber los detalles matemáticos de cada transformación.

Para el algoritmo explicaría que cada posible colocación se convierte en un conjunto de celdas ocupadas. Si dos conjuntos se cruzan, esos regalos se solapan y esa combinación no vale. Esto hace que el backtracking sea más claro y evita trabajar con matrices copiadas muchas veces.

Por último, diría que no he usado patrones solo por cumplir. En este día la parte defendible está en SRP, encapsulación, bajo acoplamiento y en tener tests que prueban tanto el ejemplo oficial como casos importantes de error.
