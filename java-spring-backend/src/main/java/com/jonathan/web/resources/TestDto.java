package com.jonathan.web.resources;
import java.io.Serializable;

@SuppressWarnings("serial")
public class TestDto implements Serializable
{
  private String message;

  public TestDto() {
  }

  public TestDto(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
