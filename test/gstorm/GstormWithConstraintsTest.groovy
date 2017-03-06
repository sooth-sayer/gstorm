package gstorm

import groovy.sql.Sql
import java.util.logging.Level
import gstorm.Table
import gstorm.Skip
import models.ClassWithConstraints

class GstormWithConstraintsTest extends GroovyTestCase {
  Sql sql
  Gstorm gstorm
  String tableName

  void setUp() {
    sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
    gstorm = new Gstorm(sql)
    gstorm.enableQueryLogging(Level.INFO)

    def tableAnnotation = ClassWithConstraints.getAnnotation(Table)
    tableName = tableAnnotation.value()
  }

  void tearDown() {
    sql.execute("drop table $tableName if exists".toString())
    sql.close()
  }

  void testModelWithSkipAnnotation() {
    gstorm.stormify(ClassWithConstraints)
    def item = new ClassWithConstraints(name: "OnlyOneAllowed")
    item.save()

    try {
      def item2 = new ClassWithConstraints(name: "OnlyOneAllowed")
      item2.save()
      assert false
    } catch (e) {
    }
  }
}

