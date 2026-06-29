# Advent of Code

Este proyecto es mi práctica de Java basada en Advent of Code. El objetivo no es solo sacar el número correcto de cada puzzle, sino construir una solución legible, con nombres claros, clases con responsabilidades separadas, patrones usados con sentido y tests que demuestren que la solución funciona.

La idea que sigo en cada día es separar el problema en partes pequeñas. Primero modelo el dominio del puzzle, después separo el parseo de la entrada y finalmente dejo la lógica de solución en clases específicas. Así evito tener todo mezclado en una sola clase grande.

## Día 1: Entrada secreta

### Qué pide el problema

El Día 1 trata de una caja fuerte con un dial circular numerado del 0 al 99. El dial empieza en 50 y la entrada es una lista de instrucciones como `L68` o `R48`: la letra indica la dirección y el número indica cuántos clics se mueve.

En la parte 1 se cuenta cuántas rotaciones terminan exactamente en la posición 0. En la parte 2 se cuenta cada clic que cae en 0, incluso si ocurre a mitad de una rotación larga. Por eso `R1000`, empezando en 50, cuenta diez llegadas a 0.

### Estructura del Día 1

El código está en `aoc.day01` y mantiene tres responsabilidades claras:

```text
aoc.day01
├── SecretEntrancePuzzle.java
├── dial
├── input
└── password
```

En `dial` está el modelo del problema. `SafeDial` encapsula la posición del dial y sabe aplicar rotaciones. `RotationDirection` modela izquierda y derecha. `DialRotation` es el objeto de valor de una instrucción ya interpretada y ofrece `fromInstruction(...)` como factory method para construir una rotación desde texto. `RotationProgram` agrupa las rotaciones y es `Iterable<DialRotation>`, de modo que los solvers pueden recorrer el programa sin depender de la lista interna.

En `input`, `RotationParser` convierte líneas de entrada en un `RotationProgram`. El parser no calcula contraseñas: solo traduce texto a objetos del dominio.

En `password`, `PasswordSolver` es la interfaz común de estrategia. `FinalPositionPasswordSolver` resuelve la parte 1 contando finales en 0. `ClickPasswordSolver` resuelve la parte 2 contando clics que caen en 0. `SecretEntrancePuzzle` coordina el caso de uso: parsea la entrada y delega en el solver correspondiente.

### Patrones usados en Día 1

Uso Strategy porque las dos partes comparten entrada y modelo, pero tienen criterios de conteo distintos. La interfaz `PasswordSolver` permite expresar esa diferencia sin meter dos algoritmos en una misma clase.

Uso Factory Method de forma sencilla en `DialRotation.fromInstruction(...)` y `RotationDirection.fromSymbol(...)`, porque la creación desde texto pertenece al dominio de la rotación y de la dirección, no al solver.

Uso Iterator con `RotationProgram`, que permite recorrer las rotaciones sin exponer una colección mutable ni acoplar los solvers a la representación interna.

### Principios aplicados

SRP aparece en la separación entre dial, parser y solvers. DRY se mantiene porque la lógica circular del dial vive en `SafeDial` y se reutiliza en ambas partes. KISS y YAGNI se respetan porque solo hay tres piezas nuevas que aportan claridad: dos estrategias y un programa iterable. La posición del dial está encapsulada y los solvers dependen de objetos de dominio, no de líneas de texto.

### Tests del Día 1

Los tests cubren el ejemplo oficial de la parte 1 con resultado 3, el ejemplo oficial de la parte 2 con resultado 6, el caso `R1000`, el comportamiento circular del dial, el parseo de instrucciones y los errores de entrada. También se comprueba que la posición inicial no se cuenta por sí sola.

## Día 2: Gift Shop

### Qué pide el problema

El Día 2 trabaja con rangos de IDs de producto, separados por comas. Hay que sumar los IDs inválidos dentro de esos rangos.

En la parte 1 un ID es inválido si está formado por dos mitades iguales, por ejemplo `55`, `6464` o `123123`. En la parte 2 la regla se amplía: el ID es inválido si está formado por una secuencia repetida dos o más veces, por ejemplo `123123123` o `1212121212`.

### Estructura del Día 2

El código está en `aoc.day02`:

