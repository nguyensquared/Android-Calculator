package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val STATE_PENDING_OPERATION = "pendingOperation"
private const val STATE_OPERAND1 = "operand1"
private const val STATE_OPERAND1_STORED = "operand1_stored"

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // Variable Declaration
    private var operand1: Double? = null
    private var operand2: Double = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        // Data Input
        val button0: Button = findViewById(R.id.zero)
        val button1: Button = findViewById(R.id.one)
        val button2: Button = findViewById(R.id.two)
        val button3: Button = findViewById(R.id.three)
        val button4: Button = findViewById(R.id.four)
        val button5: Button = findViewById(R.id.five)
        val button6: Button = findViewById(R.id.six)
        val button7: Button = findViewById(R.id.seven)
        val button8: Button = findViewById(R.id.eight)
        val button9: Button = findViewById(R.id.nine)
        val buttonDec: Button = findViewById(R.id.decimal)

        // Operations
        val buttonEq: Button = findViewById(R.id.equals)
        val buttonDiv: Button = findViewById(R.id.divide)
        val buttonMult: Button = findViewById(R.id.multiply)
        val buttonMinus: Button = findViewById(R.id.subtract)
        val buttonPlus: Button = findViewById(R.id.plus)
        val buttonNeg: Button = findViewById(R.id.neg)
        val buttonClear: Button = findViewById(R.id.clear)

        // On click listener for numbers
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDec.setOnClickListener(listener)

        // On click listener for operations
        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            displayOperation.text = pendingOperation
        }

        buttonEq.setOnClickListener(opListener)
        buttonDiv.setOnClickListener(opListener)
        buttonMult.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber.setText("")
                }
            }
        }

        buttonClear.setOnClickListener {
            val value = 0
            operand1 = null
            operand2 = 0.0
            if (value == 0){
                result.setText("")
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value }
            else {
            operand2 = value

            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = operand2
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        }
            else {
                operand1 = null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text = pendingOperation
    }
}