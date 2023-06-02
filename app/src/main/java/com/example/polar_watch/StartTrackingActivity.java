package com.example.polar_watch;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.wear.ambient.AmbientModeSupport;

import com.example.polar_watch.calculation.Bike;
import com.example.polar_watch.calculation.ElectricBike;
import com.example.polar_watch.calculation.Motorbike;
import com.example.polar_watch.calculation.Vehicle;
import com.example.polar_watch.calculation.Walking;
import com.example.polar_watch.databinding.ActivityStartTrackingBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class StartTrackingActivity extends AppCompatActivity implements AmbientModeSupport.AmbientCallbackProvider {

    private final String TAG = "StartTrackingActivity";
    private ActivityStartTrackingBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private AmbientModeSupport.AmbientController ambientController;

    // Timer coordination
    private CountDownTimer countDownTimer;
    private long howLongWait = 0;

    // Data for Calculation
    private String type;
    private String label;
    private String engine;
    private double mpg;
    private double fuelPrice;
    private float coveredDistance;
    private double emissionFactor;

    // update location
    private Location oldLocation;

    // account manager
    private AccountManager am;
    private Account[] accounts;

    private float convertMeterToKm() {
        return coveredDistance * (float) 0.001;
    }

    private double determineEmissionFactor() {

        emissionFactor = 0;

        if(type.equals("Car")) {
            if(engine.equals("Gasoline")) {
                emissionFactor = 123.4; // per km
            } else if(engine.equals("Diesel")) {
                emissionFactor = 121.5; // per km
            } else if(engine.equals("Electric")) {
                // https://www.transportenvironment.org/discover/how-clean-are-electric-cars/
                emissionFactor = 75; // per km
            }
        }

        if(type.equals("Motorcycle")) {
            if(engine.equals("Gasoline")) {
                emissionFactor = 150; // per km
            } else if(engine.equals("Diesel")) {
                emissionFactor = 143; // per km
            } else if(engine.equals("Electric")) {
                // https://www.transportenvironment.org/discover/how-clean-are-electric-cars/
                emissionFactor = 42.5; // per km
            }
        }

        return emissionFactor;
    }

    private double calculateCarbonEmission() {
        double result = 0;

        if(type.equals("Walking")) {
            Walking walking = new Walking(coveredDistance);
            result = walking.getCarbonEmissions();
        } else if(type.equals("Bike")) {
            Bike bike = new Bike(coveredDistance);
            result = bike.getCarbonEmissions();
        } else if(type.equals("E-Bike")) {
            ElectricBike electricBike = new ElectricBike(coveredDistance);
            result = electricBike.getCarbonEmissions();
        } else if(type.equals("Car")) {
            Vehicle vehicle = new Vehicle(coveredDistance, determineEmissionFactor(), mpg);
            result = vehicle.getCarbonEmissions();
        } else if(type.equals("Motorcycle")) {
            Motorbike motorbike = new Motorbike(coveredDistance, determineEmissionFactor(), mpg);
            result = motorbike.getCarbonEmissions();
        }

        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize button
        binding = ActivityStartTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button button = binding.Screen2ButtonId;

        // initialize firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // request user permission
        requestUserNamePermission();
        am = AccountManager.get(this);
        accounts = am.getAccounts();

        /* TODO TEST
        Log.i(TAG, "ACCOUNT MANAGER SIZE Again: " + accounts.length);

        for (int i = 0; i < accounts.length; i++) {
            Log.i(TAG, "Account name " + accounts[i].name);
        }
         */


        // Use Ambient mode
        // https://developer.android.com/training/wearables/views/always-on#ambient-mode-class
        ambientController = AmbientModeSupport.attach(this);

        // Step 1 initialize data for calculation
        initializeCalculationData();


        // Step 2 stop tracking
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Step 3 store data to database
                firebaseIntegration();

                Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                String confirmationType = "The tracking is successfully stopped.";
                intent.putExtra("confirmationType", confirmationType);
                startActivity(intent);

                countDownTimer.cancel();
                finish(); // close this activity windows
            }
        });


        startTimer();
    }

    private void startTimer() {
        long millisUntilFinished = 150000000;
        countDownTimer = new CountDownTimer(millisUntilFinished, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (howLongWait == 0) {
                    requestLocation();
                    howLongWait = 100000;
                }
                howLongWait -= 1000;

                // TODO connect to firebase
                // Log.i(TAG, "Countdown seconds remaining:" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer in StartTrackingActivity successfully finished.");
            }
        };
        countDownTimer.start();
    }

    private void requestUserNamePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.READ_CONTACTS}, 1);

        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.GET_ACCOUNTS}, 1);

        }
    }

    private void firebaseIntegration() {

        // TODO Step 3 make calculation
        double producedCE = calculateCarbonEmission();
        float converMtoKm = convertMeterToKm();

        String mail = "jonas.meier@mail.com";
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        // SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", mail);
        user.put("coveredDistance", converMtoKm);
        user.put("savedCarbonEmission", producedCE);
        // user.put("date", timeStamp);

        Date date = new java.util.Date();

        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String day = dayFormat.format(date);

        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        String month = monthFormat.format(date);

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(date);

        user.put("day", day);
        user.put("month", month);
        user.put("year", year);

        // Add a new document with a generated ID
        db.collection("finaldb")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "Data inserted"); // need a little time for processing
                        // Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void initializeCalculationData() {
        type = "";
        label = "";
        engine = "";
        mpg = 0;
        fuelPrice = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            label = extras.getString("label");
            if (isOtherValuesAvailable(extras)) {
                engine = extras.getString("engine");
                mpg = Double.parseDouble(extras.getString("mpg"));
                fuelPrice = Double.parseDouble(extras.getString("fuelPrice"));
            }
        }
    }

    private void requestLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    if(oldLocation == null)
                        oldLocation = location;
                    else {
                        // Returns the approximate distance in meters between this location and the given location.
                        coveredDistance = oldLocation.distanceTo(location);
                        Log.i(TAG, "Covered distance: " + coveredDistance);

                        // Log.i(TAG, "LastLocation Latitude " + location.getLatitude());
                        // Log.i(TAG, "LastLocation Longitude " + location.getLongitude());
                        oldLocation = location;
                    }
                }
            }
        });
    }

    private boolean isOtherValuesAvailable(Bundle extras) {
        return extras.getString("engine") != null &&
                extras.getString("mpg") != null &&
                extras.getString("fuelPrice") != null;
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new TrackingAmbientCallback();
    }
}