```text
aoc.day02
├── GiftShopPuzzle.java
├── input
├── product
└── solver
```

En `input`, `ProductRangeParser` convierte la línea de entrada en objetos `ProductIdRange`.

En `product`, `ProductIdRange` representa un rango cerrado y valida que sus límites sean positivos y estén ordenados. `RepeatedPatternProductId` concentra operaciones de dominio para construir y comprobar IDs repetidos.

En `solver`, `InvalidProductIdRule` es la estrategia de regla inválida. `DoublePatternInvalidProductIdRule` implementa la regla de la parte 1. `RepeatedPatternInvalidProductIdRule` implementa la regla de la parte 2 y mantiene un `Set<Long>` para evitar sumar dos veces el mismo ID cuando puede generarse con más de una longitud de secuencia. `ProductIdSumCalculator` recibe una regla y suma los IDs inválidos producidos por ella.

`GiftShopPuzzle` coordina el flujo: parsea rangos y usa un calculador para cada parte.

### Patrones usados en Día 2

Uso Strategy porque las reglas de invalidez cambian entre partes, pero el cálculo de suma es el mismo. `ProductIdSumCalculator` no necesita saber si se está aplicando la regla doble o la regla de secuencia repetida.

### Principios aplicados

SRP queda claro: parsear rangos, representar rangos, generar IDs inválidos y sumar resultados son responsabilidades separadas. DRY aparece en `ProductIdSumCalculator`, que reutiliza el mismo flujo de suma para ambas reglas. KISS se mantiene porque las reglas siguen siendo clases pequeñas. No se hace fuerza bruta sobre rangos enormes; se generan candidatos repetidos y se comprueba si caen dentro del rango. El `Set` de la parte 2 se conserva porque evita duplicados reales.

### Tests del Día 2

Los tests cubren el ejemplo oficial de parte 1 con resultado `1227775554` y el de parte 2 con resultado `4174379265`. También prueban rangos pequeños, ausencia de IDs inválidos, detección de patrones repetidos y prevención de duplicados como `111111`.

## Día 3: Vestíbulo

### Qué pide el problema

El Día 3 trabaja con bancos de baterías. Cada línea contiene dígitos del 1 al 9 y hay que formar el mayor número posible eligiendo baterías sin cambiar su orden original.

En la parte 1 se eligen dos baterías. En la parte 2 se eligen doce baterías, por lo que el resultado puede superar `int` y se usa `long`.

La regla importante es que no se ordenan los dígitos. La solución mantiene la lógica voraz: para cada posición del resultado se busca el mayor dígito posible dejando suficientes baterías para completar las posiciones restantes.

### Estructura del Día 3

El código está en `aoc.day03`:

```text
aoc.day03
├── LobbyPuzzle.java
├── battery
├── input
└── solver
```

En `battery`, `BatteryBank` encapsula los dígitos disponibles y contiene el método `maximumJoltageUsingBatteries(...)`, que implementa la selección voraz preservando el orden.

En `input`, `BatteryBankParser` transforma cada línea en un `BatteryBank` y valida que los caracteres sean dígitos válidos del 1 al 9.

En `solver`, `JoltageSolver` es la interfaz común. `TwoBatteryJoltageSolver` resuelve la parte 1 y `TwelveBatteryJoltageSolver` resuelve la parte 2. Ambos reutilizan la misma lógica del modelo y solo fijan cuántas baterías se eligen.

`LobbyPuzzle` coordina parser y solvers.

### Patrones usados en Día 3

Uso Strategy de forma moderada para separar el solver de dos baterías y el solver de doce baterías. La interfaz común `JoltageSolver` mantiene el punto de entrada igual para las dos partes.

### Principios aplicados

SRP separa parseo, modelo y cálculo por parte. DRY queda en `BatteryBank.maximumJoltageUsingBatteries(...)`, que evita duplicar el algoritmo para 2 y 12 baterías. KISS se mantiene con una estrategia por parte y un único algoritmo voraz. YAGNI mantiene el diseño centrado en lo que pide el problema. La encapsulación protege la lista interna de baterías mediante copia defensiva.

### Tests del Día 3

