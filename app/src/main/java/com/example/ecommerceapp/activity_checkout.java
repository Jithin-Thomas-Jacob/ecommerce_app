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

    // Declare instance variables
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

        // Set up window insets to adjust for system UI (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the views
        backButtonView = findViewById(R.id.backButton);
        totalPriceView = findViewById(R.id.total_price);
        subTotalTextView = findViewById(R.id.subtotal_price);
        taxTextView = findViewById(R.id.tax_price);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        cardDetailsLayout = findViewById(R.id.card_details_layout); // Ensure this ID exists in XML
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

        // Set up the RadioGroup listener to toggle the visibility of the card details layout
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedPaymentMethod = findViewById(checkedId);
            if (selectedPaymentMethod != null) {
                if (selectedPaymentMethod.getId() == R.id.radio_cash_payment) {
                    // Hide card details layout if Cash on Delivery is selected
                    cardDetailsLayout.setVisibility(View.GONE);
                } else if (selectedPaymentMethod.getId() == R.id.radio_card_payment) {
                    // Show card details layout if Debit/Credit Card is selected
                    cardDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up the confirm order button click listener to navigate to the thank you page
        confirmOrderButton.setOnClickListener(v -> {
            if (isAddressValid()) {
                // If Cash on Delivery is selected
                RadioButton selectedPaymentMethod = (RadioButton) paymentMethodGroup.findViewById(paymentMethodGroup.getCheckedRadioButtonId());
                if (selectedPaymentMethod.getId() == R.id.radio_cash_payment) {
                    // Proceed to thank you page (Cash on Delivery)
                    navigateToThankYouPage();
                } else {
                    // Proceed to thank you page (Card Payment)
                    if (isCardDetailsValid()) {
                        // Validate the card details (if necessary)
                        navigateToThankYouPage();
                    } else {
                        Toast.makeText(this, "Please enter valid card details", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Show validation error for address
                Toast.makeText(this, "Please fill in your address details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to validate the address fields
    private boolean isAddressValid() {
        // Validate street address
        String street = addressStreet.getText().toString().trim();
        if (street.isEmpty()) {
            addressStreet.setError("Street address cannot be empty.");
            return false;
        }

        // Validate city (only letters and spaces)
        String city = addressCity.getText().toString().trim();
        if (city.isEmpty() || !city.matches("[a-zA-Z\\s]+")) {
            addressCity.setError("Please enter a valid city name.");
            return false;
        }

        // Validate province
        String province = addressProvince.getText().toString().trim();
        if (province.isEmpty() || !isValidProvince(province)) {
            addressProvince.setError("Please enter a valid province.");
            return false;
        }

        // Validate postal code (Canadian postal code format)
        String postalCode = addressPostalCode.getText().toString().trim();
        if (postalCode.isEmpty() || !postalCode.matches("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$")) {
            addressPostalCode.setError("Please enter a valid postal code (e.g., A1A 1A1).");
            return false;
        }

        return true;
    }

    // Helper method to validate Canadian provinces
    private boolean isValidProvince(String province) {
        String[] validProvinces = {
                "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
                "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan",
                "Northwest Territories", "Nunavut", "Yukon"
        };

        // Check if the province is valid
        for (String validProvince : validProvinces) {
            if (validProvince.equalsIgnoreCase(province)) {
                return true;
            }
        }
        return false;
    }

    // Function to validate the card details with industry-based checks
    private boolean isCardDetailsValid() {
        // Validate card number (16 digits)
        String cardNum = cardNumber.getText().toString().replaceAll("\\D", "");
        if (cardNum.length() != 16) {
            cardNumber.setError("Please enter a valid 16-digit card number");
            return false;
        }

        // Validate cardholder name (only letters and spaces)
        if (cardHolderName.getText().toString().isEmpty() || !cardHolderName.getText().toString().matches("[a-zA-Z\\s]+")) {
            cardHolderName.setError("Please enter a valid cardholder name");
            return false;
        }

        // Validate expiry date (MM/YY format)
        String expiry = expiryDate.getText().toString().trim();

        // If the expiry is in MMYY format, convert it to MM/YY
        if (expiry.length() == 4 && expiry.matches("^(0[1-9]|1[0-2])\\d{2}$")) {
            expiry = expiry.substring(0, 2) + "/" + expiry.substring(2);
            expiryDate.setText(expiry);
        }

        // Validate the MM/YY format now
        if (expiry.isEmpty() || !expiry.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            expiryDate.setError("Please enter a valid expiry date (MM/YY)");
            return false;
        }


        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        // Split the expiry date into month and year
        String[] dateParts = expiry.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int year = Integer.parseInt(dateParts[1]);

        // Add 2000 to the expiry year to get the full year (e.g., 26 -> 2026)
        year += 2000;

        // Check if expiry date is in the future
        if (month < 1 || month > 12) {
            expiryDate.setError("Invalid month");
            return false;
        }

        // If the expiry year is greater than the current year, it's valid
        if (year > currentYear) {
        } else if (year == currentYear && month >= currentMonth) {
        } else {
            expiryDate.setError("Card has expired");
            return false;
        }

        // Now validate CVV
        String cvvNum = cvv.getText().toString().trim();
        if (cvvNum.isEmpty() || cvvNum.length() != 3 || !cvvNum.matches("\\d{3}")) {
            cvv.setError("Please enter a valid 3-digit CVV");
            return false;
        }


        // If all validations pass
        return true;
    }

    // Function to navigate to the "Thank You" page
    private void navigateToThankYouPage() {
        Intent intent = new Intent(this, thankYou_activity.class); // Replace with your actual Thank You Activity
        startActivity(intent);
    }
}
