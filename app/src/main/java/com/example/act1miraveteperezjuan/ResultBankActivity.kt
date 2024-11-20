package com.example.act1miraveteperezjuan

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
    }
}

