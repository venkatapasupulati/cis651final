package com.suuniv.afinal.paw;

public class PawModel {
    private String paw_name;

    private String breed;

    private String age;

    private String  quirks;

   private String  vaccinations;

   private String ownerInfo;

    public String getPaw_name() {
        return paw_name;
    }

    public void setPaw_name(String paw_name) {
        this.paw_name = paw_name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getQuirks() {
        return quirks;
    }

    public void setQuirks(String quirks) {
        this.quirks = quirks;
    }

    public String getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(String vaccinations) {
        this.vaccinations = vaccinations;
    }

    public String getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(String ownerInfo) {
        this.ownerInfo = ownerInfo;
    }
}
