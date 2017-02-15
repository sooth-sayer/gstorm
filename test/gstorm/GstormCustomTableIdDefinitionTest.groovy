package gstorm

import groovy.sql.Sql
import java.util.logging.Level
import gstorm.Table
import models.ClassWithCustomIdDefinition

class GstormCustomTableIdDefinitionTest extends GroovyTestCase {
  Sql sql
  Gstorm gstorm
  String tableName
  String idColumnName

  void setUp() {
    sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
    gstorm = new Gstorm(sql)
    gstorm.enableQueryLogging(Level.INFO)

    def tableAnnotation = ClassWithCustomIdDefinition.getAnnotation(Table)
    tableName = tableAnnotation.value()
    idColumnName = ClassWithCustomIdDefinition.declaredFields.find { !it.synthetic && it.getAnnotation(Id) }.name
  }

  void tearDown() {
    sql.execute("drop table $tableName if exists".toString())
    sql.close()
  }

  void testCustomTableIdDefinition() {
    gstorm.stormify(ClassWithCustomIdDefinition)
    assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName.toUpperCase()])
      .collect { it.column_name }
      .contains(idColumnName.toUpperCase())
  }
}
