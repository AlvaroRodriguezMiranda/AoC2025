# Advent of Code 2025

Este proyecto es mi práctica de Java basada en Advent of Code 2025. La idea no es solo obtener el resultado correcto, sino tener una solución que pueda explicar en clase: código dividido por responsabilidades, nombres claros, tests, patrones usados con sentido y algoritmos que se puedan razonar.

El proyecto se mantiene como un único proyecto Maven. Cada día tiene una clase principal que coordina el flujo, un paquete de entrada para parsear el texto, clases de modelo para representar el dominio y, cuando encaja, un paquete `solver` con estrategias separadas para cada parte.

## Estructura general

```text
src/main/java/aoc
├── day01
├── day02
├── ...
└── day12
```

Cada día sigue una idea parecida:

- `input`: convierte el texto del puzzle en objetos del dominio.
- `model` o paquete de dominio (`dial`, `battery`, `paper`, `factory`, etc.): representa los conceptos del problema.
- `solver`: contiene las estrategias o casos de uso de solución cuando hay más de una forma clara de resolver el día.
- clase principal del día: coordina parser y solver.

Esta separación ayuda a aplicar SRP, reducir acoplamiento y probar cada parte sin depender siempre del archivo de entrada real.

## Día 1: Entrada secreta

### Qué pide el problema

El puzzle trabaja con un dial circular numerado del 0 al 99. El dial empieza en 50 y la entrada tiene instrucciones como `L68` o `R48`.

En la parte 1 se cuenta cuántas rotaciones terminan exactamente en 0. En la parte 2 se cuenta cada clic que cae en 0, aunque ocurra a mitad de una rotación larga.

### Idea principal

La lógica importante está en modelar bien el dial circular. `SafeDial` encapsula la posición y aplica rotaciones usando módulo. Para la parte 2, también calcula cuántas veces una rotación pasa por una posición concreta.

### Estructura y clases

```text
aoc.day01
├── SecretEntrancePuzzle
├── dial
├── input
└── password
```

Clases importantes:

- `SafeDial`: representa el dial y oculta cómo se actualiza la posición.
- `DialRotation`: representa una instrucción ya parseada.
- `RotationDirection`: enum con comportamiento para izquierda y derecha.
- `RotationProgram`: colección iterable de rotaciones.
- `RotationParser`: convierte líneas de texto en un `RotationProgram`.
- `PasswordSolver`: interfaz común.
- `FinalPositionPasswordSolver`: estrategia de parte 1.
- `ClickPasswordSolver`: estrategia de parte 2.

### Patrones y principios

Uso Strategy con `PasswordSolver`, porque las dos partes usan la misma entrada pero cuentan cosas distintas.

Uso Factory Method / static factory con `DialRotation.fromInstruction(...)` y `RotationDirection.fromSymbol(...)`, porque centralizan la conversión desde texto a objetos del dominio.

Uso Iterator porque `RotationProgram` implementa `Iterable<DialRotation>` y permite recorrer las rotaciones sin exponer una lista mutable.

También hay polimorfismo en `RotationDirection`: cada dirección sabe aplicar su movimiento.

Principios:

- SRP: parser, dial y solvers están separados.
- DRY: la lógica circular vive en `SafeDial` y se reutiliza.
- Encapsulación: la posición del dial no se modifica desde fuera.
- Bajo acoplamiento: los solvers trabajan con `RotationProgram`, no con strings.

### Tests

Los tests cubren el ejemplo oficial, `R1000`, giros circulares, parseo de instrucciones, errores de entrada y que la posición inicial no se cuente sola.

### Cómo defendería este día

Explicaría que primero separé los conceptos del problema: dial, rotación y contraseña. Después separé las dos formas de calcular la contraseña en estrategias distintas, porque parte 1 y parte 2 no cuentan lo mismo.

## Día 2: Gift Shop

### Qué pide el problema

La entrada tiene rangos de IDs de producto. Hay que sumar los IDs inválidos.

