package gstorm.builders

import gstorm.metadata.ClassMetaData

class CreateTableQueryBuilder extends AbstractQueryBuilder {

    CreateTableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field ->
          def columnConstraintsStr = field.columnConstraints ? " ${field.columnConstraints}" : ''
          "${field.name} ${field.columnType}${columnConstraintsStr}"
        }

        if (!classMetaData.isWithoutId()) {
            def idDefinition = classMetaData.getIdDefinition() ?: "NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY"
            columnDefs.add(0, "${classMetaData.idFieldName ?: 'ID'} ${idDefinition}")
        }

        new StringBuilder("CREATE").append(SPACE)
                .append(classMetaData.isCsv() ? 'TEXT TABLE' : 'TABLE').append(SPACE)
                .append("IF NOT EXISTS").append(SPACE)
                .append(tableName).append(SPACE)
                .append("(${ columnDefs.join(', ') })")
                .toString()
    }
}
