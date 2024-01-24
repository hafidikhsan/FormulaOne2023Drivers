package com.hafidikhsana.formulaonedrivers;

import com.google.gson.annotations.SerializedName;

public class Drivers {

    @SerializedName("driver_number")
    private int driverNumber;

    @SerializedName("broadcast_name")
    private String broadcastName;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("name_acronym")
    private String nameAcronym;

    @SerializedName("team_name")
    private String teamName;

    @SerializedName("team_colour")
    private String teamColour;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("headshot_url")
    private String headshotUrl;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("session_key")
    private int sessionKey;

    @SerializedName("meeting_key")
    private int meetingKey;

    @Override
    public String toString() {
        return "Drivers{" +
                "driverNumber=" + driverNumber +
                ", fullName='" + fullName + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    public int getDriverNumber() {
        return driverNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNameAcronym() {
        return nameAcronym;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getSessionKey() {
        return sessionKey;
    }

    public String getHeadshotUrl() {
        return headshotUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