En la parte 1 un ID es inválido si está formado por dos mitades iguales, como `55` o `123123`. En la parte 2 un ID es inválido si está formado por una secuencia repetida dos o más veces, como `123123123`.

### Idea principal

No se recorren todos los números de rangos enormes. Se generan candidatos con patrón repetido y luego se comprueba si caen dentro del rango. En parte 2 se usa un `Set<Long>` para evitar sumar dos veces el mismo ID si se puede generar de varias formas.

### Estructura y clases

```text
aoc.day02
├── GiftShopPuzzle
├── input
├── product
└── solver
```

Clases importantes:

- `ProductRangeParser`: parsea rangos.
- `ProductIdRange`: rango cerrado de IDs.
- `RepeatedPatternProductId`: utilidades del dominio para construir y reconocer patrones repetidos.
- `InvalidProductIdRule`: interfaz de regla.
- `DoublePatternInvalidProductIdRule`: regla de parte 1.
- `RepeatedPatternInvalidProductIdRule`: regla de parte 2.
- `ProductIdSumCalculator`: suma los IDs producidos por una regla.

### Patrones y principios

Uso Strategy con `InvalidProductIdRule`, porque cambia la regla para decidir si un ID es inválido.

Principios:

- SRP: parseo, rango, generación de IDs y suma están separados.
- DRY: el cálculo de suma se comparte en `ProductIdSumCalculator`.
- KISS: se generan candidatos válidos en vez de recorrer rangos completos.
- Encapsulación: `ProductIdRange` concentra la comprobación `contains`.

### Tests

Los tests cubren ejemplos oficiales, rangos pequeños, rangos sin inválidos, patrones repetidos y duplicados como `111111`.

### Cómo defendería este día

Diría que Strategy encaja porque la pregunta de parte 1 y parte 2 es la misma, pero la regla de invalidez cambia. También destacaría que la solución evita fuerza bruta sobre rangos grandes.

## Día 3: Vestíbulo

### Qué pide el problema

Cada línea representa un banco de baterías con dígitos del 1 al 9. Hay que formar el mayor número posible manteniendo el orden original.

En la parte 1 se eligen 2 baterías. En la parte 2 se eligen 12 baterías, por eso el resultado usa `long`.

### Idea principal

El algoritmo es voraz: para cada posición del resultado se elige el mayor dígito posible, pero dejando suficientes dígitos para completar el resto. No se ordenan los dígitos; se mantiene el orden original.

### Estructura y clases

```text
aoc.day03
├── LobbyPuzzle
├── battery
├── input
└── solver
```

Clases importantes:

- `BatteryBank`: encapsula los dígitos y el algoritmo voraz.
- `BatteryBankParser`: parsea líneas.
- `JoltageSolver`: interfaz común.
- `TwoBatteryJoltageSolver`: parte 1.
- `TwelveBatteryJoltageSolver`: parte 2.

### Patrones y principios

Uso Strategy con `JoltageSolver`, porque cambia la cantidad de baterías elegidas.

Principios:

- SRP: parseo, modelo y solvers están separados.
- DRY: el algoritmo general está en `BatteryBank.maximumJoltageUsingBatteries(...)`.
- KISS: un algoritmo voraz directo resuelve ambas partes.
- Encapsulación: la lista interna de dígitos se copia.

### Tests

Los tests cubren ejemplos oficiales, orden original, caso de 12 baterías y entradas inválidas.

### Cómo defendería este día

Lo defendería explicando que el punto clave no es ordenar los dígitos, sino elegir una subsecuencia máxima. La abstracción del solver solo fija si se eligen 2 o 12 baterías.

## Día 4: Printing Department

### Qué pide el problema

El mapa contiene rollos de papel `@` y huecos `.`.

En la parte 1 se cuentan los rollos accesibles: un rollo es accesible si tiene menos de cuatro rollos alrededor. En la parte 2 se retiran por rondas todos los rollos accesibles hasta que no quedan más.

