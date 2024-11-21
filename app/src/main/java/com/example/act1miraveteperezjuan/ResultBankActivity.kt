package com.example.act1miraveteperezjuan

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultBankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_bank)

        // Ajustar padding según las barras de sistema (como la barra de estado y navegación)
        val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recoger los datos enviados desde MainActivity
        val salarioBruto = intent.getDoubleExtra("SALARIO_BRUTO_KEY", 0.0)
        val retencionIRPF = intent.getDoubleExtra("RETENCION_IRPF_KEY", 0.0)
        val salarioNeto = intent.getDoubleExtra("SALARIO_NETO_KEY", 0.0)
        val deduccionesTotal = intent.getDoubleExtra("DEDUCCIONES_TOTAL_KEY", 0.0)

        // Referencia al TextView donde se mostrarán los resultados
        val resultTextView = findViewById<TextView>(R.id.resultTVBank)

        // Crear el texto de resultado con los valores recibidos, sin decimales
        val resultText = """
        Salario Bruto Anual: %.0f €
        Retención IRPF: %.0f%%
        Salario Neto Anual: %.0f €
        Deducciones adicionales: %.0f €
        """.trimIndent().format(salarioBruto, retencionIRPF * 100, salarioNeto, deduccionesTotal)


        // Establecer el texto formateado en el TextView
        resultTextView.text = resultText

        // Establecer un estilo para que se vea más profesional
        resultTextView.textSize = 20f // Tamaño de texto más grande
        resultTextView.setTextColor(getColor(R.color.bankinterDarkGray)) // Color negro para el texto
        resultTextView.setLineSpacing(1.5f, 1.0f) // 1.5f es el espacio adicional entre líneas

        val saludTextView = findViewById<TextView>(R.id.resultTVSalud)
        val (saludFinanciera, color) = evaluarSaludFinanciera(salarioNeto, deduccionesTotal, retencionIRPF)
        saludTextView.text = saludFinanciera
        saludTextView.textSize = 22f
        saludTextView.setTextColor(color)
    }

        private fun evaluarSaludFinanciera(salarioNeto: Double, deducciones: Double, retencionIRPF: Double): Pair<String, Int> {
        // Calculamos el porcentaje de deducción
        val porcentajeDeduccion = deducciones / salarioNeto
        var salud = "mala"
        var color = Color.RED

        // Determinamos la salud financiera y el color según las condiciones
        if (salarioNeto > 25000 && porcentajeDeduccion < 0.1 && retencionIRPF < 0.2) {
            salud = "buena"
            color = Color.GREEN
        } else if (salarioNeto > 15000 && porcentajeDeduccion < 0.2) {
            salud = "media"
            color = Color.YELLOW
        }

        // Devolvemos el texto y el color
        return "Mi salud financiera es $salud" to color
    }

}

