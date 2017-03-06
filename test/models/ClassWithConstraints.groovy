package models

import gstorm.Table
import gstorm.Id
import gstorm.Constraints

@Table(value="ConstraintsTest")
class ClassWithConstraints {
  @Id
  Integer testId

  @Constraints("UNIQUE")
  String name
}