### Idea principal

La cuadrícula sabe contar vecinos. La parte 2 reutiliza la misma regla de accesibilidad, pero la aplica de forma repetida creando una nueva cuadrícula sin los rollos retirados.

### Estructura y clases

```text
aoc.day04
├── PrintingDepartmentPuzzle
├── input
├── paper
└── solver
```

Clases importantes:

- `PaperRollMapParser`: parsea la cuadrícula.
- `PaperRollGrid`: representa el mapa y cuenta vecinos.
- `GridPosition`: coordenada de fila y columna.
- `PaperRollSolver`: interfaz común.
- `AccessibleRollSolver`: parte 1.
- `RemovableRollSolver`: parte 2.
- `ForkliftAccessSolver`: fachada que conserva el uso desde tests.

### Patrones y principios

Uso Strategy con `PaperRollSolver`, porque parte 1 y parte 2 usan la misma regla base pero distinto flujo.

Principios:

- SRP: parser, cuadrícula y estrategias están separados.
- DRY: la regla de accesibilidad se reutiliza.
- Encapsulación: `PaperRollGrid` oculta las filas internas.
- Composición: `ForkliftAccessSolver` usa estrategias en vez de heredar.

### Tests

Los tests cubren ejemplos oficiales, conteo de vecinos, mapas inválidos y retiradas acumuladas.

### Cómo defendería este día

Explicaría que parte 2 no es otra regla nueva, sino la misma regla aplicada por rondas. Por eso separé contar accesibles de retirar repetidamente.

## Día 5: Cafeteria

### Qué pide el problema

La entrada tiene rangos de ingredientes frescos y una lista de ingredientes disponibles.

En la parte 1 se cuentan los ingredientes disponibles que caen en algún rango fresco. En la parte 2 se cuentan todos los IDs cubiertos por los rangos, fusionando solapamientos y rangos adyacentes.

### Idea principal

La parte 2 se resuelve ordenando rangos y fusionándolos. Así no se enumeran todos los IDs si los rangos son grandes.

### Estructura y clases

```text
aoc.day05
├── CafeteriaPuzzle
├── input
├── inventory
└── solver
```

Clases importantes:

- `InventoryDatabaseParser`: parsea rangos y disponibles.
- `InventoryDatabase`: agrupa datos parseados.
- `IngredientIdRange`: rango inclusivo.
- `FreshIngredientSolver`: interfaz común.
- `AvailableFreshIngredientSolver`: parte 1.
- `AllFreshIngredientSolver`: parte 2.
- `FreshIngredientCounter`: fachada compatible con tests.

### Patrones y principios

Uso Strategy con `FreshIngredientSolver`, porque cada parte responde una pregunta distinta sobre el mismo inventario.

Principios:

- SRP: parseo, modelo y cálculo están separados.
- DRY: `IngredientIdRange.contains(...)` concentra pertenencia a rango.
- KISS: fusión de rangos en vez de enumerar IDs.
- Encapsulación: `InventoryDatabase` copia sus listas.

### Tests

Los tests cubren ejemplos oficiales, rangos solapados, rangos separados y rangos adyacentes.

### Cómo defendería este día

Explicaría que parte 1 mira una lista concreta de ingredientes, mientras que parte 2 trabaja con el conjunto total cubierto por los rangos. Esa diferencia justifica dos estrategias.

## Día 6: Trash Compactor

### Qué pide el problema

La entrada es una hoja de operaciones escrita en columnas.

En la parte 1 los problemas se leen de arriba hacia abajo. En la parte 2 se leen por columnas de derecha a izquierda. Después de parsear, se suman los resultados de todos los problemas.

### Idea principal

La diferencia entre partes está en cómo se interpreta la hoja, no en cómo se resuelve una operación. Por eso se reutilizan `MathProblem`, `MathOperation` y `WorksheetSolver`.

### Estructura y clases

