package com.example.numad25sp_changliu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuicCalcActivity extends AppCompatActivity {
    private TextView calcDisplay;
    private StringBuilder inputExpression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quic_calc);

        calcDisplay = findViewById(R.id.calc_display);
        setupButtons();
    }

    private void setupButtons() {
        int[] buttonIds = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_add, R.id.btn_subtract, R.id.btn_equals, R.id.btn_backspace};

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String text = button.getText().toString();

                if (text.equals("=")) {
                    evaluateExpression();
                } else if (text.equals("x")) {
                    if (inputExpression.length() > 0) {
                        inputExpression.deleteCharAt(inputExpression.length() - 1);
                    }
                } else {
                    inputExpression.append(text);
                }
                updateDisplay();
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void updateDisplay() {
        if (inputExpression.length() == 0) {
            calcDisplay.setText("CALC");
        } else {
            calcDisplay.setText(inputExpression.toString());
        }
    }

    private void evaluateExpression() {
        try {
            double result = eval(inputExpression.toString());
            inputExpression.setLength(0);
            inputExpression.append(result);
        } catch (Exception e) {
            inputExpression.setLength(0);
            calcDisplay.setText("Error");
            return;
        }
        updateDisplay();
    }

    private double eval(String expression) {
        return new org.mozilla.javascript.ContextFactory().call(cx -> {
            return (double) cx.evaluateString(cx.initStandardObjects(), expression, "calc", 1, null);
        });
    }
}
