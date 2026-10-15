# MVVM

## ¿Qué es?

M: Model \
V: View \
VM: ViewModel

## Model

Capa de datos (una clase, data class, etc.), por ejemplo:

````kotlin
data class Contador(
    val valor: Int = 0
)
````

## ViewModel

El lugar donde vive el dato actual y su lógica. Sobrevive a cambios de configuración (como al rotar 
la pantalla), si no se usara, al rotar la pantalla se reinicia el dato guardado, por ejemplo:

````kotlin
class ContadorViewModel : ViewModel() {
    private val _contador = MutableStateFlow(Contador())
    val contador = _contador.asStateFlow()

    fun incrementar() {
        _contador.update { actual -> actual.copy(valor = actual.valor + 1) }
    }

    fun decrementar() {
        _contador.update { actual ->
            actual.copy(valor = actual.valor - 1)
        }
    }
}
````

### MutableStateFlow

Es nativo de Kotlin, permite suscribirse a sus cambios con `.collect()`, es decir, cada vez que el
valor guardado cambie, se ejecutará el código que le demos al `.collect()`.

### asStateFlow

No queremos modificar el valor directamente en la interfáz gráfica, así que usamos `asStateFlow()`
para leer y suscribirnos al valor, pero sin tener las funciones para modificar el valor.

## View

Interfáz gráfica para el usuario, por ejemplo:

````kotlin
@Composable
@Preview
fun ContadorScreen(
    viewModel: ContadorViewModel = viewModel()
) {
    val contador by viewModel.contador.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        Text("Contador: ${contador.valor}")

        Button(
            onClick = { viewModel.incrementar() }
        ) {
            Text("Incrementar")
        }

        Button(
            onClick = { viewModel.decrementar() }
        ) {
            Text("Decrementar")
        }
    }
}
````

### viewModel()

La activity tiene un contenedor donde guarda los view models, para obtener el que queremos, debemos
específicar el que queremos, si no existe en su depósito, lo crea y nos lo da.

### by

Es otra funcionalidad nativa de Kotlin llamada **delegación**. Sirve para reutilizar getters y setters.
Por ejemplo, si tenemos 10 clases y queremos tener la misma lógica en una propiedad/atributo suyo en
las 10, no queremos repetir la misma lógica 10 veces, para eso sirve el **by**. Por ejemplo:

````kotlin
var nombre: String = "Maxi"
    get() {
        println("Leyendo nombre")
        return field
    }
    set(value) {
        println("Cambiando nombre")
        field = value
    }
````

Al obtener o modificar el valor, se ejecutará esa lógica.

Kotlin permite que un objeto externo se encargue de leer o escribir una propiedad:

````kotlin
var nombre: String by Delegates.observable("Maxi") {
    propiedad, anterior, nuevo ->
    println("Cambió de $anterior a $nuevo")
}

nombre = "Pedro"
````

Salida:
````text
Cambió de Maxi a Pedro
````

Y no tuvimos que implementar manualmente un setter con lógica personalizada.

Internamente, funciona utilizando operadores especiales llamados `getValue` y si es modificable 
`setValue`. Creemos nuestro propio delegado:

````kotlin
class MiDelegado {

    private var valor = 0

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): Int {
        println("Leyendo ${property.name}")
        return valor
    }

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        nuevoValor: Int
    ) {
        println("Modificando ${property.name}")
        valor = nuevoValor
    }
}
````
````kotlin
var contador: Int by MiDelegado()

contador = 10

println(contador)
````

Salida:
````text
Modificando contador
Leyendo contador
10
````

Cuando escribimos `contador = 10`, kotlin lo traduce conceptualmente a una llamada: 
`delegado.setValue(..., 10)`. Cuando leemos: `println(contador)` usa `delegado.getValue()`.