```text
aoc.day06
├── TrashCompactorPuzzle
├── input
├── worksheet
└── solver
```

Clases importantes:

- `WorksheetParser`: parsea la hoja en los dos modos.
- `MathProblem`: números y operación.
- `MathOperation`: enum para suma y multiplicación.
- `WorksheetSolver`: suma resultados.
- `TrashCompactorSolver`: interfaz común.
- `TopToBottomWorksheetSolver`: parte 1.
- `RightToLeftWorksheetSolver`: parte 2.

### Patrones y principios

Uso Strategy con `TrashCompactorSolver`, porque cada parte usa un modo distinto de lectura.

Uso Factory Method con `MathOperation.fromSymbol(...)`, que convierte `+` o `*` en una operación.

Uso polimorfismo en `MathOperation`, porque cada operación implementa su propio `apply`.

Principios:

- SRP: parser, problema, operación y solver están separados.
- DRY: el cálculo de problemas se reutiliza.
- KISS: las estrategias solo eligen el modo de parseo.
- Encapsulación: `MathProblem` copia los números.

### Tests

Los tests cubren ejemplos oficiales, parseo, lectura derecha-izquierda, operaciones y suma total.

### Cómo defendería este día

Diría que separé la lectura visual de la hoja del cálculo matemático. Así la parte 2 cambia el parseo, pero no duplica la lógica de resolver operaciones.

## Día 7: Laboratories

### Qué pide el problema

El mapa representa un manifold de taquiones. `S` marca el inicio y `^` son splitters.

En la parte 1 se cuentan los splitters activados. En la parte 2 se cuentan todas las líneas temporales posibles, usando `BigInteger` porque el número puede crecer mucho.

### Idea principal

Parte 1 puede juntar haces por columna con un `Set`, porque si llegan a la misma columna se comportan igual. Parte 2 necesita un `Map<Integer, BigInteger>` porque importa cuántas líneas llegan a cada columna.

### Estructura y clases

```text
aoc.day07
├── LaboratoriesPuzzle
├── input
├── manifold
└── solver
```

Clases importantes:

- `TachyonManifoldParser`: parsea el mapa.
- `TachyonManifold`: representa cuadrícula e inicio.
- `TachyonSplitterCounter`: conteo de parte 1.
- `QuantumTimelineCounter`: conteo de parte 2.
- `TachyonSolver<T>`: interfaz común.
- `SplitCountingSolver`: parte 1.
- `QuantumTimelineSolver`: parte 2.

### Patrones y principios

Uso Strategy con `TachyonSolver<T>`, porque las partes calculan resultados distintos sobre el mismo manifold.

Principios:

- SRP: parser, manifold, contadores y solvers están separados.
- DRY: las consultas de cuadrícula están en `TachyonManifold`.
- KISS: cada contador mantiene su estructura de datos concreta.
- Encapsulación: el manifold copia sus celdas.

### Tests

Los tests cubren ejemplos oficiales, parseo, splitters, líneas temporales y mapas inválidos.

### Cómo defendería este día

Explicaría que la parte 2 no puede ser solo un `Set`, porque perdería cuántas líneas temporales llegan a cada columna. Por eso cambia la estructura de datos.

## Día 8: Playground

### Qué pide el problema

Hay cajas de conexiones en 3D. Se conectan primero las parejas más cercanas.

En la parte 1 se aplican las primeras 1000 conexiones y se multiplican los tamaños de los tres circuitos más grandes. En la parte 2 se conectan cajas hasta formar un único circuito y se usa la última conexión necesaria.

### Idea principal

Se generan todas las conexiones posibles, se ordenan por distancia al cuadrado y se conectan con Union-Find. Esto evita manejar listas de componentes de forma manual.

### Estructura y clases

```text
aoc.day08
├── PlaygroundPuzzle
├── circuit
├── input
└── solver
```

Clases importantes:

