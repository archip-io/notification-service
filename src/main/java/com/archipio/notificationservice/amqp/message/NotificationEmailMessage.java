package com.archipio.notificationservice.amqp.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEmailMessage implements Serializable {

  private String to;
  private String subject;
  private Template template;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Template {

    private String code;
    private List<Parameter> parameters = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Parameter {

      private String name;
      private String value;
    }
  }
}
