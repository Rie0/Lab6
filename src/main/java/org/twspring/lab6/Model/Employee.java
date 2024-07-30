package org.twspring.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID cannot be empty")
    @Size(min = 3, message = "ID must have at least 3 characters")
    private String id;

    @NotEmpty
    @Size(min = 5, message = "Name must have at least 5 letters")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Name must only contain letters")
    private String name;

    @NotEmpty(message = "Email cannot be empty") //added
    @Email(message = "Please enter a valid email")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")//added
    @Pattern(regexp = "^05[0-9]{8}$" , message = "Phone number must begin with 05 and include 10 numbers with no letters.")
    private String phoneNumber;

    @NotNull(message = "Age cannot be empty")
    @Positive(message = "Age cannot be negative or zero")
    @Min(value = 26, message = "Age cannot be less than 26")
    private int age;

    @NotEmpty(message = "Position cannot be empty")
    @Pattern(regexp = "^(Supervisor|Coordinator)$" , message = "Position can only be Supervisor or Coordinator")
    private String position;

    @NotNull(message = "On leave field cannot be empty")//added
    @AssertFalse(message = "On leave must be initiated to false")
    private boolean onLeave;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Hire date cannot be empty") //does it work tho??
    //@ Date should be after 2000
    @PastOrPresent(message = "Hire date cannot be in the future") //fills all requirements
    //use regex?
    private LocalDate hireDate;

    @NotNull(message = "Annual leaves cannot be empty")
    @PositiveOrZero(message = "Annual leaves must be a positive number or a zero")
    private int annualLeave;

    //Method to check if the year the employee was hired is not before 2010. Saying it's the year the company was established
    @Min(value = 2010, message = "Year must be 2010 or later")
    public int getHireYear(){
        return hireDate.getYear();
    }

}


    //method to check the year.

