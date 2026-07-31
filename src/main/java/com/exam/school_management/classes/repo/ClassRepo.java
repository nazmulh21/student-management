package com.exam.school_management.classes.repo;


import com.exam.school_management.classes.dto.ClassProjos;
import com.exam.school_management.classes.model.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface ClassRepo extends JpaRepository<ClassInfo, Long> {

    @Query("select new com.exam.school_management.classes.dto.ClassProjos(c.id,c.className) from ClassInfo c")
    List<ClassProjos> getClassList();
}
