package automobile.java;

import java.time.Year;
import java.util.Objects;

/**
 * Automobile class using status-string returns for results and validations.
 * Success paths return user-friendly messages or booleans; no exceptions are required.
 */
public class Automobile {
    private String ownerName; // used by summary/ownership transfer
    private String make;
    private String model;
    private String color;
    private int year;
    private long mileage;         // miles
    private double fuelLevel;     // gallons
    private double fuelCapacity;  // gallons
    private boolean engineOn;
    private boolean removed;
    private String vin; // optional identifier

    // --- Constructors ---
    public Automobile() {
        this("", "", "", 0, 0L, null, 15.0);
    }

    public Automobile(String ownerName, String make, String model, String color, int year, long mileage) {
        this(ownerName, make, model, color, year, mileage, null, 15.0);
    }

    public Automobile(String ownerName, String make, String model, String color, int year, long mileage, String vin, double fuelCapacity) {
        // minimal validation for assignment scope
        this.ownerName = ownerName == null ? "" : ownerName.trim();
        this.make = make == null ? "" : make.trim();
        this.model = model == null ? "" : model.trim();
        this.color = color == null ? "" : color.trim();
        this.year = year;
        this.mileage = Math.max(0L, mileage);
        this.vin = (vin == null || vin.isBlank()) ? null : vin.trim();
        this.fuelCapacity = fuelCapacity > 0 ? fuelCapacity : 15.0;
        this.fuelLevel = 0.0;
        this.engineOn = false;
        this.removed = false;
    }

    // --- Utility ---
    private boolean matches(String make, String model, String color, int year) {
        if (removed) return false;
        return eq(this.make, make) && eq(this.model, model) && eq(this.color, color) && this.year == year;
    }

    private static boolean eq(String a, String b) {
        if (a == null || b == null) return false;
        return a.trim().equalsIgnoreCase(b.trim());
    }

    private void clearInfo() { // logical deletion for assignment use
        this.make = null;
        this.model = null;
        this.color = null;
        this.year = 0;
        this.vin = null;
        this.mileage = 0L;
        this.fuelLevel = 0.0;
        this.engineOn = false;
        this.removed = true;
    }

    private static void validateVehicleData(String make, String model, String color, int year, int mileage) {
        if (make == null || make.trim().isEmpty()) throw new IllegalArgumentException("make is required");
        if (model == null || model.trim().isEmpty()) throw new IllegalArgumentException("model is required");
        if (color == null || color.trim().isEmpty()) throw new IllegalArgumentException("color is required");
        if (year < 1886) throw new IllegalArgumentException("year must be >= 1886");
        if (mileage < 0) throw new IllegalArgumentException("mileage must be >= 0");
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }

    // --- Methods (status-string style) ---
    /** Adds a new vehicle (overwrites current state). */
    public String addNewVehicle(String make, String model, String color, int year, int mileage) {
        try {
            validateVehicleData(make, model, color, year, mileage);
            this.make = make.trim();
            this.model = model.trim();
            this.color = color.trim();
            this.year = year;
            this.mileage = mileage;
            this.removed = false;
            return "Vehicle added successfully.";
        } catch (Exception e) {
            return "Failed to add vehicle: " + e.getMessage();
        }
    }

    /** Returns all attributes as a string array in a consistent order. */
    public String[] listVehicleInfo() {
        try {
            return new String[] {
                "Owner: " + nullSafe(ownerName),
                "Make: " + nullSafe(make),
                "Model: " + nullSafe(model),
                "Color: " + nullSafe(color),
                "Year: " + year,
                "Mileage: " + mileage
            };
        } catch (Exception e) {
            return new String[] { "ERROR: " + e.getMessage() };
        }
    }

    /** Updates all attributes at once. */
    public String updateVehicleAttributes(String make, String model, String color, int year, int mileage) {
        try {
            validateVehicleData(make, model, color, year, mileage);
            this.make = make.trim();
            this.model = model.trim();
            this.color = color.trim();
            this.year = year;
            this.mileage = mileage;
            return "Vehicle updated successfully.";
        } catch (Exception e) {
            return "Failed to update vehicle: " + e.getMessage();
        }
    }

    public String startEngine() {
        if (removed) return "Cannot start a removed vehicle.";
        if (engineOn) return "Engine is already running";
        if (fuelLevel <= 0.0) return "Add fuel before starting";
        engineOn = true;
        return "Engine started";
    }

    public String stopEngine() {
        if (removed) return "Cannot stop a removed vehicle.";
        if (!engineOn) return "Engine is already off";
        engineOn = false;
        return "Engine stopped";
    }

    public String updateMileage(int tripMiles) {
        if (removed) return "Cannot update mileage on a removed vehicle.";
        if (tripMiles > 0) {
            mileage += tripMiles;
            return "Mileage updated to " + mileage + " miles";
        } else {
            return "Invalid mileage entry";
        }
    }

    public String transferOwnership(String newOwnerName) {
        if (removed) return "Cannot transfer ownership of a removed vehicle.";
        if (newOwnerName != null && !newOwnerName.trim().isEmpty()) {
            this.ownerName = newOwnerName.trim();
            return "Ownership transferred to " + this.ownerName;
        } else {
            return "Invalid new owner name";
        }
    }

    public String removeVehicle(String autoMake, String autoModel, String autoColor, int autoYear) {
        if (removed) return "Vehicle already removed.";
        if (matches(autoMake, autoModel, autoColor, autoYear)) {
            clearInfo();
            return "Vehicle information removed.";
        }
        return "Input values do not match stored vehicle information.";
    }

    public String displayVehicleSummary() {
        return removed ? "[REMOVED VEHICLE]" :
            "Owner: " + ownerName + ", Make: " + make + ", Model: " + model +
            ", Color: " + color + ", Year: " + year + ", Mileage: " + mileage +
            ", Engine On: " + engineOn;
    }

    public String drive(double miles) {
        if (removed) return "Cannot drive a removed vehicle.";
        if (miles <= 0) return "Miles must be positive.";
        if (!engineOn) return "Engine is off.";
        final double MPG = 25.0;
        double fuelNeeded = miles / MPG;
        if (fuelNeeded > fuelLevel) return "Not enough fuel.";
        mileage += Math.round(miles);
        fuelLevel -= fuelNeeded;
        return "Drove " + miles + " miles.";
    }

    public double refuel(double gallons) {
        if (removed) return 0.0;
        if (gallons <= 0.0) return 0.0;
        double space = fuelCapacity - fuelLevel;
        if (space <= 0.0) return 0.0;
        double added = Math.min(gallons, space);
        fuelLevel += added;
        return added;
    }

    // --- Getters ---
    public String getOwnerName() { return ownerName; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public int getYear() { return year; }
    public long getMileage() { return mileage; }
    public boolean isEngineOn() { return engineOn; }
    public boolean isRemoved() { return removed; }
    public String getVin() { return vin; }
    public double getFuelLevel() { return fuelLevel; }
    public double getFuelCapacity() { return fuelCapacity; }

    @Override public String toString() { return displayVehicleSummary(); }
    @Override public int hashCode() { return Objects.hash(
        make == null ? null : make.toUpperCase(),
        model == null ? null : model.toUpperCase(),
        color == null ? null : color.toUpperCase(),
        year
    ); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Automobile other)) return false;
        return eq(this.make, other.make) && eq(this.model, other.model) && eq(this.color, other.color) && this.year == other.year;
    }
}