Los tests cubren los ejemplos oficiales: parte 1 con resultado `357` y parte 2 con resultado `3121910778619`. También verifican que el algoritmo respeta el orden original, que no ordena los dígitos, que funciona con doce baterías y que rechaza bancos o cantidades inválidas.

## D?a 4: Printing Department

### Qu? pide el problema

El D?a 4 trata de un mapa donde aparecen rollos de papel marcados con `@` y espacios vac?os marcados con `.`.

En la parte 1 hay que contar los rollos accesibles para los montacargas. Un rollo es accesible cuando tiene menos de cuatro rollos en las ocho posiciones adyacentes.

En la parte 2 los rollos accesibles se retiran por rondas. Despu?s de cada retirada, el mapa cambia y se vuelven a buscar rollos accesibles hasta que no queda ninguno que pueda retirarse.

### Estructura del D?a 4

El c?digo est? en `aoc.day04`:

```text
aoc.day04
??? PrintingDepartmentPuzzle.java
??? input
??? paper
??? solver
```

En `paper`, `GridPosition` representa una coordenada y `PaperRollGrid` encapsula la cuadr?cula. La cuadr?cula sabe si hay un rollo en una posici?n, cuenta vecinos y puede crear una nueva cuadr?cula sin los rollos retirados.

En `input`, `PaperRollMapParser` convierte las l?neas del archivo en un `PaperRollGrid` y valida los caracteres permitidos.

En `solver`, `PaperRollSolver` es la interfaz com?n para las estrategias. `AccessibleRollSolver` resuelve la parte 1 contando rollos accesibles. `RemovableRollSolver` resuelve la parte 2 aplicando rondas de retirada. `ForkliftAccessSolver` queda como fachada para usar ambas estrategias desde el puzzle y desde los tests.

`PrintingDepartmentPuzzle` coordina parser y solver.

### Patrones usados en D?a 4

Uso Strategy para separar la regla de la parte 1 y la regla de la parte 2. Las dos trabajan con el mismo modelo (`PaperRollGrid`), pero una solo cuenta accesibles y la otra repite retiradas.

### Principios aplicados

SRP queda repartido entre parser, modelo de cuadr?cula y estrategias de soluci?n. DRY se mantiene porque la regla de accesibilidad se reutiliza al contar accesibles y al retirar por rondas. KISS se mantiene con una fachada peque?a y dos estrategias directas. La cuadr?cula encapsula su representaci?n interna y devuelve una nueva cuadr?cula cuando se retiran rollos.

### Tests del D?a 4

Los tests cubren el ejemplo oficial de parte 1 con resultado `13` y parte 2 con resultado `43`. Tambi?n prueban el conteo de vecinos, mapas inv?lidos, rollos con cuatro vecinos y retiradas acumuladas en varias rondas.

## D?a 5: Cafeteria

### Qu? pide el problema

El D?a 5 trabaja con rangos de ingredientes frescos y con una lista de ingredientes disponibles.

En la parte 1 se cuenta cu?ntos ingredientes disponibles est?n dentro de alg?n rango fresco. Si un ingrediente cae en varios rangos solapados, sigue contando una sola vez porque aparece una sola vez en la lista de disponibles.

En la parte 2 ya no se usa la lista de disponibles. Hay que contar cu?ntos IDs distintos est?n cubiertos por todos los rangos frescos, fusionando rangos solapados o adyacentes.

### Estructura del D?a 5

El c?digo est? en `aoc.day05`:

```text
aoc.day05
??? CafeteriaPuzzle.java
??? input
??? inventory
??? solver
```

En `inventory`, `IngredientIdRange` representa un rango inclusivo, `InventoryDatabase` agrupa los rangos frescos y los IDs disponibles, y `FreshIngredientCounter` act?a como fachada para las dos estrategias.

En `input`, `InventoryDatabaseParser` lee las dos secciones del archivo: primero los rangos, luego la l?nea en blanco y despu?s los IDs disponibles.

En `solver`, `FreshIngredientSolver` es la interfaz com?n. `AvailableFreshIngredientSolver` resuelve la parte 1 revisando los IDs disponibles. `AllFreshIngredientSolver` resuelve la parte 2 ordenando y fusionando rangos para contar cada ID fresco una sola vez.

