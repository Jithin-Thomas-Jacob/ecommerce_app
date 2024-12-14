package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class activity_checkout extends AppCompatActivity {

    private RadioGroup paymentMethodGroup;
    private LinearLayout cardDetailsLayout;
    private Button confirmOrderButton;
    private EditText addressStreet, addressCity, addressProvince, addressPostalCode;
    private EditText cardNumber, cardHolderName, expiryDate, cvv;
    private TextView totalPriceView, subTotalTextView, taxTextView;
    private ImageView backButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButtonView = findViewById(R.id.backButton);
        totalPriceView = findViewById(R.id.total_price);
        subTotalTextView = findViewById(R.id.subtotal_price);
        taxTextView = findViewById(R.id.tax_price);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        cardDetailsLayout = findViewById(R.id.card_details_layout);
        confirmOrderButton = findViewById(R.id.confirm_order_button);
        addressStreet = findViewById(R.id.address_street_input);
        addressCity = findViewById(R.id.address_city_input);
        addressProvince = findViewById(R.id.address_province_input);
        addressPostalCode = findViewById(R.id.address_postalCode_input);

        cardNumber = findViewById(R.id.card_number);
        cardHolderName = findViewById(R.id.cardholder_name);
        expiryDate = findViewById(R.id.expiry_details);
        cvv = findViewById(R.id.cvv);

        double subTotal = getIntent().getDoubleExtra("subtotal_price", 0.0);
        double tax = getIntent().getDoubleExtra("tax_price", 0.0);
        double total = getIntent().getDoubleExtra("total_price", 0.0);

        subTotalTextView.setText(String.format("Sub Total: $%.2f", subTotal));
        taxTextView.setText(String.format("Tax: $%.2f", tax));
        totalPriceView.setText(String.format("Total: $%.2f", total));

        backButtonView.setOnClickListener(v -> {
            finish();
        });

        cardDetailsLayout.setVisibility(View.GONE);

        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedPaymentMethod = findViewById(checkedId);
            if (selectedPaymentMethod != null) {
                if (selectedPaymentMethod.getId() == R.id.radio_cash_payment) {
                    cardDetailsLayout.setVisibility(View.GONE);
                } else if (selectedPaymentMethod.getId() == R.id.radio_card_payment) {
                    cardDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmOrderButton.setOnClickListener(v -> {
            if (isAddressValid()) {
                RadioButton selectedPaymentMethod = (RadioButton) paymentMethodGroup.findViewById(paymentMethodGroup.getCheckedRadioButtonId());
                if (selectedPaymentMethod.getId() == R.id.radio_cash_payment) {
                    navigateToThankYouPage();
                } else {
                    if (isCardDetailsValid()) {
                        navigateToThankYouPage();
                    } else {
                        Toast.makeText(this, "Please enter valid card details", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in your address details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isAddressValid() {

        String street = addressStreet.getText().toString().trim();
        if (street.isEmpty()) {
            addressStreet.setError("Street address cannot be empty.");
            return false;
        }

        String city = addressCity.getText().toString().trim();
        if (city.isEmpty() || !city.matches("[a-zA-Z\\s]+")) {
            addressCity.setError("Please enter a valid city name.");
            return false;
        }

        String province = addressProvince.getText().toString().trim();
        if (province.isEmpty() || !isValidProvince(province)) {
            addressProvince.setError("Please enter a valid province.");
            return false;
        }

        String postalCode = addressPostalCode.getText().toString().trim();
        if (postalCode.isEmpty() || !postalCode.matches("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$")) {
            addressPostalCode.setError("Please enter a valid postal code (e.g., A1A 1A1).");
            return false;
        }

        return true;
    }

    private boolean isValidProvince(String province) {
        String[] validProvinces = {
                "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
                "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan",
                "Northwest Territories", "Nunavut", "Yukon"
        };

        for (String validProvince : validProvinces) {
            if (validProvince.equalsIgnoreCase(province)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCardDetailsValid() {
        String cardNum = cardNumber.getText().toString().replaceAll("\\D", "");
        if (cardNum.length() != 16) {
            cardNumber.setError("Please enter a valid 16-digit card number");
            return false;
        }


        if (cardHolderName.getText().toString().isEmpty() || !cardHolderName.getText().toString().matches("[a-zA-Z\\s]+")) {
            cardHolderName.setError("Please enter a valid cardholder name");
            return false;
        }

        String expiry = expiryDate.getText().toString().trim();

        if (expiry.length() == 4 && expiry.matches("^(0[1-9]|1[0-2])\\d{2}$")) {
            expiry = expiry.substring(0, 2) + "/" + expiry.substring(2);
            expiryDate.setText(expiry);
        }

        if (expiry.isEmpty() || !expiry.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            expiryDate.setError("Please enter a valid expiry date (MM/YY)");
            return false;
        }


        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        String[] dateParts = expiry.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int year = Integer.parseInt(dateParts[1]);

        year += 2000;

        if (month < 1 || month > 12) {
            expiryDate.setError("Invalid month");
            return false;
        }

        if (year > currentYear) {
        } else if (year == currentYear && month >= currentMonth) {
        } else {
            expiryDate.setError("Card has expired");
            return false;
        }

        String cvvNum = cvv.getText().toString().trim();
        if (cvvNum.isEmpty() || cvvNum.length() != 3 || !cvvNum.matches("\\d{3}")) {
            cvv.setError("Please enter a valid 3-digit CVV");
            return false;
        }

        return true;
    }

    private void navigateToThankYouPage() {
        Intent intent = new Intent(this, thankYou_activity.class); // Replace with your actual Thank You Activity
        startActivity(intent);
    }
}
