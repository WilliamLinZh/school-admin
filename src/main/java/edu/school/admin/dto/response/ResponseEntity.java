package edu.school.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain=true)
@AllArgsConstructor
public class ResponseEntity {
    String message;
}