`CafeteriaPuzzle` coordina parser y contador.

### Patrones usados en D?a 5

Uso Strategy porque las dos partes responden preguntas distintas sobre los mismos datos. La parte 1 trabaja sobre IDs disponibles y la parte 2 trabaja sobre rangos completos, pero ambas se exponen como `FreshIngredientSolver`.

### Principios aplicados

SRP separa parseo, modelo de inventario y c?lculo. DRY aparece en `IngredientIdRange.contains(...)`, que concentra la comprobaci?n de pertenencia a un rango. KISS se mantiene porque la parte 2 no enumera todos los IDs: ordena rangos, los fusiona y suma tama?os. La encapsulaci?n est? en `InventoryDatabase`, que copia las listas recibidas.

### Tests del D?a 5

Los tests cubren el ejemplo oficial de parte 1 con resultado `3` y parte 2 con resultado `14`. Tambi?n prueban ingredientes dentro de rangos, rangos solapados, rangos separados y rangos adyacentes.

## D?a 6: Trash Compactor

### Qu? pide el problema

El D?a 6 trabaja con una hoja de operaciones escrita en columnas. Cada bloque de columnas representa un problema matem?tico con varios n?meros y una operaci?n.

En la parte 1 los problemas se leen de arriba hacia abajo. En la parte 2 se leen por columnas de derecha a izquierda, formando n?meros de otra manera. Despu?s de parsear, el c?lculo sigue siendo sumar el resultado de todos los problemas.

### Estructura del D?a 6

El c?digo est? en `aoc.day06`:

```text
aoc.day06
??? TrashCompactorPuzzle.java
??? input
??? worksheet
??? solver
```

En `worksheet`, `MathProblem` representa un problema y `MathOperation` representa la operaci?n. `WorksheetSolver` suma los resultados de una lista de problemas.

En `input`, `WorksheetParser` se encarga de dividir la hoja en bloques de columnas y convertir cada bloque en objetos `MathProblem`. Tiene un m?todo para la lectura de parte 1 y otro para la lectura de parte 2.

En `solver`, `TrashCompactorSolver` es la interfaz com?n. `TopToBottomWorksheetSolver` resuelve la parte 1 usando la lectura vertical. `RightToLeftWorksheetSolver` resuelve la parte 2 usando la lectura de derecha a izquierda.

`TrashCompactorPuzzle` coordina las dos estrategias.

### Patrones usados en D?a 6

Uso Strategy para separar los dos modos de resolver el d?a: lectura vertical y lectura de derecha a izquierda.

Uso Factory Method en `MathOperation.fromSymbol(...)`, que centraliza c?mo se convierte `+` o `*` en una operaci?n del dominio.

### Principios aplicados

SRP separa interpretaci?n de la hoja, representaci?n de problemas, operaciones matem?ticas y solvers por parte. DRY se mantiene porque `MathProblem`, `MathOperation` y `WorksheetSolver` se reutilizan en las dos partes. KISS se mantiene con dos estrategias peque?as que solo cambian el m?todo de parseo. La encapsulaci?n aparece en `MathProblem`, que copia la lista de n?meros.

### Tests del D?a 6

Los tests cubren el ejemplo oficial de parte 1 con resultado `4277556` y parte 2 con resultado `3263827`. Tambi?n prueban parseo de l?neas, lectura de derecha a izquierda, rechazo de caracteres inv?lidos, operaciones matem?ticas y suma total de problemas.

## D?a 7: Laboratories

### Qu? pide el problema

El D?a 7 trabaja con un manifold de taquiones representado como una cuadr?cula. El punto `S` marca el inicio y los caracteres `^` representan splitters.

En la parte 1 se cuenta cu?ntos splitters activa el haz. Cuando el haz encuentra un splitter, se divide hacia izquierda y derecha en la siguiente fila.

En la parte 2 se cuentan todas las l?neas temporales posibles. En esta parte no basta con saber qu? columnas est?n activas: tambi?n importa cu?ntas l?neas llegan a cada columna, por eso se usa `BigInteger`.

### Estructura del D?a 7

El c?digo est? en `aoc.day07`:

```text
aoc.day07
??? LaboratoriesPuzzle.java
??? input
??? manifold
??? solver
```

