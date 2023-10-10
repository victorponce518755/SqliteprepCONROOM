package alanis.jorge.sqliteprep
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    private lateinit var db: AppDatabase
    private lateinit var txtResults: TextView


    // PARA EL ROOM HAY QUE MODIFICAR EL GRADLE KTS MODULE APP Y AGREGAR LOS PLUGINS DE ROOM
    // Y KAPT
    // EN EL GRADLE KTS MODULE APP AGREGAR LAS DEPENDENCIAS DE ROOM
    // EN PLUGINS AGREGAR
    // id("org.jetbrains.kotlin.kapt")
    // EN DEPENDENCIAS AGREGAR
    //implementation("androidx.room:room-runtime:2.5.2")
    // kapt("androidx.room:room-compiler:2.5.2")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")


    //Despues de esto genero una base de datos nueva, en nuestro folder del proyecto debe ser kotlin class
    // y le ponemos el nombre de la base de datos, en este caso AppDatabase, generamos los archivos que dicen ahi dentro


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        db= Room.databaseBuilder(applicationContext, AppDatabase::class.java, "MyDatabase.db").build()

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtAge = findViewById<EditText>(R.id.edtAge)
        val edtUserId = findViewById<EditText>(R.id.edtUserId)
        txtResults = findViewById(R.id.txtResults)

        val btnInsert = findViewById<Button>(R.id.btnInsert)
        val btnRetrieve = findViewById<Button>(R.id.btnRetrieve)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnInsert.setOnClickListener {
            val name = edtName.text.toString()
            val age = edtAge.text.toString().toIntOrNull()

            if (name.isNotBlank() && age != null) {
                //val success = databaseHelper.addUser(name, age)
                //showToast(success)
            GlobalScope.launch(Dispatchers.IO) {//aqui mando a llamar el hilo----------
                val user = User(name = name, age = age) // estp es nuevo
                val result = db.userDao().insertUser(user) // esto es nuevo
                launch (Dispatchers.Main){
                    showToast(result != -1L) // esto es nuevo
                }
            }

            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRetrieve.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {//aqui mando a llamar el hilo----------
                val users = db.userDao().getAllUsers() // esto es nuevo

                launch(Dispatchers.Main)
                {
                    val output = StringBuilder() // esto es nuevo



                for (user in users) { // esto es nuevo
                    output.append("Id: ${user.id}, Nombre: ${user.name}, Edad: ${user.age}\n")
                } // esto es nuevo


                txtResults.text = output.toString()
            }
        }

        }


        btnUpdate.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()
            val name = edtName.text.toString()
            val age = edtAge.text.toString().toIntOrNull()

            if (id != null && name.isNotBlank() && age != null) {

                GlobalScope.launch(Dispatchers.IO) {//aqui mando a llamar el hilo----------
                val user = User(id=id,name=name,age= age) // esto es nuevo
                val result = db.userDao().updateUser(user) // esto es nuevo
                launch(Dispatchers.Main)
                    { showToast(result > 0) }
                }
            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()

            if (id != null) {
                GlobalScope.launch(Dispatchers.IO) {//aqui mando a llamar el hilo----------
                    val user = User(id = id, name = "", age = 0) // esto es nuevo
                    val result = db.userDao().deleteUser(user) // esto es nuevo
                    launch(Dispatchers.Main) {
                        showToast(result > 0)
                    }
                }
            } else {
                Toast.makeText(this, "Favor de introducir un id valido!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(success: Boolean) {
        if (success) {
            Toast.makeText(this, "Operacion exitosa", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Operacion fallida!", Toast.LENGTH_SHORT).show()
        }
    }
}

// si nos da error en la version cambiamos las versiones en el gradle kts module app
// compileOPtions{
// sourceCompatibility = JavaVersion.VERSION_17
// targetCompatibility = JavaVersion.VERSION_17
// }
//
// kotlinOptions{
// jvmTarget = "17"
// }
//


// ademas agregamos cada que se ejecute la bd , un launch para que se ejecute en un hilo diferente