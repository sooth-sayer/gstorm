package gstorm

import groovy.sql.Sql
import java.util.logging.Level
import gstorm.Table
import gstorm.Skip
import models.ClassWithSkipAnnotation

class GstormWithSkipTest extends GroovyTestCase {
  Sql sql
  Gstorm gstorm
  String tableName
  String skipColumnName

  void setUp() {
    sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
    gstorm = new Gstorm(sql)
    gstorm.enableQueryLogging(Level.INFO)

    def tableAnnotation = ClassWithSkipAnnotation.getAnnotation(Table)
    tableName = tableAnnotation.value()
    skipColumnName = ClassWithSkipAnnotation.declaredFields.find { !it.synthetic && it.getAnnotation(Skip) }.name
  }

  void tearDown() {
    sql.execute("drop table $tableName if exists".toString())
    sql.close()
  }

  void testModelWithSkipAnnotation() {
    gstorm.stormify(ClassWithSkipAnnotation)
    assert !sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName.toUpperCase()])
      .collect { it.column_name }
      .contains(skipColumnName.toUpperCase())
  }
}