En `manifold`, `TachyonManifold` representa la cuadr?cula y la posici?n inicial. `TachyonSplitterCounter` contiene el conteo de splitters de la parte 1 y `QuantumTimelineCounter` contiene el conteo de l?neas temporales de la parte 2.

En `input`, `TachyonManifoldParser` convierte el texto en un `TachyonManifold` y valida el mapa.

En `solver`, `TachyonSolver<T>` es la interfaz com?n. `SplitCountingSolver` resuelve la parte 1 y devuelve `Integer`. `QuantumTimelineSolver` resuelve la parte 2 y devuelve `BigInteger`.

`LaboratoriesPuzzle` coordina parser y estrategias.

### Patrones usados en D?a 7

Uso Strategy para separar el solver de parte 1 y el solver de parte 2. Las dos estrategias trabajan con el mismo `TachyonManifold`, pero devuelven tipos distintos porque la parte 2 puede crecer mucho m?s.

### Principios aplicados

SRP separa parser, modelo del manifold, contadores y estrategias. DRY se mantiene porque la validaci?n y las consultas sobre la cuadr?cula est?n en `TachyonManifold`. KISS se mantiene dejando el algoritmo de cada parte en su clase. La encapsulaci?n aparece en la copia interna de las celdas del manifold.

### Tests del D?a 7

Los tests cubren el ejemplo oficial, el parseo del manifold, el conteo de splitters, el conteo de l?neas temporales, mapas inv?lidos y casos donde varias l?neas temporales llegan a la misma columna.

## D?a 8: Playground

### Qu? pide el problema

El D?a 8 trabaja con cajas de conexiones en un espacio tridimensional. Cada caja tiene coordenadas y se conectan primero las parejas m?s cercanas.

En la parte 1 se aplican las primeras 1000 conexiones por distancia y se multiplican los tama?os de los tres circuitos m?s grandes.

En la parte 2 se siguen conectando cajas hasta que todo forma un ?nico circuito. La respuesta es el producto de las coordenadas `X` de las dos cajas de la conexi?n que consigue unirlo todo.

### Estructura del D?a 8

El c?digo est? en `aoc.day08`:

```text
aoc.day08
??? PlaygroundPuzzle.java
??? circuit
??? input
??? solver
```

En `circuit`, `JunctionBox` representa una caja, `JunctionConnection` representa una conexi?n candidata, `JunctionConnectionPlanner` ordena las conexiones por distancia y `CircuitNetwork` mantiene los grupos conectados con uni?n de conjuntos. `CircuitSizeCalculator` queda como fachada compatible para los tests existentes.

En `input`, `JunctionBoxParser` convierte las l?neas del archivo en cajas.

En `solver`, `CircuitSolver` es la interfaz com?n. `ThreeLargestCircuitSizeSolver` resuelve la parte 1. `LastConnectionXCoordinateSolver` resuelve la parte 2.

`PlaygroundPuzzle` coordina parser y estrategias.

### Patrones usados en D?a 8

Uso Strategy para separar los dos c?lculos: uno corta despu?s de un n?mero fijo de conexiones y otro busca la conexi?n que deja un ?nico circuito.

### Principios aplicados

SRP separa cajas, conexiones candidatas, red de circuitos, planificaci?n de conexiones y solvers. DRY aparece en `JunctionConnectionPlanner`, que ordena las conexiones una sola vez con la misma regla para ambas partes. KISS se mantiene usando distancia al cuadrado y uni?n de conjuntos. La encapsulaci?n est? en `CircuitNetwork`, que oculta los arrays internos de padres y tama?os.

### Tests del D?a 8

Los tests cubren el ejemplo oficial, parseo de cajas, distancia entre cajas, conexi?n de circuitos, tama?os de circuitos y la conexi?n que deja un ?nico circuito.

## D?a 9: Movie Theater

### Qu? pide el problema

El D?a 9 trabaja con baldosas rojas en una sala de cine. Cada baldosa tiene coordenadas y se buscan rect?ngulos formados entre pares de baldosas.

En la parte 1 se busca el rect?ngulo de mayor ?rea entre cualquier pareja de baldosas rojas.

