package com.example.polar_watch.calculation;

public class Walking extends Vehicle {
    private static final double EMISSION_FACTOR = 56.0; // CO2 emissions in g/km when walking

    public Walking(double distance) {
        super(distance, EMISSION_FACTOR / 1000, 0); // convert emission factor to kg/km
    }

    @Override
    protected double calculateFuelEfficiency() {
        return 56.0;
    }
}
