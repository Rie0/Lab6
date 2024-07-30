package org.twspring.lab6.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.lab6.ApiResponse.ApiResponse;
import org.twspring.lab6.Model.Employee;
import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    //===========================GET===========================
    @GetMapping("/get/employees")
    public ResponseEntity getEmployees(){
        if(employees.isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
        }
        return ResponseEntity.status(200).body(employees);
    }

    @GetMapping("get/employees/by_position/{position}")
    public ResponseEntity getEmployeePosition(@PathVariable String position){
        ArrayList<Employee> foundEmployees = new ArrayList<>();

        if(position.equals("Supervisor")||position.equals("Coordinator")){

        for(Employee emp : employees){
            if (emp.getPosition().equals(position)){
                foundEmployees.add(emp);
            }
        }

        if(foundEmployees.isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
        }else {
            return ResponseEntity.status(200).body(foundEmployees);
        }
        }
        return ResponseEntity.status(404).body(new ApiResponse("404","Positions can only be Coordinator or Supervisor"));
    }

    @GetMapping("get/employees/by_age/{minAge}-{maxAge}")
    public ResponseEntity getEmployeeAgeRange(@PathVariable int minAge, @PathVariable int maxAge){
        ArrayList<Employee> foundEmployees = new ArrayList<>();
        if(minAge>maxAge){
            return ResponseEntity.status(400).body(new ApiResponse("400","Minimum age cannot be greater than maximum age"));
        }
        if(minAge<26){
            return ResponseEntity.status(400).body(new ApiResponse("400","Minimum age cannot be less than 26"));
        }
        for(Employee emp : employees){
            if(emp.getAge()>=minAge&&emp.getAge()<=maxAge){
                foundEmployees.add(emp);
            }
        }
        if(foundEmployees.isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
        }
        return ResponseEntity.status(200).body(foundEmployees);
    }

    @GetMapping("get/employees/with_no_annual_leave")
    public ResponseEntity getEmployeesWithNoAnnualLeave(){
        ArrayList<Employee> foundEmployees = new ArrayList<>();
        for(Employee emp : employees){
            if(emp.getAnnualLeave()<=0){
                foundEmployees.add(emp);
            }
        }
        if(foundEmployees.isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
        }
        return ResponseEntity.status(200).body(foundEmployees);
    }
    //===========================POST===========================
    @PostMapping("post/employee")
    public ResponseEntity createEmployee(@Valid @RequestBody Employee employee, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();//can use an array list to print all errors using getFieldErrors()
            return ResponseEntity.status(400).body(new ApiResponse("400",message));
        }
        //Extra, logic to ensure unique IDs
        for (Employee emp : employees) {
            if(emp.getId().equals(employee.getId())){
                return ResponseEntity.status(409).body(new ApiResponse("409","ID already taken"));
            }
        }
        employees.add(employee);
        return ResponseEntity.status(201).body(new ApiResponse("201","Employee created successfully"));
    }

    //===========================UPDATE===========================
    @PutMapping("/update/employee/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @Valid @RequestBody Employee updatedEmployee, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse("400",message));
        }

        for (Employee emp : employees) {
            if (emp.getId().equals(id)){
                employees.set(employees.indexOf(emp), updatedEmployee);
                return ResponseEntity.status(200).body(new ApiResponse("200","Employee updated successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
    }

    @PutMapping("update/employee/{id}/apply_for_annual_leave")
    public ResponseEntity applyForAnnualLeave(@PathVariable String id){
        for (Employee emp : employees) {
            if (emp.getId().equals(id)){
                if(emp.getAnnualLeave()>0&&!emp.isOnLeave()){
                    emp.setOnLeave(true);
                    emp.setAnnualLeave(emp.getAnnualLeave()-1);
                    return ResponseEntity.status(200).body(new ApiResponse("200","Employee is now on leave"));
                } else {
                    return ResponseEntity.status(400).body(new ApiResponse("400","Employee is not eligible for a leave"));
                }
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("404","No employee found"));

    }

    @PutMapping("update/employee/{requesterId}/promote/{employeeId}")
    public ResponseEntity promote(@PathVariable String requesterId, @PathVariable String employeeId){
        //check if requester is a supervisor.
        for (Employee emp : employees) {
            if (emp.getId().equals(requesterId)){
                if (!emp.getPosition().equals("Supervisor")){
                    return ResponseEntity.status(403).body(new ApiResponse("403","Employee is not Supervisor"));
                }
            }
        }
        for (Employee emp : employees) {
            if (emp.getId().equals(employeeId)){
                if (emp.getPosition().equals("Supervisor")){
                    return ResponseEntity.status(400).body(new ApiResponse("400","Employee is already supervisor"));
                }
                if (emp.getAge()>=30&&!emp.isOnLeave()){
                    emp.setPosition("Supervisor");
                    return ResponseEntity.status(200).body(new ApiResponse("200","Employee promoted to supervisor successfully"));
                }else{
                        return ResponseEntity.status(400).body(new ApiResponse("400","Employee is not eligible for a promotion"));
                    }
                }
            }
        return ResponseEntity.status(404).body(new ApiResponse("404","Supervisor or employee not found"));
    }

    //===========================DELETE===========================

    @DeleteMapping("/delete/employee/{id}") //Deleted: return 410 when searching for a deleted employee :D
    public ResponseEntity deleteEmployee(@PathVariable String id){
        for (Employee emp : employees) {
            if(emp.getId().equals(id)){
                employees.remove(emp);
                return ResponseEntity.status(200).body(new ApiResponse("200","Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("404","No employees found"));
    }




}
