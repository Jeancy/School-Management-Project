
package com.jeancy.sms.repository;

import com.jeancy.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

// the JpaRepository interface provide all the CRUD methods.
//The StudentRepository interface gets access to all the CRUD methods as implemented by 
//SimpleJpaRepository class
public interface StudentRepository extends JpaRepository <Student, Long>{
    // no implementation needed here.
}
