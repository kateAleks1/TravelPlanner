package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DestinationTypeDto {
   private Integer typeId;
   private String typeName;

   public Integer getTypeId() {
      return typeId;
   }

   public void setTypeId(int typeId) {
      this.typeId = typeId;
   }

   public String getTypeName() {
      return typeName;
   }

   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }
}