En la parte 2 el rect?ngulo tiene que quedar dentro de la zona roja o verde delimitada por el camino cerrado de baldosas. Para evitar recorrer una cuadr?cula enorme, la zona v?lida se calcula con compresi?n de coordenadas.

### Estructura del D?a 9

El c?digo est? en `aoc.day09`:

```text
aoc.day09
??? MovieTheaterPuzzle.java
??? input
??? theater
??? solver
```

En `theater`, `RedTile` representa una baldosa y encapsula el c?lculo de ?rea inclusiva con otra baldosa. `RedGreenTileArea` representa la zona v?lida de la parte 2. `LargestRectangleFinder` queda como fachada compatible para los tests existentes.

En `input`, `RedTileParser` convierte el texto en baldosas.

En `solver`, `TheaterAreaSolver` es la interfaz com?n. `UnrestrictedRectangleAreaSolver` resuelve la parte 1. `RedGreenRectangleAreaSolver` resuelve la parte 2 comprobando que el rect?ngulo est? dentro de la zona v?lida.

`MovieTheaterPuzzle` coordina parser y estrategias.

### Patrones usados en D?a 9

Uso Strategy para separar el c?lculo sin restricciones de la parte 1 y el c?lculo restringido por zona v?lida de la parte 2.

### Principios aplicados

SRP separa parser, modelo de baldosa, zona v?lida y solvers. DRY aparece en `RedTile.rectangleAreaWith(...)`, que concentra la f?rmula de ?rea inclusiva. KISS se mantiene con dos estrategias peque?as y un objeto espec?fico para la zona roja/verde. La encapsulaci?n evita que los solvers conozcan los detalles internos de la compresi?n de coordenadas.

### Tests del D?a 9

Los tests cubren el ejemplo oficial, parseo de baldosas, c?lculo de ?rea inclusiva, b?squeda del ?rea m?xima, validaci?n de entrada y ?rea m?xima dentro de la zona roja/verde.

## D?a 10: Factory

### Qu? pide el problema

El D?a 10 trabaja con m?quinas de una f?brica. Cada l?nea describe una m?quina con luces, botones y requisitos de voltaje.

En la parte 1 solo importan las luces. Cada bot?n alterna algunas luces y hay que encontrar el m?nimo n?mero de pulsaciones para llegar al patr?n objetivo.

En la parte 2 importan los requisitos de voltaje. Cada bot?n suma a algunos contadores y hay que encontrar el m?nimo n?mero total de pulsaciones para cumplir todos los requisitos.

### Estructura del D?a 10

El c?digo est? en `aoc.day10`:

```text
aoc.day10
??? FactoryPuzzle.java
??? factory
??? input
??? solver
```

En `factory`, `FactoryMachine` representa una m?quina, `ButtonWiring` representa qu? posiciones afecta un bot?n y `MachineInitializer` contiene los algoritmos principales: b?squeda para luces y resoluci?n del sistema de voltajes.

En `input`, `FactoryManualParser` convierte cada l?nea del manual en una `FactoryMachine`.

En `solver`, `FactorySolver<T>` es la interfaz com?n. `LightInitializationSolver` resuelve la parte 1 y devuelve `Integer`. `JoltageInitializationSolver` resuelve la parte 2 y devuelve `Long`.

`FactoryPuzzle` coordina parser y estrategias.

### Patrones usados en D?a 10

Uso Strategy para separar el c?lculo de luces y el c?lculo de voltaje. Las dos partes usan las mismas m?quinas, pero el objetivo y el algoritmo son distintos.

### Principios aplicados

SRP separa parseo, modelo de m?quina, algoritmos de inicializaci?n y estrategias por parte. DRY se mantiene porque `FactoryMachine` y `ButtonWiring` se reutilizan en las dos partes. KISS aparece en la separaci?n de dos algoritmos claros: BFS para luces y sistema lineal para voltajes. La encapsulaci?n est? en las copias defensivas de listas dentro de `FactoryMachine`.

### Tests del D?a 10

Los tests cubren el ejemplo oficial de parte 1 con resultado `7` y parte 2 con resultado `33`. Tambi?n prueban parseo del manual, m?nimo de pulsaciones de luces, m?nimo de pulsaciones de voltaje y casos de validaci?n de m?quinas.

