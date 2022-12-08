package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.app.databinding.ActivityMainBinding
import java.sql.*
import java.util.Properties

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var conn: Connection? = null
    private var username = "root"
    private var password = "admin"
    private var ipWS = "192.168.1.57"
    private var port = "3306"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        insertRow();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun insertRow() {
        val singer = findViewById<EditText>(R.id.txtSinger)
        val sendButton = findViewById<Button>(R.id.btnSave)
        sendButton.setOnClickListener {
            if (executeQuery("${singer.text}")) {
                Toast.makeText(this, "Cantante registrado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun executeQuery(value: String): Boolean {
        var stmt: Statement? = null
        getConnection()

        try {
            stmt = conn!!.createStatement()
            return stmt.execute("INSERT INTO singer ('name') VALUES ('${value}');")
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return false
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {}
                stmt = null
            }
            if (conn != null) {
                try {
                    conn!!.close()
                } catch (sqlEx: SQLException) {}
                conn = null
            }
        }
    }

    private fun getConnection() {
        val connectionProps = Properties()
        connectionProps["user"] = username
        connectionProps["password"] = password

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection("jdbc:mysql://$ipWS:$port/students", connectionProps)
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}