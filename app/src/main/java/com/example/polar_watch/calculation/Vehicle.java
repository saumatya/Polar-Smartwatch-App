package com.example.polar_watch.calculation;

public class Vehicle {
    protected double distance; // Distance traveled by the vehicle
    protected double emissionFactor; // Emission factor of the fuel used by the vehicle
    protected double carbonEmissions; // Carbon emissions produced by the vehicle
    protected double mpg; // FE (Fuel efficiency)
    public Vehicle(double distance, double emissionFactor, double mpg) {
        this.distance = distance;
        this.emissionFactor = emissionFactor;
        this.mpg = mpg;
        this.carbonEmissions = calculateCarbonEmissions();
    }

    public double getCarbonEmissions() {
        return this.carbonEmissions;
    }

    protected double calculateFuelEfficiency() {
        return mpg;
    };

    protected double calculateCarbonEmissions() {
        double fuelConsumption = this.distance / calculateFuelEfficiency(); // FC
        return fuelConsumption * this.emissionFactor;
    }
}

/*
    public static void main(String[] args) {
        double distance = 10.0; // distance in km

        // Create instances of each vehicle class
        Walking walking = new Walking(distance);
        Bike bike = new Bike(distance);
        ElectricBike electricBike = new ElectricBike(distance);
        Motorbike motorbike = new Motorbike(distance, 0.5); // fuel consumption of 0.5 L/km

        // Calculate carbon emissions for each vehicle
        double walkingEmissions = walking.calculateCarbonEmissions();
        double bikeEmissions = bike.calculateCarbonEmissions();
        double electricBikeEmissions = electricBike.calculateCarbonEmissions();
        double motorbikeEmissions = motorbike.calculateCarbonEmissions();

        // Print out the results
        System.out.println("Carbon emissions for walking: " + walkingEmissions + " kg");
        System.out.println("Carbon emissions for regular bike: " + bikeEmissions + " kg");
        System.out.println("Carbon emissions for electric bike: " + electricBikeEmissions + " kg");
        System.out.println("Carbon emissions for motorbike: " + motorbikeEmissions + " kg");
    }

 */