- `JunctionBoxParser`: parsea cajas.
- `JunctionBox`: coordenadas y distancia.
- `JunctionConnection`: conexión candidata.
- `JunctionConnectionPlanner`: genera y ordena conexiones.
- `CircuitNetwork`: Union-Find con `parents` y `sizes`.
- `CircuitSolver`: interfaz común.
- `ThreeLargestCircuitSizeSolver`: parte 1.
- `LastConnectionXCoordinateSolver`: parte 2.
- `CircuitSizeCalculator`: fachada compatible con tests.

### Patrones y principios

Uso Strategy con `CircuitSolver`, porque las dos partes recorren conexiones con objetivos distintos.

`CircuitNetwork` aplica la idea de Union-Find / Disjoint Set. Es una estructura de datos, no un patrón GoF, pero es importante porque mejora eficiencia y encapsula la lógica de unir componentes.

Principios:

- SRP: cajas, conexiones, red y solvers están separados.
- DRY: `JunctionConnectionPlanner` centraliza la ordenación por distancia.
- KISS: se usa distancia al cuadrado para evitar raíces.
- Encapsulación: `CircuitNetwork` oculta arrays internos.

### Tests

Los tests cubren ejemplos oficiales, parseo, distancia, unión de circuitos, tamaños y última conexión.

### Cómo defendería este día

Diría que Union-Find encaja porque el problema trata de ir uniendo grupos. No necesito guardar todas las conexiones activas, solo saber qué cajas pertenecen al mismo circuito.

## Día 9: Movie Theater

### Qué pide el problema

La entrada contiene baldosas rojas con coordenadas.

En la parte 1 se busca el rectángulo de mayor área entre cualquier par de baldosas. En la parte 2 el rectángulo debe estar dentro de la zona roja o verde delimitada por el camino cerrado.

### Idea principal

La parte 1 compara pares de baldosas. La parte 2 construye la zona permitida con compresión de coordenadas y flood fill desde fuera para distinguir exterior e interior sin recorrer una cuadrícula enorme.

### Estructura y clases

```text
aoc.day09
├── MovieTheaterPuzzle
├── input
├── theater
└── solver
```

Clases importantes:

- `RedTileParser`: parsea coordenadas.
- `RedTile`: baldosa y cálculo de área inclusiva.
- `RedGreenTileArea`: zona válida de parte 2, con compresión y flood fill.
- `TheaterAreaSolver`: interfaz común.
- `UnrestrictedRectangleAreaSolver`: parte 1.
- `RedGreenRectangleAreaSolver`: parte 2.
- `LargestRectangleFinder`: fachada compatible con tests.

### Patrones y principios

Uso Strategy con `TheaterAreaSolver`, porque parte 1 no tiene restricciones y parte 2 valida contra una zona permitida.

Principios:

- SRP: parser, baldosa, zona válida y solvers están separados.
- DRY: `RedTile.rectangleAreaWith(...)` concentra la fórmula del área.
- Encapsulación: la compresión de coordenadas queda dentro de `RedGreenTileArea`.
- Bajo acoplamiento: el solver pregunta si un rectángulo cabe, sin conocer el flood fill interno.

### Tests

Los tests cubren ejemplos oficiales, parseo, área inclusiva, validación de entrada y área máxima dentro de la zona válida.

### Cómo defendería este día

Explicaría que la parte 2 sería demasiado grande si se recorrieran todas las coordenadas reales. Por eso la compresión de coordenadas reduce el problema a bloques relevantes.

## Día 10: Factory

### Qué pide el problema

Cada línea describe una máquina con luces, botones y requisitos de voltaje.

En la parte 1 los botones alternan luces y se busca el mínimo de pulsaciones para llegar al patrón objetivo. En la parte 2 los botones suman a contadores de voltaje y se busca el mínimo total de pulsaciones para cumplir requisitos.

### Idea principal

Parte 1 usa BFS sobre máscaras de bits, porque pulsar botones cambia estados de luces. Parte 2 se modela como un sistema lineal con restricciones enteras no negativas.

