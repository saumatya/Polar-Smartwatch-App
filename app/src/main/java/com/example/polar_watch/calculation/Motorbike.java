package com.example.polar_watch.calculation;

public class Motorbike extends Vehicle {
    private double fuelConsumption; // fuel consumption in L/km
    private static final double EMISSION_FACTOR = 2.9; // CO2 emissions factor in kg/L

    public Motorbike(double distance, double fuelConsumption, double mpg) {
        super(distance, EMISSION_FACTOR, mpg);
        this.fuelConsumption = fuelConsumption;
    }

    @Override
    protected double calculateFuelEfficiency() {
        return fuelConsumption;
    }

}
