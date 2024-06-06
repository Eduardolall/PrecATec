package com.example.preatec

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.preatec.databinding.ActivityWmenuBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class Wmenu : AppCompatActivity() {
    private lateinit var binding: ActivityWmenuBinding
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L // 1 segundo
    private val entries = ArrayList<Entry>()
    private val heartRates = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWmenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mtToolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.mtToolbar,
            R.string.close_drawer, R.string.open_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.nvMenuLateral.setNavigationItemSelectedListener {
            onNavigationItemSelected(it)
            true
        }
        startFetchingData()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_call -> makePhoneCall("4423645258")
            R.id.menu_camera -> openCamera()
            R.id.menu_gallery -> openGallery()
            R.id.menu_agenda -> addEventToCalendar()
            else -> Toast.makeText(this, "Other menu item selected", Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        } else {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 2)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 100)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 200)
    }

    private fun addEventToCalendar() {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, "New Event")
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Somewhere")
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            System.currentTimeMillis() + 60 * 60 * 1000
        )
        startActivity(intent)
    }

    private fun startFetchingData() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                fetchData()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun fetchData() {
        // Se crea la URL para obtener los sensores
        val url = Uri.parse("https://preactec-server.onrender.com/sensor/getV")
            .buildUpon()
            .build().toString()

        // Se crea una petición de tipo JsonObjectRequest para obtener los sensores con GET
        val peticion = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                // Imprime la respuesta completa del servidor en los logs
                Log.d("initChartAndTable", "Response received: $response")

                // La respuesta es un objeto JSON con los campos "value" y "time"
                val value = response.optString("value")
                val time = response.optString("time")

                if (value.isEmpty() || time.isEmpty()) {
                    Log.e("initChartAndTable", "Invalid JSON response structure")
                    Toast.makeText(this, "Invalid JSON response structure", Toast.LENGTH_LONG)
                        .show()
                    return@JsonObjectRequest
                }

                // Convierte los valores a Float y añade a la lista de entradas
                val valueFloat = value.toFloat()
                val timeFloat = time.toFloat()
                Log.d("initChartAndTable", "Adding data point: time=$timeFloat, value=$valueFloat")
                entries.add(Entry(timeFloat, valueFloat))
                heartRates.add(valueFloat.toInt())

                // Se actualiza la gráfica en el hilo principal
                runOnUiThread {
                    val dataSet = LineDataSet(entries, "Pulsos Cardíacos")
                    dataSet.color = resources.getColor(R.color.colorPrimaryDark, null)
                    dataSet.valueTextColor = resources.getColor(R.color.colorAccent, null)

                    binding.chart.data = LineData(dataSet)
                    binding.chart.invalidate()

                    updateTableWithData(heartRates)
                }
            } catch (e: Exception) {
                Log.e("initChartAndTable", "Error processing response: ${e.message}")
                Toast.makeText(this, "Error processing response: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }, { error ->
            // Si la petición no es exitosa se muestra un mensaje de error
            Log.e("initChartAndTable", "Error fetching data: $error")
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(peticion)
    }

    private fun updateTableWithData(heartRates: List<Int>) {
        // Utiliza binding para encontrar la tabla
        val table = binding.tableLayout
        table.removeAllViews()

        val header = TableRow(this)
        val headerTime = TextView(this)
        headerTime.text = "Time"
        headerTime.setPadding(8, 8, 8, 8)
        val headerRate = TextView(this)
        headerRate.text = "Rate"
        headerRate.setPadding(8, 8, 8, 8)
        header.addView(headerTime)
        header.addView(headerRate)
        table.addView(header)

        heartRates.forEachIndexed { index, bpm ->
            val row = TableRow(this)
            val columnIndex = TextView(this)
            columnIndex.text = "$index"
            columnIndex.setPadding(8, 8, 8, 8)
            val columnBpm = TextView(this)
            columnBpm.text = bpm.toString()
            columnBpm.setPadding(8, 8, 8, 8)

            row.addView(columnIndex)
            row.addView(columnBpm)
            table.addView(row)
        }
    }
}