### Estructura y clases

```text
aoc.day10
├── FactoryPuzzle
├── factory
├── input
└── solver
```

Clases importantes:

- `FactoryManualParser`: parsea máquinas.
- `FactoryMachine`: luces, botones y requisitos.
- `ButtonWiring`: máscara de botón.
- `MachineInitializer`: algoritmos principales.
- `FactorySolver<T>`: interfaz común.
- `LightInitializationSolver`: parte 1.
- `JoltageInitializationSolver`: parte 2.

### Patrones y principios

Uso Strategy con `FactorySolver<T>`, porque parte 1 y parte 2 tienen objetivos y algoritmos distintos.

Principios:

- SRP: parser, modelo, inicializador y solvers están separados.
- DRY: `FactoryMachine` y `ButtonWiring` se comparten.
- KISS: cada solver delega en el algoritmo correspondiente.
- Encapsulación: `FactoryMachine` copia listas internas.

### Tests

Los tests cubren ejemplos oficiales, parseo, BFS de luces, resolución de voltaje y validaciones.

### Cómo defendería este día

Diría que hay dos problemas matemáticos distintos: estados binarios para luces y ecuaciones para voltaje. Por eso la separación en estrategias ayuda a explicarlo.

## Día 11: Reactor

### Qué pide el problema

La red de dispositivos es un grafo dirigido.

En la parte 1 se cuentan caminos desde `you` hasta `out`. En la parte 2 se cuentan caminos desde `svr` hasta `out` que pasan por `dac` y `fft`.

### Idea principal

El conteo usa DFS con memoización. En parte 2 la clave de memoización incluye también si ya se han visitado los dispositivos requeridos.

### Estructura y clases

```text
aoc.day11
├── ReactorPuzzle
├── input
├── reactor
└── solver
```

Clases importantes:

- `DeviceNetworkParser`: parsea el grafo.
- `DeviceNetwork`: salidas de cada dispositivo.
- `PathCounter`: DFS y memoización.
- `ReactorSolver`: interfaz común.
- `DirectPathSolver`: parte 1.
- `RequiredDevicePathSolver`: parte 2.

### Patrones y principios

Uso Strategy con `ReactorSolver`, porque cambia la regla de conteo de caminos.

Principios:

- SRP: parser, grafo, contador y solver están separados.
- DRY: `PathCounter` concentra la recursión y memoización.
- Encapsulación: `DeviceNetwork` oculta el mapa interno.
- Bajo acoplamiento: el solver trabaja con grafo, no con texto.

### Tests

Los tests cubren ejemplos oficiales, parseo, caminos directos, caminos con visitas obligatorias, ciclos y dispositivos inexistentes.

### Cómo defendería este día

Explicaría que el problema es de grafos. La memoización evita recalcular caminos desde el mismo nodo, y en parte 2 se amplía el estado con las visitas obligatorias.

## Día 12: Christmas Tree Farm

### Qué pide el problema

La entrada define formas de regalos y regiones debajo de árboles. Hay que contar en cuántas regiones caben los regalos indicados.

La solución tiene en cuenta rotaciones y volteos, y evita solapamientos entre celdas ocupadas `#`.

### Idea principal

Cada orientación de una forma genera posibles colocaciones. Las colocaciones se representan con `BitSet`, así comprobar solapamientos es rápido. Para regiones grandes se usa una comprobación directa de área cuando encaja con la optimización del código.

### Estructura y clases

```text
aoc.day12
├── ChristmasTreeFarmPuzzle
├── farm
├── input
└── solver
```

Clases importantes:

- `FarmSummaryParser`: parsea formas y regiones.
- `PresentShape`: forma base y orientaciones.
- `ShapeOrientation`: orientación normalizada.
- `TreeRegion`: región y cantidades.
- `FarmSummary`: resumen del input.
- `PresentFitter`: algoritmo de encaje.
- `ChristmasTreeFarmSolver`: interfaz de caso de uso.
- `FittingRegionSolver`: cuenta regiones válidas.

