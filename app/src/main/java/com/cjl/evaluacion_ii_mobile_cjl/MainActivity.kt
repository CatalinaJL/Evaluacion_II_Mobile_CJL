package com.cjl.evaluacion_ii_mobile_cjl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.cjl.evaluacion_ii_mobile_cjl.db.Producto
import com.cjl.evaluacion_ii_mobile_cjl.db.ProductosDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // llamada a metodo lifecyclescope para la DB Producto
        lifecycleScope.launch(Dispatchers.IO) {
            val productsDao = ProductosDB.getInstance(this@MainActivity)
            val cantProductos = productsDao.getCantProductos()

            // if para indicar que si la cantidad de productos es menor que uno
            // se muestren los que se indican en la app por defecto
            if(cantProductos<1){
                productsDao.insertProducto(Producto(0, "Pasta", true))
                productsDao.insertProducto(Producto(1, "Salsa de Tomates", false))
                productsDao.insertProducto(Producto(1, "Queso Rallado", true))
                productsDao.insertProducto(Producto(1, "Champiñones", true))
            }

        }

        setContent {
          ListadoProductosUI()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListadoProductosUI(){

    val contexto = LocalContext.current

    // variables de estado

    val (productos, setProductos) = remember{ mutableStateOf(emptyList<Producto>()) }

    // funcion efecto secundario de corrutinas
    LaunchedEffect(productos) {
        withContext(Dispatchers.IO){
            val dao=ProductosDB.getInstance(contexto)
            setProductos(dao.getProductos())
        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally
    ){
        Text("ShopList!")
        Spacer(modifier=Modifier.height(20.dp))
        // Lazy Column para mostrar productos
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(productos){ product ->
                ProductoItemUI(product){
                    setProductos(emptyList<Producto>())
                }
            }
        }

        Spacer(modifier=Modifier.height(20.dp))
        Button(onClick = {
            val intent= Intent(contexto,AgregarProducto::class.java)
            contexto.startActivity(intent)
        }){
            Text("Agregar Producto")
        }


    }

}

@Composable
fun ProductoItemUI(producto:Producto, onSave:() -> Unit ={}){
    //llamado a corrutina, ya que nos encontramos fuera del alcance
    val alcanceCorrutina= rememberCoroutineScope()
    // variable de contexto para obtenerlo
    val context = LocalContext.current

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ){
        if(producto.comprado){
            Icon(
                Icons.Filled.Check,
                contentDescription = "Producto Comprado",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch(Dispatchers.IO){
                        val dao =ProductosDB.getInstance(context)
                        producto.comprado=false
                        dao.updateProducto(producto)
                        onSave()
                    }

                }
            )
        }else{
            Icon(
                Icons.Filled.Clear,
                contentDescription = "Producto Pendiente",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch(Dispatchers.IO){
                        val dao =ProductosDB.getInstance(context)
                        producto.comprado=true
                        dao.updateProducto(producto)
                        onSave()
                    }

                }
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = producto.producto,
            modifier= Modifier.weight(2f))
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Eliminar Producto",
            modifier = Modifier.clickable {
                alcanceCorrutina.launch(Dispatchers.IO){
                    val dao =ProductosDB.getInstance(context)
                    dao.deleteProducto(producto)
                    onSave()

                }

            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProductoItemUIPreview(){
//    val producto=Producto(0, "Mandarinas", false)
     //ProductoItemUI(producto)

//}

