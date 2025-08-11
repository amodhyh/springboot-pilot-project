package com.yasitha.test1.Repository;

import com.yasitha.test1.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepo extends JpaRepository<Permission,Long> {
    Permission findPermissionById(Long id);

    @Query("select p from com.yasitha.test1.Model.Role r join r.permissions p where r.name = :roleName")
    Set<Permission> findPermissionsByRoleName(@Param("roleName") String roleName);

}
