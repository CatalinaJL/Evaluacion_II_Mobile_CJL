package com.cjl.evaluacion_ii_mobile_cjl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.cjl.evaluacion_ii_mobile_cjl.db.Producto
import com.cjl.evaluacion_ii_mobile_cjl.db.ProductosDB
import com.cjl.evaluacion_ii_mobile_cjl.ui.theme.Evaluacion_II_Mobile_CJLTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarProducto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // llamada a metodo lifecyclescope para la DB Producto
        lifecycleScope.launch(Dispatchers.IO) {
            val productsDao = ProductosDB.getInstance(this@AgregarProducto)
        }

        setContent {
           AgregarProductoUI()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AgregarProductoUI() {
    val contexto = LocalContext.current
    // Obtención dato TextField
    var product by remember { mutableStateOf(" ") }
    val (productos, setProductos) = remember{ mutableStateOf(emptyList<Producto>()) }
    // funcion efecto secundario de corrutinas
    LaunchedEffect(productos) {
        withContext(Dispatchers.IO){
            val dao=ProductosDB.getInstance(contexto)
            setProductos(dao.getProductos())
        }

    }

    val alcanceCorrutina= rememberCoroutineScope()


    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(text = "Ingrese el producto")

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = " ",
            onValueChange = { product = it},
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            alcanceCorrutina.launch(Dispatchers.IO){
                val dao = ProductosDB.getInstance(contexto)
                //val nombreProducto = dao.insertProducto()


            }

        }) {
            Text(text = "Agregar producto")
        }



        Spacer(modifier = Modifier.height(80.dp))
        Button(onClick = {
            val intent = Intent(contexto, MainActivity::class.java)
            contexto.startActivity(intent)
        }){
            Text("Inicio")
        }

    }
}