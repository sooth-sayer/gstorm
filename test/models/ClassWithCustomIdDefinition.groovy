package models

import gstorm.Table
import gstorm.Id

@Table(value="CustomIdDefinitionTest", idDefinition="INT PRIMARY KEY")
class ClassWithCustomIdDefinition {
  @Id
  Integer testId
  String name
}
