package gstorm.metadata

import gstorm.helpers.TypeMapper
import gstorm.Constraints

import java.lang.reflect.Field

class FieldMetaData {
    def type, name, columnName, columnType, columnConstraints

    FieldMetaData(Field field) {
        this.type = field.type
        this.name = field.name
        this.columnType = TypeMapper.instance.getSqlType(field.type)
        this.columnName = field.name
        this.columnConstraints = field.getAnnotation(Constraints)?.value()
    }
}
