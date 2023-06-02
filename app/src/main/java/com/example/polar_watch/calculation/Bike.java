package com.example.polar_watch.calculation;

public class Bike extends Vehicle {
    private static final double EMISSION_FACTOR = 21.0; // CO2 emissions in g/km

    public Bike(double distance) {
        super(distance, EMISSION_FACTOR / 1000, 0); // convert emission factor to kg/km
    }

    @Override
    protected double calculateFuelEfficiency() {
        return 21.0; // average fuel efficiency of a bike in km/L
    }
}
