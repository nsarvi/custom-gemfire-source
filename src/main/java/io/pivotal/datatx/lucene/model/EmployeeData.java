package io.pivotal.datatx.lucene.model;

import java.io.Serializable;
import java.util.Collection;

public class EmployeeData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String firstName;
    private String lastName;
    private int emplNumber;
    private String email;
    private int salary;
    private int hoursPerWeek;
    private long dob;
    private Collection<Contact> contacts;

    public EmployeeData() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getEmplNumber() {
        return emplNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getSalary() {
        return salary;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public Collection<Contact> getContacts() {
        return this.contacts;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmplNumber(int emplNumber) {
        this.emplNumber = emplNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public void setContacts(Collection<Contact> contacts) {
        this.contacts = contacts;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emplNumber=" + emplNumber +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", hoursPerWeek=" + hoursPerWeek +
                ", dob=" + dob +
                ", contacts=" + contacts +
                '}';
    }
}