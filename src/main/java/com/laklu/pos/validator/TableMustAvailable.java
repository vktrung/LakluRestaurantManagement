package com.laklu.pos.validator;

import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.StatusTable;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TableMustAvailable extends BaseRule {

    private final List<Tables> tables;

    @Override
    public String getValidateField() {
        return "tableStatus";
    }

    @Override
    public boolean isValid() {
        return tables.stream().allMatch(table -> table.getStatus() == StatusTable.AVAILABLE);
    }

    @Override
    public String getMessage() {
        return "Bàn không ở trạng thái có sẵn, không thể cập nhật!";
    }
}
