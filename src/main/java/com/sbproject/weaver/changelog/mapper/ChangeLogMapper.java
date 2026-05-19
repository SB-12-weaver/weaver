package com.sbproject.weaver.changelog.mapper;

import com.sbproject.weaver.changelog.dto.ChangeLogDetailResponse;
import com.sbproject.weaver.changelog.dto.ChangeLogResponse;
import com.sbproject.weaver.changelog.dto.DiffResponse;
import com.sbproject.weaver.changelog.entity.EmployeeChangeDiff;
import com.sbproject.weaver.changelog.entity.EmployeeChangeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

    ChangeLogResponse roResponse(EmployeeChangeLog entity);

    //@Mapping(source = "diffs", target = "diffs")
    //ChangeLogDetailResponse toDetailResponse(EmployeeChangeLog entity);

    @Mapping(source = "beforeValue", target = "before")
    @Mapping(source = "afterValue", target = "after")
    DiffResponse toDiffResponse(EmployeeChangeDiff diff);
}
