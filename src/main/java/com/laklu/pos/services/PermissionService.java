package com.laklu.pos.services;

import com.laklu.pos.dataObjects.response.PermissionGroupResponse;
import com.laklu.pos.dataObjects.response.PermissionResponse;
import com.laklu.pos.entities.Permission;
import com.laklu.pos.enums.PermissionGroup;
import com.laklu.pos.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class PermissionService  {
    private final PermissionRepository permissionRepository;

    public List<PermissionGroupResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();

        Map<PermissionGroup, List<Permission>> permissionMap = permissions.stream()
                .collect(groupingBy(Permission::getPermissionGroup));

        List<PermissionGroupResponse> result = new ArrayList<>();
        for (Map.Entry<PermissionGroup, List<Permission>> entry : permissionMap.entrySet()) {
            PermissionGroup group = entry.getKey();
            List<PermissionResponse> permissionDTOs = entry.getValue().stream()
                    .map(p -> new PermissionResponse(p.getId(), p.getAlias(), p.getName(), p.getDescription()))
                    .collect(Collectors.toList());

            result.add(new PermissionGroupResponse(
                    group.getLabel(),
                    group.getAlias(),
                    group.getDescription(),
                    permissionDTOs
            ));
        }

        return result;
    }
}
