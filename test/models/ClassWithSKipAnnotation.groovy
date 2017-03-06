package models

import gstorm.Table
import gstorm.Id
import gstorm.Skip

@Table(value="CustomIdDefinitionTest", idDefinition="INT PRIMARY KEY")
class ClassWithSkipAnnotation {
  @Id
  Integer testId
  String name

  @Skip
  Integer notMappedField
}

