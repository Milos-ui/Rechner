package at.ac.tgm.hit.sew7.mtomi.calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText firstNumberEditText;
    private EditText secondNumberEditText;
    private EditText resultEditText;
    private RadioGroup calculationTypeRadioGroup;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNumberEditText = findViewById(R.id.firstNumberEditText);
        secondNumberEditText = findViewById(R.id.secondNumberEditText);
        resultEditText = findViewById(R.id.resultEditText);
        calculationTypeRadioGroup = findViewById(R.id.calculationTypeRadioGroup);

        // Initialisierung der SharedPreferences
        sharedPreferences = getSharedPreferences("CalculatorMemory", MODE_PRIVATE);

        // Laden der gespeicherten Werte beim Start der App
        recallMemory();

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        // Button "MS" (Memory Store)
        Button memoryStoreButton = findViewById(R.id.memoryStoreButton);
        memoryStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeMemory(); // Speichern der aktuellen Werte
            }
        });

        // Button "MR" (Memory Recall)
        Button memoryRecallButton = findViewById(R.id.memoryRecallButton);
        memoryRecallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recallMemory(); // Wiederherstellen der gespeicherten Werte
            }
        });
    }

    private void calculate() {
        String firstNumberStr = firstNumberEditText.getText().toString();
        String secondNumberStr = secondNumberEditText.getText().toString();

        if (!firstNumberStr.isEmpty() && !secondNumberStr.isEmpty()) {
            double firstNumber = Double.parseDouble(firstNumberStr);
            double secondNumber = Double.parseDouble(secondNumberStr);

            int selectedRadioButtonId = calculationTypeRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

            if (selectedRadioButton != null) {
                String operator = selectedRadioButton.getText().toString();
                double result = performCalculation(firstNumber, secondNumber, operator);
                resultEditText.setText(String.valueOf(result));
            }
        }
    }

    private double performCalculation(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private void storeMemory() {
        // Zahl und Operator holen
        String firstNumber = firstNumberEditText.getText().toString();
        String secondNumber = secondNumberEditText.getText().toString();
        int selectedRadioButtonId = calculationTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String operator = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        // Ergebniss holen
        String result = resultEditText.getText().toString();

        // speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstNumber", firstNumber);
        editor.putString("secondNumber", secondNumber);
        editor.putString("operator", operator);
        editor.putString("result", result);
        editor.apply();
    }

    private void recallMemory() {
        // abrufen
        String savedFirstNumber = sharedPreferences.getString("firstNumber", "");
        String savedSecondNumber = sharedPreferences.getString("secondNumber", "");
        String savedOperator = sharedPreferences.getString("operator", "");
        String savedResult = sharedPreferences.getString("result", "");

        // set
        firstNumberEditText.setText(savedFirstNumber);
        secondNumberEditText.setText(savedSecondNumber);

        RadioButton radioButton = findViewById(getResources().getIdentifier(savedOperator, "id", getPackageName()));
        if (radioButton != null) {
            radioButton.setChecked(true);
        }

        resultEditText.setText(savedResult);
    }
}

