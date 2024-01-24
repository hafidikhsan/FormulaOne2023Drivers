package com.hafidikhsana.formulaonedrivers;

public class Drivers {

    int driverNumber;
    String broadcastName;
    String fullName;
    String nameAcronym;
    String teamName;
    String teamColour;
    String firstName;
    String lastName;
    String headshotUrl;
    String countryCode;
    int sessionKey;
    int meetingKey;

    Drivers(int driverNumber, String fullName, String nameAcronym, String teamName) {
        this.driverNumber = driverNumber;
        this.fullName = fullName;
        this.nameAcronym = nameAcronym;
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Drivers{" +
                "fullName='" + fullName + '\'' +
                '}';
    }
}