### Patrones y principios

No hay Strategy de dos partes, porque el código solo tiene una parte de cálculo. Sí hay una capa `solver` que separa el caso de uso del parser y del algoritmo de encaje.

Hay polimorfismo interno en las transformaciones de `PresentShape`, porque cada transformación sabe cómo convertir una celda.

Principios:

- SRP: parser, formas, regiones y encaje están separados.
- DRY: `PresentShape.orientations()` centraliza rotaciones y volteos.
- KISS: `BitSet` simplifica el chequeo de solapamientos.
- Encapsulación: las listas internas se copian.

### Tests

Los tests cubren el ejemplo oficial, parseo del resumen, orientaciones, encaje de regalos, rechazo por área y rechazo por dimensiones.

### Cómo defendería este día

Diría que aquí no forcé una parte 2 artificial. Lo importante es explicar el modelo de formas, orientaciones y colocaciones con `BitSet`.

## Patrones usados en el proyecto

### Strategy

Aparece cuando una interfaz tiene implementaciones diferentes para resolver partes del puzzle:

- `PasswordSolver`
- `InvalidProductIdRule`
- `JoltageSolver`
- `PaperRollSolver`
- `FreshIngredientSolver`
- `TrashCompactorSolver`
- `TachyonSolver`
- `CircuitSolver`
- `TheaterAreaSolver`
- `FactorySolver`
- `ReactorSolver`

En estos casos, Strategy ayuda porque parte 1 y parte 2 comparten entrada o modelo, pero no siempre comparten el mismo algoritmo.

### Factory Method / static factory

Aparece cuando una clase centraliza la creación desde texto o símbolo:

- `DialRotation.fromInstruction(...)`
- `RotationDirection.fromSymbol(...)`
- `MathOperation.fromSymbol(...)`

Esto reduce condiciones repartidas por el código y deja la conversión cerca del dominio.

### Iterator

Aparece en `RotationProgram`, que implementa `Iterable<DialRotation>`. Así los solvers recorren un programa de rotaciones sin depender de cómo se guarda internamente.

### Polimorfismo

Aparece en enums como `RotationDirection` y `MathOperation`, y también en las transformaciones internas de `PresentShape`.

## Principios de diseño aplicados

- SRP: los parsers leen texto, los modelos representan el dominio y los solvers calculan respuestas.
- DRY: la lógica común entre parte 1 y parte 2 se reutiliza en clases de dominio o servicios compartidos.
- KISS: las soluciones mantienen algoritmos directos y defendibles.
- YAGNI: no se añaden patrones que no aportan claridad.
- Encapsulación: muchas clases copian listas o esconden estructuras internas.
- Alta cohesión: cada paquete agrupa clases del mismo tema.
- Bajo acoplamiento: los solvers trabajan con objetos del dominio, no con líneas de texto.
- Composición sobre herencia: las clases coordinadoras reciben estrategias y servicios, en vez de crear jerarquías grandes.

## Patrones que no forcé

No usé Singleton porque no hay objetos globales necesarios. Los solvers y parsers se pueden crear normalmente.

No usé Observer porque ningún día necesita notificar cambios a varios suscriptores.

No usé Decorator porque no hay comportamiento que se añada dinámicamente a objetos existentes.

No usé Adapter porque no estoy integrando APIs externas incompatibles.

No usé Command porque las operaciones del puzzle no necesitan modelarse como comandos independientes con historial o deshacer.

La idea general fue usar patrones cuando explican una diferencia real del problema, no añadirlos solo para que aparezcan en el proyecto.

## Tests

Los tests están en `src/test/java` y cubren:

- ejemplos oficiales de cada día,
- parsers de entrada,
- modelos de dominio,
- solvers principales,
- casos de borde y errores de validación.

También mantengo los `input.txt` locales fuera de la explicación como datos de ejecución. El README no depende de subir inputs personales para justificar el diseño.
