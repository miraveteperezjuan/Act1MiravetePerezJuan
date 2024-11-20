package com.example.act1miraveteperezjuan

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Declaración de variables
    private lateinit var salario: EditText
    private lateinit var pagas: EditText
    private lateinit var edad: EditText
    private lateinit var grupoProfesional: EditText
    private lateinit var discapacidad: EditText
    private lateinit var estado: EditText
    private lateinit var hijos: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()

        // Listener del botón
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginButton).setOnClickListener {
            if (validateInputs()) calculateAndNavigate()
        }
    }

    private fun initComponents() {
        // Inicialización de componentes visuales
        salario = findViewById(R.id.TextNumberSalario)
        pagas = findViewById(R.id.textNumberPagas)
        edad = findViewById(R.id.textNumberEdad)
        grupoProfesional = findViewById(R.id.editTextGrupoProfesional)
        discapacidad = findViewById(R.id.editDiscapacidad)
        estado = findViewById(R.id.estadoCivil)
        hijos = findViewById(R.id.textHijo)
    }

    private fun validateInputs(): Boolean {
        // Comprueba si todos los campos están llenos
        return listOf(salario, pagas, edad, grupoProfesional, discapacidad, estado, hijos).all {
            it.text.isNotEmpty()
        }.also { valid ->
            if (!valid) displayErrors()
        }
    }

    private fun calculateAndNavigate() {
        // Recoger datos
        val salarioBruto = salario.text.toString().toDouble()
        val numeroPagas = pagas.text.toString().toInt()
        val edadUsuario = edad.text.toString().toInt()
        val discapacidadPorcentaje = discapacidad.text.toString().toDouble()
        val estadoCivil = estado.text.toString().lowercase()
        val numHijos = hijos.text.toString().toInt()

        // Calcular retenciones y salario neto
        val retencionBase = when {
            salarioBruto > 60000 -> 0.45
            salarioBruto > 30000 -> 0.37
            salarioBruto > 12000 -> 0.20
            else -> 0.10
        }

        // Calcular las reducciones
        val reduccionHijos = numHijos * 0.02  // 2% por cada hijo
        val reduccionDiscapacidad = if (discapacidadPorcentaje > 0) 0.05 else 0.0  // 5% si tiene discapacidad
        val reduccionEdad = if (edadUsuario >= 65) 0.03 else 0.0  // 3% si tiene más de 65 años
        val reduccionEstadoCivil = if (estadoCivil == "casado") 0.02 else 0.0  // 2% si está casado

        // Deducciones adicionales
        val deduccionVivienda = salarioBruto * 0.05  // 5% por pago de hipoteca o alquiler
        val deduccionAhorro = salarioBruto * 0.03    // 3% por ahorro en plan de pensiones
        val deduccionEducacion = numHijos * 0.02    // 2% por cada hijo en educación superior

        // Sumar todas las reducciones y deducciones
        val reduccionTotal = reduccionHijos + reduccionDiscapacidad + reduccionEdad + reduccionEstadoCivil
        val deduccionTotal = deduccionVivienda + deduccionAhorro + deduccionEducacion

        // Calcular la retención final
        val retencionFinal = maxOf(retencionBase - reduccionTotal, 0.0)  // No puede ser negativa

        // Calcular el salario neto
        val salarioNeto = salarioBruto * (1 - retencionFinal)

        // Calcular el salario neto después de deducciones
        val salarioNetoFinal = salarioNeto - deduccionTotal

        // Navegar al resultado
        navigateToResult(salarioBruto, retencionFinal, salarioNetoFinal, deduccionTotal)
    }

    private fun navigateToResult(salarioBruto: Double, retencionIRPF: Double, salarioNeto: Double, deduccionesTotal: Double) {
        val intent = Intent(this, ResultBankActivity::class.java).apply {
            putExtra("SALARIO_BRUTO_KEY", salarioBruto)
            putExtra("RETENCION_IRPF_KEY", retencionIRPF)
            putExtra("SALARIO_NETO_KEY", salarioNeto)
            putExtra("DEDUCCIONES_TOTAL_KEY", deduccionesTotal)  // Pasamos las deducciones adicionales
        }
        startActivity(intent)
    }

    private fun displayErrors() {
        // Mostrar mensaje en campos vacíos
        listOf(
            salario to "Introduce el salario",
            pagas to "Introduce las pagas",
            edad to "Introduce tu edad",
            grupoProfesional to "Introduce tu grupo profesional",
            discapacidad to "Introduce tu grado de discapacidad",
            estado to "Introduce tu estado civil",
            hijos to "Introduce el número de hijos"
        ).forEach { (field, error) ->
            if (field.text.isEmpty()) field.error = error
        }
    }
}