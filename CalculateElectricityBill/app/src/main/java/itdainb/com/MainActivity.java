package itdainb.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText OldValue, NewValue, SHBTValue;
    TextView UseWkhValue, Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        update();
    }

    void addControl() {
        OldValue = findViewById(R.id.OldValue);
        NewValue = findViewById(R.id.NewValue);
        SHBTValue = findViewById(R.id.SHBTValue);
        UseWkhValue = findViewById(R.id.UseWkhValue);
        Result = findViewById(R.id.Result);
    }

    public int getInt(TextView view) {
        return Integer.parseInt(view.getText().toString());
    }

    public double getDouble(TextView view) {
        return Double.parseDouble(view.getText().toString());
    }

    void clear() {
        UseWkhValue.setText("");
        Result.setText("");
    }

    void update() {
        OldValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                clear();

                if (OldValue.getText().toString().trim().equalsIgnoreCase("")) {
                    OldValue.setError("Chỉ số cũ không được để trống!");
                    return;
                }

                if (NewValue.getText().toString().trim().equalsIgnoreCase("")) {
                    return;
                }

                int I_OldValue = getInt(OldValue);
                int I_NewValue = getInt(NewValue);

                if (I_NewValue < I_OldValue) {
                    OldValue.setError("Chỉ số cũ phải nhỏ hơn chỉ số mới!");
                    return;
                }

                UseWkhValue.setText("" + (I_NewValue - I_OldValue) + " kWh");
            }
        });

        NewValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                clear();

                if (NewValue.getText().toString().trim().equalsIgnoreCase("")) {
                    NewValue.setError("Chỉ số mới không được để trống!");
                    return;
                }

                int I_NewValue = getInt(NewValue);

                if (OldValue.getText().toString().trim().equalsIgnoreCase("")) {
                    return;
                }

                int I_OldValue = getInt(OldValue);

                if (I_NewValue < I_OldValue) {
                    NewValue.setError("Chỉ số mới phải lớn hơn chỉ số cũ!");
                    return;
                }

                UseWkhValue.setText("" + (I_NewValue - I_OldValue) + " kWh");
            }
        });
    }

    public void Delete(View view) {
        OldValue.setText("");
        NewValue.setText("");
        SHBTValue.setText("");
        UseWkhValue.setText("");
        Result.setText("");

        OldValue.setError(null);
        NewValue.setError(null);
        SHBTValue.setError(null);

        OldValue.requestFocus();
    }

    public void Exit(View view) {
        finish();
        System.exit(0);
    }

    public void showResult(CharSequence nganh, double price) {
        int I_price = (int) price;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        Result.setText("Tổng tiền điện giá " + nganh + ":\n" + nf.format(I_price) + " VNĐ");
    }

    public void CalcSHBT(View view) {
        SHBTValue.setError(null);

        if (OldValue.getError() != null || NewValue.getError() != null) {
            return;
        }

        if (SHBTValue.getText().toString().trim().equalsIgnoreCase("")) {
            SHBTValue.setError("Số hộ (SHBT) không được để trống!");
            SHBTValue.requestFocus();
            return;
        }

        int I_OldValue = getInt(OldValue);
        int I_NewValue = getInt(NewValue);

        int B_A =  I_NewValue - I_OldValue;
        double C = getDouble(SHBTValue);

        double price;

        if (B_A <= 50 * C) {
            price = B_A * 1484;
        } else if (B_A <= 100 * C) {
            price = (50*C * 1484) + (B_A - 50*C) * 1533;
        } else if (B_A <= 200 * C) {
            price = (50*C * 1484) + (50*C * 1533) + (B_A - 100*C) * 1786;
        } else if (B_A <= 300 * C) {
            price = (50*C * 1484) + (50*C * 1533) + (100*C * 1786) + (B_A - 200*C) * 2242;
        } else if (B_A <= 400 * C) {
            price = (50*C * 1484) + (50*C * 1533) + (100*C * 1786) + (100*C * 2242) + (B_A - 300*C) * 2503;
        } else {
            price = (50*C * 1484) + (50*C * 1533) + (100*C * 1786) + (100*C * 2242) + (100*C * 2503) + (B_A - 400*C) * 2587;
        }

        showResult("sinh hoạt (" + C + ") hộ", price);
//        Result.setText("Tổng tiền điện giá sinh hoạt (" + C + ") hộ:\n" + price + " VNĐ");
    }

    public void CalcKD(View view) {
        SHBTValue.setError(null);

        if (OldValue.getError() != null || NewValue.getError() != null) {
            return;
        }

        int I_OldValue = getInt(OldValue);
        int I_NewValue = getInt(NewValue);

        int price = (I_NewValue - I_OldValue) * 2320;

        showResult("kinh doanh", price);
//        Result.setText("Tổng tiền điện giá kinh doanh:\n" + price + " VNĐ");
    }

    public void CalcSX(View view) {
        SHBTValue.setError(null);

        if (OldValue.getError() != null || NewValue.getError() != null) {
            return;
        }

        int I_OldValue = getInt(OldValue);
        int I_NewValue = getInt(NewValue);

        int price = (I_NewValue - I_OldValue) * 1518;

        showResult("sản xuất", price);
//        Result.setText("Tổng tiền điện giá sản xuất:\n" + price + " VNĐ");
    }
}