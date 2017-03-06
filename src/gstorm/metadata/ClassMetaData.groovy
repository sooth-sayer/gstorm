package gstorm.metadata

import gstorm.Csv
import gstorm.Id
import gstorm.Skip
import gstorm.Table
import gstorm.WithoutId

import java.lang.reflect.Field

class ClassMetaData {
    final Class modelClass
    final String tableName
    final FieldMetaData idField
    final String idDefinition
    private final List<FieldMetaData> fields
    private Map _fieldsCache        // just to avoid iterating over list of fields and finding by name.

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = extractTableName(modelClass)
        this.idField = getIdFieldOfClass(modelClass)
        this.fields = getOtherFieldsOfClass(modelClass)
        this.idDefinition = idDefinition(modelClass)
        this._fieldsCache = this.fields.collectEntries { fieldMetaData -> [fieldMetaData.name, fieldMetaData] }
    }

    FieldMetaData getAt(String fieldName) {
        this._fieldsCache[fieldName]
    }

    List<FieldMetaData> getFields() {
        Collections.unmodifiableList(this.fields);
    }

    List<String> getFieldNames() {
        Collections.unmodifiableList(this.fields*.name);
    }

    String getIdFieldName() {
        idField?.name
    }

    private String extractTableName(Class modelClass) {
        modelClass.getAnnotation(Table)?.value()?.trim() ?: modelClass.simpleName
    }

    boolean isWithoutId() {
        modelClass.isAnnotationPresent(WithoutId)
    }

    boolean isCsv(){
        modelClass.isAnnotationPresent(Csv)
    }

    String getIdDefinition() {
      this.idDefinition
    }

    private List<FieldMetaData> getOtherFieldsOfClass(Class modelClass) {
        fieldsDeclaredIn(modelClass)
                .findAll { !it.isAnnotationPresent(Id) && !it.isAnnotationPresent(Skip) }
                .collect { field -> new FieldMetaData(field) }
    }

    private FieldMetaData getIdFieldOfClass(Class modelClass) {
        def idField = fieldsDeclaredIn(modelClass).find { it.isAnnotationPresent(Id) }
        idField ? new FieldMetaData(idField) : null
    }

    private ArrayList<Field> fieldsDeclaredIn(Class modelClass) {
        modelClass.declaredFields.findAll { !it.synthetic }
    }

    private String idDefinition(Class modelClass) {
        modelClass.getAnnotation(Table)?.idDefinition()
    }

}
