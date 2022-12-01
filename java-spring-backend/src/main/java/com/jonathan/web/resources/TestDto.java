package com.jonathan.web.resources;

//public class TestDto extends Serializable
public class TestDto
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