## D?a 11: Reactor

### Qu? pide el problema

El D?a 11 trabaja con una red de dispositivos. Cada dispositivo puede enviar datos a otros dispositivos, as? que el problema se modela como un grafo dirigido.

En la parte 1 se cuentan los caminos desde `you` hasta `out`.

En la parte 2 se cuentan los caminos desde `svr` hasta `out` que pasan por `dac` y `fft`, en cualquier orden.

### Estructura del D?a 11

El c?digo est? en `aoc.day11`:

```text
aoc.day11
??? ReactorPuzzle.java
??? input
??? reactor
??? solver
```

En `reactor`, `DeviceNetwork` representa el grafo y `PathCounter` contiene el conteo de caminos con memoizaci?n.

En `input`, `DeviceNetworkParser` convierte l?neas como `aaa: bbb ccc` en un `DeviceNetwork`.

En `solver`, `ReactorSolver` es la interfaz com?n. `DirectPathSolver` resuelve la parte 1. `RequiredDevicePathSolver` resuelve la parte 2 con los dos dispositivos obligatorios.

`ReactorPuzzle` coordina parser y estrategias.

### Patrones usados en D?a 11

Uso Strategy para separar el conteo directo de caminos y el conteo con visitas obligatorias. Las dos estrategias usan el mismo grafo y el mismo `PathCounter`, pero aplican reglas distintas.

### Principios aplicados

SRP separa parser, grafo, contador de caminos y estrategias por parte. DRY aparece en `PathCounter`, que concentra el recorrido y la memoizaci?n. KISS se mantiene porque cada solver fija los nombres de dispositivos que pertenecen a su parte. La encapsulaci?n aparece en `DeviceNetwork`, que oculta el mapa interno de salidas.

### Tests del D?a 11

Los tests cubren el ejemplo oficial de parte 1 con resultado `5` y parte 2 con resultado `2`. Tambi?n prueban parseo de red, conteo de caminos, visitas obligatorias, dispositivo inicial inexistente y detecci?n de ciclos.

## D?a 12: Christmas Tree Farm

### Qu? pide el problema

El D?a 12 trabaja con formas de regalos y regiones debajo de ?rboles. Cada forma est? dibujada con `#` y `.`, y cada regi?n indica cu?ntos regalos de cada forma hay que colocar.

La soluci?n cuenta cu?ntas regiones pueden contener todos sus regalos sin solapamientos entre celdas ocupadas `#`. Las formas se pueden rotar y voltear.

### Estructura del D?a 12

El c?digo est? en `aoc.day12`:

```text
aoc.day12
??? ChristmasTreeFarmPuzzle.java
??? farm
??? input
??? solver
```

En `farm`, `PresentShape` representa una forma, `ShapeOrientation` representa una orientaci?n normalizada, `TreeRegion` representa una regi?n, `FarmSummary` agrupa el input parseado y `PresentFitter` comprueba si los regalos caben.

En `input`, `FarmSummaryParser` lee las formas y las regiones.

En `solver`, `ChristmasTreeFarmSolver` define el caso de uso sobre un `FarmSummary`. `FittingRegionSolver` resuelve la parte implementada contando las regiones donde caben los regalos.

`ChristmasTreeFarmPuzzle` coordina parser y solver.

### Patrones usados en D?a 12

Uso una capa de solver para separar el caso de uso del parser y del algoritmo de encaje. Tambi?n hay polimorfismo interno en las transformaciones de `PresentShape`, porque cada transformaci?n sabe c?mo convertir una celda.

### Principios aplicados

SRP separa parseo, formas, regiones y encaje. DRY aparece en `PresentShape.orientations()`, que centraliza rotaciones y volteos. KISS se mantiene usando `BitSet` para representar colocaciones ocupadas y comprobar solapamientos. La encapsulaci?n est? en las copias defensivas de `FarmSummary`, `TreeRegion`, `PresentShape` y `ShapeOrientation`.

### Tests del D?a 12

Los tests cubren el ejemplo oficial con resultado `2`. Tambi?n prueban parseo del resumen, orientaciones de formas, encaje de regalos, rechazo por ?rea insuficiente y rechazo de formas que no pueden colocarse por dimensiones.
