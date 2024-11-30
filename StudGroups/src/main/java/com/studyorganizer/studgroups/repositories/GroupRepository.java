package com.studyorganizer.studgroups.repositories;

import com.studmodel.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findGroupsByDisabled(Boolean disabled);

    Group findGroupByName(String name);
}
